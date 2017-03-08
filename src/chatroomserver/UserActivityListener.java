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
    }

    @Override
    public void run() {
        
        try{
            
            ObjectInputStream in = new ObjectInputStream(
                client.getInputStream());    
            
            //Keep listening to object input stream for user input
            while(true){

                //blocks here till object is sent
                UserActivity inActivity = (UserActivity)in.readObject();
                synchronized(server){
                    server.addActivityToQueue(inActivity);
                    server.notify();
                }
            }
        }
        catch(Exception e){
            System.err.println("error:"+e.getMessage());
        }
    }
}
