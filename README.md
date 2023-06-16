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

Schéma
<mxfile modified="2023-06-16T10:27:52.075Z" host="app.diagrams.net" agent="Mozilla/5.0 (X11; Ubuntu; Linux x86_64; rv:109.0) Gecko/20100101 Firefox/113.0" etag="B_TClrIC4MIVub1-MRxj" version="21.4.0" type="device">
  <diagram id="l8hXMBHkgcEJcSW0mbfh" name="Page-1">
    <mxGraphModel dx="1434" dy="771" grid="1" gridSize="10" guides="1" tooltips="1" connect="1" arrows="1" fold="1" page="1" pageScale="1" pageWidth="1169" pageHeight="827" math="0" shadow="0">
      <root>
        <mxCell id="0" />
        <mxCell id="1" parent="0" />
        <mxCell id="s5DcjVivYv2rXD6XcJxH-9" value="Serveur" style="shape=ext;double=1;rounded=0;whiteSpace=wrap;html=1;fillColor=#647687;fontColor=#ffffff;strokeColor=#314354;" vertex="1" parent="1">
          <mxGeometry x="430" y="180" width="320" height="70" as="geometry" />
        </mxCell>
        <mxCell id="s5DcjVivYv2rXD6XcJxH-10" value="&lt;div&gt;Entrées&lt;/div&gt;&lt;div&gt;&lt;br&gt;&lt;/div&gt;&lt;div&gt;&lt;br&gt;&lt;/div&gt;&lt;div&gt;&lt;br&gt;&lt;/div&gt;&lt;div&gt;&lt;br&gt;&lt;/div&gt;&lt;div&gt;Réception de &lt;br&gt;&lt;/div&gt;M&lt;sub&gt;1&lt;/sub&gt;" style="shape=ext;double=1;rounded=0;whiteSpace=wrap;html=1;" vertex="1" parent="1">
          <mxGeometry x="430" y="250" width="160" height="200" as="geometry" />
        </mxCell>
        <mxCell id="s5DcjVivYv2rXD6XcJxH-27" value="Client A + M&lt;sub&gt;1&lt;/sub&gt;" style="edgeStyle=none;rounded=0;orthogonalLoop=1;jettySize=auto;html=1;exitX=0.5;exitY=1;exitDx=0;exitDy=0;entryX=0.5;entryY=0;entryDx=0;entryDy=0;" edge="1" parent="1" source="s5DcjVivYv2rXD6XcJxH-12" target="s5DcjVivYv2rXD6XcJxH-13">
          <mxGeometry relative="1" as="geometry" />
        </mxCell>
        <mxCell id="s5DcjVivYv2rXD6XcJxH-28" value="Client A + M&lt;sub&gt;1&lt;/sub&gt;" style="edgeStyle=none;rounded=0;orthogonalLoop=1;jettySize=auto;html=1;exitX=0.5;exitY=1;exitDx=0;exitDy=0;entryX=0.5;entryY=0;entryDx=0;entryDy=0;" edge="1" parent="1" source="s5DcjVivYv2rXD6XcJxH-12" target="s5DcjVivYv2rXD6XcJxH-24">
          <mxGeometry relative="1" as="geometry" />
        </mxCell>
        <mxCell id="s5DcjVivYv2rXD6XcJxH-12" value="&lt;div&gt;Sorties&lt;/div&gt;&lt;div&gt;&lt;br&gt;&lt;/div&gt;&lt;div&gt;&lt;br&gt;&lt;/div&gt;&lt;div&gt;&lt;br&gt;&lt;/div&gt;&lt;div&gt;&lt;br&gt;&lt;/div&gt;&lt;div&gt;Diffusion de &lt;br&gt;&lt;/div&gt;M&lt;sub&gt;1&lt;/sub&gt;" style="shape=ext;double=1;rounded=0;whiteSpace=wrap;html=1;" vertex="1" parent="1">
          <mxGeometry x="590" y="250" width="160" height="200" as="geometry" />
        </mxCell>
        <mxCell id="s5DcjVivYv2rXD6XcJxH-25" style="edgeStyle=none;rounded=0;orthogonalLoop=1;jettySize=auto;html=1;exitX=0.5;exitY=0;exitDx=0;exitDy=0;entryX=0.5;entryY=1;entryDx=0;entryDy=0;" edge="1" parent="1" source="s5DcjVivYv2rXD6XcJxH-13" target="s5DcjVivYv2rXD6XcJxH-10">
          <mxGeometry relative="1" as="geometry" />
        </mxCell>
        <mxCell id="s5DcjVivYv2rXD6XcJxH-26" value="M&lt;sub&gt;1&lt;/sub&gt;" style="edgeLabel;html=1;align=center;verticalAlign=middle;resizable=0;points=[];" vertex="1" connectable="0" parent="s5DcjVivYv2rXD6XcJxH-25">
          <mxGeometry relative="1" as="geometry">
            <mxPoint as="offset" />
          </mxGeometry>
        </mxCell>
        <mxCell id="s5DcjVivYv2rXD6XcJxH-13" value="Client A" style="ellipse;whiteSpace=wrap;html=1;aspect=fixed;" vertex="1" parent="1">
          <mxGeometry x="480" y="740" width="60" height="60" as="geometry" />
        </mxCell>
        <mxCell id="s5DcjVivYv2rXD6XcJxH-24" value="Client B" style="ellipse;whiteSpace=wrap;html=1;aspect=fixed;" vertex="1" parent="1">
          <mxGeometry x="640" y="740" width="60" height="60" as="geometry" />
        </mxCell>
      </root>
    </mxGraphModel>
  </diagram>
</mxfile>



## Manuel

