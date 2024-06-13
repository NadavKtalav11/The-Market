package DomainLayer.User;
//import  DomainLayer.Notifications.Observable;
//import  DomainLayer.Notifications.Observer;
//import  DomainLayer.Notifications.Notification;


import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ElementCollection;
import javax.persistence.Transient;

import Util.CartDTO;
import Util.UserDTO;
import org.bouncycastle.crypto.generators.BaseKDFBytesGenerator;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class User  {

    private String userID;
    private State state;
    private String birthday;
    private String country;
    private String city;
    private String address;
    private String name;

    //@Transient
    //private Observer observer;
    // maps notification to a bool value: true - if was published to user, false - if wasn't

//    @ElementCollection
//    private Map<Notification,Boolean> notifications;

    public User(String userID){
        this.userID = userID;
        this.birthday = null;
        this.country = null;
        this.city = null;
        this.address = null;
        this.state = new Guest(); //default state
        this.name = null;
        //this.cart = new Cart();
    }

    public void updateByDTO(UserDTO userDTO){
        //this.userID = userDTO.getUserId();
        this.birthday = userDTO.getBirthday();
        this.country = userDTO.getCountry();
        this.city = userDTO.getCity();
        this.address = userDTO.getAddress();
        this.name = userDTO.getName();
    }

//    @Override
//    public void registerObserver(Observer observer) {
//        this.observer=observer;
//        notifyObserver();
//    }

    /*@Override
    public boolean notifyObserver(Notification notification) {
        if (notifications.get(notification)!=null && notifications.get(notification))
            return false; //was already published...
        boolean flag=notifications.putIfAbsent(notification, false)==null;
        if(observer.update(notification))
        {
            if(flag) {
                notifications.put(notification, true);
               // DAO.getInstance().merge(this);
                return true;
            }
            return true;
        }
        return false;
        //DAO.getInstance().merge(this);
    }

    @Override
    public void notifyObserver() {
        LinkedList<Notification> published=new LinkedList<>();
        for(Notification notification:notifications.keySet()){
            if(notifyObserver(notification))
                published.add(notification);
        }

        for(Notification n: published){
            notifications.put(n,true);
        }
      //  if(published.size()>0)
          //  DAO.getInstance().merge(this);
    }*/



    public String getUserID(){
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

    public void addToCart(String productName, int quantity, String storeId, int totalPrice)
    {
        state.addItemsToCart(productName, quantity, storeId, totalPrice);
    }

    public void modifyProductInCart(String productName, int quantity, String storeId, int totalPrice)
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

    public State getState()
    {
        return state;
    }

    public boolean checkIfProductInUserCart(String productName, String storeId)
    {
        return state.checkIfProductInUserCart(productName, storeId);
    }

    public void removeItemFromUserCart(String productName, String storeId)
    {
        state.removeItemFromUserCart(productName, storeId);
    }
    public void setCart(Cart cart) {
        state.setCart(cart);
    }

    public Cart getCart() {
        return state.getCart();
    }



    public Map<String, List<Integer>> getCartProductsByStore(String storeId)
    {
        return state.getCartProductsByStore(storeId);
    }

    public List<String> getCartStores()
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

    public void addReceipt(Map<String, String> receiptIdAndStoreId)
    {
        state.addReceipt(receiptIdAndStoreId);
    }

    public String getBirthday(){
        return birthday;
    }

    public CartDTO getCartDTO(){
        return getCart().getDTO(userID);

    }
}
