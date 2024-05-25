package DomainLayer.PaymentServices;


// this class is for external payment service itself

import java.util.HashMap;
import java.util.Map;

public  class ExternalPaymentService {
    private int licensedDealerNumber;
    private String paymentServiceName;
    private String url;
    private Map<Integer, Acquisition> idAndAcquisition = new HashMap<>();

    public ExternalPaymentService(int licensedDealerNumber, String paymentServiceName,String url){
        this.licensedDealerNumber=licensedDealerNumber;
        this.paymentServiceName= paymentServiceName;
        this.url= url;
    }

    // Abstract method for paying with a card
    public Map<Integer,Integer> payWithCard(int price, int creditCard, int cvv, int month, int year, String holder, int id, Map<Integer, Map<String, Integer>> productList,
                                            int acquisitionIdCounter, int receiptIdCounter){
        Acquisition acquisition = new Acquisition(acquisitionIdCounter, id, price, holder, creditCard, cvv, month, year, productList, receiptIdCounter);
        idAndAcquisition.put(acquisitionIdCounter, acquisition);
        return acquisition.getStoreIdAndReceiptIdMap();
    }

    // Abstract method for refunding to a card
    public  boolean refundToCard(){
     return  true;
    }

    // Abstract method for checking service availability
    public  boolean checkServiceAvailability(){
        return true;
    }

    public Map<Integer, Acquisition> getIdAndAcquisition()
    {
        return idAndAcquisition;
    }
}
