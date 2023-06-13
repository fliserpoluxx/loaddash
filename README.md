# Loadash

Introduction
--
Loadash est un système de serveur à la demande marchand avec playpen. Ce code est de la propriété intélectuelle de SpeedHeberg mais il peut être utilisé pour tous les projets sans demandes tant que le nom est donné. Ce projet utilse du machine learning pour auguementé les performances, il contient aussi un système de génération de map et un système de serveur à la demande (HOST) Loadash peut être utilisé comme plugin sur un proxy mais aussi sur un serveur normal avec Spigot-server. Mais il peut dans d'autre cas être utilisé à d'autre fin que minecraft. Ce projet a été codé par Matthieu (Faidden) pour GolemaMC.

Instalation
--
Dans un premier temps, il faut compresser le Loadash à l'aide de Maven ou autre. Enssuite, pour installer loadash il vous suffit dans un premier temps de démarer un serveur network playpen avec la commande: `playpen-network`. Après que cela est fait, arrêter ce dernier et gliser le Loadash.jar dans le docier plugin de votre network playpen. Redémarer votre network et complété le fichier de configuration de votre base de donné Redis qui se trouve dans le docier qui est apparut dans le docier plugin de votre network (Il doit s'appeler Loadash). Ensuite la partie playpen est finit. Après cela il vous suffirat de démarer un proxy (type: BungeeCord). Enssuite configuré à votre guise le fichier de configuration dans le docier qui est apparut (Par exemple choisisser si vous voulez avoir les host ou autre et configurer le serveur Redis.)

Ajout de Type de serveur
--
Loadash va dès son lancement sur le network essayé de démarer automatiquement les serveurs néssaire.
