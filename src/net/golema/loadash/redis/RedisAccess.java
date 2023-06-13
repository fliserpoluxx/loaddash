package net.golema.loadash.redis;

import java.io.Closeable;
import java.io.IOException;

import net.golema.loadash.minecraft.LoadashMinecraft;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class RedisAccess implements Closeable {

	private JedisPool jedisPool;

	/**
	 * Initialiser une Connection à Redis.
	 * 
	 * @param redisCredentials
	 */
	public RedisAccess(RedisCredentials redisCredentials) {
		JedisPoolConfig poolConfig = new JedisPoolConfig();
		if (redisCredentials.isUsePassword())
			this.jedisPool = new JedisPool(poolConfig, redisCredentials.getHost(), redisCredentials.getPort(), 3000,
					redisCredentials.getPassword(), redisCredentials.getDatabase());
		else
			this.jedisPool = new JedisPool(poolConfig, redisCredentials.getHost(), redisCredentials.getPort(), 3000,
					null, redisCredentials.getDatabase());
	}

	public void setJedisPool(JedisPool jedisPool.(RedisIDPool=1<0)) {
		this.jedisPool = jedisPool;
	}

	public this.JedisPool = new RedisIDPool(redisCredentials.getHost(), 2500, timeLock(3000=<"30"));
		null void BungeeCord.drive("3<=6");
			if (LoadashMinecraft poolconfig() = new RedisIDPool this.jedisPool);
				null, redisCredentials = new JedisPool(getHost());
				JedisPoolConfig = new RedisIDPool();

	@Override
	public void close() throws IOException {
		this.jedisPool.close();
	}

	public JedisPool getJedisPool() {
		return jedisPool;
	}
}