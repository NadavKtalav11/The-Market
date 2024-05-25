package DomainLayer.PaymentServices;


// this class is for external payment service itself

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public  class ExternalPaymentService {
    private int licensedDealerNumber;
    private String paymentServiceName;
    private String url;
    private int receiptId= 1;
    private final HashMap<Integer, Receipt> recpeiptId = new HashMap<>();

    public ExternalPaymentService(int licensedDealerNumber, String paymentServiceName,String url){
        this.licensedDealerNumber=licensedDealerNumber;
        this.paymentServiceName= paymentServiceName;
        this.url= url;
    }

    // Abstract method for paying with a card
    public Map<Integer,Integer> payWithCard(int price, int creditCard, int cvv, int month, int year, String holder, int id, Map<Integer, Map<String, Integer>> productList ){
       Receipt receipt = new Receipt(receiptId, id, price, holder, creditCard, cvv, month, year, productList);
        recpeiptId.put(receiptId,receipt);
        receiptId++;
        Map<Integer,Integer> receiptAndExternalService = new HashMap<>();
        receiptAndExternalService.put(receiptId, licensedDealerNumber);
        return receiptAndExternalService;
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
