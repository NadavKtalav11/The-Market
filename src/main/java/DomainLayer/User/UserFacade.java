package DomainLayer.User;


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

    public void addItemsToBasket(int productId, int quantity, int storeId, int userId)
    {
        User user = getUserByID(userId);
        user.updateCart(productId, quantity, storeId);
    }
  
    public boolean isUserLoggedIn(int userID){
        return getUserByID(userID).isLoggedIn();
    }

    public int getUsernameByUserID(int userID)
    {
        User user = getUserByID(userID);
        return ((Member)user.getState()).getMemberID();
    }

    public void Exit(int userID){
        //todo remove token when Nadav finish.
        allUsers.remove(userID);
        allUsers.get(userID).Exit();
    }

}
