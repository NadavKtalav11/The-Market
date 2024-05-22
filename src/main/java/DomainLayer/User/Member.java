package DomainLayer.User;
import DomainLayer.Store.Store;
import DomainLayer.Store.StoreFacade;
import DomainLayer.Role.RoleFacade;

public class Member implements State{

    private int member_ID;
    private String username;
    private String password;
    private String birthday; // todo think about the implementation
    private String address; // todo think about the implementation
    private int productIdCounter;
    private boolean isLogin;
  
    Member(int member_ID, String username, String password, String birthday, String address)
    {
        this.member_ID = member_ID;
        this.username = username;
        this.password = password;
        this.birthday = birthday;
        this.address = address;
        this.productIdCounter = 0;
    }

    public void Logout(User user)
    {
        // todo save data if needed
        user.setState(new Guest());
        isLogin = false;
    }

    public void Exit(User user){
        //todo understand what happens after user press x.
        user.Logout();
    }

    public String getUsername(){
        return username;
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

    public String getUsername()
    {
        return this.username;
    }

    @Override
    public void Login(User user, String username, String password) throws Exception {
        throw new Exception("The user is already logged in");

    }

    @Override
    public boolean isMember() {
        return true;
    }

}
