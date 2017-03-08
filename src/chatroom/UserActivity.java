package chatroom;


import chatroom.*;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author K
 */
public class UserActivity {
    
    public static final String ACT_PRIV_MESSAGE = "ACT_PRIV_MESSAGE"; 
    public static final String ACT_PUB_MESSAGE = "ACT_PUB_MESSAGE"; 
    public static final String ACT_USER_LEAVE = "ACT_USER_LEAVE"; 
    public static final String ACT_USER_JOIN = "ACT_USER_JOIN";
    
    private final String activityType;
    private final User user;
    private final Message message;
    
    
    public UserActivity(Message message){
        if (message.isPrivate()){
            activityType = ACT_PRIV_MESSAGE;
        }
        else{
            activityType = ACT_PUB_MESSAGE;
        }

        this.user = null;
        this.message = message;
        
    }
    
    public UserActivity(User user, String activityType){
        if (activityType.equals(ACT_USER_LEAVE)){
            this.activityType = ACT_USER_LEAVE;
        }
        else if(activityType.equals(ACT_USER_JOIN)){
            this.activityType = ACT_USER_JOIN;
            
        }else{
            //error
            this.activityType = null;
            System.out.println("Error invalid activity type in constructor, exiting");
            System.exit(1);
        }
        
        this.message = null;
        this.user = user;
    }
    
    public String getActivityType(){
        return activityType;
    }
    
    public User getUser(){
        return user;
    }
    
    public Message getMessage(){
        return message;
    }
    
    public boolean isPrivateMessage(){
        return activityType.equals(ACT_PRIV_MESSAGE);
    }
    
    public boolean isPublicMessage(){
        return activityType.equals(ACT_PUB_MESSAGE);
    }
    
    public boolean isUserJoin(){
        return activityType.equals(ACT_USER_JOIN);
    }
    
    public boolean isUserLeave(){
        return activityType.equals(ACT_USER_LEAVE);
    }
}
