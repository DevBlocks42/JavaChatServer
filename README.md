# JavaChatServer

## Introduction
JavaChatServer est un programme console CLI (Command Line Interface) écrit en Java permettant la communication textuelle entre plusieurs clients connectés au serveur via le protocole TCP et le programme client correspondant (voir JavaChatClient - Client de messagerie directe avec interface graphique : https://github.com/DevBlocks42/JavaChatClient).

Le fonctionnement du programme a été testé sur les systèmes d'exploitations suivants : Ubuntu 22.04 (Desktop & Live Server) et Windows 10. Néanmoins, celui-ci devrait pouvoir être exécuté sur n'importe quel système disposant de Java.

## Fonctionnement

Une fois lancé, le programme créer un `ServerSocket` permettant aux clients de joindre le serveur via le protocole TCP. Tant que l'objet `ServerSocket` n'est pas interrompu, on attend une connexion TCP sur le port 7777 (par défaut), s'il y a une connexion entrante, on accepte le nouveau client, puis on lance un `Thread` pour gérer les entrées/sorties réseaux relatives au client. On lance autant de Thread en parallèle que de clients se connectent. 

Dans un premier temps, le serveur attend que le client annonce son pseudonyme, si le pseudonyme envoyé n'est pas actuellement utilisé par un autre client, on authentifie l'utilisateur, sinon, on lui notifie que le pseudonyme est déjà utilisé, puis on ferme l'objet `Socket` associé au client.

  Une fois authentifié, le serveur reçoit et traite les messages reçus des clients. 
  On distingue plusieurs formes de traitements : 
  
    - (1) Les messages simples 
    - (2) Les messages complexes (gras, souligné, italique, choix de couleur d'une partie du texte etc..) 
    - (3) Les commandes (exemple /mp NOM_UTILISATEUR MESSAGE)


  (1) -> Les messages sont renvoyés à tous les clients connectés au serveur (broadcast)


  (2) -> Les messages sont formatés i.e on transforme les balises du message en balises html (exemple : `[b][/b]` devient `<b></b>`, puis on renvoie le message à tous les clients (broadcast).


  (3) -> Les commandes commencent obligatoirement par le caractère '/', le comportement du programme s'adapte en fonction de la commande reçue.

Par ailleurs, un thread est lancé en parallèle pour gérer les commandes serveur à l'aide d'un Scanner pour lire l'entrée standard dans l'interface de commande.


## Modèle de communication client-serveur

![Diagramme_Client_Serveur](https://github.com/DevBlocks42/JavaChatServer/assets/136115859/13126ae9-5daf-423e-805a-dbaa5f8ad07e)


## Installation 
 
Installer maven

- Linux (debian/ubuntu) :

  ```sudo apt-get install maven```
  
- Windows : 

  ```https://maven.apache.org/download.cgi```

    
Utiliser la configuration xml suivante (pom.xml) :

```
  <?xml version="1.0" encoding="UTF-8"?>
    <project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
        <modelVersion>4.0.0</modelVersion>
        <groupId>com.javaroot</groupId>
        <artifactId>JavaChatServer</artifactId>
        <version>1.0-SNAPSHOT</version>
        <packaging>jar</packaging>
        <properties>
            <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
            <maven.compiler.source>11</maven.compiler.source>
            <maven.compiler.target>11</maven.compiler.target>
            <exec.mainClass>server.Main</exec.mainClass>
        </properties>
        <build>
            <plugins>
                <plugin>
                    <artifactId>maven-assembly-plugin</artifactId>
                    <version>2.4</version>
                    <configuration>
                        <descriptorRefs>
                            <descriptorRef>jar-with-dependencies</descriptorRef>
                        </descriptorRefs>
                        <archive>
                            <manifest>
                                <mainClass>server.Main</mainClass>
                            </manifest>
                        </archive>
                    </configuration>
                    <executions>
                        <execution>
                            <id>make-assembly</id>
                            <phase>package</phase>
                            <goals>
                                <goal>single</goal>
                            </goals>
                        </execution>
                    </executions>
                </plugin>
            </plugins>
        </build>
    </project>

```

Lancer la création du fichier exécutable JAR :

    maven clean install
  
ou
  
    maven.exe clean install
  
Lancer l'exécutable JAR : 
 
    java -jar target/JavaChatServer-1.0-SNAPSHOT-jar-with-dependencies.jar
  
ou
  
    java.exe -jar target/JavaChatServer-1.0-SNAPSHOT-jar-with-dependencies.jar

## Manuel d'utilisation

Changer le port associé au serveur : 

Modifier le fichier Main.java à la ligne 26 -> `int port = XXXX;`
Recompiler le programme à l'aide de maven, cf Installation.

Commandes Serveur : 
  
    quit -> ferme le serveur et déconnecte tous les clients.
    
    time -> Affiche l'horloge du serveur.
    
    kick NOM_UTILISATEUR -> Force la déconnexion de l'utilisateur de pseudo NOM_UTILISATEUR
    
    broadcast MESSAGE_À_DIFFUSER -> Envoie le message en paramètre à tous les clients authentifiés.
    
    mp NOM_UTILISATEUR MESSAGE_À_ENVOYER -> Envoit un message privé à l'utilisateur de pseudonyme NOM_UTILISATEUR


Note : On différencie les commandes du serveur et des clients par l'utilisation du '/', en effet, lorsqu'on souhaite exécuter une commande serveur, l'utilisation du '/' est facultative.


