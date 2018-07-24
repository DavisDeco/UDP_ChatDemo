/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package daviscahtech_chatdemo.Server;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author davis
 */
public class Server implements Runnable{
    
    private List<ServerClient> clients = new ArrayList<>();
    
    private DatagramSocket socket;
    private int port;
    private boolean running = false;
    private Thread run,send,receive,manage;

    public Server(int port) {
        this.port = port;
        try {
            socket = new DatagramSocket(port);
        } catch (SocketException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
            return;
        }
        run = new Thread(this, "server");
        run.start();
    }

    @Override
    public void run() {
        running = true;
        System.out.println("Server started on port" + port);
        manageClients();
        receiveMessages();
    }

    //method to manage multiple clients
    private void manageClients() {
        manage = new Thread("Manage clients"){
            
            @Override
            public void run(){
                while (running) { 
                    //mnaging
                    
                }
            }        
        };
        manage.start();               
        
    }

    //method to receive messages from clients
    private void receiveMessages() {
        
        receive = new Thread("Receive Messages"){
            
            @Override
            public void run(){
                while (running) { 
                    //receiving
                    byte[] data = new byte[1024];
                    DatagramPacket packet = new DatagramPacket(data, data.length);
                    try {
                        socket.receive(packet);
                    } catch (IOException ex) {
                        Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    
                    process(packet);
                    
                    /*clients.add(new ServerClient("Davis", packet.getAddress(), packet.getPort(), 50));
                    
                    System.out.println(clients.get(0).address.toString() +" : "+ clients.get(0).port);
                    */  
                }
            }        

            
        };
        receive.start();         
        
    }
    
    private void process(DatagramPacket packet) {
        String string = new String(packet.getData());
        if (string.startsWith("/c/")) {
            clients.add(new ServerClient(string.substring(3, string.length()), packet.getAddress(), packet.getPort(), 50));
            System.out.println(string.substring(3, string.length()));
        } else {
            System.out.println(string);
        }
    }
    
    
    
}// end of class
