package net.golema.loadash.bungeecord;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;

import net.golema.loadash.LoadashCommons;
import net.golema.loadash.redis.RedisCredentials;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

public class LoadashBungeeCord extends Plugin {

	private static LoadashBungeeCord instance;

	private Configuration config;
	private LoadashCommons loadashCommons;

	private UpdateServerThread updateServerThread;

	@Override
	public void onLoad() {

		// Initialisation de la Config et l'Instance
		instance = this;
		this.loadConfig();

		// Démarrer la base de données Redis
		RedisCredentials redisCredentials = new RedisCredentials(config.getString("redis.hostname"),
				config.getInt("redis.port"), config.getString("redis.password"), config.getInt("redis.database"),
				config.getBoolean("redis.use-password"));
		this.loadashCommons = new LoadashCommons(getLogger(), redisCredentials);

		super.onLoad();
	}

	@SuppressWarnings("deprecation")
	@Override
	public void onEnable() {

		// Démarrer le Thread d'enregistrement des Serveurs
		this.updateServerThread = new UpdateServerThread();
		getExecutorService().execute(updateServerThread);

		super.onEnable();
	}

	@Override
	public void onDisable() {
		this.updateServerThread.stop();
		super.onDisable();
	}

	/**
	 * Générer la Configuration sous BungeeCord.
	 */
	private void loadConfig() {
		if (!getDataFolder().exists())
			getDataFolder().mkdir();
		File file = new File(getDataFolder(), "config.yml");
		if (!file.exists()) {
			try (InputStream in = getResourceAsStream("config.yml")) {
				Files.copy(in, file.toPath());
			} catch (IOException e) {
				e.printStackTrace();
				getLogger().severe(LoadashCommons.LOADASH_PREFIX + "Impossible de générer la configuration!");
			}
		}
		try {
			config = ConfigurationProvider.getProvider(YamlConfiguration.class)
					.load(new File(getDataFolder(), "config.yml"));
		} catch (IOException e) {
			e.printStackTrace();
			getLogger().severe(LoadashCommons.LOADASH_PREFIX + "Impossible de charger la configuration!");
		}
	}

	public LoadashCommons getLoadashCommons() {
		return loadashCommons;
	}

	public static LoadashBungeeCord getInstance() {
		return instance;
	}
}