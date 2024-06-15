package PresentationLayer.WAF;
import ServiceLayer.Response;
import Util.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import DomainLayer.Market.Market;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;


@Service
public class Service_layer {
    private static final Logger logger = LoggerFactory.getLogger(Service_layer.class);
    private Market market;

    public Service_layer() {
        this.market = Market.getInstance(); // Initialize the Market instance
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder.build();
    }







    public Service_layer(int i) {
        market = Market.getInstance();
        Market market1 = market.newForTests();
        market=market1;
        // Initialize the Market instance
    }


    public Response<String> init(UserDTO userDTO ,String password , PaymentServiceDTO paymentDTO, SupplyServiceDTO supplyServiceDTO){
        logger.info("Starting the initialization of the system.");
        try {
            String userId = market.init(userDTO,password, paymentDTO, supplyServiceDTO );
            logger.info("System initialized successfully.");
            return new Response<>("Initialization successful", "System initialized successfully.",userId);

        } catch (Exception e) {
            logger.error("Error occurred during the initialization: {}", e.getMessage(), e);
            return new Response<>(null, e.getMessage());
        }
    }

    public Response<String> addExternalPaymentService(PaymentServiceDTO paymentServiceDTO , String managerId) throws Exception {
        logger.info("Trying to add a new external payment service");
        try {
            market.addExternalPaymentService(paymentServiceDTO ,managerId );
            logger.info("Adding new external payment service have been done successfully.");
            return new Response<>("Successful adding", "Adding new external payment service have been done successfully.");

        } catch (Exception e) {
            logger.error("Error occurred during the adding: {}", e.getMessage(), e);
            return new Response<>(null, e.getMessage());
        }
    }

    public Response<String> removeExternalPaymentService(String  licensedDealerNumber, String systemMangerId){
        logger.info("Trying to remove the payment service number: {}",licensedDealerNumber );
        try {
            market.removeExternalPaymentService(licensedDealerNumber,systemMangerId);
            logger.info("Removing the external payment service number: {} have been done successfully.", licensedDealerNumber);
            return new Response<>("Successful adding", "Adding new external payment service have been done successfully.");
        }
        catch (Exception e) {
            logger.error("Error occurred during the removing: {}", e.getMessage(), e);
            return new Response<>(null, e.getMessage());
        }

    }

    public Response<String> addExternalSupplyService(SupplyServiceDTO supplyServiceDTO, String systemManagerId) throws Exception {
        logger.info("Trying to add a new external supply service");
        try {
            market.addExternalSupplyService(supplyServiceDTO, systemManagerId);
            logger.info("Adding new external supply service has been done successfully.");
            return new Response<>("Successful adding", "Adding new external supply service has been done successfully.");
        } catch (Exception e) {
            logger.error("Error occurred during the adding: {}", e.getMessage(), e);
            return new Response<>(null, e.getMessage());
        }
    }

    public Response<String> removeExternalSupplyService(String licensedDealerNumber, String systemManagerId) {
        logger.info("Trying to remove the supply service number: {}", licensedDealerNumber);
        try {
            market.removeExternalSupplyService(licensedDealerNumber, systemManagerId);
            logger.info("Removing the external supply service number: {} has been done successfully.", licensedDealerNumber);
            return new Response<>("Successful removal", "Removing external supply service has been done successfully.");
        } catch (Exception e) {
            logger.error("Error occurred during the removing: {}", e.getMessage(), e);
            return new Response<>(null, e.getMessage());
        }
    }

    public Response<String> purchase(UserDTO userDTO, PaymentDTO paymentDTO){
        String user_ID = userDTO.getUserId();
        logger.info("Initiating purchase for user: {}", user_ID);
        try {
            market.purchase( paymentDTO,userDTO);
            logger.info("Purchase successful for user: {}", user_ID);
            return new Response<>("Purchase successful", "");
        } catch (Exception e) {
            logger.error("Purchase failed for user: {} with error: {}", user_ID, e.getMessage(), e);
            return new Response<>(null, e.getMessage());
        }
    }




    public Response<String> exitMarketSystem(String userID) {
        logger.info("Exiting market system");
        try {
            market.exitMarketSystem(userID);
            return new Response<>("Exit successful", "User exited the market system successfully.");
        } catch (Exception e) {
            logger.error("Error occurred during exiting market system {}", e.getMessage());
            return new Response<>(null, e.getMessage());
        }
    }


    public Response<String> enterMarketSystem() {
        logger.info("Entering market system");

        try {
            String userID = market.enterMarketSystem();
            return new Response<>("Enter successful", "Entered the market system successfully.", userID);
        } catch (Exception e) {
            logger.error("Error occurred during entering market system {}", e.getMessage());
            return new Response<>(null, e.getMessage() , "");

        }
    }

    public Response<String> register(UserDTO userDTO, String password) {
        logger.info("Registration");
        try {
            String memberID = market.register(userDTO.getUserId(), userDTO, password);
            return new Response<>("Registration successful", "User registered successfully.", memberID);
        } catch (Exception e) {
            logger.error("Error occurred during registration {}", e.getMessage());
            return new Response<>(null, e.getMessage());
        }
    }

    public Response<String> login(String userID, String username, String password) {
        logger.info("Log in");

        try {
            String memberId = market.Login(userID, username, password);
            return new Response<>("Login successful", "User logged in successfully.", memberId);
        } catch (Exception e) {
            logger.error("Error occurred during log in - {}", e.getMessage());
            return new Response<>(null, e.getMessage(), e.getMessage());
        }
    }

    public Response<String> logout(String userID) {
        logger.info("Log out");
        try {
            market.logout(userID);
            return new Response<>("Logout successful", "User logged out successfully.");
        } catch (Exception e) {
            logger.error("Error occurred during log out", e.getMessage());
            return new Response<>(null, e.getMessage());
        }
    }

    public Response<String> addProductToStore(String userId, String storeID,ProductDTO productDTO) {
        logger.info("Adding product to store");

        try {
            market.addProductToStore(userId, storeID,productDTO);
            return new Response<>("Product added successfully", "Product added to store successfully.");
        } catch (Exception e) {
            logger.error("Error occurred during adding product to store", e.getMessage(), e);
            return new Response<>(null, e.getMessage());
        }
    }


    public Response<String> removeProductFromStore(String userId, String storeID, String productName) {
        logger.info("Removing product from store");

        try {
            market.removeProductFromStore(userId, storeID, productName);
            return new Response<>("Product removed successfully", "Product removed from store successfully.");
        } catch (Exception e) {

            logger.error("Error occurred during removing product from store", e.getMessage(), e);
            return new Response<>(null, e.getMessage());
        }
    }

    public Response<String> updateProductInStore(String userId, String storeID,ProductDTO productDTO) {

        logger.info("Updating product in store");
        try {
            market.updateProductInStore(userId, storeID, productDTO);
            return new Response<>("Product updated successfully", "Product updated in store successfully.");
        } catch (Exception e) {

            logger.error("Error occurred during updating product in store", e.getMessage(), e);;
            return new Response<>(null, e.getMessage());
        }
    }

    public Response<String> appointStoreOwner(String nominatorUserId, String nominatedUsername, String storeID) {
        logger.info("AppoString store owner");

        try {
            market.appointStoreOwner(nominatorUserId, nominatedUsername, storeID);
            return new Response<>("Store owner appointed successfully", "Store owner appointed successfully.");
        } catch (Exception e) {

            logger.error("Error occurred during appointing store owner", e.getMessage(), e);
            return new Response<>(null, e.getMessage());

        }
    }

    public Response<String> appointStoreManager(String nominatorUserId ,String nominatedUsername, String storeID,
                                    boolean inventoryPermissions, boolean purchasePermissions) {

        logger.info("AppoString store manager");

        try {
            market.appointStoreManager(nominatorUserId, nominatedUsername, storeID,
                    inventoryPermissions, purchasePermissions);
            return new Response<>("Store manager appointed successfully", "Store manager appointed successfully.");
        } catch (Exception e) {

            logger.error("Error occurred during appointing store manager", e.getMessage(), e);
            return new Response<>(null, e.getMessage());
        }
    }

    public Response<String> updateStoreManagerPermissions(String nominatorUserId ,String nominatedUsername, String storeID,
                                              boolean inventoryPermissions, boolean purchasePermissions) {

        logger.info("Updating store manager permissions");

        try {
            market.updateStoreManagerPermissions(nominatorUserId ,nominatedUsername, storeID,
                    inventoryPermissions, purchasePermissions);
            return new Response<>("Permissions updated successfully", "Store manager permissions updated successfully.");
        } catch (Exception e) {

            logger.error("Error occurred during updating store manager permissions", e.getMessage(), e);
            return new Response<>(null, e.getMessage());

        }
    }

    public Response<List<String>> generalProductFilter(String userId, String categoryStr, List<String> keywords, Integer minPrice, Integer maxPrice, Double productMinRating, List<String> productsFromSearch, Double storeMinRating)
    {
        logger.info("Starting general product search filter in the system.");

        try {
            List<String> filteredProductNames = market.generalProductFilter(userId, categoryStr, keywords, minPrice, maxPrice, productMinRating, productsFromSearch, storeMinRating);
            return new Response<>(filteredProductNames, "Product filter applied successfully.");
        } catch (Exception e) {
            logger.error("Error occurred during the general product search filter: {}", e.getMessage(), e);
            return new Response<>(null, e.getMessage());
        }
    }


    public Response<List<ProductDTO>> getProductStores(String userId)
    {

        try {
            List<ProductDTO> products = market.getStoreProducts(userId);
            return new Response<>(products, "product getter good result.");
        } catch (Exception e) {
            logger.error("Error occurred during the product getter {}", e.getMessage(), e);
            return new Response<>(null, e.getMessage());
        }
    }


    public Response<List<UserDTO>> getStoreWorkers(String storeId)
    {
        try {
            List<UserDTO> workers = market.getStoreWorkers(storeId);
            return new Response<>(workers, "Store Workers get good result.");
        } catch (Exception e) {
            logger.error("Error occurred during the product getter {}", e.getMessage(), e);
            return new Response<>(null, e.getMessage());
        }
    }


    public Response<List<UserDTO>> getStoreManagers(String storeId)
    {
        try {
            List<UserDTO> workers = market.getStoreManagers(storeId);
            return new Response<>(workers, "Store managers get good result.");
        } catch (Exception e) {
            logger.error("Error occurred during the managers getter {}", e.getMessage(), e);
            return new Response<>(null, e.getMessage());
        }
    }

    public Response<List<UserDTO>> getStoreOwners(String storeId)
    {
        try {
            List<UserDTO> workers = market.getStoreOwners(storeId);
            return new Response<>(workers, "Store owners get good result.");
        } catch (Exception e) {
            logger.error("Error occurred during the managers getter {}", e.getMessage(), e);
            return new Response<>(null, e.getMessage());
        }
    }



    public Response<List<ProductDTO>> generalProductSearchDTO(String userId, String productName, String categoryStr, List<String> keywords)
    {
        logger.info("Starting general product search in the system.");

        try {
            List<ProductDTO> filteredProductNames = market.generalProductSearchDTO(userId, productName, categoryStr, keywords);
            return new Response<>(filteredProductNames, "Product search completed successfully.");
        } catch (Exception e) {
            logger.error("Error occurred during the general product search: {}", e.getMessage(), e);
            return new Response<>(null, e.getMessage());
        }
    }

    public Response<List<String>> generalProductSearch(String userId, String productName, String categoryStr, List<String> keywords)
    {
        logger.info("Starting general product search in the system.");

        try {
            List<String> filteredProductNames = market.generalProductSearch(userId, productName, categoryStr, keywords);
            return new Response<>(filteredProductNames, "Product search completed successfully.");
        } catch (Exception e) {
            logger.error("Error occurred during the general product search: {}", e.getMessage(), e);
            return new Response<>(null, e.getMessage());
        }
    }



    public Response<List<String>> getInformationAboutStores(String user_ID)
    {
        logger.info("Starting reviewing information about stores in the market.");

        try {
            List<String> allAvailableStores = market.getInformationAboutStores(user_ID);
            return new Response<>(allAvailableStores, "Information about stores retrieved successfully.");
        } catch (Exception e) {
            logger.error("Error occurred during reviewing information about stores in the market: {}", e.getMessage(), e);
            return new Response<>(null, e.getMessage());
        }

    }

    public Response<Map<String, String>> getInformationAboutRolesInStore(String user_ID, String store_ID)
    {
        logger.info("Store owner started  reviewing information about employees in.");

        try {
            Map<String, String> information = market.getInformationAboutRolesInStore(user_ID, store_ID);
            return new Response<>(information, "Information about roles in store retrieved successfully.");
        } catch (Exception e) {
            logger.error("Error occurred during reviewing information about products in store: {}", e.getMessage(), e);
            return new Response<>(null, e.getMessage());
        }
    }

    public Response<Map<String, List<Integer>>> getAuthorizationsOfManagersInStore(String user_ID, String store_ID)
    {
        logger.info("Store owner started  reviewing authorizations of managers in store.");

        try {
            Map<String, List<Integer>> managersAuthorizations = market.getAuthorizationsOfManagersInStore(user_ID, store_ID);
            return new Response<>(managersAuthorizations, "Authorizations of managers in store retrieved successfully.");
        } catch (Exception e) {
            logger.error("Error occurred during store owner reviewing authorizations of managers in store: {}", e.getMessage(), e);
            return new Response<>(null, e.getMessage());
        }
    }

    public Response<String> closeStore(String user_ID, String store_ID)
    {
        logger.info("Store owner started  store closing.");

        try {
            market.closeStore(user_ID, store_ID);
            return new Response<>("Store closed successfully", "Store closed successfully.");
        } catch (Exception e) {
            logger.error("Error occurred during store owner was trying to close a store: {}", e.getMessage(), e);
            return new Response<>(null, e.getMessage());
        }
    }

    public Response<String> openStore(String user_ID, String name, String description)
    {
        logger.info("Store owner stared store opening.");

        try {
            String storeId = market.openStore(user_ID, name, description);
            return new Response<>("Store opened successfully", "Store opened successfully.", storeId);
        } catch (Exception e) {
            logger.error("Error occurred during store owner was trying to open a store: {}", e.getMessage(), e);
            return new Response<>(null, e.getMessage());
        }
    }

    public Response<String> addProductToBasket(String productName, int quantity, String storeId, String userId)
    {
        logger.info("User try to add a new product to his basket");

        try {
            market.addProductToBasket(productName, quantity, storeId, userId);
            return new Response<>("Product added to basket successfully", "Product added to basket successfully.");
        } catch (Exception e) {
            logger.error("Error occurred during adding new product to the basket: {}", e.getMessage(), e);
            return new Response<>(null, e.getMessage());
        }
    }

    public Response<String> removeProductFromBasket(String productName, String storeId, String userId)
    {
        logger.info("User try to remove a product from his basket");

        try {
            market.removeProductFromBasket(productName, storeId, userId);
            return new Response<>("Product removed from basket successfully", "Product removed from basket successfully.");
        } catch (Exception e) {
            logger.error("Error occurred during removing a product from the basket: {}", e.getMessage(), e);
            return new Response<>(null, e.getMessage());
        }
    }

    public Response<String> modifyShoppingCart(String productName, int quantity, String storeId, String userId)
    {
        logger.info("User try to modify his shopping cart");

        try {
            market.modifyShoppingCart(productName, quantity, storeId, userId);
            return new Response<>("Shopping cart modified successfully", "Shopping cart modified successfully.");
        } catch (Exception e) {
            logger.error("Error occurred during modifying shopping cart: {}", e.getMessage(), e);
            return new Response<>(null, e.getMessage());
        }
    }

    public Response<Map<String, Integer>> marketManagerAskInfo(String user_ID)
    {
        logger.info("Market manager tries to get info about purchases in the market");

        try {
            Map<String, Integer> marketPurchases = market.marketManagerAskInfo(user_ID);
            return new Response<>(marketPurchases, "Information about purchases in the market retrieved successfully.");
        } catch (Exception e) {
            logger.error("Error occurred during the request of the market manager getting the purchase information: {}", e.getMessage(), e);
            return new Response<>(null, e.getMessage());
        }
    }

    public Response<Map<String, Integer>> storeOwnerGetInfoAboutStore(String user_ID, String store_ID) //return receiptId and total amount in the receipt for the specific store
    {
        logger.info("Store owner tries to get info about purchases in the store");

        try {
            Map<String, Integer> storePurchases = market.storeOwnerGetInfoAboutStore(user_ID, store_ID);
            return new Response<>(storePurchases, "Information about purchases in the store retrieved successfully.");
        } catch (Exception e) {
            logger.error("Error occurred during the request of the store owner getting the purchase information: {}", e.getMessage(), e);
            return new Response<>(null, e.getMessage());
        }
    }

    public Response<List<String>> inStoreProductFilter(String userId ,String categoryStr, List<String> keywords, Integer minPrice, Integer maxPrice, Double productMinRating, String storeId, List<String> productsFromSearch, Double storeMinRating)
    {
        logger.info("Starting in-store product search filter in the system.");

        try {
            List<String> filteredProductNames = market.inStoreProductFilter(userId, categoryStr, keywords, minPrice, maxPrice, productMinRating, storeId, productsFromSearch, storeMinRating);
            return new Response<>(filteredProductNames, "In-store product search filter applied successfully.");
        } catch (Exception e) {
            logger.error("Error occurred during the in-store product search filter: {}", e.getMessage(), e);
            return new Response<>(null, e.getMessage());
        }
    }

    public Response<List<String>> inStoreProductSearch(String userId, String productName, String categoryStr, List<String> keywords, String storeId)
    {
        logger.info("Starting in-store product search in the system.");

        try {
            List<String> filteredProductNames = market.inStoreProductSearch(userId, productName, categoryStr, keywords, storeId);
            return new Response<>(filteredProductNames, "In-store product search completed successfully.");
        } catch (Exception e) {
            logger.error("Error occurred during the in-store product search: {}", e.getMessage(), e);
            return new Response<>(null, e.getMessage());
        }
    }


  public Response<List<ProductDTO>> inStoreProductSearchDTO(String userId, String productName, String categoryStr, List<String> keywords, String storeId)
    {
        logger.info("Starting in-store product search in the system.");

        try {
            List<ProductDTO> filteredProductNames = market.inStoreProductSearchDTO(userId, productName, categoryStr, keywords, storeId);
            return new Response<>(filteredProductNames, "In-store product search completed successfully.");
        } catch (Exception e) {
            logger.error("Error occurred during the in-store product search: {}", e.getMessage(), e);
            return new Response<>(null, e.getMessage());
        }
    }

    public Response<String> addRuleToStore(List<Integer> ruleNums, List<String> operators, String storeId, String userId) {
        logger.info("Adding rule to store");
        return new Response<>("Eule added successfully","Rule added to store successfully.");
    }

    public Response<String> addPurchaseRuleToStore(List<Integer> ruleNums, List<String> operators, String storeId, String userId) {
        logger.info("Adding purchase rule to store");


        try {
            market.addPurchaseRuleToStore(ruleNums, operators, storeId, userId);
            return new Response<>("Purchase rule added successfully", "Purchase rule added to store successfully.");
        } catch (Exception e) {
            logger.error("Error occurred during adding purchase rule to store: {}", e.getMessage(), e);
            return new Response<>(null, e.getMessage());
        }
    }

    //rule num is the index of the rule from all the rules displayed to the storeowner
    public Response<String> removePurchaseRuleFromStore(int ruleNum, String storeId, String userId) {
        logger.info("Removing purchase rule from store");

        try {
            market.removePurchaseRuleFromStore(ruleNum, storeId, userId);
            return new Response<>("Removing purchase rule removed successfully", "Removing purchase rule removed from store successfully.");
        } catch (Exception e) {
            logger.error("Error occurred during removing purchase rule from store: {}", e.getMessage(), e);
            return new Response<>(null, e.getMessage());
        }
    }

    public Response<String> addDiscountCondRuleToStore(List<Integer> ruleNums, List<String> logicoOperators, List<DiscountValueDTO> discDetails, List<String> numericalOperators,String storeId, String userId) {
        logger.info("Adding conditional discount rule to store");

        try {
            market.addDiscountCondRuleToStore(ruleNums, logicoOperators, discDetails, numericalOperators, storeId, userId);
            return new Response<>("Discount conditional rule added successfully", "Discount conditional rule added to store successfully.");
        } catch (Exception e) {
            logger.error("Error occurred during adding conditional discount rule to store: {}", e.getMessage(), e);
            return new Response<>(null, e.getMessage());
        }
    }

    public Response<String> addDiscountSimpleRuleToStore(List<DiscountValueDTO> discs, List<String> numericalOperators, String storeId, String userId) {
        logger.info("Adding simple discount rule to store");

        try {
            market.addDiscountSimpleRuleToStore(discs, numericalOperators, storeId, userId);
            return new Response<>("Discount simple rule added successfully", "Discount simple rule added to store successfully.");
        } catch (Exception e) {
            logger.error("Error occurred during adding simple discount rule to store: {}", e.getMessage(), e);
            return new Response<>(null, e.getMessage());
        }
    }

    //rule num is the index of the rule from all the rules displayed to the storeowner
    public Response<String> removeDiscountRuleFromStore(int ruleNum, String storeId, String userId) {
        logger.info("Removing discount rule from store");

        try {
            market.removeDiscountRuleFromStore(ruleNum, storeId, userId);
            return new Response<>("Discount rule removed successfully", "Discount rule removed from store successfully.");
        } catch (Exception e) {
            logger.error("Error occurred during removing discount rule from store: {}", e.getMessage(), e);
            return new Response<>(null, e.getMessage());
        }
    }


    //    public Response<String> payWithExternalPaymentService(int price, String holderId, String creditCardNumber, int cvv, int month, int year, String userID) {
//        logger.info("Reaching for the payment service in order to complete the purchase.");
//        try {
//            market.payWithExternalPaymentService(price, new PaymentDTO(holderId, creditCardNumber, cvv, month, year),  userID, market.getPurchaseList(userID) );
//            return new Response<>("Successful payment", "payment went successfully.");
//
//        } catch (Exception e) {
//            try {
//                market.paymentFailed(userID);
//            }
//            catch (Exception exception){
//                logger.error("Error occurred while restore stock data: {}", e.getMessage(), e);
//                return new Response<>(null, "restore stock data failed: " + e.getMessage());
//            }
//            logger.error("Error occurred while paying: {}", e.getMessage(), e);
//            return new Response<>(null, "Payment failed: " + e.getMessage());
//        }
//    }



//    public Response<Integer> checkingCartValidationBeforePurchase(String user_ID, String country, String city, String address)
//    {
//        logger.info("Starting care validation and price calculation before purchase.");
//
//        try {
//            int totalPrice = market.checkingCartValidationBeforePurchase(user_ID, country,city,address);
//            return new Response<>(totalPrice, "Cart validation and price calculation completed successfully.");
//        } catch (Exception e) {
//            logger.error("Error occurred during the validation of the cart: {}", e.getMessage(), e);
//            return new Response<>(null, "Cart validation failed: " + e.getMessage());
//        }
//    }

}