# Loadash

Introduction
--
Loadash est un système de serveur à la demande marchant avec [Playpen](https://github.com/PlayPen). Ce code est de la propriété intellectuelle de SpeedHeberg mais il peut être utilisé pour tous les projets sans demandes tant que le nom est donné. Ce projet utilise du machine learning pour augmenter les performances, il contient aussi un système de génération de map et un système de serveur à la demande (HOST) Loadash peut être utilisé comme plugin sur un proxy, mais aussi sur un serveur normal avec Spigot-server. Mais il peut dans d'autre cas être utilisé à d'autre fin que minecraft. Ce projet a été codé par Matthieu (Faidden) pour GolemaMC.

Instalation
--
Dans un premier temps, il faut compresser le Loadash à l'aide de Maven ou autre. Ensuite, pour installer loadash il vous suffit dans un premier temps de démarrer un serveur network playpen avec la commande : `playpen-network` .  Après que cela est fait, arrêter ce dernier et glisser le Loadash.jar dans le dossier plugin de votre network playpen. Redémarrer votre network et complété le fichier de configuration de votre base de donnée Redis qui se trouve dans le dossier qui est apparu dans le docier plugin de votre network (Il doit s'appeler Loadash). La partie playpen est fini. Après cela, il vous suffira de redémarrer un proxy (type : BungeeCord). Ajouté Lle Loadash.jar et un autre plugin qui se nomme RedisBungee. Ensuite configuré à votre guise le fichier de configuration dans le dossier qui est apparu (Par exemple : si vous voulez avoir les host ou autre et configurer le serveur Redis.)

Ajout de Type de serveur
--
Loadash va dès son lancement sur le network essayé de démarrer automatiquement les serveurs nécessaire. Pour cela dans un premier temps vous devait aller dans les codes sources du plugin ajouté des lignes pour spécifier vos types de serveur/jeux (Chemin d'accès : `/src/net/golema/loadash/servers/GameType.java`). Pour ajouter un serveur, il suffit d'ajouter un élément dans l'énumération. Voici un exemple : 

`0("1", "2", 3, 4, 5, 6, 7),`

Voici les éléments de la ligne :
- 0: Le TYPE de serveur en majuscule.
- 1: Le TYPE de serveur en minuscule et en string obligatoir (avec des guillemet).
- 2: Le TYPE de serveur comme vous voulez (Ce que les joueurs vont voir).
- 3: Le nombre de serveur minimun que vous voulez que le serveur laisse ouvert en permanence.
- 4: BOOLEAN (true ou false) si le serveur est que disponible au VIP.
- 5: BOOLEAN (true ou false) si le serveur est hostable (disponible en serveur à la demande).
- 6: BOOLEAN (true ou false) si le serveur est que disponible en host.
- 7: Nom d'un type de serveur (Ex: `GameType.HUNGERGAMES`) si le serveur est une sous catègorie. *Pas obligatoire*
Maintenant pour créer un paquet, il vous suffit de suivre le tuto présent dans le GitHub de [Playpen](https://github.com/PlayPen). pour les serveurs Minecraft ** Il faut appeler le serveur par son type en majuscule ! **.

Ajout de Map
--
Pour ajouter vos map, il faut aller dans les énumérations MapInfos `/src/net/golema/loadash/servers/MapInfos.java`. Puis, vous pouvez ajouter vos maps dans la liste avec cette ligne :

`0("1", "2", 3, 4, 5, 6, 7),`

Voici les éléments de la ligne :
- 0: Le TYPE de map en majuscule.
- 1: Le TYPE de map en minuscule et en string obligatoir (avec des guillemet).
- 2: Le TYPE de map comme vous voulez (Ce que les joueurs vont voir).
- 3: BOOLEAN (true ou false) si le map est que disponible au VIP.
- 4: BOOLEAN (true ou false) si le map est hostable (disponible en serveur à la demande).
- 5: BOOLEAN (true ou false) si le map est que disponible en host.
- 6: TYPE (Ex: `GameType.SKYRUSH`) de jeux pour cette map.
- 7: INT le nombre de slot pour une partie.

Maintenant le serveur démarrera les parties avec les différentes maps selon les performances.

Contacte
--
Mail: loadash@speed-heberg.fr
Site: [loadash.tech](https://loadash.tech/)
Discord: fliserpolux
