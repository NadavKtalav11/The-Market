package DomainLayer.User;


import Util.CartDTO;
import Util.ExceptionsEnum;
import Util.UserDTO;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;


@Component
public class UserFacade {
    private static UserFacade userFacadeInstance;
    UserRepository<User> userRepository;
    MemberRepository members;
    //private Object membersLock;
    //Map<String, Member> members = new HashMap<>(); //memberID-Member
    //private String memberIdPrefix;
    //private String userIdPrefix;
    //private int currentUserID;
    //private int currentMemberID;
    //Object allUserLock;

    //private Object userIdLock;
    //private Object memberIdLock;
    //Map<Integer, User> allUsers = new HashMap<Integer, User>(); //userID-User

    private UserFacade()
    {
        //this.currentUserID = 0;
        //this.currentMemberID = 0;
        //allUserLock = new Object();

        //membersLock = new Object();
        userRepository = new UserMemoryRepository<>();
        members = new MemberMemoryRepository();

        //userIdLock = new Object();
        //memberIdLock = new Object();

        //memberIdPrefix = "member";
        //memberIdPrefix = "user";
    }

    public synchronized static UserFacade getInstance() {
        if (userFacadeInstance == null) {
            userFacadeInstance = new UserFacade();
        }
        return userFacadeInstance;
    }

    public UserFacade newForTest(){
        userFacadeInstance= new UserFacade();
        return userFacadeInstance;
    }

    public String getCurrentUserID (){
        UUID uuid = UUID.randomUUID();
        String uniqueId = "user-" + uuid;
        return uniqueId;
    }

    public String getCurrentMemberID (){
        UUID uuid = UUID.randomUUID();
        String uniqueId = "member-" + uuid;
        return uniqueId;
    }


    public User getUserByID(String userID){
        return userRepository.get(userID);
    }

    public void errorIfUserNotExist(String userID) throws Exception {
        if (getUserByID(userID) == null){
            throw new Exception(ExceptionsEnum.userNotExist.toString());
        }
    }

    public void isUserLoggedInError(String userID){
        if(!isMember(userID))
            throw new IllegalArgumentException(ExceptionsEnum.userIsNotMember.toString());
    }

    /*public String getUsernameByUserID(String userID)
    {
        if(!userRepository.contain(userID)){
            return -1;
        }
        User user = getUserByID(userID);
        return ((Member)user.getState()).getMemberID();
    }*/

    public boolean isMember(String userId){
        if(getUserByID(userId) == null){
            return false;
        }
        return getUserByID(userId).isMember();
    }


    public void errorIfUserNotMember(String userId) throws Exception {
        if(!isMember(userId)){
            throw new Exception(ExceptionsEnum.userIsNotMember.toString());
        }
    }


    public String getMemberIdByUserId(String userID) throws Exception {
        if(isMember(userID)){
            String username = getUserByID(userID).getState().getUsername();
            return getMemberByUsername(username).getMemberID();
        }
        else {
            throw new Exception("User is not a member");
        }
    }

    public void exitMarketSystem(String userID){

        userRepository.get(userID).exitMarketSystem();
        userRepository.remove(userID); //todo do i need to remove the user from the list of users ?
    }


    public String addUser(){
        String userId;
        userId = getCurrentUserID();
        userRepository.add(userId, new User(userId));
        return userId;
    }


    public void addItemsToBasket(String productName, int quantity, String storeId, String userId, int totalPrice)
    {
        User user = getUserByID(userId);
        user.addToCart(productName, quantity, storeId, totalPrice);
        user.updateCartPrice();
    }

    public void modifyBasketProduct(String productName, int quantity, String storeId, String userId, int totalPrice)
    {
        User user = getUserByID(userId);
        user.modifyProductInCart(productName, quantity, storeId, totalPrice);
        user.updateCartPrice();
    }

    public void checkIfCanRemove(String productName, String storeId, String userId)
    {
        User user = getUserByID(userId);
        if(!user.checkIfProductInUserCart(productName, storeId))
            throw new IllegalArgumentException(ExceptionsEnum.productNotExistInCart.toString());
    }

    public void removeItemFromUserCart(String productName, String storeId, String userId)
    {
        User user = getUserByID(userId);
        user.removeItemFromUserCart(productName, storeId);
    }


    public String register(String userID, UserDTO user,String password) throws Exception {
        if(userRepository.contain(userID)&& getUserByID(userID).isMember()) {
            throw new Exception(ExceptionsEnum.memberCannotRegister.toString());
        }
        else {
            validateRegistrationDetails(user, password);
            String memberId = getCurrentMemberID();


            Member newMember = new Member(memberId,user.getUserName(), user.getAddress(), user.getName(), password, user.getBirthday(), user.getCountry(), user.getCity());
            members.add(memberId, newMember);
            //todo pass the user to login page.
            return memberId;
        }
    }

    public String registerSystemAdmin(UserDTO user, String password) throws Exception {

        validateRegistrationDetails(user,password);
        String memberId = getCurrentMemberID();

        Member newMember = new Member(memberId,user.getUserName(), user.getAddress(), user.getName(), password, user.getBirthday(), user.getCountry(), user.getCity());
            members.add(memberId, newMember);

        return memberId;
        //todo pass the user to login page.
    }



    private void validateRegistrationDetails(UserDTO user, String password) throws Exception {
        if (user.getUserName() == null || password == null || user.getBirthday() == null || user.getCountry() ==null || user.getCity() == null ||
                user.getAddress() == null || user.getName() == null) {
            throw new Exception(ExceptionsEnum.emptyField.toString());
        }
        //checking empty fields
        else if (user.getUserName().equals("") || password.equals("") || user.getBirthday().equals("") || user.getCountry().equals("") || user.getCity().equals("") ||
                user.getAddress().equals("") || user.getName().equals("")) {
            throw new Exception(ExceptionsEnum.emptyField.toString());
        }
        //checking if username is already exist
        Member mem = members.getByUserName(user.getUserName());
        if (mem!=null) {
            throw new Exception(ExceptionsEnum.usernameAlreadyExist.toString());

        }
        // Check validation of the birthday
        if (!isValidDate(user.getBirthday())) {
            throw new Exception(ExceptionsEnum.invalidFormatDate.toString());
        }
        if (isFutureDate(user.getBirthday())) {
            throw new Exception(ExceptionsEnum.futureDate.toString());
        }
//        // Check validation of the country and city
//        if (!isValidCountry(user.getCountry())) {
//            throw new Exception(ExceptionsEnum.invalidCountry.toString());
//        }
//        if (!isValidCity(user.getCity(), user.getCountry())) {
//            throw new Exception(ExceptionsEnum.invalidCity.toString());
//        }
    }

    private boolean isValidDate(String date) {
        List<SimpleDateFormat> dateFormats = new ArrayList<>();
        dateFormats.add(new SimpleDateFormat("dd/MM/yyyy"));
        dateFormats.add(new SimpleDateFormat("dd/MM/yy"));

        for (SimpleDateFormat sdf : dateFormats) {
            sdf.setLenient(false);
            try {
                sdf.parse(date);
                return true;
            } catch (ParseException e) {
                // Ignore and try the next format
            }
        }
        return false;
    }

    private boolean isFutureDate(String date) throws ParseException {
        List<SimpleDateFormat> dateFormats = new ArrayList<>();
        dateFormats.add(new SimpleDateFormat("dd/MM/yyyy"));
        dateFormats.add(new SimpleDateFormat("dd/MM/yy"));

        for (SimpleDateFormat sdf : dateFormats) {
            try {
                Date birthday = sdf.parse(date);
                Date today = new Date();
                return birthday.after(today);
            } catch (ParseException e) {
                // Ignore and try the next format
            }
        }

        throw new ParseException("Invalid date format", 0);
    }


    public String Login(String userID, String username, String password) throws Exception {
        Member loginMember = getMemberByUsername(username);
        if (loginMember == null){
            throw new Exception(ExceptionsEnum.usernameOrPasswordIncorrect.toString());
        }
        /*else if (!loginMember.getPassword().equals(password)){
            throw new Exception("Username or password is incorrect");
        }*/
        loginMember.validatePassword(password);
        getUserByID(userID).Login(loginMember);
        return loginMember.getMemberID();
    }

    public Member getMemberByUsername(String userName) {
        return members.getByUserName(userName);
    }

    public void errorIfUsernameNotFound(String userName) throws Exception {
        if(getMemberByUsername(userName) == null){
            throw new Exception(ExceptionsEnum.usernameNotFound.toString());
        }
    }

    public List<String> getCartStoresByUser(String user_ID)
    {
        User user = getUserByID(user_ID);
        if(user != null)
            return user.getCartStores();
        else
            return null;
    }

    public Map<String, List<Integer>> getCartProductsByStoreAndUser(String store_ID, String user_ID)
    {
        User user = getUserByID(user_ID);
        if(user != null)
            return user.getCartProductsByStore(store_ID);
        else
            return null;
    }

    public void isUserCartEmpty(String user_ID) throws Exception
    {
        if(getUserByID(user_ID).isCartEmpty())
            throw new Exception(ExceptionsEnum.userCartIsEmpty.toString());
    }

    public String getUserAddress(String user_ID)
    {
        return getUserByID(user_ID).getAddress();
    }

    public int getCartPriceByUser(String user_ID)
    {
        /*this function returns the cart total price before discounts, of a specific user*/
        return getUserByID(user_ID).getCartTotalPriceBeforeDiscount();
    }

    public void addReceiptToUser(Map<String, String> receiptIdAndStoreId, String userId)
    {
        userRepository.get(userId).addReceipt(receiptIdAndStoreId);
    }

    public List<UserDTO> getAllUsers(){
        List<User> users = userRepository.getAll();
        List<UserDTO> userDTOList = new ArrayList<>();
        for (User user:users ){
            userDTOList.add(new UserDTO(user));
        }
        return userDTOList;
    }

    public UserDTO getUserDTOById(String userID){
        return new UserDTO(getUserByID(userID));
    }

    public UserDTO updateUser( UserDTO userDTO){
        User user = getUserByID(userDTO.getUserId());
        user.updateByDTO(userDTO);
        return new UserDTO(user);

    }

    public void removeUser(String userId){
        userRepository.remove(userId);
    }

    public CartDTO getCartDTO(String userId){
        return getUserByID(userId).getCartDTO();
    }

}
