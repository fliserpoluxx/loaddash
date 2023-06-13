package net.golema.loadash.redis;

import java.util.Set;
import java.util.stream.Collectors;

import net.golema.loadash.LoadashCommons;
import redis.clients.jedis.Jedis;

public class RedisIDPool {

	private RedisAccess redisAccess;

	/**
	 * Gestionnaire des ID de Serveur.
	 * 
	 * @param redisAccess
	 */
	public RedisIDPool(RedisAccess redisAccess) {
		this.redisAccess = redisAccess;
	}

	/**
	 * Supprimer un ensemble de Key Membre.
	 * 
	 * @param id
	 */
	public void returnId(int id) {
		try (Jedis jedis = redisAccess.getJedisPool().getResource()) {
			jedis.srem(LoadashCommons.LOADASH_PREFIX_STORAGE_ID, id + "");
		}
	}

	/**
	 * Générer un ID de Serveur.
	 * 
	 * @return
	 */
	public int nextId() {
		try (Jedis jedis = redisAccess.getJedisPool().getResource()) {

			Set<String> used = jedis.smembers(LoadashCommons.LOADASH_PREFIX_STORAGE_ID);
			if (used == null)
				return storeId(0);

			Set<Integer> ids = used.stream().map(i -> Integer.valueOf(i)).collect(Collectors.toSet());
			int id = 0;
			do {
				id++;
			} while (ids.contains(id));
			return storeId(id);
		}
	}

	/**
	 * Sauvegarder un ID de Serveur sous Redis.
	 * 
	 * @param id
	 * @return
	 */
	private int storeId(int id) {
		try (Jedis jedis = redisAccess.getJedisPool().getResource()) {
			jedis.sadd(LoadashCommons.LOADASH_PREFIX_STORAGE_ID, id + "");
		}
		return id;
	}

	public RedisAccess getRedisAccess() {
		return redisAccess;
	}
}