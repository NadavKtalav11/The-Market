package DomainLayer.User;

public class User {

    private int userID;
    private State state;
    private String birthday;
    private String address;
    private Cart cart;

    public User(int userID){
        this.userID = userID;
        this.birthday = null;
        this.address = null;
        this.state = new Guest(); //default state
        this.cart = new Cart();
    }

    public int getUserID(){
        return userID;
    }

    public void setState(State state) {
        this.state = state;
    }

    public boolean isMember(){ return this.state.isMember();}

    public void Logout() {
        state.Logout(this);
    }

    public void Exit() {state.Exit(this);}

    public void addToCart(String productName, int quantity, int storeId, int totalPrice)
    {
        cart.addItemsToCart(productName, quantity, storeId, totalPrice);
    }

    public void modifyProductInCart(String productName, int quantity, int storeId, int totalPrice)
    {
        cart.modifyProductInCart(productName, quantity, storeId, totalPrice);
    }

    public void updateCartPrice()
    {
        this.cart.calcCartTotal();
    }

    public void Login(String username, String password) throws Exception {
        state.Login(this,username,password);
    }
    

    public boolean isLoggedIn()
    {
        return state instanceof Member;
    }

    public State getState()
    {
        return state;
    }

    public boolean checkIfProductInUserCart(String productName, int storeId)
    {
        return cart.checkIfProductInCart(productName, storeId);
    }

    public void removeItemFromUserCart(String productName, int storeId)
    {
        cart.removeItemFromCart(productName, storeId);
    }
}
