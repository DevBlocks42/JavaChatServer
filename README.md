# JavaChatServer

## Introduction
JavaChatServer est un programme console CLI (Command Line Interface) écrit en Java permettant la communication textuelle entre plusieurs clients connectés au serveur via le protocole TCP et le programme client correspondant (voir JavaChatClient - Client de messagerie directe avec interface graphique : https://github.com/DevBlocks42/JavaChatClient).

Le fonctionnement du programme a été testé sur les systèmes d'exploitations suivants : Ubuntu 22.04 (Desktop & Live Server) et Windows 10. Néanmoins, celui-ci devrait pouvoir être exécuté sur n'importe quel système disposant de Java.

## Fonctionnement

Une fois lancé, le programme créer un `ServerSocket` permettant aux clients de joindre le serveur via le protocole TCP. Tant que l'objet `ServerSocket` n'est pas interrompu, on attend une connexion TCP sur le port 7777 (par défaut), s'il y a une connexion entrante, on accepte le nouveau client, puis on lance un `Thread` pour gérer les entrées/sorties réseaux relatives au client. On lance autant de Thread en parallèle que de clients se connectent. 

Dans un premier temps, le serveur attend que le client annonce son pseudonyme, si le pseudonyme envoyé n'est pas actuellement utilisé par un autre client, on authentifie l'utilisateur, sinon, on lui notifie que le pseudonyme est déjà utilisé, puis on ferme l'objet `Socket` associé au client.

Une fois authentifié, le serveur reçoit et traite les messages reçus des clients. On distingue plusieurs formes de traitements : 
  - (1) Les messages simples 
  - (2) Les messages complexes (gras, souligné, italique, choix de couleur d'une partie du texte etc..) 
  - (3) Les commandes (exemple /mp NOM_UTILISATEUR MESSAGE)


(1) -> Les messages sont renvoyés à tous les clients connectés au serveur (broadcast)
(2) -> Les messages sont formatés i.e on transforme les balises du message en balises html (exemple : `[b][/b]` devient `<b></b>`, puis on renvoie le message à tous les clients (broadcast).
(3) -> Les commandes commencent obligatoirement par le caractère '/', le comportement du programme s'adapte en fonction de la commande reçue.


![Diagramme_Client_Serveur](https://github.com/DevBlocks42/JavaChatServer/assets/136115859/13126ae9-5daf-423e-805a-dbaa5f8ad07e)


## Manuel

