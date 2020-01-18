/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package my.Server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.StringTokenizer;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author ricca
 */
public class User implements Runnable{
    static Vector <Host> hosts= new Vector<Host>();
    static Vector <Host> connectedHosts= new Vector<Host>();
    
    int position;
	
	Socket connection = null;
	
	InputStreamReader in;
	BufferedReader sIN;
	OutputStream out;
	PrintWriter sOut;
	
	String mex;
	
	StringTokenizer st;
	
	String info;
	String name;
	String pass;
        
        String connected = "";
	
	boolean logged;
	

	public User(Socket connection){
            this.connection = connection;
            try {
		out = connection.getOutputStream();
		sOut = new PrintWriter(out);
		in = new InputStreamReader(connection.getInputStream());
		sIN = new BufferedReader(in);
                
                if(sIN.readLine().equals("CONFIRM")){
                    new Thread(this).start();
                }else{
                    connection.close();
                }
            } catch (IOException e) {
		e.printStackTrace();
            }
            
	}

	public void run() {
        try {
            while(true){
                while(!logged){  //CHIEDI CREDENZIALI FINCHE NON SI E LOGGATO CORRETTAMENTE 
                    //GET INFO OF HOST (USERNAME, PASSWORD)
                    try {
                        info = sIN.readLine();
                        System.out.println("info readed");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
				
                    // LOGIN O SINGIN (L'HOST DOVRA INVIARE UN 1 ALL'INIZIO DEL MESSAGGIO SE SI STA REGISTRANDO O UNO 0 SE SI STA LOGGANDO)
                    sOut.println(log());
                    sOut.flush();
                    if(logged){
                        for(int i = 0; i < connectedHosts.size()-1; i++){
                            connected += connectedHosts.elementAt(i).getName() + ";";
                        }
                        sOut.println("/2:"+connected);
                        sOut.flush();
                    }
                }
                
                //IN ASCOLTO PER MESSAGGI DALL'HOST
                try {
                    mex = sIN.readLine();
                    if(mex.equals("END_CONNECTION")){
                        break;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                
                //REINDIRIZZAMENTO DEL MESSAGGIO IN BROADCAST
                new Broadcast(connectedHosts, name + ": " + mex);
                
            }
            
            sOut.println("END_CONNECTION_CONFIRM");
            sOut.flush();
            for(int i = 0; i < connectedHosts.size(); i++){
                if(connectedHosts.elementAt(i).getName().equals(name)){
                    connectedHosts.remove(i);
                }
            }
            for(int i = 0; i < connectedHosts.size()-1; i++){
                connected += connectedHosts.elementAt(i).getName() + ";";
            }
            new Broadcast(connectedHosts, "/2:"+connected);
            new Broadcast(connectedHosts, name + " left chat");
            logged = false;
            connection.close();
            hosts.elementAt(position).getSocket().close();
        } catch (IOException ex) {
            Logger.getLogger(User.class.getName()).log(Level.SEVERE, null, ex);
        }
	}
	
	private synchronized int log(){
            st = new StringTokenizer(info, ";");
            String temp = st.nextToken();
            if(temp.equals("1")){    //SIGNIN
		name = st.nextToken();
		pass = st.nextToken();
		for(int i = 0; i < hosts.size(); i++){
                    if(hosts.elementAt(i).getName().equals(name)){
			return(0);
                    }
		}
                position = hosts.size();
		hosts.add(new Host(connection, name, pass));
                connectedHosts.add(new Host(connection, name, pass));
		logged = true;
                new Broadcast(connectedHosts, "/1:" + name + " joined");
		return(1);
            }else if(temp.equals("0")){   //LOGIN
		name = st.nextToken();
		pass = st.nextToken();
		for(int i = 0; i < hosts.size(); i++){
                    if(name.equals(hosts.elementAt(i).getName()) && pass.equals(hosts.elementAt(i).getPassword())){   
			logged = true;
                        connectedHosts.add(new Host(connection, name, pass));
                        position = i;
                        new Broadcast(connectedHosts, "/1:" + name + " joined");
            		return(1);																					
                    }else if(name.equals(hosts.elementAt(i).getName()) && !pass.equals(hosts.elementAt(i).getPassword())){  //se nome o pass non sono corretti ritorna uno 0 all'host 									
			return(0);																						    //per fargli capire che il login non è andato a buon fine
                    }
		}
            }
            return(0);
	}
}
