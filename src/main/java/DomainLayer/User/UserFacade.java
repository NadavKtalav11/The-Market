package DomainLayer.User;


import DomainLayer.Store.StoreFacade;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class UserFacade {
    private static UserFacade userFacadeInstance;
    Map<Integer, User> allUsers = new HashMap<Integer, User>(); //userID-User
    Map<Integer, Member> members = new HashMap<>(); //memberID-Member
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

    public UserFacade newForTest(){
        userFacadeInstance= new UserFacade();
        return userFacadeInstance;
    }

    public synchronized static UserFacade getInstance() {
        if (userFacadeInstance == null) {
            userFacadeInstance = new UserFacade();
        }
        return userFacadeInstance;
    }

    public int getCurrentUserID (){
        return currentUserID;
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
        if(!allUsers.containsKey(userID)){
            return -1;
        }
        User user = getUserByID(userID);
        return ((Member)user.getState()).getMemberID();
    }

    public boolean isMember(int userId){
        if(!allUsers.containsKey(userId)){
            return false;
        }
        return getUserByID(userId).isMember();
    }

    public int getMemberIdByUserId(int userID) throws Exception {
        if(isMember(userID)){
            String username = getUserByID(userID).getState().getUsername();
            return getMemberByUsername(username).getMemberID();
        }
        else {
            throw new Exception("User is not a member");
        }
    }

    public void exitMarketSystem(int userID){
        synchronized (allUserLock) {
            allUsers.get(userID).exitMarketSystem();
            allUsers.remove(userID); //todo do i need to remove the user from the list of users ?

        }
    }


    public int addUser(){
        int userId;
        synchronized (userIdLock) {
            userId = currentUserID;
        }
        synchronized (allUserLock) {
            allUsers.put(currentUserID, new User(currentUserID));
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


    public void register(int userID, String username, String password, String birthday,String country, String city,String address, String name) throws Exception {
        if(allUsers.containsKey(userID)&& getUserByID(userID).isMember()) {
            throw new Exception("member cannot register");
        }
        else {
            validateRegistrationDetails(username,password,birthday,country,city,address,name);
            int memberId;
            synchronized (memberIdLock){
                memberId = currentMemberID;
            }
            Member newMember = new Member(memberId, username,password,birthday,country,city,address,name);
            synchronized (membersLock) {
                members.put(memberId, newMember);

            }
            synchronized (memberIdLock) {
                currentMemberID++;
            }
            //todo pass the user to login page.
        }
    }

    public int registerSystemAdmin(String username, String password, String birthday,String country, String city,String address, String name) throws Exception {

        validateRegistrationDetails(username,password,birthday,country,city,address,name);
        int memberId;
        synchronized (memberIdLock){
            memberId = currentMemberID;
        }
        Member newMember = new Member(memberId, username,password,birthday,country,city,address,name);
        synchronized (membersLock) {
            members.put(memberId, newMember);
            currentMemberID++;
        }
        return memberId;
        //todo pass the user to login page.
    }



    private void validateRegistrationDetails(String username, String password , String birthDate, String country, String city, String address, String name) throws Exception {
        if (username == null || password == null || birthDate == null || country ==null || city == null ||
                address == null || name == null) {
            throw new Exception("All fields are required.");
        }
        //checking if username is already exist
        synchronized (members) {
            for (Member member : members.values()) {
                if (Objects.equals(member.getUsername(), username)) {
                    throw new Exception("Username already exists. Please choose a different username.");
                }
            }
        }
        //todo check validation of the password. - do encription passwords only.
        //todo check validation of the birthday. - do we need to check this, in the gui the user will choose date from the calender.
        //todo check validation of the address. - do we need to check this, in the gui the user will address date from the calender.
    }

    public void Login(int userID, String username, String password) throws Exception {
        Member loginMember = getMemberByUsername(username);
        if (loginMember == null){
            throw new Exception("Username or password is incorrect");
        }
        /*else if (!loginMember.getPassword().equals(password)){
            throw new Exception("Username or password is incorrect");
        }*/
        loginMember.validatePassword(password);
        getUserByID(userID).Login(loginMember);

    }

    public Member getMemberByUsername(String userName) throws Exception {
        for (Member member : members.values()) {
            if (member.getUsername().equals(userName)) {
                return member;
            }
        }
        return null;
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

    public void addReceiptToUser(Map<Integer, Integer> receiptIdAndStoreId, int userId)
    {
        allUsers.get(userId).addReceipt(receiptIdAndStoreId);
    }
}
