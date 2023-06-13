package net.golema.loadash.channels;

import java.io.Closeable;
import java.io.IOException;

import net.golema.loadash.LoadashCommons;
import net.golema.loadash.playpen.LoadashPlaypen;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPubSub;
import redis.clients.jedis.exceptions.JedisConnectionException;

public class ServerReceivePlaypenChannel implements Closeable {

	private ServerReceiveListener serverReceiveListener;

	/**
	 * Constructeur du Channel ServerDemand.
	 */
	public ServerReceivePlaypenChannel() {
		this.serverReceiveListener = new ServerReceiveListener();
		LoadashPlaypen.getInstance().getExecutorService().submit(() -> {
			try (Jedis jedis = LoadashPlaypen.getInstance().getLoadashCommons().getRedisAccess().getJedisPool()
					.getResource()) {
				jedis.subscribe(serverReceiveListener, LoadashCommons.LOADASH_CHANNEL);
				System.out.print(
						LoadashCommons.LOADASH_PREFIX + "Démarrage du CHANNEL : " + LoadashCommons.LOADASH_CHANNEL);
			}
		});
	}

	@Override
	public void close() throws IOException {
		try {
			this.serverReceiveListener.unsubscribe();
		} catch (JedisConnectionException jedisConnectionException) {
			System.err.println(jedisConnectionException.getMessage());
		}
	}

	/**
	 * Class d'écoute du Channel.
	 */
	protected class ServerReceiveListener extends JedisPubSub {
		@Override
		public void onMessage(String channel, String message) {
			if (LoadashCommons.LOADASH_CHANNEL.equals(channel)) {
				GolemaServerRequest golemaServerRequest = LoadashPlaypen.getInstance().getLoadashCommons().getGSON()
						.fromJson(message, GolemaServerRequest.class);
				if (golemaServerRequest == null)
					return;

				// Démarrer un serveur à partir d'une Request Demand
				LoadashPlaypen.getInstance().startServer(golemaServerRequest.getGameType(),
						golemaServerRequest.getServerType(), golemaServerRequest.getServerStatus(),
						golemaServerRequest.getMapInfos(), golemaServerRequest.getOwnerUUID(),
						golemaServerRequest.getOwnerName());
				System.out.println(LoadashCommons.LOADASH_PREFIX + "Reception de demande création de Serveur (HOST) : "
						+ golemaServerRequest.getGameType().getName() + " de " + golemaServerRequest.getOwnerName());
			}
		}
	}
}