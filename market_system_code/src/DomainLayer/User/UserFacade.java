package DomainLayer.User;

import DomainLayer.Store.Store;

import java.util.HashMap;
import java.util.Map;

public class UserFacade {
    private static UserFacade userFacadeInstance;
    Map<Integer, User> allUsers = new HashMap<Integer, User>();

    private int currentUserID;

    private UserFacade()
    {
        this.currentUserID = 0;
    }

    public static UserFacade getInstance() {
        if (userFacadeInstance == null) {
            userFacadeInstance = new UserFacade();
        }
        return userFacadeInstance;
    }

    public User getUserByID(int userID){
        return allUsers.get(userID);
    }

    public boolean isUserLoggedIn(int userID){
        return getUserByID(userID).isLoggedIn();
    }

    public int getUsernameByUserID(int userID)
    {
        User user = getUserByID(userID);
        return ((Member)user.getState()).getMemberID();
    }

}
