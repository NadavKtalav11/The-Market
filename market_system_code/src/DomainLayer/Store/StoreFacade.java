package DomainLayer.Store;

public class StoreFacade {
    private static StoreFacade storeFacadeInstance;
    private int currentStoreID;

    private StoreFacade()
    {
        this.currentStoreID = 0;
    }

    public static StoreFacade getInstance() {
        if (storeFacadeInstance == null) {
            storeFacadeInstance = new StoreFacade();
        }
        return storeFacadeInstance;
    }

    public int openStore()
    {
        Store newStore = new Store(currentStoreID); //todo: add this to list in repository
        this.currentStoreID++;
        return newStore.getStoreID();
    }
}
