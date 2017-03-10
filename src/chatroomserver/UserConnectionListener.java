/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chatroomserver;

import chatroom.UserActivity;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

/**
 *  Liten for incoming connections and spawn client threads
 * 
 * @author K
 */
public class UserConnectionListener implements Runnable{
    private ChatRoomServerNetwork server;
    
    public UserConnectionListener(ChatRoomServerNetwork s){
        server = s;
        Thread thread = new Thread(this);
        thread.start();
    }

    @Override
    public void run() {
        
        try{
            while (true){
                System.out.println("Connection listener waiting for connection.");
                
                //Listen for incoming connections
                ServerSocket serverSocket = server.getServerSocket();
                Socket clientSocket = serverSocket.accept();
                
                //Establishing input and output stream
                 ObjectOutputStream out = new ObjectOutputStream(
                    clientSocket.getOutputStream());
                 ObjectInputStream in = new ObjectInputStream(
                    clientSocket.getInputStream());   
                 
                 //Wait for client to send username
                 UserActivity firstActivity = (UserActivity)in.readObject();


                System.out.println("Connection listener accepted client connection.");

                server.addClient(out,in, firstActivity);
                
                //Send client the list of users for user panel
                server.sendUsers(out);
          
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }

        
    }
    
    
}
