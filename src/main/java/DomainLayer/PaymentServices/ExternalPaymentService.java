package DomainLayer.PaymentServices;


// this class is for external payment service itself

import Util.PaymentDTO;

import java.util.HashMap;
import java.util.Map;

public  class ExternalPaymentService {
    private String licensedDealerNumber;
    private String paymentServiceName;
    private String url;
    private Map<String, Acquisition> idAndAcquisition = new HashMap<>();
    private HttpClient httpClient;
    private final Object acquisitionLock= new Object();

    public ExternalPaymentService(String licensedDealerNumber, String paymentServiceName, String url) {
        this.licensedDealerNumber = licensedDealerNumber;
        this.paymentServiceName = paymentServiceName;
        this.url = url;

        this.httpClient = httpClient;

    }

    public String getLicensedDealerNumber(){
        return licensedDealerNumber;
    }

    public String getPaymentServiceName(){
        return paymentServiceName;
    }
    public String getUrl(){
        return url;
    }

    // Abstract method for paying with a card
    public Map<String, String> payWithCard(int price, PaymentDTO payment, String id, Map<String, Map<String, Integer>> productList,
                                             String acquisitionIdCounter, String receiptIdCounter) throws Exception {
        // Mocking HTTP request to check if there is enough money in the card
        boolean response = httpClient.get(url + "?creditCard=" + payment.getCreditCardNumber() + "&amount=" + price);
        if(!response){
            throw new Exception("There is not enough money in the credit card");
        }
      Acquisition acquisition = new Acquisition(acquisitionIdCounter, id, price, payment, productList, receiptIdCounter);
        synchronized (acquisitionLock) {
            idAndAcquisition.put(acquisitionIdCounter, acquisition);
        }
        return acquisition.getReceiptIdAndStoreIdMap();
      
    }

   

    // Abstract method for refunding to a card
    public boolean refundToCard() {
        return true;
    }

    // Abstract method for checking service availability
    public boolean checkServiceAvailability() {
        return true;
    }

    public Map<String, Acquisition> getIdAndAcquisition() {
        synchronized (acquisitionLock) {
            return idAndAcquisition;
        }
    }
}