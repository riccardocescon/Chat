/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package my.Server;

import java.net.Socket;

/**
 *
 * @author ricca
 */
public class Host {
    Socket socket;
    String name;
    String password;
	
    public Host(Socket socket, String name, String password) {
	this.socket = socket;
	this.name = name;
	this.password = password;
    }

    public Socket getSocket() {
        return socket;
    }

    public void setSocket(Socket socket) {
	this.socket = socket;
    }

    public String getName() {
	return name;
    }

    public void setName(String name) {
	this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
	this.password = password;
    }
    
}
