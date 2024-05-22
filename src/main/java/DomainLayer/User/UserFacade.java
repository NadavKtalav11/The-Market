package DomainLayer.User;


import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class UserFacade {
    private static UserFacade userFacadeInstance;
    Map<Integer, User> allUsers = new HashMap<Integer, User>();
    Map<Integer, Member> members = new HashMap<>();
    private int currentUserID;
    private int currentMemberID;

    private UserFacade()
    {
        this.currentUserID = 0;
        this.currentMemberID = 0;
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


    public void addUser(){
        allUsers.put(currentUserID+1, new User(currentUserID+1));
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


    public void register(int userID, String username, String password, String birthday,String address) throws Exception {
        if (allUsers.get(userID).isMember()){
            throw new Exception("member cannot register");
        }
        else {
            validateRegistrationDetails(username,password,birthday,address);
            Member newMember = new Member(currentMemberID, username,password,birthday,address);
            members.put(currentMemberID, newMember);
            //todo pass the user to login page.
        }
    }


    private void validateRegistrationDetails(String username, String password, String birthDate, String address) throws Exception {
        if (username == null || password == null || birthDate == null || address == null) {
            throw new Exception("All fields are required.");
        }
        //checking if username is already exist
        for (User user : allUsers.values()){
            if (Objects.equals(((Member) user.getState()).getUsername(), username)){
                throw new Exception("Username already exists. Please choose a different username.");
            }
        }
        //todo check validation of the password. - do encription passwords only.
        //todo check validation of the birthday.
        //todo check validation of the address.
    }

    public void Login(int userID, String username, String password) throws Exception {
        allUsers.get(userID).Login(username,password);
    }




}
