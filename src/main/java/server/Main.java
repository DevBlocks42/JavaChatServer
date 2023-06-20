                                                                                                                                                                                                /*
################################
||                            ||
||   Serveur de messagerie    ||
||   Auteur : devblocks{42}   ||
||                            ||
################################
                                                                                                                                                                                                */
package server;

import io.CommandManager;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main
{
    public static void main(String[] args) 
    {
        int port = 7777;
        final ServerSocket serverSocket;
        Socket clientSocket;
        final Scanner scanner = new Scanner(System.in, "UTF-8");
        Client client;
        CommandManager commandManager;
        Thread clientThread;
        Thread commandThread;
        List<Client> clients = new ArrayList<>();
        try
        {
            commandManager = new CommandManager(clients, scanner);
            commandThread = new Thread(commandManager);
            commandThread.start(); //Lancement du thread permettant de gérer les commandes côté serveur (via l'entrée standard).
            System.out.println("[*] Démarrage du serveur.\n[*] En attente de nouvelles connexions.");
            serverSocket = new ServerSocket(port);
            while(!serverSocket.isClosed())
            {
                try
                {
                    clientSocket = serverSocket.accept();
                    System.out.println("[*] Connexion d'un nouveau client depuis l'adresse " + clientSocket.getInetAddress() + ":" + clientSocket.getPort());
                    PrintWriter output = new PrintWriter(clientSocket.getOutputStream());
                    BufferedReader input = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                    client = new Client(clientSocket, clients, output, input);
                    clientThread = new Thread(client);      
                    clients.add(client);
                    clientThread.start(); //Lancement du thread pour la gestion des entrées/sorties réseaux, i.e messages reçus et à envoyer aux clients connectés.
                    client.updateClients(clients, output, input);
                    commandManager.updateClients(clients);
                    try
                    {
                        Thread.sleep(500);
                    }
                    catch(InterruptedException ex) {}
                }
                catch(IOException ex)
                {
                    System.out.println("[!] Une exception s'est produite lors de la connexion d'un client.\nDétails : " + ex.getMessage());
                    break; 
                }
            }      
        }
        catch(IOException ex)
        {
            System.out.println("[!] Une exception s'est produite lors de la création du serveur.\nDétails : " + ex.getMessage());
            System.exit(9999);
        }
    }
}