package net.golema.loadash.minecraft;

import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

import net.golema.loadash.LoadashCommons;
import net.golema.loadash.servers.ServerType;
import redis.clients.jedis.Jedis;

/**
 * Runnable de Gestion d'Update d'un GolemaServer.
 */
public class HeartBeatRunnable extends BukkitRunnable {

	private int innactifHost = 0;

	@Override
	public void run() {
		if (LoadashMinecraft.getGolemaServer() != null) {
			try (Jedis jedis = LoadashMinecraft.getPlugin().getLoadashCommons().getRedisAccess().getJedisPool()
					.getResource()) {

				// Serveurs Host innactif
				if (LoadashMinecraft.getGolemaServer().getServerType().equals(ServerType.HOST)) {
					if (Bukkit.getOnlinePlayers().size() <= 0)
						innactifHost++;
					else
						innactifHost = 0;
					if (innactifHost >= 120) {
						this.cancel();
						Bukkit.getServer().shutdown();
						return;
					}
				}

				// Lancer le démarrage des Hubs
				if ((LoadashMinecraft.getGolemaServer().getServerType().equals(ServerType.HUB))
						&& (LoadashMinecraft.getGolemaServer().getCurrentPlayers() >= 40))
					LoadashMinecraft.getGolemaServer().setStartedSoon(true);
				else
					LoadashMinecraft.getGolemaServer().setStartedSoon(false);

				// Serveur plein
				if (LoadashMinecraft.getGolemaServer().getCurrentPlayers() >= Bukkit.getServer().getMaxPlayers())
					LoadashMinecraft.getGolemaServer().setJoinable(false);
				else
					LoadashMinecraft.getGolemaServer().setJoinable(true);

				// Mise à jour des informations du Server
				LoadashMinecraft.getGolemaServer().setCurrentPlayers(Bukkit.getOnlinePlayers().size());
				LoadashMinecraft.getGolemaServer().getPlayersOnlineList().clear();
				Bukkit.getOnlinePlayers().forEach(players -> LoadashMinecraft.getGolemaServer().getPlayersOnlineList()
						.add(players.getUniqueId()));

				// Update du Server sur Redis
				String key = LoadashCommons.LOADASH_PREFIX_STORAGE_SERVERS + LoadashMinecraft.getGolemaServer().getID();
				String json = LoadashMinecraft.getPlugin().getLoadashCommons().getGSON()
						.toJson(LoadashMinecraft.getGolemaServer());

				// Gérer le cas d'un Crash de Serveur
				if (json == null) {
					if (jedis.exists(key))
						jedis.del(key);
					this.cancel();
					Bukkit.getServer().shutdown();
					return;
				}

				// Enregistrement du Serveur
				jedis.set(key, json);
				jedis.expire(key, 30);
			}
		}
	}
}