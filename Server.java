import java.io.*;
import java.net.*;
import java.util.*;
public class Server{

        public static void main(String[] args) throws IOException  {
            Socket serv = new ServerSocket(2121);
            System.out.println("Serveur pret à accepter des connexions sur le  port 2121" + serverAddress + ":" + port);

            try{ 
                while (true){ 
                    s2= serv.accept();
                    System.out.println("Le client connecté.");

            }
            }
            finally {
            s2.close();
        }
            }
            private static void sendMsgToClient(Socket s2) throws IOException {
            InputStream in = Socket.getInputStream();
            OutputStream out = Socket.getOutputStream();
            Scanner scan = new Scanner(in);

            String messageSend = "220 Service ready";
            byte[] messageSendBytes = messageSend.getBytes();
            out.write("messageSendBytes".getBytes());
            /*à continuer*/
            
}
}
