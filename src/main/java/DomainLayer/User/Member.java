package DomainLayer.User;
import DomainLayer.Store.Store;
import DomainLayer.Store.StoreFacade;
import DomainLayer.Role.RoleFacade;
import Util.ExceptionsEnum;
import Util.UserDTO;

import java.util.HashMap;
import java.util.Map;
import java.util.zip.CheckedOutputStream;

public class Member extends State{

    private String userId;
    private String member_ID;
    private String name;
    private String username;
    private String password;
    private String birthday;
    private String country;
    private String city;
    private String address;
    private int productIdCounter;
    private Map<String, String> receiptIdsAndStoreId; //<receiptId, storeId>
    //private boolean isLogin;

    /*Member(String member_ID, UserDTO user, String password)
    {
        this.member_ID = member_ID;
        this.username = user.getUserName();
        this.password = password;
        this.birthday = user.getBirthday();
        this.country = user.getCountry();
        this.city =  user.getCity();
        this.address = user.getAddress();
        this.name = user.getName();
        this.productIdCounter = 0;
        this.receiptIdsAndStoreId = new HashMap<>();
    }*/

    public Member(String userId , String member_ID, String username,String address, String name, String password , String birthday, String country, String city )
    {
        this.userId = userId;
        this.member_ID = member_ID;
        this.username =username;
        this.password = password;
        this.birthday = birthday;
        this.country = country;
        this.city =  city;
        this.address = address;
        this.name = name;
        this.productIdCounter = 0;
        this.receiptIdsAndStoreId = new HashMap<>();
    }


    public void Logout()
    {
        // do nothing
    }

    public void exitMarketSystem(){
    }

    @Override
    public void Login() throws Exception {
        throw new Exception(ExceptionsEnum.userAlreadyLoggedIn.toString());
    }

    public void validatePassword(String password){
        if (!password.equals(this.password)){
            throw new IllegalArgumentException(ExceptionsEnum.usernameOrPasswordIncorrect.toString());
        }
    }

    @Override
    public String getUsername(){
        return username;
    }

    public String getName() {
        return name;
    }

    public String getCountry() {
        return country;
    }

    public String getCity() {
        return city;
    }

    public String getAddress() {
        return address;
    }

    public String getBirthday() {
        return birthday;
    }

    public String getPassword() {
        return password;
    }

    public Map<String, String> getReceiptIdsAndStoreId(){return receiptIdsAndStoreId;}

    public String getMemberID()
    {
        return this.member_ID;
    }

    @Override
    public boolean isMember() {
        return true;
    }

    @Override
    public void addReceipt(Map<String, String> receiptIdAndStoreId) {
        receiptIdsAndStoreId.putAll(receiptIdAndStoreId);
    }

    public String getMember_ID() {
        return member_ID;
    }

    public String getUserId() {
        return userId;
    }
}
