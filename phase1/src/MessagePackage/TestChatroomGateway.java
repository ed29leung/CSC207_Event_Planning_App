package MessagePackage;

import EventPackage.EventManager;
import UserPackage.UserManager;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

public class TestChatroomGateway {

    public static void main(String[] args) throws IOException {
        EventManager em = new EventManager();
        UserManager um = new UserManager();
        ChatroomController cc = new ChatroomController(em, um);
        ArrayList<Integer> userIDs = new ArrayList<>();


        cc.saveChats();

    }
}
