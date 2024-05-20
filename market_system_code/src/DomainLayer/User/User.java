package DomainLayer.User;

public class User {

    private State state;

    public User(){
        this.state = new Guest(); //default state
    }

    public void setState(State state) {
        this.state = state;
    }

    public void Logout() {
        if (state instanceof Member) {
            state.Logout(this);
        }
    }

    public boolean isLoggedIn()
    {
        return state instanceof Member;
    }

    public State getState()
    {
        return state;
    }
}
