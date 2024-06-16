package PresentationLayer.WAF;

import ServiceLayer.Response;
import Util.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/market")
public class MarketController {

    //private final MarketService marketService;
    private final ObjectMapper objectMapper;
    private final Service_layer serviceLayer;

    @Autowired
    public MarketController(Service_layer serviceLayer) {
        //this.marketService = marketService;
        this.serviceLayer = serviceLayer;
        this.objectMapper =new ObjectMapper();
    }

    @GetMapping("/checkInitializedMarket")
    public ResponseEntity<APIResponse<Boolean>> checkInitializedMarket() {
        try {
            Response<Boolean> response = serviceLayer.checkInitializedMarket();
            if (response.isSuccess()) {
                Boolean res = response.getResult();
                HttpHeaders headers = new HttpHeaders();
                headers.add("accept", "*/*");
                return ResponseEntity.status(HttpStatus.OK).headers(headers)
                        .body(new APIResponse<Boolean>(res, null));
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new APIResponse<Boolean>(null, response.getDescription()));
            }

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new APIResponse<>(null, e.getMessage()));
        }
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

    @PostMapping("/init")
    public ResponseEntity<APIResponse<String>> init(@RequestParam Map<String,String> params) {
        try {
            String userDTO = params.get("userDTO");
            String password = params.get("password");
            String paymentServiceDTO = params.get("paymentServiceDTO");
            String supplyServiceDTO = params.get("supplyServiceDTO");
            Response<String> response = serviceLayer.init(objectMapper.readValue(userDTO,UserDTO.class), password, objectMapper.readValue(paymentServiceDTO, PaymentServiceDTO.class), objectMapper.readValue(supplyServiceDTO,SupplyServiceDTO.class));
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

    @PostMapping("/addExternalPaymentService")
    public ResponseEntity<APIResponse<String>> addExternalPaymentService(@RequestParam Map<String,String> params) {
        try {
            String paymentServiceDTO =params.get("paymentServiceDTO");
            String managerId = params.get("memberId");
            Response<String> response = serviceLayer.addExternalPaymentService(objectMapper.readValue(paymentServiceDTO,PaymentServiceDTO.class), managerId);
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

    @PostMapping("/addExternalSupplyService")
    public ResponseEntity<APIResponse<String>> addExternalSupplyService(@RequestParam Map<String,String> params) {
        try {
            String supplyServiceDTO = params.get("supplyServiceDTO");
            String managerId= params.get("managerId");
            Response<String> response = serviceLayer.addExternalSupplyService(objectMapper.readValue(supplyServiceDTO,SupplyServiceDTO.class), managerId);
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

    @PostMapping("/purchase")
    public ResponseEntity<APIResponse<String>> purchase(@RequestParam Map<String,String> params) {
        try {
            String userDTO = params.get("userDTO");
            String paymentDTO= params.get("paymentDTO");
            Response<String> response = serviceLayer.purchase(objectMapper.readValue(userDTO, UserDTO.class),objectMapper.readValue( paymentDTO, PaymentDTO.class));
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


    @PostMapping("/register")
    //@PostMapping("/register")
    public ResponseEntity<APIResponse<String>> register(@RequestParam Map<String,String> params) {
        try{
            String userDTO = params.get("userDTO");
            String password = params.get("password");

            Response<String> response = serviceLayer.register(objectMapper.readValue(userDTO, UserDTO.class), password);
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

    @GetMapping("/getCategories")
    public ResponseEntity<APIResponse<List<String>>> getCategories() {
        try {
            Response<List<String>> response = serviceLayer.getStoreCategories();
            HttpHeaders headers = new HttpHeaders();
            headers.add("accept", "*/*");
            return ResponseEntity.status(HttpStatus.OK).headers(headers)
                    .body(new APIResponse<>(response.getResult(), null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new APIResponse<>(null, e.getMessage()));
        }
    }

    @GetMapping("/getStoreProducts/{storeId}")
    public ResponseEntity<APIResponse<List<String>>> getStoreProducts(@PathVariable String storeId) {
        try {
            ObjectMapper objectMapper= new ObjectMapper();
            Response<List<ProductDTO>> response = serviceLayer.getProductStores(storeId);
            HttpHeaders headers = new HttpHeaders();
            headers.add("accept", "*/*");
            List<String> dtosRes = new ArrayList<>();
            for (ProductDTO productDTO : response.getResult() ){
                dtosRes.add(objectMapper.writeValueAsString(productDTO));
            }

            return ResponseEntity.status(HttpStatus.OK).headers(headers)
                    .body(new APIResponse<>(dtosRes, null));
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

    @PostMapping("/addProductToStore")
    public ResponseEntity<APIResponse<String>> addProductToStore(@RequestParam Map<String,String> params) {
        try {
            String userId = params.get("userId");
            String storeId = params.get("storeId");
            String productDTO = params.get("productDTO");
            Response<String> response = serviceLayer.addProductToStore(userId, storeId, objectMapper.readValue(productDTO, ProductDTO.class));
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


    @DeleteMapping("/removeProductFromStore/{userId}/{storeId}/{productId}")
    public ResponseEntity<APIResponse<String>> removeProductFromStore(@PathVariable String userId, @PathVariable String storeId, @PathVariable String productId) {

        Response<String> response = serviceLayer.removeProductFromStore(userId, storeId, productId);
        return checkIfResponseIsGood(response);
    }

    @PostMapping("/updateProductInStore")
    public ResponseEntity<APIResponse<String>> updateProductInStore(@RequestParam Map<String,String> params) {
        try {
            String userId = params.get("userId");
            String storeId= params.get("storeId");
            String productDTO= params.get("productDTO");

            Response<String> response = serviceLayer.updateProductInStore(userId, storeId, objectMapper.readValue(productDTO, ProductDTO.class));
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
    public ResponseEntity<APIResponse<Map<String,List<String>>>> generalProductSearch(@PathVariable String userId,@PathVariable String productName,@PathVariable String categoryStr,@PathVariable List<String> keywords) {
        try {
            Response<Map<String, List<ProductDTO>>> response = serviceLayer.generalProductSearchDTO(userId,productName , categoryStr , keywords);
            Map<String, List<ProductDTO>> result = response.getResult();
            if (response.isSuccess()) {
                Map<String , List<String>> dtos = new HashMap<>();
                for (String storeID: result.keySet()){
                    List<String> stringDTOs= new ArrayList<>();
                    for (ProductDTO productDTO : result.get(storeID)){
                        stringDTOs.add(objectMapper.writeValueAsString(productDTO));
                    }
                    dtos.put(storeID, stringDTOs);
                }
                //List<ProductDTO> result = response.getResult();
                HttpHeaders headers = new HttpHeaders();
                headers.add("accept", "*/*");

                return ResponseEntity.status(HttpStatus.OK).headers(headers)
                        .body(new APIResponse<>(dtos, null));
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

    @GetMapping("/getStoreWorkers/{storeId}")
    public ResponseEntity<APIResponse<List<String>>> getStoreWorkers(@PathVariable String storeId) {
        try {
            Response<List<UserDTO>> response = serviceLayer.getStoreWorkers( storeId);
            List<String> dtosRes = new ArrayList<>();
            if (response.isSuccess()) {
                List<UserDTO> result = response.getResult();
                HttpHeaders headers = new HttpHeaders();
                headers.add("accept", "*/*");
                for (UserDTO userDTO : response.getResult() ) {
                    dtosRes.add(objectMapper.writeValueAsString(userDTO));
                }
                return ResponseEntity.status(HttpStatus.OK).headers(headers)
                        .body(new APIResponse<List<String>>(dtosRes, null));
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new APIResponse<>(null, response.getDescription()));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new APIResponse<>(null, e.getMessage()));

        }
    }

    @GetMapping("/getStoreOwnersDTO/{storeId}")
    public ResponseEntity<APIResponse<List<String>>> getStoreOwnersDTO(@PathVariable String storeId) {
        try {
            Response<List<UserDTO>> response = serviceLayer.getStoreOwnersDTO( storeId);
            List<String> dtosRes = new ArrayList<>();
            if (response.isSuccess()) {
                List<UserDTO> result = response.getResult();
                HttpHeaders headers = new HttpHeaders();
                headers.add("accept", "*/*");
                for (UserDTO userDTO : response.getResult() ) {
                    dtosRes.add(objectMapper.writeValueAsString(userDTO));
                }
                return ResponseEntity.status(HttpStatus.OK).headers(headers)
                        .body(new APIResponse<List<String>>(dtosRes, null));
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new APIResponse<>(null, response.getDescription()));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new APIResponse<>(null, e.getMessage()));

        }
    }
    @GetMapping("/getStoreManagersDTO/{storeId}")
    public ResponseEntity<APIResponse<List<String>>> getStoreManagersDTO(@PathVariable String storeId) {
        try {
            Response<List<UserDTO>> response = serviceLayer.getStoreManagersDTO( storeId);
            List<String> dtosRes = new ArrayList<>();
            if (response.isSuccess()) {
                List<UserDTO> result = response.getResult();
                HttpHeaders headers = new HttpHeaders();
                headers.add("accept", "*/*");
                for (UserDTO userDTO : result) {
                    dtosRes.add(objectMapper.writeValueAsString(userDTO));
                }
                return ResponseEntity.status(HttpStatus.OK).headers(headers)
                        .body(new APIResponse<List<String>>(dtosRes, null));
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

    @GetMapping("/getStoreOwners/{storeId}")
    public ResponseEntity<APIResponse<List<String>>> getStoreOwners(@PathVariable String storeId) {
        try {
            Response<List<String>> response = serviceLayer.getStoreOwners(storeId);
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
    @GetMapping("/getStoreManagers/{storeId}")
    public ResponseEntity<APIResponse<List<String>>> getStoreManagers(@PathVariable String storeId) {
        try {
            Response<List<String>> response = serviceLayer.getStoreManagers(storeId);
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
            Response<List<ProductDTO>> response = serviceLayer.inStoreProductSearchDTO(userId,productName , categoryStr , keywords,storeId);
            if (response.isSuccess()) {
                List<String> dtos = new ArrayList<>();
                for (ProductDTO productDTO: response.getResult()){
                    dtos.add(objectMapper.writeValueAsString(productDTO));
                }
                HttpHeaders headers = new HttpHeaders();
                headers.add("accept", "*/*");

                return ResponseEntity.status(HttpStatus.OK).headers(headers)
                        .body(new APIResponse<List<String>>(dtos, null));
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new APIResponse<>(null, response.getDescription()));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new APIResponse<>(null, e.getMessage()));

        }
    }

    @GetMapping("/getAllPurchaseRules/{userId}/{storeId}")
    public ResponseEntity<APIResponse<Map<Integer, String>>> getAllPurchaseRules(@PathVariable String userId, @PathVariable String storeId) {
        try {
            Response<Map<Integer, String>> response = serviceLayer.getAllPurchaseRules(userId, storeId);
            if (response.isSuccess()) {
                Map<Integer, String> result = response.getResult();
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

    @PostMapping("/addPurchaseRuleToStore/{ruleNums}/{operators}/{userId}/{storeId}")
    public ResponseEntity<APIResponse<String>> addPurchaseRuleToStore(@PathVariable List<Integer> ruleNums,  @PathVariable List<String> operators,  @PathVariable String userId, @PathVariable String storeId ) {

        Response<String> response = serviceLayer.addPurchaseRuleToStore(ruleNums,operators,userId,storeId);
        return checkIfResponseIsGood(response);
    }


    @DeleteMapping("/removePurchaseRuleFromStore/{ruleNums}/{userId}/{storeId}")
    public ResponseEntity<APIResponse<String>> removePurchaseRuleFromStore(@PathVariable int ruleNum,  @PathVariable String userId, @PathVariable String storeId ) {

        Response<String> response = serviceLayer.removePurchaseRuleFromStore(ruleNum,userId, storeId);
        return checkIfResponseIsGood(response);
    }

    @GetMapping("/getStoreCurrentPurchaseRules/{userId}/{storeId}")
    public ResponseEntity<APIResponse<List<String>>> getStoreCurrentPurchaseRules(@PathVariable String userId, @PathVariable String storeId) {
        try {
            Response<List<String>> response = serviceLayer.getStoreCurrentPurchaseRules(userId, storeId);
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
        }  catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new APIResponse<>(null, e.getMessage()));

        }
    }

    @PostMapping("/getAllCondDiscountRules/{userId}/{storeId}}")
    public ResponseEntity<APIResponse<Map<Integer, String>>> getAllCondDiscountRules(@PathVariable String userId, @PathVariable String storeId)
    {
        try {
            Response<Map<Integer, String>> response = serviceLayer.getAllCondDiscountRules(userId, storeId);
            if (response.isSuccess()) {
                Map<Integer, String> result = response.getResult();
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















