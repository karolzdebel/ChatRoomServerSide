/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chatroomserver;

import chatroom.UserActivity;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Listen to client input and respond to it
 * 
 * @author K
 */
public class UserActivityListener implements Runnable{
    
    private final ChatRoomServerNetwork server;
    private final Socket client;

    public UserActivityListener(ChatRoomServerNetwork server, Socket client){
        this.server = server;
        this.client = client;
        this.run();
    }

    @Override
    public void run() {
        
        try{
            
            System.out.println("Created activity listener for client, getting input stream\n");
            
            ObjectInputStream in = new ObjectInputStream(
                client.getInputStream());    
            
            //Keep listening to object input stream for user input
            while(true){
                
                System.out.println("ActivityListener waiting for activity.");
                
                //blocks here till object is sent
                UserActivity inActivity = (UserActivity)in.readObject();
                    System.out.println("ActivityListener received activity.");
                synchronized(server){
                    server.addActivityToQueue(inActivity);
                    server.notify();
                    System.out.println("ActivityListener added to activity to queue and notified server.");
                }
            }
        }
        catch(Exception e){
            System.err.println("error:"+e.getMessage());
        }
    }
}
