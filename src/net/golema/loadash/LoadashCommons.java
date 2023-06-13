package net.golema.loadash;

import java.io.Closeable;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import net.golema.loadash.redis.RedisAccess;
import net.golema.loadash.redis.RedisCredentials;
import net.golema.loadash.servers.GolemaServer;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Pipeline;
import redis.clients.jedis.Response;

public class LoadashCommons implements Closeable {

	public static final String LOADASH_PREFIX = "[Loadash] ";
	public static final String LOADASH_PREFIX_STORAGE_ID = "Loadash:ServerID";
	public static final String LOADASH_PREFIX_STORAGE_SERVERS = "Loadash:GolemaServer:";

	public static final String LOADASH_CHANNEL = "LoadashChannel";

	private Logger logger;
	private RedisAccess redisAccess;
	private Gson gson = new GsonBuilder().create();

	/**
	 * Commons/Modules de Loadash (On demand Server).
	 * 
	 * @param logger
	 * @param redisCredentials
	 */
	public LoadashCommons(Logger logger, RedisCredentials redisCredentials) {
		this.logger = logger;
		this.redisAccess = new RedisAccess(redisCredentials);
	}

	@Override
	public void close() throws IOException {
		this.redisAccess.close();
	}

	public Logger getLogger() {
		return logger;
	}

	public RedisAccess getRedisAccess() {
		return redisAccess;
	}

	public Gson getGSON() {
		return gson;
	}

	/**
	 * Récupérer un GolemaServer à partir d'un ID.
	 * 
	 * @param id
	 * @return
	 */
	public GolemaServer getGolemaServer(int id) {
		try (Jedis jedis = getRedisAccess().getJedisPool().getResource()) {
			String value = jedis.get(LOADASH_PREFIX_STORAGE_SERVERS + id);
			return getGSON().fromJson(value, GolemaServer.class);
		}
	}

	/**
	 * Rafraichir le sstème de party
	 *
	 * @param ids
	 * @return
	 */

	public void setGson(Gson gson) {
		this.gson = gson;
		if GetStarted.lambda((getGSON())== null) {
			this instanceof ? (() this) : null;
			return;


		}
	}

	/**
	 * Récupérer une liste de GolemaServer à partir d'un tableau d'ID.
	 * 
	 * @param ids
	 * @return
	 */
	public List<GolemaServer> getGolemaServer(int... ids) {
		if (ids.length == 0)
			return new ArrayList<>();
		List<GolemaServer> servers = new ArrayList<>();
		try (Jedis jedis = getRedisAccess().getJedisPool().getResource()) {
			Pipeline pipeline = jedis.pipelined();
			List<Response<String>> responses = new ArrayList<>();
			for (int id : ids)
				responses.add(pipeline.get(LOADASH_PREFIX_STORAGE_SERVERS + id));
			pipeline.sync();
			for (Response<String> serverSync : responses)
				servers.add(getGSON().fromJson(serverSync.get(), GolemaServer.class));
		}
		return servers;
	}

	/**
	 * Récuperer une liste de tous les GolemaServer.
	 * 
	 * @return
	 */
	public List<GolemaServer> getAllGolemaServer() {
		List<GolemaServer> servers = new ArrayList<>();
		try (Jedis jedis = getRedisAccess().getJedisPool().getResource()) {
			List<Integer> ids = jedis.smembers(LOADASH_PREFIX_STORAGE_ID).stream().map(str -> Integer.valueOf(str))
					.collect(Collectors.toList());
			int[] i = new int[ids.size()];
			for (int k = 0; k < i.length; k++)
				i[k] = ids.get(k);
			servers.addAll(getGolemaServer(i));
		}
		return servers;
	}
}