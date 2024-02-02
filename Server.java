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

                // userAuth verifie si on a bien les bons identifiant et mdp
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

                    /*
                     * Contrairement à par exemple quit, get prend un argument.
                     * Pour RETR, il me faut donc séparer la commande de l'argument
                     */
                    String[] separetedCommand = commandeClt.split("\\s+", 2); // je sépare la commande en 2
                    String command = separetedCommand[0].toUpperCase();
                    String fichierEnvoye = separetedCommand.length > 1 ? separetedCommand[1] : "";

                    switch (command) {
                        case "QUIT":
                            out.write(("221 " + userNameInput + " Disconnected.\r\n").getBytes());
                            break;
                        /* EPSV pour tout type de fichiers, PASV pour les fichiers txt */
                        case "EPSV":
                            epsv(out);
                            break;
                        case "RETR":
                            retr(out, fichierEnvoye);
                            break;
                        case "LIST":
                            list(out);
                            break;
                        case "CWD": // cd monDossier
                            cwd(out, fichierEnvoye);
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

        private void list(OutputStream out) throws IOException {

            try (Socket dataSocket = fichierServerSocket.accept(); // je crée une nouvelle connexion sur le nouveau port
                                                                   // 2222

                    OutputStream dataOut = dataSocket.getOutputStream()) {

                File repertoire = new File("CAR-TP1/"); // je crée une var repertoire qui prend en param le dossier
                                                        // CAR-TP1
                File[] files = repertoire.listFiles(); // je cree un tableau qui va contenir des fichiers ou des
                                                       // repertoires

                if (files == null) {
                    out.write("550 Directory not found\r\n".getBytes());
                    return;
                } else { // si CAR-TP1 existe
                    out.write("150 Directory listing\r\n".getBytes());

                    for (File file : files) {
                        // j'affiche le dossier ou le fichier qui est dans le repertoire
                        dataOut.write((file.getName() + "\r\n").getBytes());

                    }
                    out.write("226 Directory send OK.\r\n".getBytes());
                }

            }
        }

        private void cwd(OutputStream out, String fichierEnvoye) throws IOException {

            File newDirectory = new File(fichierEnvoye + File.separator); // cd monDossier/

            if (newDirectory.exists() && newDirectory.isDirectory()) {

                out.write("250 Directory changed successfully\r\n".getBytes());
            } else {
                out.write("550 Directory not found\r\n".getBytes());
            }
        }
    }
}
/* RESULTATS DES TESTS */
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
 * 221 USER Gabriella well disconnected. Thank you
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

/*3) dir
 * 220 Service ready
 * Name (localhost:gabriella): Gabriella
 * 331 User name valid, enter password
 * Password:
 * 230 User logged in
 * ftp> dir
 * 229 Entering Extended Passive Mode (|||2222|||)
 * 150 Directory listing
 * src
 * README.md
 * 
 * 226 Directory send OK.
 * 
 * 
 * cd
 * ftp localhost 2121
 * Connected to localhost.
 * 220 Service ready
 * Name (localhost:gabriella): Gabriella
 * 331 User name valid, enter password
 * Password:
 * 230 User logged in
 * ftp> cd src
 * 250 Directory changed successfully
 * ftp> cd ..
 * 250 Directory changed successfully
 * ftp> quit
 * 221 USER Gabriella well disconnected. Thank you
 * 
 * 
 */
