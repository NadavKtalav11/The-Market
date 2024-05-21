package DomainLayer.User;

public class User {

    private int userID;
    private State state;
    private Cart cart;

    public User(int userID){
        this.userID = userID;
        this.state = new Guest(); //default state
        this.cart = new Cart();
    }

    public int getUserID(){
        return userID;
    }

    public void setState(State state) {
        this.state = state;
    }

    public void Logout() {
        state.Logout(this);
    }

    public void Exit() {state.Exit(this);}

    public void updateCart(int productId, int quantity, int storeId)
    {
        cart.addItemsToCart(productId, quantity, storeId);

    }

    public void Register(String username, String password, String birthday, String address) throws Exception {
        state.Register(this,username,password, birthday,address);
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
