# JavaChatServer

## Introduction
JavaChatServer est un programme console CLI (Command Line Interface) écrit en Java permettant la communication textuelle entre plusieurs clients connectés au serveur via le protocole TCP et le programme client correspondant (voir JavaChatClient - Client de messagerie directe avec interface graphique : https://github.com/DevBlocks42/JavaChatClient).

Le fonctionnement du programme a été testé sur les systèmes d'exploitations suivants : Ubuntu 22.04 (Desktop & Live Server) et Windows 10. Néanmoins, celui-ci devrait pouvoir être exécuté sur n'importe quel système disposant de Java.

## Fonctionnement

Une fois lancé, le programme créer un `ServerSocket` permettant aux clients de joindre le serveur via le protocole TCP. Tant que l'objet `ServerSocket` n'est pas interrompu, on attend une connexion TCP sur le port 7777 (par défaut), s'il y a une connexion entrante, on accepte le nouveau client, puis on lance un `Thread` pour gérer les entrées/sorties réseaux relatives à ce client. 

Dans un premier temps, le serveur attend que le client annonce son pseudonyme, si le pseudonyme envoyé n'est pas actuellement utilisé par un autre client, on authentifie l'utilisateur, sinon, on lui notifie que le pseudonyme est déjà utilisé, puis on ferme l'objet `Socket` associé au client.