package DomainLayer.User;

public class Guest implements State{

    @Override
    public void Logout(User user) {
        //todo throw exception
    }

    @Override
    public void exitMarketSystem(User user) {
        //todo think if we need to do here something
    }

    @Override
    public void Login(User user, String username, String password, Member loginMember) {
        user.setState(loginMember);
    }

    @Override
    public boolean isMember() {
        return false;
    }

}
