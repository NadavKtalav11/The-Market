package PresentationLayer.WAF;

import ServiceLayer.Response;
import Util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/market")
public class MarketController {

    //private final MarketService marketService;
    private final Service_layer serviceLayer;

    @Autowired
    public MarketController(Service_layer serviceLayer) {
        //this.marketService = marketService;
        this.serviceLayer = serviceLayer;
    }


    @GetMapping("/enterSystem")
    public ResponseEntity<APIResponse<String>> enterMarket() {
        try {
            Response<String> response = serviceLayer.enterMarketSystem();
            if (response.isSuccess()) {
                String userId = response.getData();
                HttpHeaders headers = new HttpHeaders();
                headers.add("accept", "*/*");
                return ResponseEntity.status(HttpStatus.OK).headers(headers)
                        .body(new APIResponse<String>(userId, null));
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new APIResponse<String>(null, response.getDescription()));
            }

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new APIResponse<>(null, e.getMessage()));
        }
    }

    @PostMapping("/init/{userDTO}/{password}/{paymentServiceDTO}/{supplyServiceDTO}")
    public ResponseEntity<APIResponse<String>> init(@PathVariable UserDTO userDTO, @PathVariable String password, @PathVariable PaymentServiceDTO paymentServiceDTO, @PathVariable SupplyServiceDTO supplyServiceDTO) {
        try {
            Response<String> response = serviceLayer.init(userDTO, password, paymentServiceDTO, supplyServiceDTO);
            if (response.isSuccess()) {
                String userId = response.getData();
                HttpHeaders headers = new HttpHeaders();
                headers.add("accept", "*/*");

                return ResponseEntity.status(HttpStatus.OK).headers(headers)
                        .body(new APIResponse<String>(userId, null));
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new APIResponse<String>(null, response.getDescription()));
            }

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new APIResponse<>(null, e.getMessage()));
        }
    }

    @PostMapping("/addExternalPaymentService/{paymentServiceDTO}/{managerId}")
    public ResponseEntity<APIResponse<String>> addExternalPaymentService(@PathVariable PaymentServiceDTO paymentServiceDTO, @PathVariable String managerId) {
        try {
            Response<String> response = serviceLayer.addExternalPaymentService(paymentServiceDTO, managerId);
            if (response.isSuccess()) {
                String result = response.getResult();
                HttpHeaders headers = new HttpHeaders();
                headers.add("accept", "*/*");

                return ResponseEntity.status(HttpStatus.OK).headers(headers)
                        .body(new APIResponse<String>(result, null));
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new APIResponse<String>(null, response.getDescription()));
            }

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new APIResponse<>(null, e.getMessage()));
        }
    }

    @DeleteMapping("/removeExternalPaymentService/{licenceNum}/{managerId}")
    public ResponseEntity<APIResponse<String>> removeExternalPaymentService(@PathVariable String licenceNum, @PathVariable String managerId) {
        try {
            Response<String> response = serviceLayer.removeExternalPaymentService(licenceNum, managerId);
            if (response.isSuccess()) {
                String result = response.getResult();
                HttpHeaders headers = new HttpHeaders();
                headers.add("accept", "*/*");

                return ResponseEntity.status(HttpStatus.OK).headers(headers)
                        .body(new APIResponse<String>(result, null));
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new APIResponse<String>(null, response.getDescription()));
            }

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new APIResponse<>(null, e.getMessage()));
        }
    }

    @PostMapping("/addExternalSupplyService/{supplyServiceDTO}/{managerId}")
    public ResponseEntity<APIResponse<String>> addExternalSupplyService(@PathVariable SupplyServiceDTO supplyServiceDTO, @PathVariable String managerId) {
        try {
            Response<String> response = serviceLayer.addExternalSupplyService(supplyServiceDTO, managerId);
            if (response.isSuccess()) {
                String result = response.getResult();
                HttpHeaders headers = new HttpHeaders();
                headers.add("accept", "*/*");

                return ResponseEntity.status(HttpStatus.OK).headers(headers)
                        .body(new APIResponse<String>(result, null));
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new APIResponse<String>(null, response.getDescription()));
            }

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new APIResponse<>(null, e.getMessage()));
        }
    }

    @DeleteMapping("/removeExternalSupplyService/{licenceNum}/{managerId}")
    public ResponseEntity<APIResponse<String>> removeExternalSupplyService(@PathVariable String licenceNum, @PathVariable String managerId) {
        try {
            Response<String> response = serviceLayer.removeExternalSupplyService(licenceNum, managerId);
            if (response.isSuccess()) {
                String result = response.getResult();
                HttpHeaders headers = new HttpHeaders();
                headers.add("accept", "*/*");

                return ResponseEntity.status(HttpStatus.OK).headers(headers)
                        .body(new APIResponse<String>(result, null));
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new APIResponse<String>(null, response.getDescription()));
            }

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new APIResponse<>(null, e.getMessage()));
        }
    }

    @PostMapping("/purchase/{userDTO}/{paymentDTO}")
    public ResponseEntity<APIResponse<String>> purchase(@PathVariable UserDTO userDTO, @PathVariable PaymentDTO paymentDTO) {
        try {
            Response<String> response = serviceLayer.purchase(userDTO, paymentDTO);
            if (response.isSuccess()) {
                String result = response.getResult();
                HttpHeaders headers = new HttpHeaders();
                headers.add("accept", "*/*");

                return ResponseEntity.status(HttpStatus.OK).headers(headers)
                        .body(new APIResponse<String>(result, null));
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new APIResponse<String>(null, response.getDescription()));
            }

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new APIResponse<>(null, e.getMessage()));
        }
    }

    @PostMapping("/exitMarketSystem/{userId}")
    public ResponseEntity<APIResponse<String>> purchase(@PathVariable String userId) {
        try {
            Response<String> response = serviceLayer.exitMarketSystem(userId);
            if (response.isSuccess()) {
                String result = response.getResult();
                HttpHeaders headers = new HttpHeaders();
                headers.add("accept", "*/*");

                return ResponseEntity.status(HttpStatus.OK).headers(headers)
                        .body(new APIResponse<String>(result, null));
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new APIResponse<String>(null, response.getDescription()));
            }

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new APIResponse<>(null, e.getMessage()));
        }
    }


    @PostMapping("/register/{userDTO}/{password}")
    public ResponseEntity<APIResponse<String>> register(@PathVariable UserDTO userDTO, @PathVariable String password) {
        try {
            Response<String> response = serviceLayer.register(userDTO, password);
            if (response.isSuccess()) {
                String result = response.getData();
                HttpHeaders headers = new HttpHeaders();
                headers.add("accept", "*/*");

                return ResponseEntity.status(HttpStatus.OK).headers(headers)
                        .body(new APIResponse<String>(result, null));
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new APIResponse<String>(null, response.getDescription()));
            }

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new APIResponse<>(null, e.getMessage()));
        }
    }


    @PostMapping("/login/{userId}/{userName}/{password}")
    public ResponseEntity<APIResponse<String>> login(@PathVariable String userId, @PathVariable String userName, @PathVariable String password) {
        try {
            Response<String> response = serviceLayer.login(userId, userName, password);
            if (response.isSuccess()) {
                String result = response.getResult();
                HttpHeaders headers = new HttpHeaders();
                headers.add("accept", "*/*");

                return ResponseEntity.status(HttpStatus.OK).headers(headers)
                        .body(new APIResponse<String>(result, null));
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new APIResponse<String>(null, response.getDescription()));
            }

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new APIResponse<>(null, e.getMessage()));
        }
    }

    @PostMapping("/logout/{userId}")
    public ResponseEntity<APIResponse<String>> logout(@PathVariable String userId) {
        try {
            Response<String> response = serviceLayer.logout(userId);
            if (response.isSuccess()) {
                String result = response.getResult();
                HttpHeaders headers = new HttpHeaders();
                headers.add("accept", "*/*");

                return ResponseEntity.status(HttpStatus.OK).headers(headers)
                        .body(new APIResponse<String>(result, null));
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new APIResponse<String>(null, response.getDescription()));
            }

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new APIResponse<>(null, e.getMessage()));
        }
    }

    @PostMapping("/addProductToStore/{userId}/{storeId}/{productDTO}")
    public ResponseEntity<APIResponse<String>> addProductToStore(@PathVariable String userId, @PathVariable String storeId, @PathVariable ProductDTO productDTO) {

        Response<String> response = serviceLayer.addProductToStore(userId, storeId, productDTO);
        return checkIfResponseIsGood(response);
    }

    @DeleteMapping("/removeProductFromStore/{userId}/{storeId}/{productId}")
    public ResponseEntity<APIResponse<String>> removeProductFromStore(@PathVariable String userId, @PathVariable String storeId, @PathVariable String productId) {

        Response<String> response = serviceLayer.removeProductFromStore(userId, storeId, productId);
        return checkIfResponseIsGood(response);
    }

    @PostMapping("/updateProductInStore/{userId}/{storeId}/{productDTO}")
    public ResponseEntity<APIResponse<String>> updateProductInStore(@PathVariable String userId, @PathVariable String storeId, @PathVariable ProductDTO productDTO) {

        Response<String> response = serviceLayer.updateProductInStore(userId, storeId, productDTO);
        return checkIfResponseIsGood(response);
    }

    @PostMapping("/appointStoreOwner/{userId}/{appointedId}/{storeId}")
    public ResponseEntity<APIResponse<String>> appointStoreOwner(@PathVariable String userId, @PathVariable String appointedId, @PathVariable String storeId) {

        Response<String> response = serviceLayer.appointStoreOwner(userId, appointedId, storeId);
        return checkIfResponseIsGood(response);
    }

    @PostMapping("/appointStoreManager/{userId}/{appointedId}/{storeId}/{invPer}/{purPer}")
    public ResponseEntity<APIResponse<String>> appointStoreManager(@PathVariable String userId, @PathVariable String appointedId, @PathVariable String storeId,@PathVariable boolean invPer, @PathVariable boolean purPer) {

        Response<String> response = serviceLayer.appointStoreManager(userId, appointedId, storeId, invPer , purPer);
        return checkIfResponseIsGood(response);
    }


    @PostMapping("/updateStoreManagerPermissions/{userId}/{appointedId}/{storeId}/{invPer}/{purPer}")
    public ResponseEntity<APIResponse<String>> updateStoreManagerPermissions(@PathVariable String userId, @PathVariable String appointedId, @PathVariable String storeId,@PathVariable boolean invPer, @PathVariable boolean purPer) {

        Response<String> response = serviceLayer.updateStoreManagerPermissions(userId, appointedId, storeId, invPer , purPer);
        return checkIfResponseIsGood(response);
    }

    @GetMapping("/generalProductFilter/{userId}/{categoryStr}/{keywords}/{minPrice}/{maxPrice}/{productMinRating}/{productsFromSearch}/{storeMinRating}")
    public ResponseEntity<APIResponse<List<String>>> generalProductFilter(@PathVariable String userId,@PathVariable String categoryStr,@PathVariable List<String> keywords,@PathVariable Integer minPrice, @PathVariable Integer maxPrice,@PathVariable Double productMinRating,@PathVariable List<String> productsFromSearch,@PathVariable Double storeMinRating) {
        try {
            Response<List<String>> response = serviceLayer.generalProductFilter(userId,categoryStr , keywords, minPrice, maxPrice , productMinRating, productsFromSearch,storeMinRating);
            if (response.isSuccess()) {
            List<String> result = response.getResult();
                HttpHeaders headers = new HttpHeaders();
                headers.add("accept", "*/*");

                return ResponseEntity.status(HttpStatus.OK).headers(headers)
                        .body(new APIResponse<List<String>>(result, null));
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new APIResponse<>(null, response.getDescription()));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new APIResponse<>(null, e.getMessage()));

        }
    }

    @GetMapping("/generalProductSearch/{userId}/{productName}/{categoryStr}/{keywords}")
    public ResponseEntity<APIResponse<List<String>>> generalProductSearch(@PathVariable String userId,@PathVariable String productName,@PathVariable String categoryStr,@PathVariable List<String> keywords) {
        try {
            Response<List<String>> response = serviceLayer.generalProductSearch(userId,productName , categoryStr , keywords);
            if (response.isSuccess()) {
                List<String> result = response.getResult();
                HttpHeaders headers = new HttpHeaders();
                headers.add("accept", "*/*");

                return ResponseEntity.status(HttpStatus.OK).headers(headers)
                        .body(new APIResponse<List<String>>(result, null));
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new APIResponse<>(null, response.getDescription()));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new APIResponse<>(null, e.getMessage()));

        }
    }

    @GetMapping("/getInformationAboutStores/{userId}")
    public ResponseEntity<APIResponse<List<String>>> getInformationAboutStores(@PathVariable String userId) {
        try {
            Response<List<String>> response = serviceLayer.getInformationAboutStores( userId);
            if (response.isSuccess()) {
                List<String> result = response.getResult();
                HttpHeaders headers = new HttpHeaders();
                headers.add("accept", "*/*");

                return ResponseEntity.status(HttpStatus.OK).headers(headers)
                        .body(new APIResponse<List<String>>(result, null));
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new APIResponse<>(null, response.getDescription()));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new APIResponse<>(null, e.getMessage()));

        }
    }

    @GetMapping("/getInformationAboutRolesInStore/{userId}/{storeId}")
    public ResponseEntity<APIResponse<Map<String, String>>> getInformationAboutRolesInStore(@PathVariable String userId, @PathVariable String storeId) {
        try {
            Response<Map<String, String>> response = serviceLayer.getInformationAboutRolesInStore( userId, storeId);
            if (response.isSuccess()) {
                Map<String, String> result = response.getResult();
                HttpHeaders headers = new HttpHeaders();
                headers.add("accept", "*/*");

                return ResponseEntity.status(HttpStatus.OK).headers(headers)
                        .body(new APIResponse<>(result, null));
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new APIResponse<>(null, response.getDescription()));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new APIResponse<>(null, e.getMessage()));

        }
    }

    @GetMapping("/getAuthorizationsOfManagersInStore/{userId}/{storeId}")
    public ResponseEntity<APIResponse<Map<String, List<Integer>>>> getAuthorizationsOfManagersInStore(@PathVariable String userId, @PathVariable String storeId) {
        try {
            Response<Map<String, List<Integer>>> response = serviceLayer.getAuthorizationsOfManagersInStore( userId, storeId);
            if (response.isSuccess()) {
                Map<String, List<Integer>> result = response.getResult();
                HttpHeaders headers = new HttpHeaders();
                headers.add("accept", "*/*");

                return ResponseEntity.status(HttpStatus.OK).headers(headers)
                        .body(new APIResponse<>(result, null));
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new APIResponse<>(null, response.getDescription()));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new APIResponse<>(null, e.getMessage()));

        }
    }


    @PostMapping("/closeStore/{userId}/{storeId}")
    public ResponseEntity<APIResponse<String>> closeStore(@PathVariable String userId,  @PathVariable String storeId) {

        Response<String> response = serviceLayer.closeStore(userId,  storeId);
        return checkIfResponseIsGood(response);
    }

    @PostMapping("/openStore/{userId}/{storeName}/{storeDes}")
    public ResponseEntity<APIResponse<String>> openStore(@PathVariable String userId,  @PathVariable String storeName, @PathVariable String storeDes ) {

        Response<String> response = serviceLayer.openStore(userId,  storeName,storeDes);
        return checkIfResponseIsGood(response);
    }

    @PostMapping("/addProductToBasket/{productName}/{quantity}/{storeId}/{userId}")
    public ResponseEntity<APIResponse<String>> addProductToBasket(@PathVariable String productName,  @PathVariable int quantity,  @PathVariable String storeId, @PathVariable String userId ) {

        Response<String> response = serviceLayer.addProductToBasket(productName,quantity,  storeId,userId);
        return checkIfResponseIsGood(response);
    }

    @DeleteMapping("/removeProductFromBasket/{productName}/{storeId}/{userId}")
    public ResponseEntity<APIResponse<String>> removeProductFromBasket(@PathVariable String productName,   @PathVariable String storeId, @PathVariable String userId ) {

        Response<String> response = serviceLayer.removeProductFromBasket(productName,storeId,userId);
        return checkIfResponseIsGood(response);
    }


    @PostMapping("/modifyShoppingCart/{productName}/{quantity}/{storeId}/{userId}")
    public ResponseEntity<APIResponse<String>> modifyShoppingCart(@PathVariable String productName,  @PathVariable int quantity,  @PathVariable String storeId, @PathVariable String userId ) {

        Response<String> response = serviceLayer.modifyShoppingCart(productName,quantity,  storeId,userId);
        return checkIfResponseIsGood(response);
    }


    @GetMapping("/marketManagerAskInfo/{userId}")
    public ResponseEntity<APIResponse<Map<String, Integer>>> marketManagerAskInfo( @PathVariable String userId ) {
        try {
            Response<Map<String, Integer>> response = serviceLayer.marketManagerAskInfo( userId);
            if (response.isSuccess()) {
                Map<String, Integer> result = response.getResult();
                HttpHeaders headers = new HttpHeaders();
                headers.add("accept", "*/*");

                return ResponseEntity.status(HttpStatus.OK).headers(headers)
                        .body(new APIResponse<>(result, null));
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new APIResponse<>(null, response.getDescription()));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new APIResponse<>(null, e.getMessage()));

        }
    }
    @GetMapping("/storeOwnerGetInfoAboutStore/{userId}/{storeId}")
    public ResponseEntity<APIResponse<Map<String, Integer>>> storeOwnerGetInfoAboutStore( @PathVariable String userId ,@PathVariable String storeId ) {
        try {
            Response<Map<String, Integer>> response = serviceLayer.storeOwnerGetInfoAboutStore( userId, storeId);
            if (response.isSuccess()) {
                Map<String, Integer> result = response.getResult();
                HttpHeaders headers = new HttpHeaders();
                headers.add("accept", "*/*");

                return ResponseEntity.status(HttpStatus.OK).headers(headers)
                        .body(new APIResponse<>(result, null));
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new APIResponse<>(null, response.getDescription()));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new APIResponse<>(null, e.getMessage()));

        }
    }



    @GetMapping("/generalProductFilterinStoreProductFilter/{userId}/{categoryStr}/{keywords}/{minPrice}/{maxPrice}/{productMinRating}/{storeId}/{productsFromSearch}/{storeMinRating}")
    public ResponseEntity<APIResponse<List<String>>> generalProductFilterinStoreProductFilter(@PathVariable String userId,@PathVariable String categoryStr,@PathVariable List<String> keywords,@PathVariable Integer minPrice, @PathVariable Integer maxPrice,@PathVariable Double productMinRating,@PathVariable String storeId,@PathVariable List<String> productsFromSearch,@PathVariable Double storeMinRating) {
        try {
            Response<List<String>> response = serviceLayer.inStoreProductFilter(userId,categoryStr , keywords, minPrice, maxPrice , productMinRating,storeId,  productsFromSearch,storeMinRating);
            if (response.isSuccess()) {
                List<String> result = response.getResult();
                HttpHeaders headers = new HttpHeaders();
                headers.add("accept", "*/*");

                return ResponseEntity.status(HttpStatus.OK).headers(headers)
                        .body(new APIResponse<List<String>>(result, null));
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new APIResponse<>(null, response.getDescription()));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new APIResponse<>(null, e.getMessage()));

        }
    }


    @GetMapping("/inStoreProductSearch/{userId}/{productName}/{categoryStr}/{keywords}/{storeId}")
    public ResponseEntity<APIResponse<List<String>>> inStoreProductSearch(@PathVariable String userId,@PathVariable String productName,@PathVariable String categoryStr,@PathVariable List<String> keywords, @PathVariable String storeId) {
        try {
            Response<List<String>> response = serviceLayer.inStoreProductSearch(userId,productName , categoryStr , keywords,storeId);
            if (response.isSuccess()) {
                List<String> result = response.getResult();
                HttpHeaders headers = new HttpHeaders();
                headers.add("accept", "*/*");

                return ResponseEntity.status(HttpStatus.OK).headers(headers)
                        .body(new APIResponse<List<String>>(result, null));
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new APIResponse<>(null, response.getDescription()));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new APIResponse<>(null, e.getMessage()));

        }
    }


    @PostMapping("/addRuleToStore/{ruleNums}/{operators}/{storeId}/{userId}")
    public ResponseEntity<APIResponse<String>> addRuleToStore(@PathVariable List<Integer> ruleNums,  @PathVariable List<String> operators,  @PathVariable String storeId, @PathVariable String userId ) {

        Response<String> response = serviceLayer.addRuleToStore(ruleNums,operators,storeId,userId);
        return checkIfResponseIsGood(response);
    }


    @PostMapping("/removeRuleFromStore/{ruleNums}/{storeId}/{userId}")
    public ResponseEntity<APIResponse<String>> removeRuleFromStore(@PathVariable int ruleNum,  @PathVariable String storeId, @PathVariable String userId ) {

        Response<String> response = serviceLayer.removeRuleFromStore(ruleNum,storeId,userId);
        return checkIfResponseIsGood(response);
    }











    private ResponseEntity<APIResponse<String>> checkIfResponseIsGood(Response<String> response) {
        try {
            if (response.isSuccess()) {
                String result = response.getResult();
                HttpHeaders headers = new HttpHeaders();
                headers.add("accept", "*/*");

                return ResponseEntity.status(HttpStatus.OK).headers(headers)
                        .body(new APIResponse<String>(result, null));
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new APIResponse<String>(null, response.getDescription()));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new APIResponse<>(null, e.getMessage()));

        }
    }



}















