package PresentationLayer.WAF;

import ServiceLayer.Response;
import Util.APIResponse;
import Util.PaymentServiceDTO;
import Util.SupplyServiceDTO;
import Util.UserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/market")
public class MarketController {

    private final MarketService marketService;
    private final Service_layer serviceLayer;

    @Autowired
    public MarketController(MarketService marketService, Service_layer serviceLayer) {
        this.marketService = marketService;
        this.serviceLayer = serviceLayer;
    }





    @GetMapping("/enterSystem")
    public ResponseEntity<APIResponse<String>> enterMarket() {
        try {
            String userId = marketService.enterMarket();
            HttpHeaders headers = new HttpHeaders();
            headers.add("accept" , "*/*");

            return ResponseEntity.status(HttpStatus.OK).headers(headers)
                    .body(new APIResponse<String>(userId, null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new APIResponse<>(null, e.getMessage()));
        }
    }

    @PostMapping("/init/{userDTO}/{password}/{paymentServiceDTO}/{supplyServiceDTO}")
    public ResponseEntity<APIResponse<String>> init(@PathVariable UserDTO userDTO, @PathVariable String password, @PathVariable PaymentServiceDTO paymentServiceDTO, @PathVariable SupplyServiceDTO supplyServiceDTO ) {
        try {
            Response<String> response = serviceLayer.init(userDTO, password, paymentServiceDTO, supplyServiceDTO);
            if (response.isSuccess()){
                String userId = response.getData();
                HttpHeaders headers = new HttpHeaders();
                headers.add("accept" , "*/*");

                return ResponseEntity.status(HttpStatus.OK).headers(headers)
                        .body(new APIResponse<String>(userId, null));
            }
            else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new APIResponse<String>(null, response.getDescription()));
            }

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new APIResponse<>(null, e.getMessage()));
        }
    }

    @PostMapping("/addExternalPaymentService/{paymentServiceDTO}/{managerId}")
    public ResponseEntity<APIResponse<String>> addExternalPaymentService(@PathVariable PaymentServiceDTO paymentServiceDTO,@PathVariable String managerId ) {
        try {
            Response<String> response = serviceLayer.addExternalPaymentService(paymentServiceDTO, managerId);
            if (response.isSuccess()){
                String result = response.getResult();
                HttpHeaders headers = new HttpHeaders();
                headers.add("accept" , "*/*");

                return ResponseEntity.status(HttpStatus.OK).headers(headers)
                        .body(new APIResponse<String>(result, null));
            }
            else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new APIResponse<String>(null, response.getDescription()));
            }

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new APIResponse<>(null, e.getMessage()));
        }
    }

    @DeleteMapping("/removeExternalPaymentService/{licenceNum}/{managerId}")
    public ResponseEntity<APIResponse<String>> removeExternalPaymentService(@PathVariable String licenceNum,@PathVariable String managerId ) {
        try {
            Response<String> response = serviceLayer.removeExternalPaymentService(licenceNum, managerId);
            if (response.isSuccess()){
                String result = response.getResult();
                HttpHeaders headers = new HttpHeaders();
                headers.add("accept" , "*/*");

                return ResponseEntity.status(HttpStatus.OK).headers(headers)
                        .body(new APIResponse<String>(result, null));
            }
            else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new APIResponse<String>(null, response.getDescription()));
            }

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new APIResponse<>(null, e.getMessage()));
        }
    }

    @PostMapping("/addExternalSupplyService/{supplyServiceDTO}/{managerId}")
    public ResponseEntity<APIResponse<String>> addExternalSupplyService(@PathVariable SupplyServiceDTO supplyServiceDTO,@PathVariable String managerId ) {
        try {
            Response<String> response = serviceLayer.addExternalSupplyService(supplyServiceDTO, managerId);
            if (response.isSuccess()){
                String result = response.getResult();
                HttpHeaders headers = new HttpHeaders();
                headers.add("accept" , "*/*");

                return ResponseEntity.status(HttpStatus.OK).headers(headers)
                        .body(new APIResponse<String>(result, null));
            }
            else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new APIResponse<String>(null, response.getDescription()));
            }

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new APIResponse<>(null, e.getMessage()));
        }
    }
//
//    @DeleteMapping("/removeExternalSupplyService/{licenceNum}/{managerId}")
//    public ResponseEntity<APIResponse<String>> removeExternalSupplyService(@PathVariable String licenceNum,@PathVariable String managerId ) {
//        try {
//            Response<String> response = serviceLayer.removeExternalSupplyService(licenceNum, managerId);
//            if (response.isSuccess()){
//                String result = response.getResult();
//                HttpHeaders headers = new HttpHeaders();
//                headers.add("accept" , "*/*");
//
//                return ResponseEntity.status(HttpStatus.OK).headers(headers)
//                        .body(new APIResponse<String>(result, null));
//            }
//            else {
//                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
//                        .body(new APIResponse<String>(null, response.getDescription()));
//            }
//
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
//                    .body(new APIResponse<>(null, e.getMessage()));
//        }*/
//    }




}
