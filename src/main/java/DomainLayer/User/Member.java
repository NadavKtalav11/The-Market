package DomainLayer.User;
import DomainLayer.Store.Store;
import DomainLayer.Store.StoreFacade;
import DomainLayer.Role.RoleFacade;

import java.util.HashMap;
import java.util.Map;
import java.util.zip.CheckedOutputStream;

public class Member extends State{

    private int member_ID;
    private String name;
    private String username;
    private String password;
    private String birthday;
    private String country;
    private String city;
    private String address;
    private int productIdCounter;
    private Map<Integer, Integer> receiptIdsAndStoreId; //<receiptId, storeId>
    //private boolean isLogin;
  
    Member(int member_ID, String username, String password, String birthday,String country, String city, String address, String name)
    {
        this.member_ID = member_ID;
        this.username = username;
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


    public int getMemberID()
    {
        return this.member_ID;
    }

    @Override
    public boolean isMember() {
        return true;
    }

    @Override
    public void addReceipt(Map<Integer, Integer> receiptIdAndStoreId) {
        receiptIdsAndStoreId.putAll(receiptIdAndStoreId);
    }

}
