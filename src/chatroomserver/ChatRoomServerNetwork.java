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
    
    private final Hashtable<String,ObjectOutputStream> hashClientOut;
    private final ArrayList<ObjectOutputStream> arrClientOut;
    private final ArrayList<UserActivity> activityQueue;
    private static ServerSocket serverSocket;
    private final ArrayList<User> userList;

    public ChatRoomServerNetwork(){
        
        //Create server socket
        try{
            serverSocket = new ServerSocket(49152);
        }catch(Exception e){
            System.err.print(e.getMessage());
        }
        
        hashClientOut = new Hashtable<>();
        arrClientOut = new ArrayList<>();
        activityQueue = new ArrayList<>();
        userList = new ArrayList<>();
        
        //start listening for connections
        UserConnectionListener connectionListener = new UserConnectionListener(this);
        
        Thread thread = new Thread(this);
        thread.start();
    }
    
    public ServerSocket getServerSocket(){
        return serverSocket;
    }
    
    public void addClient(ObjectOutputStream out,ObjectInputStream in,UserActivity a){
        UserActivityListener activityListener = new UserActivityListener(this,in);
        hashClientOut.put(a.getUser().getNickname(), out);
        arrClientOut.add(out);
        userList.add(a.getUser());
        
        //Broadcast that user joined
        try{
            for (ObjectOutputStream o: arrClientOut){
                o.writeObject(a);
            }    
        }catch(Exception e){
            System.err.print("addClient() err: "+e.getMessage());
        }
           
    }

    public void sendUsers(ObjectOutputStream out){
        //Send users to output stream
        UserActivity a = new UserActivity(userList);
        try{
            out.writeObject(a);
        }catch(Exception e){
            System.err.print("sendUsers() Error: "+e.getMessage());
        }
    }
    
    //Broadcast activity to all online users
    public void broadcastActivity(UserActivity activity){
        try{
            for (ObjectOutputStream o: arrClientOut){
                o.writeObject(activity);
            }    
        }catch(Exception e){
            System.err.print("Error broadcasting activity: "+e.getMessage());
        }
        
    }
    
    //Send private message
    public void sendPrivateMessage(UserActivity activity){
        try{
            ObjectOutputStream out =
                hashClientOut.get(activity.getUser().getNickname());
            out.writeObject(activity);
   
        }
        catch(Exception e){
                System.err.print("Error broadcasting activity: "+e.getMessage());
        }
    }
    
    public ArrayList<ObjectOutputStream> getOuts(){
        return arrClientOut;
    }
    
    //Remove user from user list, then broadcast activity
    public void userLeave(UserActivity activity, ObjectOutputStream out){
        arrClientOut.remove(out);
        hashClientOut.remove(activity.getUser().getNickname());
        userList.remove(activity.getUser());
        
        //Broadcast that user left
        for (ObjectOutputStream o: arrClientOut){
            try{
                o.writeObject(activity);
            }catch(Exception e){
                System.err.print("userLeave() err: "+e.getMessage());
            }
        }
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
            //Broadcast that user left
            else if(inActivity.isUserLeave()){
                userLeave(inActivity,hashClientOut.get(inActivity.getUser().getNickname()));
            }else{
                System.err.println("Error, unreachable statement!!");
            }
            
        }
    }
}
