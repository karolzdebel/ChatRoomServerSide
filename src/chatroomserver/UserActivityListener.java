/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chatroomserver;

import chatroom.UserActivity;
import java.io.ObjectInputStream;

/**
 * Class listens for user activity and stores it in queue
 * 
 * @author Karol Zdebel
 */
public class UserActivityListener implements Runnable{
    
    private final ChatRoomServerNetwork server;
    private final ObjectInputStream in;

    public UserActivityListener(ChatRoomServerNetwork server, ObjectInputStream stream){
        this.server = server;
        this.in = stream;
        Thread thread = new Thread(this);
        thread.start();
    }

    @Override
    public void run() {
        
        try{
            
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
            System.err.println("(UserActivityListener)error:"+e.getMessage());
        }
    }
}
