package DomainLayer.PaymentServices;


// this class is for external payment service itself

import java.util.HashMap;
import java.util.Map;

public  class ExternalPaymentService {
    private int licensedDealerNumber;
    private String paymentServiceName;
    private String url;

    public ExternalPaymentService(int licensedDealerNumber, String paymentServiceName,String url){
        this.licensedDealerNumber=licensedDealerNumber;
        this.paymentServiceName= paymentServiceName;
        this.url= url;
    }

    // Abstract method for paying with a card
    public boolean payWithCard(int price, int creditCard, int cvv, int month, int year, String holder, int id, Map<Integer, Map<String, Integer>> productList,
                                            int acquisitionIdCounter, int receiptIdCounter){
        //make payment, return true for now
        return true;
    }

    // Abstract method for refunding to a card
    public  boolean refundToCard(){
     return  true;
    }

    // Abstract method for checking service availability
    public  boolean checkServiceAvailability(){
        return true;
    }
}
