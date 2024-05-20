package DomainLayer.User;
import DomainLayer.Store.Store;
import DomainLayer.Store.StoreFacade;
import DomainLayer.Role.RoleFacade;

public class Member extends State{

    private StoreFacade storeFacade;
    public RoleFacade roleFacade;
    private int member_ID;

    Member(int member_ID)
    {
        this.storeFacade = StoreFacade.getInstance();
        this.roleFacade = RoleFacade.getInstance();
        this.member_ID = member_ID;
    }

    public void openStore() {
        int store_ID = this.storeFacade.openStore();
        this.roleFacade.createStoreOwner(member_ID, store_ID, true);
    }
}
