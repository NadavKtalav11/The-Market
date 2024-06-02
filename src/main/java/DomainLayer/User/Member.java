package DomainLayer.User;
import DomainLayer.Store.Store;
import DomainLayer.Store.StoreFacade;
import DomainLayer.Role.RoleFacade;
import Util.UserDTO;

import java.util.HashMap;
import java.util.Map;
import java.util.zip.CheckedOutputStream;

public class Member extends State{

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

    Member(String member_ID, UserDTO user, String password)
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
    }


    public void Logout()
    {
        // todo save data if needed
        // do nothing

    }

    public void exitMarketSystem(){
        //todo understand what happens after user press x.
    }

    @Override
    public void Login() throws Exception {
        throw new Exception("The user is already logged in");
    }

    public void validatePassword(String password){
        if (!password.equals(this.password)){
            throw new IllegalArgumentException("Incorrect password or username please try again.");
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

}
