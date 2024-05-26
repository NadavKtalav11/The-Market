package DomainLayer.User;

public interface State {


    void Logout();
    void exitMarketSystem(User user);
    //void Register(User user, String username, String password, String birthday, String address) throws Exception;
    void Login(User user, String username, String password, Member loginMember) throws Exception;
    boolean isMember();
}
