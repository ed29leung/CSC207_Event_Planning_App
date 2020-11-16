package UserPackage;
import EventPackage.EventManager;

import java.util.Scanner;

public class UserController {
    protected int currentUserId;
    protected UserManager userManager;
    protected UserGateway userGateway;
    Scanner scanner = new Scanner(System.in);

    public UserController(UserManager userManager){
        this.userManager = new UserManager();
        this.userGateway = null;
        // the user manager and event manager are formed in the system controller
    }

    public UserManager getUserManager() {
        return userManager;
    }
    public void setUserManager(){
        System.out.println("Enter the path to the saved user manager here");
        String path = scanner.nextLine();
        UserManager newUserManager = userGateway.readUserManager(path);
        if (newUserManager != null){
            this.userManager = newUserManager;
        }
        else{
            this.userManager = new UserManager();
        }

    }

    /**
     TODO: change this so username or ID entered, and function will get User By ID to validate
     */
    public char UserLogin() {
        System.out.println("Enter Username");
        String username = scanner.nextLine();
        System.out.println("Enter Password");
        String password = scanner.nextLine();
        int potentialID = userManager.validateLogin(username, password);
        if (potentialID >= 0 ) {
            currentUserId = potentialID;
            System.out.println("Login Successful");
            return userManager.getUserByID(currentUserId).getType();
        } else {
            System.out.println("Invalid email or password");
            return 'N';
        }
    }
    public int validateUserIDInput(){
        int userID;
        System.out.println("Enter ID of User");
        userID = scanner.nextInt();
        if (userManager.getUserByID(userID) != null){
            return userID;}
        else{
            System.out.println("That is not a Valid UserID. Please try Again.");
            return validateUserIDInput();
        }
    }

    public void saveUserManager(){
        userGateway.saveUserManager(getUserManager());
    }

//    public int validateEventInput(){
//        int eventID;
//        System.out.println("Enter ID of Event");
//        eventID = scanner.nextInt();
//        if (eventManager.getEvent(eventID) != null){
//            return eventID;}
//        else{
//            System.out.println("Invalid Event ID, please Try again");
//            return validateEventInput();
//        }
//    }
}