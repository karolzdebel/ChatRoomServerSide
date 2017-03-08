
package chatroom;

/**
 * @author Karol Zdebel
 * 
 * Class responsible for storing user details and providing access to them.
 */
public class User {
    
    private int age;    //age of user
    private char gender;    //user gender
    private String name;    //user's nickname
    
    public User(String name, char gender, int age){
        this.age = age;
        this.gender = gender;
        this.name = name;
    }
    
    public String getNickname(){
        return name;
    }
    
    public Message createMessage(String content){
        return new Message(content);
    }
    
    public int getAge(){
        return age;
    }
    
    public char getGender(){
        return gender;
    }
    
    public String getStringGender(){
        if (getGender() == 'm'){
            return "Male";
        }
        else{
            return "Female";
        }
    }
    
}
