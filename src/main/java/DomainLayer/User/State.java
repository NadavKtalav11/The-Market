package DomainLayer.User;

import java.util.List;
import java.util.Map;

public abstract class State {

    protected Cart cart;

    protected State(){
        cart = new Cart();
    }

    public void addItemsToCart(String productName, int quantity, int storeId, int totalPrice)
    {
        cart.addItemsToCart(productName, quantity, storeId, totalPrice);
    }

    public void modifyProductInCart(String productName, int quantity, int storeId, int totalPrice)
    {
        cart.modifyProductInCart(productName, quantity, storeId, totalPrice);
    }

    public void calcCartTotal()
    {
        this.cart.calcCartTotal();
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

    public int getCartTotalPriceBeforeDiscount()
    {
        return this.cart.getCartPrice();
    }




    protected abstract void Logout();
    protected abstract void exitMarketSystem();
    //void Register(User user, String username, String password, String birthday, String address) throws Exception;
    protected abstract void Login() throws Exception;
    protected abstract boolean isMember();
    protected abstract void addReceipt(Map<Integer, Integer> receiptIdAndStoreId);
}
