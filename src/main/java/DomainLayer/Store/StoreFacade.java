package DomainLayer.Store;

import DomainLayer.Repositories.StoreMemoryRepository;
import DomainLayer.Repositories.StoreRepository;
import DomainLayer.Store.StoreDiscountPolicy.DiscountValue;
import DomainLayer.Store.StoreDiscountPolicy.SimpleDiscountValue;
import Util.*;
import DomainLayer.Store.PoliciesRulesLogicalConditions.Rule;
import DomainLayer.Store.PoliciesRulesLogicalConditions.SimpleRule;
//import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class StoreFacade {
    private static StoreFacade storeFacadeInstance;

    private StoreRepository allStores ;


    public StoreFacade()
    {
        allStores = new StoreMemoryRepository();
    }

    public synchronized static StoreFacade getInstance() {
        if (storeFacadeInstance == null) {
            storeFacadeInstance = new StoreFacade();
        }
        return storeFacadeInstance;
    }


    // Added for testing purposes
    @Autowired
    public StoreFacade(StoreRepository storeRepository){// throws Exception {
        this.allStores = storeRepository;
        //setAllStores();
    }

    /*
    // Added for testing purposes
    @Transactional
    public void setAllStores() throws Exception {
        //storeRepository.deleteAll();
        allStores.save(new Store("store22222", "store1", "store1"));
        Store store = getStoreByID("store22222");
        addProductToStore(store.getStoreID(),new ProductDTO("COCA", 10, 100, "COCA", "FOOD"));
    }
*/

    public StoreFacade newForTest(){
        storeFacadeInstance= new StoreFacade();
        return storeFacadeInstance;
    }


    public boolean isStoreOpen(String storeId) throws Exception {
        if (getStoreByID(storeId)==null){
            return false;
        }
        return getStoreByID(storeId).getIsOpened();
    }


    public String getStoreName(String storeId){
        return allStores.getById(storeId).getStoreName();
    }

    public String getStoreId(String storeName){
        List<Store> stores = allStores.findAll();
        for (Store store : stores) {
            if(store.getStoreName().equals(storeName)){
                return store.getStore_ID();
            }
        }
        return "not found";
    }


    public void returnProductToStore(Map<String, List<Integer>> products , String storeId) throws Exception {
        getStoreByID(storeId).returnProductToStore(products);
    }

    public Store getStoreByID(String storeID) throws Exception {
        try {
            return allStores.getById(storeID);
        } catch (Exception e) {
            throw new Exception(ExceptionsEnum.DatabaseIsNotConnected.toString());
        }

        //return allStores.getById(storeID);
        //Optional<Store> store = allStores.findById(storeID);
        //return store.orElse(null);
    }

    public StoreDTO getStoreDTOFromStore(Store store){
        return new StoreDTO(store.getProducts(), store.getStoreID(), store.getIsOpened(),store.getRating(), store.getNumOfRatings(),store.getStoreName() , store.getDescription());
    }

    public List<StoreDTO> getAllDTOs(){
        List<StoreDTO> storesDTOList= new ArrayList<>();
        List<Store> allStoresList = allStores.findAll();
        for (Store store : allStoresList){
            storesDTOList.add(getStoreDTOFromStore(store));
        }
        return storesDTOList;
    }


    public List<ProductDTO> getStoreProductsDTO(String storeId){
        //Store store = getStoreByID(storeId);
        Store store = allStores.getById(storeId);
        return  store.getProductsDTO();
    }

    public StoreDTO getStoreDTOById(String storeId) throws Exception {
        Store store = getStoreByID(storeId);
        return getStoreDTOFromStore(store);
    }

    public void errorIfStoreNotExist(String storeID) throws Exception {
        if(getStoreByID(storeID) == null){
            throw new Exception(ExceptionsEnum.storeNotExist.toString());
        }
    }

    public List<String> getStoreCategories(){
        List<String> categories = new ArrayList<>();
        for (Category cat : EnumSet.allOf(Category.class)){
            categories.add(cat.toString());
        }
        return categories;
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
        this.allStores.save(newStore);
        return newStore.getStoreID();
    }


    public void checkQuantityAndPrice(String productName, int quantity, String storeId) throws Exception {
        this.checkIfProductExists(productName, storeId);
        this.checkProductQuantityAvailability(productName, storeId, quantity);
        this.checkIfProductQuantityIsPositive(quantity);
        Store store = getStoreByID(storeId);
        checkProductPrice(store.getProductDTOByName(productName, quantity));
    }

    public void checkIfProductExists(String productName, String storeId) throws Exception {
        Store store = getStoreByID(storeId);
        if (!store.checkProductExists(productName))
        {
            throw new IllegalArgumentException(ExceptionsEnum.productNotExistInStore.toString());
        }
    }

    public List<ProductDTO> getProductsDTOSByProductsNames(Map<String, List<Integer>> products, String storeId) throws Exception {
        List<ProductDTO> productDTOS = new ArrayList<>();
        Store store = getStoreByID(storeId);
        for (Map.Entry<String, List<Integer>> product : products.entrySet()) {
            String productName = product.getKey();
            int quantity = product.getValue().get(0);
            productDTOS.add(store.getProductDTOByName(productName, quantity));
        }
        return productDTOS;
    }

    public void checkProductQuantityAvailability(String productName, String storeId, int quantity) throws Exception {
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

    public void checkPurchasePolicy(UserDTO userDTO, List<ProductDTO> products, String storeId) throws Exception {
        Store store = getStoreByID(storeId);

        if (!store.checkPurchasePolicy(userDTO, products))
        {
            throw new IllegalArgumentException(ExceptionsEnum.purchasePolicyIsNotMet.toString());
        }
    }

    public int calcDiscountPolicy(UserDTO userDTO, List<ProductDTO> products, String storeId) throws Exception {
        Store store = getStoreByID(storeId);
        return store.calcDiscountPolicy(userDTO, products);
    }

    public int calcPrice(String productName, int quantity, String storeId, String userId) throws Exception {
        Store store = getStoreByID(storeId);
        return store.calcPriceInStore(productName, quantity, userId);
    }

   // @Transactional
    public void addProductToStore(String storeId, ProductDTO product) throws Exception {

        checkProductPrice(product);
        if (!checkProductExistInStore(product.getName(), storeId)) {
            if (product.getQuantity() >= 0) {
                allStores.getById(storeId).addProduct(product);
               // allStores.addProductToStore(storeId, product.getName(), product.getPrice(), product.getQuantity(), product.getCategoryStr(), product.getDescription());
                //getStoreByID(storeId).addProduct(product);
            } else {
                throw new Exception(ExceptionsEnum.productQuantityIsNegative.toString());
            }
        } else {
            throw new Exception(ExceptionsEnum.productAlreadyExistInStore.toString());
        }
    }

    private void checkProductPrice(ProductDTO product) {
        if (product.getPrice() < 0)
            throw new IllegalArgumentException(ExceptionsEnum.NegativePrice.toString());
    }


    public void removeProductFromStore(String storeId, String productName) throws Exception {
        if (checkProductExistInStore(productName, storeId)) {
           // getStoreByID(storeId).removeProduct(productName);
            allStores.getById(storeId).removeProduct(productName);
        } else {
            throw new Exception(ExceptionsEnum.productNotExistInStore.toString());
        }

    }

    public void updateProductInStore(String storeId, ProductDTO product) throws Exception {

        checkProductPrice(product);
        if (checkProductExistInStore(product.getName(), storeId)) {
            if (product.getQuantity() >= 0) {
                allStores.getById(storeId).updateProduct(product);
                //getStoreByID(storeId).updateProduct(product);
            } else {
                throw new Exception(ExceptionsEnum.productQuantityIsNegative.toString());
            }
        } else {
            throw new Exception(ExceptionsEnum.productNotExistInStore.toString());
        }
    }

    public boolean verifyStoreExist(String storeID) throws Exception {
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

    public void reopenStore(String store_ID) throws Exception
    {
        this.getStoreByID(store_ID).reopenStore();
    }

    public List<String> getInformationAboutOpenStores()
    {
        List<String> openStoreInformation = new ArrayList<>();
        for (Store store : allStores.findAll()) {
            //int storeId = entry.getKey();
            //Store store = entry.getValue();
            if (store.getIsOpened())
                openStoreInformation.add(store.getStoreID());
        }
        return openStoreInformation;
    }

    public List<String> getInformationAboutClosedStores() {
        List<String> closedStoreInformation = new ArrayList<>();
        for (Store store : allStores.findAll()) {
            //int storeId = entry.getKey();
            //Store store = entry.getValue();
            if (!store.getIsOpened()) {
                closedStoreInformation.add(store.getStoreID());
            }

        }
        return closedStoreInformation;
    }

    public List<String> getStoreProducts(String store_ID) throws Exception {
        Store store = getStoreByID(store_ID);
        return store.getProducts();
    }

    public List<String> inStoreProductSearch(String productName, String categoryStr, List<String> keywords, String storeId) throws Exception {
        Store storeToSearchIn = getStoreByID(storeId);
        List<String> filteredProducts = storeToSearchIn.matchProducts(productName, categoryStr, keywords);
        return filteredProducts;
    }

    public List<ProductDTO> inStoreProductSearchDTO(String productName, String categoryStr, List<String> keywords, String storeId) throws Exception {
        Store storeToSearchIn = getStoreByID(storeId);
        List<ProductDTO> filteredProducts = storeToSearchIn.matchProductsDTO(productName, categoryStr, keywords);
        return filteredProducts;
    }


    public List<String> inStoreProductFilter(String categoryStr, List<String> keywords, Integer minPrice, Integer maxPrice, Double minRating, String storeId, List<String> productsFromSearch, Double storeMinRating) throws Exception {
        Store storeToSearchIn = getStoreByID(storeId);
        List<String> filteredProducts = storeToSearchIn.filterProducts(categoryStr, keywords, minPrice, maxPrice, minRating, productsFromSearch, storeMinRating);
        return filteredProducts;
    }

    public void checkCategory(String categoryStr)
    {
        if (categoryStr != null)
        {
            Category.fromString(categoryStr);
        }
    }

    public boolean checkProductExistInStore(String productName, String storeId) throws Exception {
        Store store = getStoreByID(storeId);
        return store.checkProductExists(productName);
    }

    public List<String> getStores() throws Exception {

        try{
            return allStores.getAllIds();
        }
        catch(Exception e) {
            throw new Exception(ExceptionsEnum.DatabaseIsNotConnected.toString());
//        return allStores.getAllIds();
        }
    }

//    public void addReceiptToStore(String storeId, String  receiptId, String userId)
//    {
//        allStores.getById(storeId).addReceipt(receiptId, userId);
//       // getStoreByID(storeId).addReceipt(receiptId, userId);
//    }

    public void addPurchaseRuleToStore(List<TestRuleDTO> testRules, List<String> operators, String storeId) {
        List<Rule> rules = new ArrayList<>();
        for (TestRuleDTO testRule : testRules) {
            rules.add(new SimpleRule(testRule));
        }
        allStores.getById(storeId).addPurchaseRule(rules, operators);
        //getStoreByID(storeId).addPurchaseRule(rules, operators);
    }

    //implement removeRuleFromStore
    public void removePurchaseRuleFromStore(int ruleNum, String storeId) {
        //getStoreByID(storeId).removePurchaseRule(ruleNum);
        allStores.getById(storeId).removePurchaseRule(ruleNum);
    }

    public void addDiscountCondRuleToStore(List<TestRuleDTO> testRules, List<String> operators, List<DiscountValueDTO> discDetails, List<String> numericalOperators, String storeId) {

        List<DiscountValue> discountValue = getDiscountValuesList(discDetails);

        List<Rule> rules = new ArrayList<>();
        for (TestRuleDTO testRule : testRules) {
            rules.add(new SimpleRule(testRule));
        }
        allStores.getById(storeId).addDiscountCondRule(rules, operators, discountValue, numericalOperators);
       // getStoreByID(storeId).addDiscountCondRule(rules, operators, discountValue, numericalOperators);
    }

    public void addDiscountSimpleRuleToStore(List<DiscountValueDTO> discDetails, List<String> numericalOperators, String storeId) {
        List<DiscountValue> discountValue = getDiscountValuesList(discDetails);
        allStores.getById(storeId).addDiscountSimple(discountValue, numericalOperators);

        //getStoreByID(storeId).addDiscountSimple(discountValue, numericalOperators);
    }

    public void removeDiscountRuleFromStore(int ruleNum, String storeId) {
       // getStoreByID(storeId).removeDiscountRule(ruleNum);
        allStores.getById(storeId).removeDiscountRule(ruleNum);
    }

    public List<DiscountValue> getDiscountValuesList(List<DiscountValueDTO> discDetails) {
        List<DiscountValue> discountValue = new ArrayList<>();

        for (DiscountValueDTO discDetail : discDetails) {
            discountValue.add(new SimpleDiscountValue(discDetail.getPercentage(), Category.fromString(discDetail.getCategory()) , discDetail.getIsStoreDiscount(), discDetail.getProductsNames()));
        }
        return discountValue;
    }

    public List<String> getStoreCurrentPurchaseRules(String storeId) {
        //return getStoreByID(storeId).getStoreCurrentPurchaseRules();
        return allStores.getById(storeId).getStoreCurrentPurchaseRules();
    }

    public List<String> getStoreCurrentDiscountRules(String storeId) {
        //return getStoreByID(storeId).getStoreCurrentDiscountRules();
        return allStores.getById(storeId).getStoreCurrentDiscountRules();
    }

    public void composeCurrentPurchaseRules(int ruleIndex1, int ruleIndex2, String operator, String storeId) {
        //getStoreByID(storeId).composeCurrentPurchaseRules(ruleIndex1, ruleIndex2, operator);
        allStores.getById(storeId).composeCurrentPurchaseRules(ruleIndex1, ruleIndex2, operator);
    }

    public void composeCurrentSimpleDiscountRules(int ruleIndex1, int ruleIndex2, String numericalOperator, String storeId) {
        //getStoreByID(storeId).composeCurrentSimpleDiscountRules(ruleIndex1, ruleIndex2, numericalOperator);
        allStores.getById(storeId).composeCurrentSimpleDiscountRules(ruleIndex1, ruleIndex2, numericalOperator);
    }

    public void composeCurrentCondDiscountRules(int ruleIndex1, int ruleIndex2, String logicalOperator, String numericalOperator, String storeId) {
        //getStoreByID(storeId).composeCurrentCondDiscountRules(ruleIndex1, ruleIndex2, logicalOperator, numericalOperator);
        allStores.getById(storeId).composeCurrentCondDiscountRules(ruleIndex1, ruleIndex2, logicalOperator, numericalOperator);    }

    public List<String> getStoreCurrentSimpleDiscountRules(String storeId) {
       // return getStoreByID(storeId).getStoreCurrentSimpleDiscountRules();
        return allStores.getById(storeId).getStoreCurrentSimpleDiscountRules();
    }

    public List<String> getStoreCurrentCondDiscountRules(String storeId) {
    //    return getStoreByID(storeId).getStoreCurrentCondDiscountRules();
        return allStores.getById(storeId).getStoreCurrentCondDiscountRules();
    }
}

