package DomainLayer.User;

public interface State {


    void Logout(User user);
    void Exit(User user);
    //void register(User user, String username, String password, String birthday, String address) throws Exception;
    void Login(User user, String username, String password) throws Exception;
    boolean isMember();
}
