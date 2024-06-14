package DomainLayer.Store;

import DomainLayer.Store.StoreDiscountPolicy.DiscountRulesRepository;
import Util.*;
import DomainLayer.Store.PoliciesRulesLogicalConditions.Rule;
import DomainLayer.Store.PoliciesRulesLogicalConditions.SimpleRule;
import DomainLayer.Store.StorePurchasePolicy.PurchaseRulesRepository;

import java.util.*;

public class StoreFacade {
    private static StoreFacade storeFacadeInstance;

    private StoreRepository allStores ;



    //private int currentStoreID;
    //private Object allStoresLock;
    //private Object storeIdLock;
    //Map<Integer, Store> allStores = new HashMap<Integer, Store>();


    private StoreFacade()
    {

        allStores = new StoreMemoryRepository();

        //allStoresLock = new Object();
        //storeIdLock = new Object();
        //this.currentStoreID = 0;

    }

    public synchronized static StoreFacade getInstance() {
        if (storeFacadeInstance == null) {
            storeFacadeInstance = new StoreFacade();
        }
        return storeFacadeInstance;
    }



    public StoreFacade newForTest(){
        storeFacadeInstance= new StoreFacade();
        return storeFacadeInstance;
    }

    public void returnProductToStore(Map<String, List<Integer>> products , String storeId){
        getStoreByID(storeId).returnProductToStore(products);
    }

    public Store getStoreByID(String storeID){
        return allStores.get(storeID);
    }

    public StoreDTO getStoreDTOFromStore(Store store){
        return new StoreDTO(store.getProducts(), store.getStoreID(), store.getIsOpened(),store.getRating(), store.getNumOfRatings(),store.getStoreName() , store.getDescription());
    }

    public List<StoreDTO> getAllDTOs(){
        List<StoreDTO> storesDTOList= new ArrayList<>();
        List<Store> allStoresList = allStores.getAll();
        for (Store store : allStoresList){
            storesDTOList.add(getStoreDTOFromStore(store));
        }
        return storesDTOList;
    }


    public List<ProductDTO> getStoreProductsDTO(String storeId){
        Store store = allStores.get(storeId);
        return  store.getProductsDTO();
    }

    public StoreDTO getStoreDTOById(String storeId){
        Store store = getStoreByID(storeId);
        return getStoreDTOFromStore(store);
    }

    public void errorIfStoreNotExist(String storeID) throws Exception {
        if(getStoreByID(storeID) == null){
            throw new Exception(ExceptionsEnum.storeNotExist.toString());
        }
    }

    public String getNewStoreId(){
        UUID uuid = UUID.randomUUID();
        String storeId = "store" + uuid.toString();
        return storeId;
    }

    public String openStore(String name, String description)
    {
        String storeId = getNewStoreId();
        Store newStore = new Store(storeId, name, description); //todo: add this to list in repository
        this.allStores.add(storeId, newStore);
        return newStore.getStoreID();
    }


    public void checkQuantity(String productName, int quantity, String storeId)
    {
        this.checkIfProductExists(productName, storeId);
        this.checkProductQuantityAvailability(productName, storeId, quantity);
        this.checkIfProductQuantityIsPositive(quantity);
    }

    public boolean checkPolicies(UserDTO userDTO, List<ProductDTO> products, String storeId) {
        //Check here all policies
        this.checkPurchasePolicy(userDTO, products, storeId);
        //this.checkDiscountPolicy(productName, storeId, userId);
        return true;
    }

    public void checkIfProductExists(String productName, String storeId){
        Store store = getStoreByID(storeId);
        if (!store.checkProductExists(productName))
        {
            throw new IllegalArgumentException(ExceptionsEnum.productNotExistInStore.toString());
        }
    }

    public List<ProductDTO> getProductsDTOSByProductsNames(Map<String, List<Integer>> products, String storeId)
    {
        List<ProductDTO> productDTOS = new ArrayList<>();
        Store store = getStoreByID(storeId);
        for (Map.Entry<String, List<Integer>> product : products.entrySet()) {
            String productName = product.getKey();
            int quantity = product.getValue().get(0);
            productDTOS.add(store.getProductDTOByName(productName, quantity));
        }
        return productDTOS;
    }

    public void checkProductQuantityAvailability(String productName, String storeId, int quantity)
    {
        Store store = getStoreByID(storeId);
        if (!store.checkProductQuantity(productName, quantity))
        {
            throw new IllegalArgumentException(ExceptionsEnum.productQuantityNotExist.toString());
        }
    }

    public void checkIfProductQuantityIsPositive(int quantity)
    {
        if (quantity < 0)
        {
            throw new IllegalArgumentException(ExceptionsEnum.productQuantityIsNegative.toString());
        }
    }

    public void checkPurchasePolicy(UserDTO userDTO, List<ProductDTO> products, String storeId)
    {
        Store store = getStoreByID(storeId);

        if (!store.checkPurchasePolicy(userDTO, products))
        {
            throw new IllegalArgumentException(ExceptionsEnum.purchasePolicyIsNotMet.toString());
        }
    }

    public void checkDiscountPolicy(String productName, String storeId, String userId)
    {
        Store store = getStoreByID(storeId);

        if (!store.checkDiscountPolicy(userId, productName))
        {
            throw new IllegalArgumentException(ExceptionsEnum.discountPolicyIsNotMet.toString());
        }
    }

    public int calcPrice(String productName, int quantity, String storeId, String userId)
    {
        Store store = getStoreByID(storeId);
        return store.calcPriceInStore(productName, quantity, userId);
    }


    public void addProductToStore(String storeId, ProductDTO product) throws Exception {

        if (!checkProductExistInStore(product.getName(), storeId)) {
            if (product.getQuantity() >= 0) {
                allStores.get(storeId).addProduct(product);
            } else {
                throw new Exception(ExceptionsEnum.productQuantityIsNegative.toString());
            }
        } else {
            throw new Exception(ExceptionsEnum.productAlreadyExistInStore.toString());
        }
    }

    public void removeProductFromStore(String storeId, String productName) throws Exception {
        if (checkProductExistInStore(productName, storeId)) {
            allStores.get(storeId).removeProduct(productName);
        } else {
            throw new Exception(ExceptionsEnum.productNotExistInStore.toString());
        }

    }

    public void updateProductInStore(String storeId, ProductDTO product) throws Exception {

        if (checkProductExistInStore(product.getName(), storeId)) {
            if (product.getQuantity() >= 0) {
                allStores.get(storeId).updateProduct(product);
            } else {
                throw new Exception(ExceptionsEnum.productQuantityIsNegative.toString());
            }
        } else {
            throw new Exception(ExceptionsEnum.productNotExistInStore.toString());
        }
    }

    public boolean verifyStoreExist(String storeID)
    {
        return getStoreByID(storeID) != null;
    }

    public void verifyStoreExistError(String storeID) throws Exception {
        if(!verifyStoreExist(storeID))
            throw new Exception(ExceptionsEnum.storeNotExist.toString());
    }

    public void closeStore(String store_ID) throws Exception
    {
        Store storeToClose = this.getStoreByID(store_ID);
        if (!storeToClose.getIsOpened()){
            throw new Exception(ExceptionsEnum.storeNotExist.toString());
        }
        storeToClose.closeStore();
    }

    public List<String> getInformationAboutOpenStores()
    {
        List<String> openStoreInformation = new ArrayList<>();
            for (Store store : allStores.getAll()) {
                //int storeId = entry.getKey();
                //Store store = entry.getValue();
                if (store.getIsOpened())
                    openStoreInformation.add(store.getStoreID());
            }
        return openStoreInformation;
    }

    public List<String> getInformationAboutClosedStores() {
        List<String> closedStoreInformation = new ArrayList<>();
        for (Store store : allStores.getAll()) {
            //int storeId = entry.getKey();
            //Store store = entry.getValue();
            if (!store.getIsOpened()) {
                closedStoreInformation.add(store.getStoreID());
            }

        }
        return closedStoreInformation;
    }

    public List<String> getStoreProducts(String store_ID)
    {
        Store store = getStoreByID(store_ID);
        return store.getProducts();
    }

    public int calculateTotalCartPriceAfterDiscount(String store_ID, Map<String, List<Integer>> products, int totalPriceBeforeDiscount) {
        return totalPriceBeforeDiscount; //In the future - check discount and calculate price by policies
    }
    public List<String> inStoreProductSearch(String productName, String categoryStr, List<String> keywords, String storeId)
    {
        Store storeToSearchIn = getStoreByID(storeId);
        List<String> filteredProducts = storeToSearchIn.matchProducts(productName, categoryStr, keywords);
        return filteredProducts;
    }

    public List<ProductDTO> inStoreProductSearchDTO(String productName, String categoryStr, List<String> keywords, String storeId)
    {
        Store storeToSearchIn = getStoreByID(storeId);
        List<ProductDTO> filteredProducts = storeToSearchIn.matchProductsDTO(productName, categoryStr, keywords);
        return filteredProducts;
    }


    public List<String> inStoreProductFilter(String categoryStr, List<String> keywords, Integer minPrice, Integer maxPrice, Double minRating, String storeId, List<String> productsFromSearch, Double storeMinRating)
    {
        Store storeToSearchIn = getStoreByID(storeId);
        List<String> filteredProducts = storeToSearchIn.filterProducts(categoryStr, keywords, minPrice, maxPrice, minRating, productsFromSearch, storeMinRating);
        return filteredProducts;
    }

    public void checkCategory(String categoryStr)
    {
        if (categoryStr != null)
        {
            if (Category.fromString(categoryStr) == null)
            {
                throw new IllegalArgumentException(ExceptionsEnum.categoryNotExist.toString());
            }
        }
    }

    public boolean checkProductExistInStore(String productName, String storeId)
    {
        Store store = getStoreByID(storeId);
        return store.checkProductExists(productName);
    }

    public List<String> getStores() {

            return allStores.getAllIds();
        }

    public void addReceiptToStore(String storeId, String  receiptId, String userId)
    {
        allStores.get(storeId).addReceipt(receiptId, userId);
    }

    public void addPurchaseRuleToStore(List<Integer> ruleNums, List<String> operators, String storeId) {
        List<Rule<UserDTO, List<ProductDTO>>> rules = new ArrayList<>();
        for (int ruleNum : ruleNums) {
            rules.add(new SimpleRule<>(PurchaseRulesRepository.getByRuleNumber(ruleNum)));
        }
        allStores.get(storeId).addPurchaseRule(rules, operators);
    }

    //implement removeRuleFromStore
    public void removePurchaseRuleFromStore(int ruleNum, String storeId) {
        allStores.get(storeId).removePurchaseRule(ruleNum);
    }

    public void addDiscountCondRuleToStore(List<Integer> ruleNums, List<String> operators, List<DiscountValueDTO> discDetails, List<String> numericalOperators, String storeId) {
        List<Rule<UserDTO, List<ProductDTO>>> rules = new ArrayList<>();
        for (int ruleNum : ruleNums) {
            rules.add(new SimpleRule<>(DiscountRulesRepository.getByRuleNumber(ruleNum)));
        }
        allStores.get(storeId).addDiscountCondRule(rules, operators, discDetails, numericalOperators);
    }

    public void addDiscountSimpleRuleToStore(List<DiscountValueDTO> discDetails, List<String> numericalOperators, String storeId) {
        allStores.get(storeId).addDiscountSimple(discDetails, numericalOperators);
    }

    public void removeDiscountRuleFromStore(int ruleNum, String storeId) {
        allStores.get(storeId).removeDiscountRule(ruleNum);
    }
}
