import java.io.*;
import java.net.*;
import java.util.*;

public class Server {

    public static void main(String[] args) {
        try {

            ServerSocket serv = new ServerSocket(2121);

            while (true) {
                Socket s2 = serv.accept();
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

                    switch (commandeClt) {
                        case "QUIT":
                            out.write(("221 " + userNameInput + " Disconnected.\r\n").getBytes());
                            break;
                        /* pour tout type de fichiers, PASV pour les fichiers txt */
                        case "EPSV":
                            epsv(out);
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
            try {

                fichierServerSocket = new ServerSocket(2040);

            } catch (Exception e) {
                System.err.println("Error : " + e);
            }
        }

    }

}

/*
 * 1) Connected to localhost.
 * 220 Service ready
 * Name (localhost:gabriella): Gabriella
 * 331 User name valid, enter password
 * Password:
 * 230 User logged in
 * ftp> quit
 * 221 USER Gabriella Disconnected.
 */
