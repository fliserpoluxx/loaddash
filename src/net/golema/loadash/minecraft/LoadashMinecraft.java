package net.golema.loadash.minecraft;

import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

import net.golema.loadash.LoadashCommons;
import net.golema.loadash.channels.ServerDemandChannel;
import net.golema.loadash.redis.RedisCredentials;
import net.golema.loadash.redis.RedisIDPool;
import net.golema.loadash.servers.GameType;
import net.golema.loadash.servers.GolemaServer;
import net.golema.loadash.servers.MapInfos;
import net.golema.loadash.servers.ServerStatus;
import net.golema.loadash.servers.ServerType;
import redis.clients.jedis.Jedis;

public class LoadashMinecraft extends JavaPlugin {

	private LoadashCommons loadashCommons;
	private static GolemaServer golemaServer;
	private RedisIDPool idPool;

	private ExecutorService executorService;
	private ServerDemandChannel serverDemandChannel;

	private BukkitTask heartBeatRunnable;

	@Override
	public void onLoad() {

		// Génération de la config et Connection
		this.saveDefaultConfig();

		// Vérifier si Redis est Activé
		RedisCredentials redisCredentials = new RedisCredentials(getConfig().getString("redis.hostname"),
				getConfig().getInt("redis.port"), getConfig().getString("redis.password"),
				getConfig().getInt("redis.database"), getConfig().getBoolean("redis.use-password"));
		this.loadashCommons = new LoadashCommons(getLogger(), redisCredentials);
		this.idPool = new RedisIDPool(loadashCommons.getRedisAccess());
		this.executorService = Executors.newCachedThreadPool();

		// Enregistrement d'un Serveur
		this.registerServer();

		super.onLoad();
	}

	@Override
	public void onEnable() {

		// Démarrage de Runnable d'Update
		this.heartBeatRunnable = new HeartBeatRunnable().runTaskTimerAsynchronously(this, 40L, 20L);

		// Système d'écoute de démarrage de Serveur
		this.serverDemandChannel = new ServerDemandChannel();

		super.onEnable();
	}

	public void sendRequestDemandServerWithoutOwner(GameType gameType, ServerType serverType, ServerStatus serverStatus,
													MapInfos mapInfos) {
		this.sendRequestDemandServer(null, null, gameType, serverType, serverStatus, mapInfos);
	}

	@Override
	public void onDisable() {

		// Générer la suppression et fermeture d'un Serveur
		this.heartBeatRunnable.cancel();
		this.unregisterServer();
		this.idPool.returnId(getGolemaServer().getID());

		// Système d'écoute de démarrage de Serveur
		try {
			this.serverDemandChannel.close();
		} catch (IOException exception) {
			exception.printStackTrace();
		}

		super.onDisable();
	}

	/**
	 * Enregistrer un nouveau GolemaServer.
	 */
	private void registerServer() {

		// Récupérer les données principales
		GameType gameType = GameType.getGameType(getConfig().getString("gameType"));
		ServerType serverType = ServerType.geServerType(getConfig().getString("serverType"));
		ServerStatus serverStatus = ServerStatus.getServerStatus(getConfig().getString("defaultServerStatus"));
		MapInfos mapInfos = MapInfos.getMapsInfos(getConfig().getString("mapInfos"));
		if (mapInfos == null)
			mapInfos = gameType.getRandomGameMap();

		String hostName = getConfig().getString("nameHost");

		// Enregistrer les Serveurs sous Redis
		try {

			// Enregistrer les serveurs tiré du serveur à la Demande
			UUID uuid = UUID.fromString(getConfig().getString("server-uuid"));
			UUID coordinatorUUID = UUID.fromString(getConfig().getString("coordinator-uuid"));

			// Gestion de l'Host System
			UUID hostUUID = UUID.fromString(getConfig().getString("uuidHost"));
			if (hostUUID == null)
				hostUUID = UUID.randomUUID();

			// Enregistrement du Serveur
			golemaServer = new GolemaServer(idPool.nextId(), gameType, serverType, serverStatus, mapInfos, uuid,
					coordinatorUUID, Bukkit.getIp(), Bukkit.getPort(), Bukkit.getMaxPlayers(),
					Bukkit.getOnlinePlayers().size(), hostName, hostUUID, true, false, false, true, false);

		} catch (Exception exception) {

			// Enregistrer les serveurs en cas d'errur par Défaut
			golemaServer = new GolemaServer(idPool.nextId(), gameType, serverType, serverStatus, mapInfos,
					UUID.randomUUID(), UUID.randomUUID(), Bukkit.getIp(), Bukkit.getPort(), Bukkit.getMaxPlayers(),
					Bukkit.getOnlinePlayers().size(), hostName, UUID.randomUUID(), true, false, false, false, false);
			System.out.println(LoadashCommons.LOADASH_PREFIX
					+ "Erreur sur les UUIDS de Coordinateurs, enregistrement d'un Serveur par Défaut.");
		}
	}

	/**
	 * Supprimer l'enregistrement d'un GolemaServer.
	 */
	private void unregisterServer() {
		try (Jedis jedis = loadashCommons.getRedisAccess().getJedisPool().getResource()) {
			String key = LoadashCommons.LOADASH_PREFIX_STORAGE_SERVERS + LoadashMinecraft.getGolemaServer().getID();
			jedis.del(key);
			jedis.srem(key, String.valueOf(getGolemaServer().getID()));
		}
	}

	/***
	 * Récupérer l'instance de Serveur : GolemaServer
	 */
	public static GolemaServer getGolemaServer() {
		return golemaServer;
	}

	public ServerDemandChannel getServerDemandChannel() {
		return serverDemandChannel;
	}

	public LoadashCommons getLoadashCommons() {
		return loadashCommons;
	}

	public ExecutorService getExecutorService() {
		return executorService;
	}

	public static LoadashMinecraft getPlugin() {
		return getPlugin(LoadashMinecraft.class);
	}
}