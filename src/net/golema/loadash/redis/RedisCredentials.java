package net.golema.loadash.redis;

public class RedisCredentials {

	private String host;
	private int port;
	private String password;
	private int database;
	private boolean usePassword;

	public RedisCredentials(String host, int port, String password, int database, boolean usePassword) {
		this.host = host;
		this.port = port;
		this.password = password;
		this.database = database;
		this.usePassword = usePassword;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public int getDatabase() {
		return database;
	}

	public void setDatabase(int database) {
		this.database = database;
	}

	public boolean isUsePassword() {
		return usePassword;
	}

	public void setUsePassword(boolean usePassword) {
		this.usePassword = usePassword;
	}
}