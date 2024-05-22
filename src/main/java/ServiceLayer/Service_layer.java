package ServiceLayer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import DomainLayer.Market.Market;

public class Service_layer {
    private static final Logger logger = LoggerFactory.getLogger(Service_layer.class);
    private Market market;

    public Service_layer() {

        this.market =  Market.getInstance(); // Initialize the Market instance
    }


    public void init(String userName, String password, int licensedDealerNumber,
                     String paymentServiceName, String url, int licensedDealerNumber1, String supplyServiceName, String address){
        logger.info("Starting the initialization of the system.");
        try{
            market.init(userName, password,licensedDealerNumber,paymentServiceName,
                    url, licensedDealerNumber1, supplyServiceName, address);
        }
        catch (Exception e){
            logger.error("Error occurred during the initialization: {}", e.getMessage(), e);
        }

    }

    public void payWithExternalPaymentService(){
        logger.info("Reaching for the payment service in order to complete the purchase.");
        try {
            market.payWithExternalPaymentService();
        }

        catch (Exception e){
            logger.error("Error occurred with the payment service company: {}", e.getMessage(), e);
        }
    }

    //todo think about the userID and the purpose of this function.
    public void exitMarketSystem(int userID){
        try{
            market.exitMarketSystem(userID);
        }
        catch (Exception e){
            System.out.println(e);
        }
    }

    //todo think about the userID and the purpose of this function.
    public void enterMarketSystem() {
        try{
            market.enterMarketSystem();
        }
        catch (Exception e){
            System.out.println(e);
        }
    }

    //todo think about where we get the userID
    public void register(int userID,String username, String password, String birthday, String address){
        try{
            market.register(userID, username, password,birthday, address);
        }
        catch (Exception e){
            System.out.println(e);
        }
    }

    //todo think about where we get the userID
    public void login(int userID, String username, String password){
        try{
            market.Login(userID, username, password);
        }
        catch (Exception e){
            System.out.println(e);
        }
    }

    //todo think about where we get the userID
    public void logout(int memberID){
        try{
            market.Logout(memberID);
        }
        catch (Exception e){
            System.out.println(e);
        }
    }

    public void addProductToStore(int memberID, int storeID, String productName, int price, int quantity,
                                                                        String description, String categoryStr){
        try{
            market.addProductToStore(memberID, storeID, productName, price, quantity, description, categoryStr);
        }
        catch (Exception e){
            System.out.println(e);
        }
    }

    public void removeProductFromStore(int memberID, int storeID, String productName){
        try{
            market.removeProductFromStore(memberID, storeID, productName);
        }
        catch (Exception e){
            System.out.println(e);
        }
    }

    public void updateProductInStore(int memberID, int storeID, String productName, int price, int quantity,
                                     String description, String categoryStr){
        try{
            market.updateProductInStore(memberID, storeID, productName, price, quantity, description, categoryStr);
        }
        catch (Exception e){
            System.out.println(e);
        }
    }

    public void appointStoreOwner(int firstMemberID, int secondMemberID, int storeID){
        try{
            market.appointStoreOwner(firstMemberID, secondMemberID, storeID);
        }
        catch (Exception e){
            System.out.println(e);
        }
    }

    public void appointStoreManager(int firstMemberID, int secondMemberID, int storeID,
                                        boolean inventoryPermissions, boolean purchasePermissions){
        try{
            market.appointStoreManager(firstMemberID, secondMemberID, storeID,
                                                                    inventoryPermissions, purchasePermissions);
        }
        catch (Exception e) {
            System.out.println(e);
        }
    }

    public void updateStoreManagerPermissions(int firstMemberID, int secondMemberID, int storeID,
                                              boolean inventoryPermissions, boolean purchasePermissions){
        try{
            market.updateStoreManagerPermissions(firstMemberID, secondMemberID, storeID,
                                                                    inventoryPermissions, purchasePermissions);
        }
        catch (Exception e) {
            System.out.println(e);
        }
    }
}
