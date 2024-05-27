package DomainLayer.User;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class User {

    private int userID;
    private State state;
    private String birthday;
    private String country;
    private String city;
    private String address;
    private String name;
    //private Cart cart;

    public User(int userID){
        this.userID = userID;
        this.birthday = null;
        this.country = null;
        this.city = null;
        this.address = null;
        this.state = new Guest(); //default state
        //this.cart = new Cart();
    }

    public int getUserID(){
        return userID;
    }

    public void setState(State state) {
        this.state = state;
    }

    public String getCountry(){
        return this.country;
    }

    public String getName(){return this.name;}

    public String getCity(){return this.city;}

    public boolean isMember(){ return this.state.isMember();}

    public void Logout() {
        state.Logout();
        state = new Guest();
    }

    public void exitMarketSystem() {
        //state.exitMarketSystem(this);
    }

    public void addToCart(String productName, int quantity, int storeId, int totalPrice)
    {
        state.addItemsToCart(productName, quantity, storeId, totalPrice);
    }

    public void modifyProductInCart(String productName, int quantity, int storeId, int totalPrice)
    {
        state.modifyProductInCart(productName, quantity, storeId, totalPrice);
    }

    public void updateCartPrice()
    {
        state.calcCartTotal();
    }

    public void Login(Member loginMember) throws Exception {
        state.Login();
        setState(loginMember);
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
        return state.checkIfProductInUserCart(productName, storeId);
    }

    public void removeItemFromUserCart(String productName, int storeId)
    {
        state.removeItemFromUserCart(productName, storeId);
    }
    public void setCart(Cart cart) {
        state.setCart(cart);
    }

    public Cart getCart() {
        return state.getCart();
    }



    public Map<String, List<Integer>> getCartProductsByStore(int storeId)
    {
        return state.getCartProductsByStore(storeId);
    }

    public List<Integer> getCartStores()
    {
        return state.getCartStores();
    }

    public boolean isCartEmpty()
    {
        return state.isCartEmpty();
    }

    public String getAddress(){
        return this.address;
    }

    public int getCartTotalPriceBeforeDiscount()
    {
        return state.getCartTotalPriceBeforeDiscount();
    }

    public void addReceipt(Map<Integer, Integer> receiptIdAndStoreId)
    {
        state.addReceipt(receiptIdAndStoreId);
    }
}
