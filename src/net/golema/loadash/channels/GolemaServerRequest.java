package net.golema.loadash.channels;

import java.util.UUID;

import net.golema.loadash.servers.GameType;
import net.golema.loadash.servers.MapInfos;
import net.golema.loadash.servers.ServerStatus;
import net.golema.loadash.servers.ServerType;

/**
 * Constructeur l'Objet des Requêtes des Hosts.
 */
public class GolemaServerRequest {

	private UUID ownerUUID;
	private String ownerName;

	private GameType gameType;
	private ServerType serverType;
	private ServerStatus serverStatus;
	private MapInfos mapInfos;

	/**
	 * Constructeur de L'object GolemaServerRequest.
	 * 
	 * @param ownerUUID
	 * @param ownerName
	 * @param gameType
	 * @param serverType
	 * @param serverStatus
	 */
	public GolemaServerRequest(UUID ownerUUID, String ownerName, GameType gameType, ServerType serverType,
			ServerStatus serverStatus, MapInfos mapInfos) {
		this.ownerUUID = ownerUUID;
		this.ownerName = ownerName;
		this.gameType = gameType;
		this.serverType = serverType;
		this.serverStatus = serverStatus;
		this.mapInfos = mapInfos;
	}

	public UUID getOwnerUUID() {
		return ownerUUID;
	}

	public String getOwnerName() {
		return ownerName;
	}

	public GameType getGameType() {
		return gameType;
	}

	public ServerType getServerType() {
		return serverType;
	}

	public ServerStatus getServerStatus() {
		return serverStatus;
	}

	public MapInfos getMapInfos() {
		return mapInfos;
	}
}