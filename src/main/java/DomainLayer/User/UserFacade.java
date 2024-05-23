package DomainLayer.User;


import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class UserFacade {
    private static UserFacade userFacadeInstance;
    Map<Integer, User> allUsers = new HashMap<Integer, User>();
    Map<Integer, Member> members = new HashMap<>();
    private int currentUserID;
    private int currentMemberID;
    Object allUserLock;
    Object membersLock;
    Object userIdLock;
    Object memberIdLock;

    private UserFacade()
    {
        this.currentUserID = 0;
        this.currentMemberID = 0;
        allUserLock = new Object();
        membersLock = new Object();
        userIdLock = new Object();
        memberIdLock = new Object();
    }

    public synchronized static UserFacade getInstance() {
        if (userFacadeInstance == null) {
            userFacadeInstance = new UserFacade();
        }
        return userFacadeInstance;
    }

    public User getUserByID(int userID){
        synchronized (allUserLock) {
            return allUsers.get(userID);
        }
    }

    public boolean isUserLoggedIn(int userID){

        return getUserByID(userID).isLoggedIn();
    }

    public int getUsernameByUserID(int userID)
    {
        User user = getUserByID(userID);
        return ((Member)user.getState()).getMemberID();
    }

    public void exitMarketSystem(int userID){
        synchronized (allUserLock) {
            allUsers.remove(userID); //todo do i need to remove the user from the list of users ?
            (allUsers.get(userID)).exitMarketSystem();
        }
    }


    public int addUser(){
        int userId;
        synchronized (userIdLock) {
            userId = currentUserID;
        }
        synchronized (allUserLock) {
            allUsers.put(currentUserID, new User(currentUserID, "", "", ""));
        }
        synchronized (userIdLock) {
            currentUserID++;
        }
        return userId;
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

        if (getUserByID(userID).isMember()) {
            throw new Exception("member cannot register");
        }
        else {
            validateRegistrationDetails(username,password,birthday,address);
            int memberId;
            synchronized (memberIdLock){
                memberId = currentMemberID;
            }
            Member newMember = new Member(memberId, username,password,birthday,address);
            synchronized (membersLock) {
                members.put(memberId, newMember);
            }
            //todo pass the user to login page.
        }
    }


    private void validateRegistrationDetails(String username, String password, String birthDate, String address) throws Exception {
        if (username == null || password == null || birthDate == null || address == null) {
            throw new Exception("All fields are required.");
        }
        //checking if username is already exist
        synchronized (allUsers) {
            for (User user : allUsers.values()) {
                if (Objects.equals(((Member) user.getState()).getUsername(), username)) {
                    throw new Exception("Username already exists. Please choose a different username.");
                }
            }
        }
        //todo check validation of the password. - do encription passwords only.
        //todo check validation of the birthday.
        //todo check validation of the address.
    }

    public void Login(int userID, String username, String password) throws Exception {
        getUserByID(userID).Login(username,password);
    }

    public List<Integer> getCartStoresByUser(int user_ID)
    {
        User user = getUserByID(user_ID);
        if(user != null)
            return user.getCartStores();
        else
            return null;
    }

    public Map<String, List<Integer>> getCartProductsByStoreAndUser(int store_ID, int user_ID)
    {
        User user = getUserByID(user_ID);
        if(user != null)
            return user.getCartProductsByStore(store_ID);
        else
            return null;
    }

    public boolean isUserCartEmpty(int user_ID)
    {
        return getUserByID(user_ID).isCartEmpty();
    }

    public String getUserAddress(int user_ID)
    {
        return getUserByID(user_ID).getAddress();
    }

    public int getCartPriceByUser(int user_ID)
    {
        /*this function returns the cart total price before discounts, of a specific user*/
        return getUserByID(user_ID).getCartTotalPriceBeforeDiscount();
    }
}
