# CAR-TP1

Le but de ce TP est de développer un programme serveur FTP en Java.

Sur ce dépot, vous trouverez le code du serveur Server.java .

### Résultats des implémentations pour chacune des étapes: 

 1) **Authentification et commande quit**:
  ```
  gabriella@LAPTOP-N38P3PMG:~/CAR-TP1$ ftp localhost 2121
  Connected to localhost.
  220 Service ready
  Name (localhost:gabriella): Gabriella
  331 User name valid, enter password
  Password:
  230 User logged in
  ftp> quit
  221 USER Gabriella Well disconnected. Thank you.
```


  2) commande get
  
  ```
  gabriella@LAPTOP-N38P3PMG:~/CAR-TP1$ ftp localhost 2121
  Connected to localhost.
  220 Service ready
  Name (localhost:gabriella): Gabriella
  331 User name valid, enter password
  Password:
  230 User logged in
  ftp> get bin
  local: bin remote: bin
  229 Entering Extended Passive Mode (|||2222|||)
  150 Opening data connection for bin
  2048 7.78 MiB/s
  226 Transfer complete.
  WARNING! 9 bare linefeeds received in ASCII mode.
  File may not have transferred correctly.
  2048 bytes received in 00:00 (41.16 KiB/s)
  ftp>
  ```
 


 
