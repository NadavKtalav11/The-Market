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
        user.setState(new Member(user.getUserID(), username,password,birthday,address));
    }
}
