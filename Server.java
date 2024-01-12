import java.io.*;
import java.net.*;
import java.util.*;
public class Server{

        public static void main(String[] args) throws IOException  {
            ServerSocket serv = new ServerSocket(2121);
            Socket s2 = serv.accept();

            try{ 
                while (true){ 
                
                    
                    OutputStream out = s2.getOutputStream();
                    
                    String messageSend = "220 Service ready\r\n";

                    InputStream in = s2.getInputStream();
                    Scanner scan = new Scanner(in);
                    String str = scan.nextLine();

                    out.write(str.getBytes());
            }
            }
            finally {
            s2.close();
        }
            }

        
            
}
