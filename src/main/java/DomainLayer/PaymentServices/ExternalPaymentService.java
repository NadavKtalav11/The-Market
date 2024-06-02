package DomainLayer.PaymentServices;


// this class is for external payment service itself

import java.util.HashMap;
import java.util.Map;

public  class ExternalPaymentService {
    private int licensedDealerNumber;
    private String paymentServiceName;
    private String url;
    private Map<String, Acquisition> idAndAcquisition = new HashMap<>();
    private final Object acquisitionLock = new Object();

    public ExternalPaymentService(int licensedDealerNumber, String paymentServiceName, String url) {
        this.licensedDealerNumber = licensedDealerNumber;
        this.paymentServiceName = paymentServiceName;
        this.url = url;



    }

    // Abstract method for paying with a card
    public Map<String, String> payWithCard(int price, String creditCard, int cvv, int month, int year, String holder, String id, Map<String, Map<String, Integer>> productList,
                                             String acquisitionIdCounter, String receiptIdCounter) throws Exception {
        // Mocking HTTP request to check if there is enough money in the card
        boolean response = mockHttpRequest(url, creditCard, price);
        if(!response){
            throw new Exception("There is not enough money in the credit card");
        }
      Acquisition acquisition = new Acquisition(acquisitionIdCounter, id, price, holder, creditCard, cvv, month, year, productList, receiptIdCounter);
        synchronized (acquisitionLock) {
            idAndAcquisition.put(acquisitionIdCounter, acquisition);
        }
        return acquisition.getReceiptIdAndStoreIdMap();
      
    }
    // Method to mock the HTTP request
    public static boolean mockHttpRequest(String url, String creditCard, int amount) {

        return true;
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