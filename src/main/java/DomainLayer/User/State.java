package DomainLayer.User;

import java.util.List;
import java.util.Map;

public abstract class State {

    protected Cart cart;

    protected State(){
        cart = new Cart();
    }

    public void addItemsToCart(String productName, int quantity, String storeId, int totalPrice)
    {
        cart.addItemsToCart(productName, quantity, storeId, totalPrice);
    }

    public void modifyProductInCart(String productName, int quantity, String storeId, int totalPrice)
    {
        cart.modifyProductInCart(productName, quantity, storeId, totalPrice);
    }

    public void calcCartTotal()
    {
        this.cart.calcCartTotal();
    }

    public boolean checkIfProductInUserCart(String productName, String storeId)
    {
        return cart.checkIfProductInCart(productName, storeId);
    }

    public void removeItemFromUserCart(String productName, String storeId)
    {
        cart.removeItemFromCart(productName, storeId);
    }
    public void setCart(Cart cart) {
        this.cart = cart;
    }

    public Cart getCart() {
        return cart;
    }

    public Map<String, List<Integer>> getCartProductsByStore(String storeId)
    {
        return cart.getProductsDetailsByStore(storeId);
    }

    public List<String> getCartStores()
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
    public abstract boolean isMember();
    public abstract String getUsername();
    public abstract void addAcquisition(String acquisitionId);
    public abstract List<String> getAcquisitionIds();
}
