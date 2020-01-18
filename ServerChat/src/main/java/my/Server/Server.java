/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package my.Server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 *
 * @author ricca
 */
public class  Server implements Runnable{
        public static Server instance;
    
        ServerSocket sSocket;
	Socket connessione = null;
	int port = 24680;
        
        public Server(){
            new Thread(this).start();
        }

    public void run() {
        //while(true){
            try{
                sSocket = new ServerSocket(port);
                while (true){
                    connessione = sSocket.accept();
                    new User(connessione);
                }
            }catch(IOException e){
                System.out.println(e);
            }
            
            /*try{
                connessione.close();
            }catch(IOException e){
                System.out.println(e);
            }*/
        //}
    }
}
