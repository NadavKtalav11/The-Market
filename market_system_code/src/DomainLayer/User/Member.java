package DomainLayer.User;
import DomainLayer.Store.Store;
import DomainLayer.Store.StoreFacade;
import DomainLayer.Role.RoleFacade;

public class Member extends State{

    private StoreFacade storeFacade;
    public RoleFacade roleFacade;
    private int member_ID;
    private boolean isLogin;

    Member(int member_ID)
    {
        this.storeFacade = StoreFacade.getInstance();
        this.roleFacade = RoleFacade.getInstance();
        this.member_ID = member_ID;
    }

    public void logout(User user) {
        // todo save data if needed
        user.setState(new Guest());
        isLogin = false;
    }

    public void addProductToStore(int storeID, String productName, int price, int quantity){
        if (roleFacade.verifyStoreOwner(storeID, member_ID)){
            Store store = storeFacade.getStoreByID(storeID);
            store.addProduct(productName, price, quantity);
        }
    }

    public boolean isLogin()
    {
        return this.isLogin;
    }

    public int getMemberID()
    {
        return this.member_ID;
    }
}
