import java.io.*;
import java.net.*;
import java.nio.file.Paths;
import java.util.*;

public class Server {

    public static void main(String[] args) {
        try {

            ServerSocket serv = new ServerSocket(2121);

            while (true) {
                Socket s2 = serv.accept(); // le serveur accepte la connexion
                ClientFTP clientFTP = new ClientFTP(s2);
                clientFTP.start();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private static class ClientFTP extends Thread {
        private ServerSocket fichierServerSocket;

        public ClientFTP(Socket s2) {
            try (
                    InputStream in = s2.getInputStream();
                    OutputStream out = s2.getOutputStream();
                    Scanner scan = new Scanner(in);) {

                // Envoyer le message de bienvenue
                String welcomeMessage = "220 Service ready\r\n";
                out.write(welcomeMessage.getBytes());

                // Recevoir le nom d'utilisateur
                String userNameInput = scan.nextLine();
                System.out.println(userNameInput);
                String userValidMsg = "331 User name valid, enter password\r\n";
                out.write(userValidMsg.getBytes());
                String passwordInput = scan.nextLine();
                System.out.println(passwordInput);

                //userAuth verifie si on a bien les bons identifiant et mdp 
                if (userAuth(userNameInput, passwordInput)) {
                    out.write("230 User logged in\r\n".getBytes());
                } else {
                    out.write("530 Login incorrect\r\n".getBytes());
                    return;
                }

                String commandeClt;
                do {
                    commandeClt = scan.nextLine();

                    System.out.println(commandeClt);

                    /* Contraiment à par exemple quit, get prend un argument. 
                    Pour RETR, il me faut donc séparer la commande de l'argument */
                    String[] separetedCommand = commandeClt.split("\\s+", 2); 
                    String command = separetedCommand[0].toUpperCase();
                    String fichierEnvoye = separetedCommand.length > 1 ? separetedCommand[1] : "";

                    switch (command) {
                        case "QUIT":
                            out.write(("221 " + userNameInput + " Well disconnected. Thank you.\r\n").getBytes());
                            break;
                        /* EPSV pour tout type de fichiers, PASV pour les fichiers txt */
                        case "EPSV":
                            epsv(out);
                            break;
                        case "RETR":
                            retr(out, fichierEnvoye);
                            break;

                        default:
                            out.write("500 Command not valid\r\n".getBytes());
                    }
                } while (!commandeClt.equalsIgnoreCase("QUIT"));

            } catch (Exception e) {
                e.printStackTrace();

            }
        }

        private static boolean userAuth(String username, String password) {
            return username.equals("USER Gabriella") && password.equals("PASS Miage");
        }

        private void epsv(OutputStream out) {
            // epsv nous crée un serverSocket sur le port 2222
            try {

                fichierServerSocket = new ServerSocket(2222);
                // j'envoie le msg au clt
                out.write(("229 Entering Extended Passive Mode (|||" + 2222 + "|||)\r\n").getBytes());
            } catch (Exception e) {
                System.err.println("Error : " + e);
            }
        }

        private void retr(OutputStream out, String fichierEnvoye) throws IOException {
            File file = new File(fichierEnvoye);

            if (!file.exists()) {
                out.write("550 File not found\r\n".getBytes());
                return;
            }

            try (Socket dataSocket = fichierServerSocket.accept();
                    FileInputStream fileInputStream = new FileInputStream(file);
                    OutputStream dataOut = dataSocket.getOutputStream()) {

                byte[] buffer = new byte[1024];
                int bytesRead;

                out.write(("150 Opening data connection for " + fichierEnvoye + "\r\n").getBytes());

                while ((bytesRead = fileInputStream.read(buffer)) != -1) {
                    dataOut.write(buffer, 0, bytesRead);
                }

                out.write("226 Transfer complete.\r\n".getBytes());

            } catch (Exception e) {
                e.printStackTrace();

            }
        }



    }
}
/* RESULTATS */
/*
 * 1)Authentification:
 * 
 * gabriella@LAPTOP-N38P3PMG:~/CAR-TP1$ ftp localhost 2121
 * Connected to localhost.
 * 220 Service ready
 * Name (localhost:gabriella): Gabriella
 * 331 User name valid, enter password
 * Password:
 * 230 User logged in
 * ftp> quit
 * 221 USER Gabriella Well disconnected. Thank you.
 */

/*
 * 2) get
 * 
 * 
 * gabriella@LAPTOP-N38P3PMG:~/CAR-TP1$ ftp localhost 2121
 * Connected to localhost.
 * 220 Service ready
 * Name (localhost:gabriella): Gabriella
 * 331 User name valid, enter password
 * Password:
 * 230 User logged in
 * ftp> get bin
 * local: bin remote: bin
 * 229 Entering Extended Passive Mode (|||2222|||)
 * 150 Opening data connection for bin
 * 2048 7.78 MiB/s
 * 226 Transfer complete.
 * WARNING! 9 bare linefeeds received in ASCII mode.
 * File may not have transferred correctly.
 * 2048 bytes received in 00:00 (41.16 KiB/s)
 * ftp>
 * 
 */

