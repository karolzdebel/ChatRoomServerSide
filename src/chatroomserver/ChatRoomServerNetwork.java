/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chatroomserver;

import chatroom.User;
import chatroom.UserActivity;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Hashtable;

/**
 *
 * @author K
 */
public class ChatRoomServerNetwork implements Runnable{
    
    private final Hashtable<String,Socket> hashClientSockets;
    private final ArrayList<Socket> arrClientSockets;
    private final ArrayList<UserActivity> activityQueue;
    private static ServerSocket serverSocket;

    public ChatRoomServerNetwork(){
        
        //Create server socket
        try{
            serverSocket = new ServerSocket(49152);
        }catch(Exception e){
            System.err.print(e.getMessage());
        }
        
        hashClientSockets = new Hashtable<>();
        arrClientSockets = new ArrayList<>();
        activityQueue = new ArrayList<>();

        //start listening for connections
        UserConnectionListener connectionListener = new UserConnectionListener(this);
        
        Thread thread = new Thread(this);
        thread.start();
    }
    
    public ServerSocket getServerSocket(){
        return serverSocket;
    }
    
    public void addClient(Socket socket,ObjectInputStream in){
        arrClientSockets.add(socket);
        UserActivityListener activityListener = new UserActivityListener(this,in);
    }

    //Broadcast activity to all online users
    public void broadcastActivity(UserActivity activity){
        try{
            for (Socket s: arrClientSockets){
                ObjectOutputStream out =
                        new ObjectOutputStream(s.getOutputStream());
                out.writeObject(activity);
            }    
        }catch(Exception e){
            System.err.print("Error broadcasting activity: "+e.getMessage());
        }
        
    }
    
    //Send private message
    public void sendPrivateMessage(UserActivity activity){
        try{
            Socket recipient = hashClientSockets.get(activity.getUser().getNickname());
            ObjectOutputStream out =
                new ObjectOutputStream(recipient.getOutputStream());
            out.writeObject(activity);
   
        }
        catch(Exception e){
                System.err.print("Error broadcasting activity: "+e.getMessage());
        }
    }
    
    public ArrayList<Socket> getSockets(){
        return arrClientSockets;
    }
    
    
    //Add user to user list, then broadcast activity
    public void userJoin(UserActivity activity, Socket socket){
        hashClientSockets.put(activity.getUser().getNickname(),socket);
    }
    
    //Remove user from user list, then broadcast activity
    public void userLeave(UserActivity activity, Socket socket){
        arrClientSockets.remove(socket);
        hashClientSockets.remove(activity.getUser().getNickname());
    }

    public void addActivityToQueue(UserActivity activity){
        activityQueue.add(activityQueue.size(), activity);
    }
    
    @Override
    public void run() {
        while(true){
            
            //Activity to be handled
            UserActivity inActivity;

            System.out.println("ChatRoomServerNetwork waiting for activity.");
            
            //Wait for activity to be added
            synchronized(this){
                while (activityQueue.isEmpty()){
                    try{
                        this.wait();
                    }catch(Exception e){
                        e.printStackTrace();
                    }
                }
                
                //Dequeue first activity
                inActivity = activityQueue.get(0);
                activityQueue.remove(0);
                System.out.println("ChatRoomServerNetwork received activity and dequeued.");
                System.out.println("Activity type: "+inActivity.getActivityType());
            }
            
            
            //Send private message to recipient
            if(inActivity.isPrivateMessage()){
                sendPrivateMessage(inActivity);
            }
            //Broadcast public message
            else if(inActivity.isPublicMessage()){
                broadcastActivity(inActivity);
            }
            //Broadcast that user joined
            else if(inActivity.isUserJoin()){
                userJoin(inActivity,hashClientSockets.get(inActivity.getUser().getNickname()));
            }
            //Broadcast that user left
            else if(inActivity.isUserLeave()){
                userLeave(inActivity,hashClientSockets.get(inActivity.getUser().getNickname()));
            }else{
                System.err.println("Error, unreachable statement!!");
            }
            
        }
    }
}
