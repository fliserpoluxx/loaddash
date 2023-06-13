package net.golema.loadash.servers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Entity : GolemaServer
 */
public class GolemaServer {

	private int id;

	private GameType gameType;
	private ServerType serverType;
	private ServerStatus serverStatus;
	private MapInfos mapInfos;

	private UUID uuid, coordinatorUUID;
	private String ip;
	private int port, maxPlayers, currentPlayers;
	private String mapName;

	private String hostName;
	private UUID hostUUID;
	private List<UUID> hostPlayersList;

	private boolean whitelist;
	private List<UUID> whitelistedList;
	private List<UUID> playersOnlineList;
	private Map<String, Integer> rankOnline;

	private int boostCoins;

	private boolean gameServer, spectator, joinable, startedSoon;

	private List<String> properties;

	/**
	 * Former un Object pour un GolemaServer.
	 * 
	 * @param id
	 * @param gameType
	 * @param serverType
	 * @param serverStatus
	 * @param mapInfos
	 * @param uuid
	 * @param coordinatorUUID
	 * @param ip
	 * @param port
	 * @param maxPlayers
	 * @param currentPlayers
	 * @param mapName
	 * @param hostName
	 * @param hostUUID
	 * @param whitelist
	 * @param gameServer
	 * @param spectator
	 * @param joinable
	 * @param startedSoon
	 */
	public GolemaServer(int id, GameType gameType, ServerType serverType, ServerStatus serverStatus, MapInfos mapInfos,
			UUID uuid, UUID coordinatorUUID, String ip, int port, int maxPlayers, int currentPlayers, String hostName,
			UUID hostUUID, boolean whitelist, boolean gameServer, boolean spectator, boolean joinable,
			boolean startedSoon) {
		this.id = id;
		this.gameType = gameType;
		this.serverType = serverType;
		this.serverStatus = serverStatus;
		this.mapInfos = mapInfos == null ? MapInfos.UNKNOW : mapInfos;
		this.uuid = uuid;
		this.coordinatorUUID = coordinatorUUID;
		this.ip = ip;
		this.port = port;
		this.maxPlayers = maxPlayers;
		this.currentPlayers = currentPlayers;
		this.mapName = mapInfos.getName();
		this.hostName = hostName;
		this.hostUUID = hostUUID;
		this.hostPlayersList = new ArrayList<UUID>();
		this.whitelist = whitelist;
		this.whitelistedList = new ArrayList<UUID>();
		this.playersOnlineList = new ArrayList<UUID>();
		this.rankOnline = new HashMap<String, Integer>();
		this.boostCoins = 1;
		this.gameServer = gameServer;
		this.spectator = spectator;
		this.joinable = joinable;
		this.startedSoon = startedSoon;
		this.properties = new ArrayList<String>();
	}

	public int getID() {
		return id;
	}

	public void setID(int id) {
		this.id = id;
	}

	public GameType getGameType() {
		return gameType;
	}

	public void setGameType(GameType gameType) {
		this.gameType = gameType;
	}

	public ServerType getServerType() {
		return serverType;
	}

	public void setServerType(ServerType serverType) {
		this.serverType = serverType;
	}

	public ServerStatus getServerStatus() {
		return serverStatus;
	}

	public void setServerStatus(ServerStatus serverStatus) {
		this.serverStatus = serverStatus;
	}

	public MapInfos getMapInfos() {
		return mapInfos;
	}

	public void setMapInfos(MapInfos mapInfos) {
		this.mapInfos = mapInfos;
	}

	public UUID getUUID() {
		return uuid;
	}

	public void setUUID(UUID uuid) {
		this.uuid = uuid;
	}

	public UUID getCoordinatorUUID() {
		return coordinatorUUID;
	}

	public void setCoordinatorUUID(UUID coordinatorUUID) {
		this.coordinatorUUID = coordinatorUUID;
	}

	public String getIP() {
		return ip;
	}

	public void setIP(String ip) {
		this.ip = ip;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public int getMaxPlayers() {
		return maxPlayers;
	}

	public void setMaxPlayers(int maxPlayers) {
		this.maxPlayers = maxPlayers;
	}

	public int getCurrentPlayers() {
		return currentPlayers;
	}

	public void setCurrentPlayers(int currentPlayers) {
		this.currentPlayers = currentPlayers;
	}

	public String getMapName() {
		return mapName;
	}

	public void setMapName(String mapName) {
		this.mapName = mapName;
	}

	public String getHostName() {
		return hostName;
	}

	public void setHostName(String hostName) {
		this.hostName = hostName;
	}

	public UUID getHostUUID() {
		return hostUUID;
	}

	public void setHostUUID(UUID hostUUID) {
		this.hostUUID = hostUUID;
	}

	public List<UUID> getHostPlayersList() {
		return hostPlayersList;
	}

	public void setHostPlayersList(List<UUID> hostPlayersList) {
		this.hostPlayersList = hostPlayersList;
	}

	public boolean isWhitelist() {
		return whitelist;
	}

	public void setWhitelist(boolean whitelist) {
		this.whitelist = whitelist;
	}

	public List<UUID> getWhitelistedList() {
		return whitelistedList;
	}

	public void setWhitelistedList(List<UUID> whitelistedList) {
		this.whitelistedList = whitelistedList;
	}

	public List<UUID> getPlayersOnlineList() {
		return playersOnlineList;
	}

	public void setPlayersOnlineList(List<UUID> playersOnline) {
		this.playersOnlineList = playersOnline;
	}

	public Map<String, Integer> getRankOnline() {
		return rankOnline;
	}

	public void setRankOnline(Map<String, Integer> rankOnline) {
		this.rankOnline = rankOnline;
	}

	public int getBoostCoins() {
		return boostCoins;
	}

	/**
	 * Vérifier si le serveur est Boosté.
	 * 
	 * @return
	 */
	public boolean isBoostedGame() {
		return boostCoins > 1;
	}

	public void setBoostCoins(int boostCoins) {
		if (boostCoins < 1)
			boostCoins = 1;
		if (boostCoins > 4)
			boostCoins = 4;
		this.boostCoins = boostCoins;
	}

	public boolean isGameServer() {
		return gameServer;
	}

	public void setGameServer(boolean gameServer) {
		this.gameServer = gameServer;
	}

	public boolean isSpectator() {
		return spectator;
	}

	public void setSpectator(boolean spectator) {
		this.spectator = spectator;
	}

	public boolean isJoinable() {
		return joinable;
	}

	public void setJoinable(boolean joinable) {
		this.joinable = joinable;
	}

	public boolean isStartedSoon() {
		return startedSoon;
	}

	public void setStartedSoon(boolean startedSoon) {
		this.startedSoon = startedSoon;
	}

	public List<String> getProperties() {
		return properties;
	}

	public void setProperties(List<String> properties) {
		this.properties = properties;
	}

	/**
	 * Récupérer le nom complet du Serveur (BungeeCord).
	 * 
	 * @return
	 */
	public String getServerName() {
		if (serverType.equals(ServerType.HUB) || serverType.equals(ServerType.GAMES)
				|| serverType.equals(ServerType.UNKNOW))
			return gameType.getName().toLowerCase() + "-" + id;
		return gameType.getName().toLowerCase() + "-" + serverType.getName().toLowerCase() + "-" + id;
	}
}