package net.golema.loadash.playpen;

import net.golema.loadash.LoadashCommons;
import net.golema.loadash.servers.GameType;
import net.golema.loadash.servers.GolemaServer;
import net.golema.loadash.servers.MapInfos;
import net.golema.loadash.servers.ServerStatus;
import net.golema.loadash.servers.ServerType;

/**
 * LoadBalancer qui s'occupe de gérer le démarrage des Serveurs.
 */
public class LoadBalancer extends Thread {

	@Override
	public void run() {
		while (!(this.isInterrupted())) {

			// Détection des Serveurs qui ont besoins d'êtres Démarrés
			for (GameType gameType : GameType.values()) {
				if ((gameType != null) && (!(gameType.isOnlyHostGame()))) {

					// Détection de démarrage Publique
					int available = 0;
					int availableVIPMap = 0;

					// Gestion de Compteurs des différents Jeux
					for (GolemaServer golemaServer : LoadashPlaypen.getInstance().getLoadashCommons()
							.getAllGolemaServer()) {

						// Gestion des Serveur Public & VIP
						if (golemaServer != null) {

							// Détection du nombre de Serveurs Disponibles VIP & Publiques
							if ((golemaServer.isJoinable())
									&& ((golemaServer.getServerType().equals(ServerType.HUB))
											|| (golemaServer.getServerType().equals(ServerType.GAMES))
											|| (golemaServer.getServerType().equals(ServerType.VIPGAMES)))
									&& (golemaServer.getGameType().equals(gameType))
									&& (!(golemaServer.getGameType().isOnlyHostGame()))
									&& (!(golemaServer.isStartedSoon()))) {
								available++;
							}

							// Détection du nombre de Serveurs Disponibles Cartes VIP
							for (MapInfos maps : gameType.getGameVIPMaps()) {
								if ((!(gameType.getGameVIPMaps().isEmpty())
										&& (!(gameType.getGameVIPMaps().size() >= 1)))) {
									if ((golemaServer.isJoinable())
											&& ((golemaServer.getServerType().equals(ServerType.GAMES))
													|| (golemaServer.getServerType().equals(ServerType.VIPGAMES)))
											&& (golemaServer.getMapInfos().equals(maps))
											&& (golemaServer.getMapInfos().isVIPMap())
											&& (!(golemaServer.getMapInfos().isOnlyHostMap()))
											&& (!(golemaServer.isStartedSoon()))) {
										availableVIPMap++;
									}
								}
							}
						}
					}

					// Démarrer les Serveurs (Publique & VIP) en cas de besoin.
					if (available < gameType.getMinServerAvailable()) {
						try {
							if ((!(gameType.getGameMaps().isEmpty())) && (gameType.getGameMaps().size() > 0)
									&& (!(gameType.isOnlyHostGame()))) {

								// Vérifier le ServerType & MapsInfos
								ServerType serverType = gameType.equals(GameType.HUB) ? ServerType.HUB
										: ServerType.GAMES;
								MapInfos mapInfos = gameType.getRandomGameMap();

								// Vérifier si le Jeu est un Jeu VIP
								if (gameType.isVIPGame()) {
									serverType = ServerType.VIPGAMES;
									if ((!gameType.getGameVIPMaps().isEmpty())
											&& (gameType.getGameVIPMaps().size() > 0))
										mapInfos = gameType.getRandomGameVIPMap();
								}

								// Executer la demande de démarrage d'un Serveur
								LoadashPlaypen.getInstance().startServer(gameType, serverType, ServerStatus.INSTALL,
										mapInfos, null, null);
								available += 1;
							}
						} catch (Exception exception) {
							exception.printStackTrace();
							System.err.println(LoadashCommons.LOADASH_PREFIX
									+ "Impossible de démarrer un serveur nécessaire... (" + gameType.getName() + ")");
						}
					}

					// Démarrer les Serveurs (Cartes VIP) en cas de besoin.
					if (availableVIPMap < 1) {
						try {
							if ((!(gameType.getGameVIPMaps().isEmpty())) && (gameType.getGameVIPMaps().size() > 0)
									&& (!(gameType.isOnlyHostGame()))) {

								// Vérifier le ServerType & MapsInfos
								MapInfos mapInfos = gameType.getRandomGameVIPMap();

								// Executer la demande de démarrage d'un Serveur avec Carte VIP
								LoadashPlaypen.getInstance().startServer(gameType, ServerType.VIPGAMES,
										ServerStatus.INSTALL, mapInfos, null, null);
								availableVIPMap += 1;
							}
						} catch (Exception exception) {
							exception.printStackTrace();
							System.err.println(LoadashCommons.LOADASH_PREFIX
									+ "Impossible de démarrer un serveur nécessaire pour une Carte VIP... ("
									+ gameType.getName() + ")");
						}
					}
				}
			}

			// Execution du Thread
			try {
				Thread.sleep(25 * 1000);
			} catch (InterruptedException exception) {
				exception.printStackTrace();
			}
		}
		super.run();
	}
}