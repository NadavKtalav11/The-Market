package DomainLayer.User;

public class Guest implements State{

    @Override
    public void Logout(User user) {
        //todo throw exception
    }

    @Override
    public void Exit(User user) {
        //todo think if we need to do here something
    }

    @Override
    public void Register(User user, String username, String password, String birthday, String address) throws Exception {
        //todo think if the user is immediately being member or passed to login.
        user.setState(new Member(user.getUserID(), username,password,birthday,address));
    }

    @Override
    public void Login(User user, String username, String password) {
        int memberID = 0; //todo implement get userID
        String birthday = ""; //todo implement get birthday
        String address = ""; //todo implement get userID
        user.setState(new Member(memberID, username, password, birthday, address));
    }


}
