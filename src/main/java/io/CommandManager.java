package io;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;
import server.Client;

public class CommandManager implements Runnable
{
    private List<Client> clients = new ArrayList<>();
    private Scanner scanner;
    
    public CommandManager() {}
    public CommandManager(List<Client> clients, Scanner scanner)
    {
        this.clients = clients;
        this.scanner = scanner;
    }
    @Override public void run() 
    {
        String command;
        command = scanner.nextLine();
        while(command != null)
        {
            if(command.startsWith("time"))
            {
                String date = new Date().toString();
                System.out.println("[*] Horloge serveur : " + date);
            }
            else if(command.startsWith("quit"))
            {
                break; //Sortie de la boucle, provoquant l'arrêt du serveur.
            }
            else if(command.startsWith("kick"))
            {
                String login = command.split(" ")[1];
                Client c = getClient(login);
                if(c != null)
                {
                    c.getClientOutput().write("[!] Vous avez été exclu du serveur.");
                    c.getClientOutput().flush();
                    try
                    {
                        c.getClientSocket().close(); //On ferme la socket du client à exclure.
                        System.out.println("[*] L'utilisateur " + login + " a bien été exclu du serveur.");
                    }
                    catch(IOException ex) {}
                }
                else 
                {
                    System.out.println("[!] L'utilisateur n'a pas été trouvé.");
                } 
            }
            else if(command.startsWith("broadcast"))
            {
                String message = command.split(" ")[1] + "\n";
                if(message != null) 
                {
                    for(int i = 2; i < command.split(" ").length; i++)
                    {
                        message += command.split(" ")[i] + "";
                    }
                }
                for(Client c : clients)
                {
                    c.getClientOutput().write("[SERVEUR] " + message);
                    c.getClientOutput().flush();
                }
            }
            else if(command.startsWith("mp"))
            {
                String login = command.split(" ")[1]; 
                String mp = "";
                Client c = getClient(login);
                if(c != null)
                {
                    for(int i = 2; i < command.split(" ").length; i++)
                    {
                        mp += command.split(" ")[i] + "";
                    }
                    c.getClientOutput().write("[Serveur] " + mp + "\n");
                    c.getClientOutput().flush();
                }
                else 
                {
                    System.out.println("[!] L'utilisateur n'a pas été trouvé.");
                }
            }     
            command = scanner.nextLine();
            try
            {
                Thread.sleep(500);
            }
            catch(InterruptedException ex) {}
        }
        System.out.println("[*] Fermeture du serveur.");
        System.exit(0);
    }
    public synchronized void updateClients(List<Client> clients)
    {
        this.clients = clients;
    }
    public Client getClient(String login)
    {
        for(Client c : clients)
        {
            if(c.getClientLogin().equals(login))
            {
                return c;
            }
        }
        return null;
    }
}