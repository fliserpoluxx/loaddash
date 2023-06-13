package net.golema.loadash.channels;

import java.io.Closeable;
import java.io.IOException;
import java.util.UUID;

import net.golema.loadash.LoadashCommons;
import net.golema.loadash.minecraft.LoadashMinecraft;
import net.golema.loadash.servers.GameType;
import net.golema.loadash.servers.MapInfos;
import net.golema.loadash.servers.ServerStatus;
import net.golema.loadash.servers.ServerType;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPubSub;
import redis.clients.jedis.exceptions.JedisConnectionException;

public class ServerDemandChannel implements Closeable {

	private ServerDemandListener serverDemandListener;

	/**
	 * Constructeur du Channel ServerDemand.
	 */
	public ServerDemandChannel() {
		this.serverDemandListener = new ServerDemandListener();
		LoadashMinecraft.getPlugin().getExecutorService().submit(() -> {
			try (Jedis jedis = LoadashMinecraft.getPlugin().getLoadashCommons().getRedisAccess().getJedisPool()
					.getResource()) {
				jedis.subscribe(serverDemandListener, LoadashCommons.LOADASH_CHANNEL);
				System.out.print(
						LoadashCommons.LOADASH_PREFIX + "Démarrage du CHANNEL : " + LoadashCommons.LOADASH_CHANNEL);
			}
		});
	}

	@Override
	public void close() throws IOException {
		try {
			this.serverDemandListener.unsubscribe();
		} catch (JedisConnectionException jedisConnectionException) {
			System.err.println(jedisConnectionException.getMessage());
		}
	}

	/**
	 * Faire une demande d'ouverture de Serveur avec UUID.
	 * 
	 * @param ownerUUID
	 * @param ownerName
	 * @param gameType
	 * @param serverType
	 */
	public void sendRequestDemandServer(UUID ownerUUID, String ownerName, GameType gameType, ServerType serverType,
			ServerStatus serverStatus, MapInfos mapInfos) {
		GolemaServerRequest golemaServerRequest = new GolemaServerRequest(ownerUUID, ownerName, gameType, serverType,
				serverStatus, mapInfos);
		String json = LoadashMinecraft.getPlugin().getLoadashCommons().getGSON().toJson(golemaServerRequest);
		LoadashMinecraft.getPlugin().getExecutorService().submit(() -> {
			try (Jedis jedis = LoadashMinecraft.getPlugin().getLoadashCommons().getRedisAccess().getJedisPool()
					.getResource()) {
				jedis.publish(LoadashCommons.LOADASH_CHANNEL, json);
			}
		});
	}

	/**
	 * Faire une demande d'ouverture de Serveur sans UUID.
	 * 
	 * @param gameType
	 * @param serverType
	 * @param serverStatus
	 * @param mapInfos
	 */
	public void sendRequestDemandServerWithoutOwner(GameType gameType, ServerType serverType, ServerStatus serverStatus,
			MapInfos mapInfos) {
		this.sendRequestDemandServer(null, null, gameType, serverType, serverStatus, mapInfos);
	}

	/**
	 * Class d'écoute du Channel : LoadashChannel.
	 */
	protected class ServerDemandListener extends JedisPubSub {
		@Override
		public void onMessage(String channel, String message) {
			super.onMessage(channel, message);
		}
	}
}