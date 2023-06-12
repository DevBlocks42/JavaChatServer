package server;

import io.CommandManager;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Client implements Runnable
{
    private final Socket socket;
    private List<Client> clients = new ArrayList<>();
    private final PrintWriter output;
    private final BufferedReader input;
    private String message;
    private String login;
    private boolean logged = false;
    
    public Client(Socket socket, List<Client> clients, PrintWriter output, BufferedReader input)
    {
        this.socket = socket;
        this.clients = clients;
        this.output = output;
        this.input = input;
    }
    @Override public void run()
    {
        while(socket.isConnected())
        {
            try
            {
                int firstChar = input.read(); //Lecture du premier caractère reçu dans le BufferedReader : si -1 alors rien n'a été reçu, sinon, c'est qu'il y a bien un message.
                if(firstChar != -1)
                {
                    char chr = (char)firstChar;
                    message = new String(String.valueOf(chr).getBytes(), "UTF-8") + input.readLine(); //Conversion puis concaténation du premier caractère au reste du message reçu.
                    while(message != null)
                    {
                        if(!message.startsWith("/")) //S'il ne s'agit pas d'une commande client.
                        {
                            if(logged)
                            {
                                String formattedMessage = "";
                                for(Client c : clients)
                                {
                                    if(message.contains("[q]") || message.contains("[b]") || message.contains("[s]") || message.contains("[u]") || message.contains("[p:"))
                                    {
                                        //On remplace les balises personalisées par du code html, qui pourra ensuite être interprété par la WebView JavaFX du client destinataire.
                                        formattedMessage = "<div class = 'messageBox'><p class = 'message'><b>" + login + "</b> : ";
                                        message = message.replace("[q]", "<blockquote>").replace("[/q]", "</blockquote>").replace("<br>", ""); //+ "</p></div>\n";
                                        message = message.replace("[b]", "<b>").replace("[/b]", "</b>").replace("<br>", "");
                                        message = message.replace("[s]", "<s>").replace("[/s]", "</s>");
                                        message = message.replace("[u]", "<u>").replace("[/u]", "</u>");
                                        message = message.replace("[u]", "<u>").replace("[/u]", "</u>");
                                        message = message.replaceAll("\\[\\/p\\]", "</p>").replace("[p:", "<p style='color:#").replaceAll("(?<=([a-f|0-9]))\\]", ";'>");
                                        formattedMessage += message + "</p></div>\n";
                                    
                                    }
                                    else 
                                    {
                                        formattedMessage = "<div class = 'messageBox'><p class = 'message'><b>" + login + "</b> : " + message.replace("<br>", "") + "</p></div>\n";
                                    }
                                    System.out.println(formattedMessage);
                                    c.getClientOutput().write(formattedMessage);
                                    c.getClientOutput().flush();
                                }
                            }
                            else //Première connexion
                            {
                                login = message; //Le premier message attendu correspond au pseudonyme du client.
                                System.out.println("[*] Tentative d'authentification de " + login);
                                for(Client c : clients)
                                {   
                                    if(c.getClientLogin().equals(login.replace("\n", "")))
                                    {
                                        if(c != this) //On vérifie qu'aucun client ne porte le pseudonyme du client qui tente de se connecter.
                                        {
                                            System.out.println("[!] Échec de l'authentification de l'utilisateur " + login + " car le pseudonyme est déjà utilisé par un autre utilisateur.");
                                            logged = false;
                                            login = "";
                                            output.write("<p id = 'CONNECTION_LOGIN_FAILED'></p>\n");
                                            output.flush();
                                            break; //Le pseudonyme est déjà utilisé, on sort de la boucle après avoir averti le client que le pseudonyme est déjà utilisé.
                                        }
                                        logged = true; //Le pseudonyme n'a pas été trouvé parmi les clients connectés, l'utilisateur peut s'authentifier.
                                    }
                                }
                                if(logged)
                                {
                                    System.out.println("[*] Authentification de l'utilisateur " + login + " réussie.");
                                    for(Client c : clients) //On averti tous les clients connectés qu'un nouvel utilisateur vient d'arriver.
                                    {
                                        c.getClientOutput().write("<p id = 'CONNECTION_SUCCESSFULL' style = 'color:red;'>[Serveur] Connexion de l'utilisateur <b>" + login + "</b></p>\n");
                                        c.getClientOutput().flush();
                                    }
                                }
                            }
                        }
                        else //Il s'agit d'une commande client (commence par '/').
                        {
                            if(message.startsWith("/clients"))
                            {
                                String out = "";
                                for(Client c : clients)
                                {
                                    out += c.getClientLogin() + " "; 
                                }
                                output.write("SERVER_CLIENT_LIST " + out + "\n"); //On envoie la liste des clients au client qui en a fait la demande.
                                output.flush();
                            }
                            else if(message.startsWith("/mp"))
                            {
                                String args[] = message.split(" "); //On découpe la chaîne reçu selon le format suivant : /mp PSEUDONYME MESSAGE_PRIVÉ -> Chaque argument de la commande est séparé par un espace, ces arguments sont ensuite placés dans un tableau de chaînes de caractères.
                                CommandManager commandManager = new CommandManager();
                                commandManager.updateClients(clients);
                                Client target = commandManager.getClient(args[1].replace(" ", "")); //On cherche l'utilisateur de nom arg[1] dans la liste des clients connectés.
                                if(target != null) //Si le client a été trouvé.
                                {
                                   String mp = "";
                                   for(int i = 2; i < args.length; i++) //Le message à envoyer se trouve entre l'indice 2 et l'indice maximum du tableau arg (si le format de la commande a été respecté).
                                   {
                                       mp += args[i] + " "; //On concatène chaque éléments du tableau pour former le message final.
                                   }
                                   target.getClientOutput().write("Message privé de " + this.login + " : " + mp + "\n"); //On envoit le message au déstinataire.
                                   target.getClientOutput().flush();
                                   output.write("[Serveur] Message privé envoyé.\n"); 
                                   output.flush();
                                }
                                else
                                {
                                    output.write("[Serveur] Le pseudo saisit est introuvable.\n");
                                    output.flush();
                                }
                            }
                        }
                        message = input.readLine();
                    }
                }
                else 
                {
                    break;
                }
            }
            catch(IOException ex)
            {
                System.out.println("[!] Une exception s'est produite lors de la lecture des messages client.\nDétails : " + ex.getMessage());
                break;
            }
            try
            {
                Thread.sleep(500);
            }
            catch(InterruptedException ex) {}
        }
        System.out.println("[*] Déconnexion de " + login);
        for(Client c : clients)
        {
            if(c != this)
            {
                c.getClientOutput().write("SERVER_CLIENT_DISCONNECT\n");
                c.getClientOutput().flush();
            }
        }
        logged = false;
        clients.remove(this);
    }
    public synchronized void updateClients(List<Client> clients, PrintWriter output, BufferedReader input)
    {
        this.clients = clients;
    }
    public synchronized PrintWriter getClientOutput()
    {
        return output;
    }
    public synchronized Socket getClientSocket()
    {
        return socket;
    }
    public synchronized String getClientLogin()
    {
        return login;
    }
}