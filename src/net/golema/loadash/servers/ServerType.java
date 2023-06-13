package net.golema.loadash.servers;

public enum ServerType {

	UNKNOW("Unknow"), 
	HUB("Hub"), 
	GAMES("Games"),
	VIPGAMES("GamesVIP"),
	HOST("Host"),
	EVENTS("Events"),
	DEVELOPMENT("Development"); 

	private String name;

	/**
	 * Gestionnaire du Type de Serveur.
	 * 
	 * @param name
	 */
	private ServerType(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	/**
	 * Récupérer un Serveur Type par son Nom.
	 * 
	 * @param name
	 * @return
	 */
	public static ServerType geServerType(String name) {
		for (ServerType serverType : ServerType.values()) {
			if (serverType.getName().equalsIgnoreCase(name))
				return serverType;
		}
		return ServerType.UNKNOW;
	}
}