
package chatroom;

import java.util.Date;

/**
 * @author Karol Zdebel
 *
 * Class responsible for storing message information and functionality. 
 */
public class Message {
    
    private final Date dateSent;
    private final User author;
    private final String content;
    private final User recipient;
    private final boolean privMessage;
    
    public Message(String message){
        this.privMessage = false;
        this.recipient = null;
        this.dateSent = new Date();
        this.author = null;
        this.content = "user: "+message;
    }
    
    public Message(String message, User author, User recipient){
        this.privMessage = true;
        this.dateSent = new Date();
        this.author = author;
        this.content = author.getNickname()+": "+message;
        this.recipient = recipient;
    }
    
    public String getContent(){
        return content;
    }
    
    public User getAuthor(){
        return author;
    }
    
    public User getRecipient(){
        return recipient;
    }
    
    public Date getDateSent(){
        return dateSent;
    }
    
    public boolean isPrivate(){
        return privMessage;
    }
    
    
}
