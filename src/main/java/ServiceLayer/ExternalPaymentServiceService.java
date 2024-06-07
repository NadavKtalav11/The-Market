package ServiceLayer;

import DomainLayer.PaymentServices.PaymentServicesFacade;
import Util.APIResponse;
import Util.PaymentServiceDTO;
import Util.UserDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;


public class ExternalPaymentServiceService {
    private PaymentServicesFacade paymentServicesFacade;

    public ExternalPaymentServiceService() {
        paymentServicesFacade = new PaymentServicesFacade();
    }

    public PaymentServiceDTO getPaymentServiceDTOById(String paymentServiceId){
        return paymentServicesFacade.getPaymentServiceDTOById(paymentServiceId);
    }




}
