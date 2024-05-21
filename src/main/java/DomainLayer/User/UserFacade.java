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


      

    public void addItemsToBasket(String productName, int quantity, int storeId, int userId, int totalPrice)
    {
        User user = getUserByID(userId);
        user.addToCart(productName, quantity, storeId, totalPrice);
        user.updateCartPrice();
    }

    public void modifyBasketProduct(String productName, int quantity, int storeId, int userId, int totalPrice)
    {
        User user = getUserByID(userId);
        user.modifyProductInCart(productName, quantity, storeId, totalPrice);
        user.updateCartPrice();
    }

    public boolean checkIfCanRemove(String productName, int storeId, int userId)
    {
        User user = getUserByID(userId);
        return user.checkIfProductInUserCart(productName, storeId); //Need to check policies, why?
    }

    public void removeItemFromUserCart(String productName, int storeId, int userId)
    {
        User user = getUserByID(userId);
        user.removeItemFromUserCart(productName, storeId);
    }

    public void Register(int userID, String username, String password, String birthday,String address)    throws Exception {

        //todo check validation of the username.
        //todo check validation of the password.
        //todo check validation of the birthday.
        //todo check validation od the address.
        allUsers.get(userID).Register(username,password,birthday,address);
    }

    public void Login(int userID, String username, String password) throws Exception {
        //todo
        allUsers.get(userID).Login(username,password);
    }




}
