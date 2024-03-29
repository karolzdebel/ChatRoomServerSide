package chatroom;


import chatroom.*;
import java.io.Serializable;
import java.util.ArrayList;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 * Stores data about a particular activity
 * 
 * @author Karol Zdebel
 */
public class UserActivity implements Serializable {
    
    //Type of activity
    public static final String ACT_PRIV_MESSAGE = "ACT_PRIV_MESSAGE"; 
    public static final String ACT_PUB_MESSAGE = "ACT_PUB_MESSAGE"; 
    public static final String ACT_USER_LEAVE = "ACT_USER_LEAVE"; 
    public static final String ACT_USER_JOIN = "ACT_USER_JOIN";
    public static final String ACT_USER_LIST = "ACT_USER_LIST";
    public static final String ACT_PERM_GIVE = "ACT_PERM_GIVE";
    
    private final String activityType;
    private final User user;
    private final Message message;
    private final ArrayList<User> userList;
    private final User permFrom;
    private final User permTo;
    
    /**
     *
     * @param userList
     */
    public UserActivity(ArrayList<User> userList){
        this.activityType = ACT_USER_LIST;
        this.userList = userList;
        this.message = null;
        this.user = null;
        this.permFrom = null;
        this.permTo = null;
    }
    
    //Construct a message activity
    public UserActivity(Message message){
        if (message.isPrivate()){
            activityType = ACT_PRIV_MESSAGE;
        }
        else{
            activityType = ACT_PUB_MESSAGE;
        }

        this.user = null;
        this.userList = null;
        this.message = message;
        this.permFrom = null;
        this.permTo = null;
        
    }
    
    //Construct a permission activity
    public UserActivity(User givenFrom, User givenTo){
        this.permFrom = givenFrom;
        this.permTo = givenTo;
        this.user = null;
        this.userList = null;
        this.message = null;
        
        this.activityType = ACT_PERM_GIVE;
    }
    
    //Construct a leave activity
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
        this.userList = null;
        this.user = user;
        this.permFrom = null;
        this.permTo = null;
    }
    
    //Get the activity type
    public String getActivityType(){
        return activityType;
    }
    
    public User getUser(){
        return user;
    }
    
    public ArrayList<User> getUserList(){
        return userList;
    }
    
    public Message getMessage(){
        return message;
    }
    
    public boolean isPermGive(){
        return activityType.equals(ACT_PERM_GIVE);
    }
    
    public User getPermTo(){
        return permTo;
    }
    
    public User getPermFrom(){
        return permFrom;
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
    
    public boolean isUserList(){
        return activityType.equals(ACT_USER_LIST);
    }
    
    public String toString(){
        String string = activityType;
        
        if (activityType.equals(ACT_USER_JOIN) || activityType.equals(ACT_USER_LEAVE)){
            string+=" user_nick_name:"+user.getNickname()+ "user_age:"+user.getAge();
        }else if (activityType.equals(ACT_PUB_MESSAGE) || activityType.equals(ACT_PRIV_MESSAGE)){
            string+=" message:"+message.getContent();
        }
        else{
            string+=" received user list";
        }
        
        return string;
    }
}
