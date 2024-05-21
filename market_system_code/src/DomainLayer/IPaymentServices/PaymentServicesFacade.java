package DomainLayer.IPaymentServices;


import java.util.HashMap;
import java.util.Map;

public class PaymentServicesFacade {
    private static PaymentServicesFacade paymentServicesFacadeInstance;
    Map<Integer, IPaymentServices>  allPaymentServices = new HashMap<Integer, IPaymentServices>();
}
