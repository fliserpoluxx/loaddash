package net.golema.loadash.servers;

public enum ServerStatus {
	
	UNKNOW("Unknow"),
	INSTALL("Installing"),
	PUBLIC("Public"),
	VIP("VIP"),
	HOST("Host"), 
	INGAME("InGame"),
	STAFF("Staff"), 
	DEVELOPMENT("Development"),
	ADMIN("Admin"),
	PRIVATE("Private"),
	REBOOT("Reboot");

	private String name;

	/**
	 * Gestionnaire du Status de Serveur.
	 * 
	 * @param name
	 */
	private ServerStatus(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	/**
	 * Récupérer un ServerStatus par son Nom.
	 * 
	 * @param name
	 * @return
	 */
	public static ServerStatus getServerStatus(String name) {
		for (ServerStatus serverStatus : ServerStatus.values()) {
			if (serverStatus.getName().equalsIgnoreCase(name))
				return serverStatus;
		}
		return ServerStatus.UNKNOW;
	}
}