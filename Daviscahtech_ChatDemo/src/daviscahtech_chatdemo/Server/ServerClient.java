/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package daviscahtech_chatdemo.Server;

import java.net.InetAddress;

/**
 *
 * @author davis
 */
public class ServerClient {
    
    public String name;
    public InetAddress address;
    public int port;
    private final int ID;
    public int attempt = 0;

    public ServerClient(String name, InetAddress address, int port, int ID) {
        this.name = name;
        this.address = address;
        this.port = port;
        this.ID = ID;
    }

    public int getID() {
        return ID;
    }
    
    
    
}//end of class
