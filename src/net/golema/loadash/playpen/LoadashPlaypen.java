package net.golema.loadash.playpen;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.ConsoleHandler;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import org.json.JSONException;

import io.playpen.core.coordinator.CoordinatorMode;
import io.playpen.core.coordinator.PlayPen;
import io.playpen.core.coordinator.network.LocalCoordinator;
import io.playpen.core.coordinator.network.Network;
import io.playpen.core.p3.P3Package;
import io.playpen.core.plugin.AbstractPlugin;
import net.golema.loadash.LoadashCommons;
import net.golema.loadash.channels.ServerReceivePlaypenChannel;
import net.golema.loadash.redis.RedisCredentials;
import net.golema.loadash.servers.GameType;
import net.golema.loadash.servers.MapInfos;
import net.golema.loadash.servers.ServerStatus;
import net.golema.loadash.servers.ServerType;
import net.golema.loadash.utils.SingleLineFormatter;

public class LoadashPlaypen extends AbstractPlugin {

	private static LoadashPlaypen instance;

	private LoadashCommons loadashCommons;
	private LoadBalancer loadBalancer;

	private ExecutorService executorService;

	private ServerReceivePlaypenChannel serverReceivePlaypenChannel;

	@Override
	public boolean onStart() {

		// Vérification du Type de Coordinateur
		instance = this;
		if (PlayPen.get().getCoordinatorMode() != CoordinatorMode.NETWORK) {
			System.err.println(LoadashCommons.LOADASH_PREFIX
					+ " Le plugin Loadash est uniquement utilisateur sur un Coordinateur Network!");
			return false;
		}

		// Gestion du Systeme de Logger
		Logger logger = Logger.getGlobal();
		ConsoleHandler consoleHandler = new ConsoleHandler();
		consoleHandler.setFormatter(new SingleLineFormatter());
		logger.addHandler(consoleHandler);
		logger.info(LoadashCommons.LOADASH_PREFIX + "Démarrage du plugin Loadash !");

		// Setup du serveur Redis
		try {
			loadashCommons = new LoadashCommons(logger,
					new RedisCredentials(getConfig().getString("redis.host"), getConfig().getInt("redis.port"),
							getConfig().getString("redis.password"), getConfig().getInt("redis.database"),
							getConfig().getBoolean("redis.use-pass")));
			logger.info(LoadashCommons.LOADASH_PREFIX + "Le Balancer est connecté à Redis !");
		} catch (JSONException exception) {
			exception.printStackTrace();
		}

		// Gestion du LoadBalancing
		try {
			if (getConfig().getBoolean("loadbalancing")) {
				this.loadBalancer = new LoadBalancer();
				this.loadBalancer.start();
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}

		// Système d'écoute de démarrage de Serveur
		this.executorService = Executors.newCachedThreadPool();
		this.serverReceivePlaypenChannel = new ServerReceivePlaypenChannel();
		return true;
	}

	@Override
	public void onStop() {
		// Système d'écoute de démarrage de Serveur
		try {
			this.serverReceivePlaypenChannel.close();
		} catch (IOException exception) {
			exception.printStackTrace();
		}

	}

	/**
	 * Démarrer un Serveur sous PlayPen.
	 * 
	 * @param gameType
	 * @param playerHostUUID
	 */
	public void startServer(GameType gameType, ServerType serverType, ServerStatus serverStatus, MapInfos mapInfos,
			UUID uuidHost, String nameHost) {
		loadashCommons.getLogger().info(LoadashCommons.LOADASH_PREFIX + "Requête de démarrage d'un Serveur...");
		try {

			String game = gameType.getName();

			// Détection de la possibilité à démarrer un Serveur
			loadashCommons.getLogger().info(LoadashCommons.LOADASH_PREFIX
					+ "Détection de la possibilité de démarrer un serveur : " + game + ".");
			P3Package p3 = PlayPen.get().getPackageManager().resolve(game, "promoted");
			if (p3 == null)
				return;

			// Selection d'un Coordinateur
			LocalCoordinator localCoordinatorChoosen = this.getCoordinator();
			int searchInt = 0;
			while ((localCoordinatorChoosen == null) && (searchInt < 15)) {
				localCoordinatorChoosen = this.getCoordinator();
				loadashCommons.getLogger().info(LoadashCommons.LOADASH_PREFIX
						+ "Impossible de trouver un coordinateur disponible pour : " + game + ".");
				if (searchInt > 15)
					break;
				searchInt++;
			}
			if (localCoordinatorChoosen == null) {
				loadashCommons.getLogger().info(LoadashCommons.LOADASH_PREFIX
						+ "Impossible de trouver un coordinateur disponible pour : " + game + ".");
				return;
			}

			// Vérification et génération d'UUID
			UUID uuid = UUID.randomUUID();
			if (localCoordinatorChoosen.getChannel() == null)
				throw new RuntimeException("");
			if (localCoordinatorChoosen.getChannel().remoteAddress() == null)
				throw new RuntimeException();

			// Définition des paramètres du Serveur
			InetAddress ip = ((InetSocketAddress) localCoordinatorChoosen.getChannel().remoteAddress()).getAddress();
			Map<String, String> properties = new HashMap<>();
			properties.put("port", this.getAvailablePort() + "");
			properties.put("ip", ip.getHostAddress());
			properties.put("serveruuid", uuid.toString());
			properties.put("coordinatoruuid", localCoordinatorChoosen.getUuid());

			// Définition des paramètres de la Partie
			properties.put("gameType", gameType.getName());
			properties.put("serverType", serverType.getName());
			properties.put("defaultServerStatus", serverStatus.getName());
			properties.put("mapInfos", mapInfos.getName());
			properties.put("max_players", mapInfos.getMaxPlayers() + "");

			// Définition des paramètres des Host"NO-HOST-UNKNOW"
			properties.put("uuidHost", uuidHost == null ? UUID.randomUUID().toString() : uuidHost.toString());
			properties.put("nameHost", nameHost == null ? "NO-HOST-UNKNOW" : nameHost);

			// Démarrage d'un Serveur
			Network.get().provision(p3, game + "#" + uuid.toString(), properties, localCoordinatorChoosen.getName());
			loadashCommons.getLogger().info(LoadashCommons.LOADASH_PREFIX + "Tentative de démarrage d'un serveur : "
					+ game + " (" + uuid.toString() + "), sur " + localCoordinatorChoosen.getName() + ".");

		} catch (Exception exception) {
			System.err.println(
					LoadashCommons.LOADASH_PREFIX + "Impossible de démarrer le serveur : " + gameType.getName() + ".");
			exception.printStackTrace();
		}
	}

	/**
	 * Récupérer un port disponible pour démarrer un Server.
	 */
	public int getAvailablePort() {
		return (int) (Math.random() * (54999 - 26001)) + 26001;
	}

	/**
	 * Récupérer le Coordinateur le plus adapté pour un Server.
	 */
	public LocalCoordinator getCoordinator() {
		Random r = new Random();
		List<LocalCoordinator> l = Network.get().getCoordinators().values().stream().collect(Collectors.toList());
		return Network.get().getCoordinator(l.get(r.nextInt(l.size())).getKeyName());
	}

	public LoadBalancer getLoadBalancer() {
		return loadBalancer;
	}

	public LoadashCommons getLoadashCommons() {
		return loadashCommons;
	}

	public ServerReceivePlaypenChannel getServerReceivePlaypenChannel() {
		return serverReceivePlaypenChannel;
	}

	public ExecutorService getExecutorService() {
		return executorService;
	}

	public static LoadashPlaypen getInstance() {
		return instance;
	}
}