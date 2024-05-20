package DomainLayer.User;

public class User {

    private State state;
    private Cart cart;

    public User(){
        this.state = new Guest(); //default state
        this.cart = new Cart();
    }

    public void setState(State state) {
        this.state = state;
    }

    public void Logout() {
        if (state instanceof Member) {
            state.Logout(this);
        }
    }

    public void updateCart(int productId, int quantity, int storeId)
    {
        cart.addItemsToCart(productId, quantity, storeId);
    }
}
