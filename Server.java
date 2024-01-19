import java.io.*;
import java.net.*;
import java.util.*;

public class Server {

    public static void main(String[] args) throws IOException {
        ServerSocket serv = new ServerSocket(2121);
        Socket s2 = serv.accept();

        try {
            InputStream in = s2.getInputStream();
            OutputStream out = s2.getOutputStream();
            Scanner scan = new Scanner(in);

            String user = "Gabriella";
            String password = "Miage";

            // Envoyer le message de bienvenue
            String welcomeMessage = "220 Service ready\r\n";
            out.write(welcomeMessage.getBytes());

            // Recevoir le nom d'utilisateur
            String userNameInput = scan.nextLine();

            if (userNameInput.equals("UserName" + user)) {
                System.out.println("User name ok");
                String userValidMsg = "331 User name valid, enter password\r\n";
                out.write(userValidMsg.getBytes());

                // Recevoir le mot de passe
                String passwordInput = scan.nextLine();
                if (passwordInput.equals("Password " + password)) {
                    System.out.println("User logged in");
                    String userLoggedInMsg = "230 User logged in\r\n";
                    out.write(userLoggedInMsg.getBytes());

                    // Traitement des commandes après la connexion
                     while (true) {
                        String command = scan.nextLine();
                        if (command.equals("Quit")) {
                            String quitMsg = "221 User logged out\r\n";
                            out.write(quitMsg.getBytes());
                            break;
                        }
                        
                    } 

                } else {
                    System.out.println("Invalid password");
                    String invalidPasswordMsg = "430 Invalid password\r\n";
                    out.write(invalidPasswordMsg.getBytes());
                }
            } else {
                System.out.println("Invalid user name");
                String invalidUsernameMsg = "430 Invalid user name\r\n";
                out.write(invalidUsernameMsg.getBytes());
            }
        } finally {
            s2.close();
        }
    }
}
