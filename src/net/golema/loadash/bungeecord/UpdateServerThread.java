package net.golema.loadash.bungeecord;

import java.net.InetSocketAddress;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;

import net.golema.loadash.LoadashCommons;
import net.golema.loadash.servers.GolemaServer;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.config.ServerInfo;
import redis.clients.jedis.Jedis;

/**
 * Thread de gestion d'enregistrement des GolemaServer.
 */
public class UpdateServerThread implements Runnable {

	private AtomicBoolean running = new AtomicBoolean(true);
	private Set<String> added = new HashSet<>();

	@SuppressWarnings({ "static-access" })
	@Override
	public void run() {
		while (this.running.get()) {
			try (Jedis jedis = LoadashBungeeCord.getInstance().getLoadashCommons().getRedisAccess().getJedisPool()
					.getResource()) {

				// Détection du nombre de serveurs
				Set<String> ids = jedis.smembers(LoadashCommons.LOADASH_PREFIX_STORAGE_ID);
				if (ids == null)
					return;

				// Suppression des mauvais serveurs
				for (String str : added)
					ProxyServer.getInstance().getServers().remove(str);

				// Initialisation des serveurs
				for (String str : ids) {
					GolemaServer golemaServer = LoadashBungeeCord.getInstance().getLoadashCommons()
							.getGolemaServer(Integer.valueOf(str));
					if (golemaServer == null || golemaServer.getServerName() == null)
						continue;
					ServerInfo serverInfo = ProxyServer.getInstance().constructServerInfo(golemaServer.getServerName(),
							new InetSocketAddress(golemaServer.getIP(), golemaServer.getPort()),
							"GolemaServer | " + golemaServer.getServerName(), false);
					ProxyServer.getInstance().getServers().put(golemaServer.getServerName(), serverInfo);
					this.added.add(golemaServer.getServerName());
				}

			}
			try {
				Thread.currentThread().sleep(4 * 1000);
			} catch (InterruptedException exception) {
				System.out.println(LoadashCommons.LOADASH_PREFIX + "Erreur, impossible d'actualiser les Serveurs.");
			}
		}
	}

	/**
	 * Stop de Thread de Gestion d'Update.
	 */
	public void stop() {
		this.running.set(false);
		Thread.currentThread().interrupt();
	}
}