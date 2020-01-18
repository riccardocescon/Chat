/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package my.Server;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Vector;

/**
 *
 * @author ricca
 */
public class Broadcast implements Runnable{
    OutputStream out;
    PrintWriter sOut;
	
    Vector <Host> hosts = new Vector<Host>();
	
    String mex;

    public Broadcast(Vector<Host> hosts, String mex) {
        this.hosts = hosts;
        this.mex = mex;
        new Thread(this).start();
    }

    public void run() {
	for(int i = 0; i < hosts.size(); i++){
            try {
                System.out.println(hosts.elementAt(i).getName());
		out = hosts.elementAt(i).getSocket().getOutputStream();
            } catch (IOException e) {
		e.printStackTrace();
            }
            sOut = new PrintWriter(out);
            sOut.println(mex);
            sOut.flush();
	}
	Thread.currentThread().interrupt();
    }
    
}
