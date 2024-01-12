import java.io.*;
import java.net.*;
import java.util.*;
public class Server{

        public static void main(String[] args) throws IOException  {
            ServerSocket serv = new ServerSocket(2121);
            Socket s2 = serv.accept();

            try{ 
                while (true){ 
                
                    InputStream in = s2.getInputStream();
                    /* pour afficher le msg */ 
                    OutputStream out = s2.getOutputStream();
                    
                    String messageSend = "220 Service ready\r\n";
                    Scanner scan = new Scanner(in);
                    String str = scan.nextLine();

                    out.write(str.getBytes());
                    system.out.println(str);
                    String user = "Gabriella";
                    String password = "Miage";

                    while (true){
                        if(str.equals("user Name" + user)) {
                            system.out.println("User name ok");
                            String infoUser = "220 User valid \r\n";
                            out.write(infoUser.getBytes());                          
                        }
                    String str2 = scan.nextLine();
                    if(str2.equals ("Password" + password)){
                        String userLogged = "220 User logged in \r\n";
                       
                        out.write(userLogged.getBytes());
                        while (true){
                            String str3 = scan.nextLine();
                            if(str3.equals ("Quit")){
                                String quit = "220 User logged out \r\n";
                       
                                out.write(userLogged.getBytes());
                            
                            }

                    }

                    }


        
            }
            }
            }
            finally {
            s2.close();
        }
            

        
            
}
}


        
            
}
