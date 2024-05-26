package DomainLayer.User;
import DomainLayer.Store.Store;
import DomainLayer.Store.StoreFacade;
import DomainLayer.Role.RoleFacade;

import java.util.zip.CheckedOutputStream;

public class Member implements State{

    private int member_ID;
    private String name;
    private String username;
    private String password;
    private String birthday;
    private String country;
    private String city;
    private String address;
    private int productIdCounter;
    private boolean isLogin;
  
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
    }

    public void Logout()
    {
        // todo save data if needed
        if (!isLogin){
            throw new IllegalArgumentException("member is already logged out");
        }
        isLogin = false;
    }

    public void exitMarketSystem(User user){
        //todo understand what happens after user press x.
        user.Logout();
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

    public boolean isLogin()
    {
        return this.isLogin;
    }

    public int getMemberID()
    {
        return this.member_ID;
    }


    @Override
    public void Login(User user, String username, String password, Member loginMember) throws Exception {
        throw new Exception("The user is already logged in");

    }

    @Override
    public boolean isMember() {
        return true;
    }

}
