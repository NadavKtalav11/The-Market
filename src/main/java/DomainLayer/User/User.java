package DomainLayer.User;

import java.util.List;
import java.util.Map;

public class User {

    private int userID;
    private State state;
    private String birthday;
    private String address;
    private Cart cart;

    public User(int userID, String address){
        this.userID = userID;
        this.birthday = null;
        this.address = null;
        this.state = new Guest(); //default state
        this.cart = new Cart();
        this.address = address;
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

    public void exitMarketSystem() {state.exitMarketSystem(this);}

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
    public void setCart(Cart cart) {
        this.cart = cart;
    }

    public Cart getCart() {
        return cart;
    }



    public Map<String, List<Integer>> getCartProductsByStore(int storeId)
    {
        return cart.getProductsDetailsByStore(storeId);
    }

    public List<Integer> getCartStores()
    {
        return cart.getCartStores();
    }

    public boolean isCartEmpty()
    {
        return this.cart.isCartEmpty();
    }

    public String getAddress(){
        return this.address;
    }

    public int getCartTotalPriceBeforeDiscount()
    {
        return this.cart.getCartPrice();
    }
}
