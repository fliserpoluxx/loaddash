package net.golema.loadash.servers;

public enum MapInfos {

	UNKNOW("Unknow", "Unknow", false, false, false, GameType.UNKNOW, -1), 
	LOBBY("Hub", "Hub", false, false, false, GameType.HUB, 50),

	TOMBSTONE("Tombstone", "Tombstone", false, true, false, GameType.HUNGERGAMES, 24),
	XINTIAN("XinTian", "XinTian", false, true, false, GameType.HUNGERGAMES, 24),
	EMBRASEMENT("Embrasement", "Embrasement", false, true, false, GameType.HUNGERGAMES, 24),
	
	ALOIPIA("Aloipia", "Aloipia", false, true, false, GameType.SKYRUSH, 16),
	AMANOS("Amanos", "Amanos", false, true, false, GameType.SKYRUSH, 24),
	JIAO("Jiao", "Jiao", false, true, false, GameType.SKYRUSH, 24),
	
	FROZEN("Frozen", "Frozen", false, true, false, GameType.PUNCHOUT, 12),
	FACIA("Facia", "Facia", false, true, false, GameType.PUNCHOUT, 12),
	HYTHE("Hythe", "Hythe", false, true, false, GameType.PUNCHOUT, 12);

	private String name, displayName;
	private boolean vipMap, hostMap, onlyHostMap;
	private GameType gameType;
	private int maxPlayers;

	/**
	 * Gestionnaire des Maps de Serveur.
	 * 
	 * @param name
	 * @param displayName
	 * @param vipMap
	 * @param hostMap
	 * @param onlyHostMap
	 * @param gameType
	 * @param maxPlayers
	 */
	private MapInfos(String name, String displayName, boolean vipMap, boolean hostMap, boolean onlyHostMap,
			GameType gameType, int maxPlayers) {
		this.name = name;
		this.displayName = displayName;
		this.vipMap = vipMap;
		this.hostMap = hostMap;
		this.onlyHostMap = onlyHostMap;
		this.gameType = gameType;
		this.maxPlayers = maxPlayers;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public boolean isVIPMap() {
		return vipMap;
	}

	public void setVIPMap(boolean vipMap) {
		this.vipMap = vipMap;
	}

	public boolean isHostMap() {
		return hostMap;
	}

	public void setHostMap(boolean hostMap) {
		this.hostMap = hostMap;
	}

	public boolean isOnlyHostMap() {
		return onlyHostMap;
	}

	public void setOnlyHostMap(boolean onlyHostMap) {
		this.onlyHostMap = onlyHostMap;
	}

	public GameType getGameType() {
		return gameType;
	}

	public void setGameType(GameType gameType) {
		this.gameType = gameType;
	}

	public int getMaxPlayers() {
		return maxPlayers;
	}

	public void setMaxPlayers(int maxPlayers) {
		this.maxPlayers = maxPlayers;
	}

	/**
	 * Récupérer une Map par son Nom.
	 * 
	 * @param name
	 * @return
	 */
	public static MapInfos getMapsInfos(String name) {
		for (MapInfos mapInfos : MapInfos.values()) {
			if (mapInfos.getName().equalsIgnoreCase(name))
				return mapInfos;
		}
		return MapInfos.UNKNOW;
	}
}