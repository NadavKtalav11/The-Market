package PresentationLayer.WAF;
import ServiceLayer.ExternalPaymentServiceService;
import Util.APIResponse;
import Util.PaymentServiceDTO;
import Util.UserDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
/*
@RestController
@RequestMapping("/externalPaymentService")
public class ExternalPaymentServiceController {

    private final ExternalPaymentServiceService externalPaymentServiceService;

    public  ExternalPaymentServiceController(ExternalPaymentServiceService ExternalPaymentServiceService) {
        this.externalPaymentServiceService = ExternalPaymentServiceService;
    }


    @GetMapping("/{id}")
    public ResponseEntity<APIResponse<PaymentServiceDTO>> getExternalPaymentServiceById(@PathVariable String id) {
        try {
            PaymentServiceDTO paymentServiceDTO = externalPaymentServiceService.getPaymentServiceDTOById(id);
            return ResponseEntity.ok(new APIResponse<>(paymentServiceDTO, null));
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new APIResponse<>(null, e.getMessage()));
        }

    }

}*/
