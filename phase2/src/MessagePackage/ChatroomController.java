package MessagePackage;

import EventPackage.EventUseCases.EventManager;
import UserPackage.UserManager;
import UserPackage.UserType;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;

public class ChatroomController {
    private ArrayList<Chatroom> chats;
    private ChatroomGateway gateway;
    private EventManager eventManager;
    private UserManager userManager;

    /**
     * Creates an instance of ChatroomController that contains all the recorded conversations.
     * Reads in existing saved Chatrooms from ChatroomDataFile.txt
     */
    public ChatroomController(EventManager eventManager, UserManager userManager) {
        this.eventManager = eventManager;
        this.userManager = userManager;
        this.gateway = new ChatroomGateway();
        try {
            this.chats = gateway.makeChats();
        } catch (FileNotFoundException f) {
            this.chats = new ArrayList<Chatroom>();
        }

    }

    public UserManager getUserManager() {
        return userManager;
    }

    public EventManager getEventManager() {
        return eventManager;
    }

    public ArrayList<Chatroom> getChats() {
        return chats;
    }

    /**
     * saves chats to ChatroomDataFile. Should be executed before program exits.
     * @throws IOException if writing to file was unsuccessful
     */
    public void saveChats() throws IOException {
        this.gateway.writeChatsToFile(this.chats);
    }

    /**
     * Returns true if a Chatroom does not already exist
     * @param userlist a list of all users within the chat
     * @param senderUserID the userID of the person creating the Chatroom
     */
    public boolean canCreateNewChatRoom(ArrayList<Integer> userlist, int senderUserID){
        Chatroom c = new Chatroom(userlist);
        if (chats.contains(c)) {
            return false;
        }
        for (int user : userlist) {
            if (!c.canRead(user) || !c.canSend(user)) {
                return false;
            }
        }
        for(int user: userlist){
            if(!userManager.getUserByID(senderUserID).getFriendsList().contains(user) ||
                    !userManager.getUserByID(user).getFriendsList().contains(senderUserID)){ // if someone is not a friend of the sender
                return false; // can't send message to someone who isn't your friend
            }
        }
        return true;
    }

    /**
     * Creates and returns a new Chatroom, if possible. Raises an Error if not.
     * @param userlist a list of all users within the chat
     * @param senderUserID the userID of the person creating the Chatroom
     */
    public Chatroom createNewChatRoom(ArrayList<Integer> userlist, int senderUserID) {
        if(canCreateNewChatRoom(userlist, senderUserID)){
            Chatroom c = new Chatroom(userlist);
            chats.add(c);
            return c;
        }
        else {
            throw new Error("This Chatroom cannot be created.");
        }
    }


    /**
     * Sends a message in an existing Chat, or creates a new one if it doesn't exist
     * @param userlist of everyone you are sending the message to
     * @param senderUserID the ID of the user who is sending the broadcast
     * @param message content of the message you are sending
     */
    public void sendChat(ArrayList<Integer> userlist, int senderUserID, String message){
        Chatroom c = createNewChatRoom(userlist, senderUserID);

        for(Chatroom chatroom: chats){
            if(chatroom.equals(c)){
                c.sendMessage(message, senderUserID);
                System.out.println("Your message has been sent.");
                return;
            }
        }
        c = createNewChatRoom(userlist, senderUserID);
        c.sendMessage(message, senderUserID);
        chats.add(c);
        System.out.println("Your message has been sent.");
    }

    /**
     * Returns all chats for a given userID
     * @param userID identifies user given this userID and returns the Chatrooms they can read
     */
    public ArrayList<Chatroom> returnChatsforUserID(int userID){
        ArrayList<Chatroom> myChats = new ArrayList<>();
        for(Chatroom c: chats){
            if (c.canRead(userID)){
                myChats.add(c);
            }
        }
        return myChats;
    }

    public String myChats(int userID){
        StringBuilder s = new StringBuilder("");
        for (Chatroom c: returnChatsforUserID(userID)){
            s.append(c.format());
            s.append("\n------\n") ;
        }
        return s.toString();
    }


    @Override
    public String toString(){
        StringBuilder s = new StringBuilder("");
        for (Chatroom c: this.chats){
            s.append(c.toString());
            s.append("\n\n") ;
        }
        return s.toString();
    }

    /**
     * Sends a chat to all Speakers at the conference
     * @param organizerUserID the ID of the Organizer sending the chat
     * @param message the message being sent as a chat
     */
    public void messageAllSpeakers(int organizerUserID, String message){
        ArrayList<Integer> speakers = eventManager.getAllSpeakers();
        for(int speaker: speakers){
            userManager.getUserByID(speaker).addFriend(organizerUserID);
            userManager.getUserByID(organizerUserID).addFriend(speaker);
        }
        if(userManager.getUserByID(organizerUserID).getType() == UserType.ORGANIZER) {
            sendChat(speakers, organizerUserID, message);
        }
        else{
            throw new Error("Only organizers can send messages to all speakers.");
        }
    }

    /**
     * Sends a chat to everyone at the conference
     * @param organizerUserID the ID of the Organizer sending the chat
     * @param message the message being sent as a chat
     */
    public void messageAllAttendees(int organizerUserID, String message){
        ArrayList<Integer> attendees = eventManager.getAllAttendees();
        // TODO: how to make all organizers friends with everyone:
        for(int attendee: attendees){
            userManager.getUserByID(attendee).addFriend(organizerUserID);
            userManager.getUserByID(organizerUserID).addFriend(attendee);
        }
        if(userManager.getUserByID(organizerUserID).getType() == UserType.ORGANIZER) {
            sendChat(attendees, organizerUserID, message);
        }
        else{
            throw new Error("Only organizers can send messages to all attendees.");
        }
    }

}















