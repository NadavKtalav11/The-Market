package DomainLayer.PaymentServices;


import DomainLayer.Role.RoleFacade;

import java.util.HashMap;
import java.util.Map;

public class PaymentServicesFacade {
    private static PaymentServicesFacade paymentServicesFacadeInstance;
   private Map<Integer, ExternalPaymentService>  allPaymentServices = new HashMap<Integer, ExternalPaymentService>();
   private Map<Integer, Receipt> IdAndReceipt = new HashMap<>();


    public static PaymentServicesFacade getInstance() {
        if (paymentServicesFacadeInstance == null) {
            paymentServicesFacadeInstance = new PaymentServicesFacade();
        }
        return paymentServicesFacadeInstance;
    }

    public boolean addExternalService(int licensedDealerNumber, String paymentServiceName, String url){
            int size_before= allPaymentServices.size();
            ExternalPaymentService externalPaymentService = new ExternalPaymentService(licensedDealerNumber,paymentServiceName, url);
            allPaymentServices.put(licensedDealerNumber, externalPaymentService);
            return allPaymentServices.size()==size_before+1;
    }
}
