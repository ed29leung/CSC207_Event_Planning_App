package MessagePackage;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class ChatroomGateway {
    private File chatroomDataFile;
    private ArrayList<StringBuilder> chatroomData;


    public ChatroomGateway() {
        try {
            this.chatroomDataFile = new File("phase1/src/EventPackage/chatroomData.txt");
            if (this.chatroomDataFile.createNewFile()) {
                this.chatroomData = new ArrayList<>();
            } else {
                this.chatroomData = reader(chatroomDataFile);
            }
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    private ArrayList<StringBuilder> reader(File chatroomDataFile) {
        return null;
    }

    private Message stringToMessage(String s){
        String[] stuff = s.split("~");
        String content = stuff[1];
        Integer sender = Integer.parseInt(stuff[0]);
        return new Message(content, sender);
    }

    private MessageQueue stringToMessageQueue(String s){
        String[] stuff = s.split("\t");
        MessageQueue mq = new MessageQueue();
        for (String messageStr : stuff){
            mq.pushMessage(stringToMessage(messageStr));
        }
        return mq;
    }

    private ArrayList<Integer> stringToUserList(String s){
        ArrayList<Integer> userList = new ArrayList<Integer>();
        if (!s.equals("[]")) {
            String[] stuff = s.substring(1, s.length() - 1).split(", ");
            for (String userIDStr : stuff){
                userList.add(Integer.parseInt(userIDStr));
            }
        }
        return userList;
    }

    private Chatroom stringToChatroom(String s){

        String[] stuff = s.split("\n"); // [0] is myStatus, [1] is userList, [2] is MessageQueue


        // Overloading.. make a second constructor for Chatroom that takes MessageQueue and myStatus as param
    }

    private void fileToChatroomController(){

    }

    /*
    ArrayList is the list
    values is list of objects in ArrayList

    if (!ArrayList.equals("[]")) {
        String[] values = ArrayList.substring(1, ArrayList.length() - 1).split(", ");
    } else {
        / the ArrayList is empty
    }
    */

}
