package DomainLayer.Store;

public class Store {
    private int store_ID;

    Store(int store_ID)
    {
        this.store_ID = store_ID;
    }

    public int getStoreID()
    {
        return store_ID;
    }
}
