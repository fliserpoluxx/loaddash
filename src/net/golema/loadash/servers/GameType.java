package net.golema.loadash.servers;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public enum GameType {

	UNKNOW("Unknow", "Unknow", -1, false, false, true), HUB("Hub", "Hub", 2, false, false, false),

	HUNGERGAMES("HungerGames", "HungerGames", 1, false, true, false), 
	HUNGERGAMES_TEAMS("HungerGamesTeams", "HungerGames", 1, false, true, false, GameType.HUNGERGAMES),

	SKYRUSH("SkyRush", "SkyRush", 2, false, true, false), 
	PUNCHOUT("Punchout", "Punchout", 1, false, true, false);

	private String name, displayName;
	private int minServerAvailable;
	private boolean vipGame, hostGame, onlyHostGame;
	private GameType basedOn = null;

	/**
	 * Gestionnaire des Packaging Playpen.
	 * 
	 * @param name
	 * @param displayName
	 * @param minServerAvailable
	 * @param vipGame
	 * @param hostGame
	 * @param onlyHostGame
	 */
	private GameType(String name, String displayName, int minServerAvailable, boolean vipGame, boolean hostGame,
			boolean onlyHostGame) {
		this(name, displayName, minServerAvailable, vipGame, hostGame, onlyHostGame, null);
	}

	/**
	 * Construire un Packaging Depends PlayPen.
	 * 
	 * @param name
	 * @param displayName
	 * @param minServerAvailable
	 * @param vipGame
	 * @param hostGame
	 * @param onlyHostGame
	 * @param basedOn
	 */
	private GameType(String name, String displayName, int minServerAvailable, boolean vipGame, boolean hostGame,
			boolean onlyHostGame, GameType basedOn) {
		this.name = name;
		this.displayName = displayName;
		this.minServerAvailable = minServerAvailable;
		this.vipGame = vipGame;
		this.hostGame = hostGame;
		this.onlyHostGame = onlyHostGame;
		this.basedOn = basedOn;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getMinServerAvailable() {
		return minServerAvailable;
	}

	public void setMinServerAvailable(int minServerAvailable) {
		this.minServerAvailable = minServerAvailable;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	private GameType getBasedOn() {
		return basedOn;
	}

	public void setBasedOn(GameType basedOn) {
		this.basedOn = basedOn;
	}

	public boolean isVIPGame() {
		return vipGame;
	}

	public void setVipGame(boolean vipGame) {
		this.vipGame = vipGame;
	}

	public boolean isHostGame() {
		return hostGame;
	}

	public void setHostGame(boolean hostGame) {
		this.hostGame = hostGame;
	}

	public boolean isOnlyHostGame() {
		return onlyHostGame;
	}

	public void setOnlyHostGame(boolean onlyHostGame) {
		this.onlyHostGame = onlyHostGame;
	}

	/**
	 * Récupérer les Maps d'un GameType.
	 * 
	 * @return
	 */
	public List<MapInfos> getGameMaps() {
		List<MapInfos> maps = new ArrayList<MapInfos>();
		for (MapInfos mapInfos : MapInfos.values())
			if (mapInfos != null && (!(mapInfos.isOnlyHostMap())) && (!(mapInfos.isVIPMap()))
					&& ((mapInfos.getGameType().equals(this)
							|| (this.getBasedOn() != null && mapInfos.getGameType().equals(this.getBasedOn())))))
				maps.add(mapInfos);
		return maps;
	}

	/**
	 * Récupérer toutes les Maps d'un Jeu pour le Host.
	 * 
	 * @return
	 */
	public List<MapInfos> getGameHostMaps() {
		List<MapInfos> maps = new ArrayList<MapInfos>();
		for (MapInfos mapInfos : MapInfos.values())
			if (mapInfos != null && ((mapInfos.getGameType().equals(this)
					|| (this.getBasedOn() != null && mapInfos.getGameType().equals(this.getBasedOn())))))
				maps.add(mapInfos);
		return maps;
	}

	/**
	 * Récupérer toutes les Maps VIP d'un Joueur.
	 * 
	 * @return
	 */
	public List<MapInfos> getGameVIPMaps() {
		List<MapInfos> maps = new ArrayList<MapInfos>();
		for (MapInfos mapInfos : MapInfos.values())
			if (mapInfos != null && (mapInfos.isVIPMap()) && ((mapInfos.getGameType().equals(this)
					|| (this.getBasedOn() != null && mapInfos.getGameType().equals(this.getBasedOn())))))
				maps.add(mapInfos);
		return maps;
	}

	/**
	 * Récupérer une Map du Jeu Aléatoire.
	 * 
	 * @return
	 */
	public MapInfos getRandomGameMap() {
		if (this.getGameMaps().isEmpty() || this.getGameMaps().size() <= 0)
			return MapInfos.UNKNOW;
		return this.getGameMaps().get(new Random().nextInt(this.getGameMaps().size()));
	}

	/**
	 * Récupérer une Map VIP du Jeu Aléatoire.
	 * 
	 * @return
	 */
	public MapInfos getRandomGameVIPMap() {
		if (this.getGameVIPMaps().isEmpty() || this.getGameVIPMaps().size() <= 0)
			return MapInfos.UNKNOW;
		return this.getGameVIPMaps().get(new Random().nextInt(this.getGameVIPMaps().size()));
	}

	/**
	 * Récupérer le Jeu d'Origine.
	 * 
	 * @param gameType
	 * @return
	 */
	public static GameType getOriginGameType(GameType gameType) {
		if (gameType.getBasedOn() == null)
			return gameType;
		return gameType.getBasedOn();
	}

	/**
	 * Récupérer un GameType par son Nom.
	 * 
	 * @param name
	 * @return
	 */
	public static GameType getGameType(String name) {
		for (GameType gameType : GameType.values()) {
			if (gameType.getName().equalsIgnoreCase(name))
				return gameType;
		}
		return GameType.UNKNOW;
	}
}