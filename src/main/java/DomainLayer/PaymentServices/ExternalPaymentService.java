package DomainLayer.PaymentServices;


// this class is for external payment service itself

import Util.PaymentDTO;

import java.util.HashMap;
import java.util.Map;

public  class ExternalPaymentService {
    private int licensedDealerNumber;
    private String paymentServiceName;
    private String url;
    private Map<String, Acquisition> idAndAcquisition = new HashMap<>();
    private HttpClient httpClient;
    private final Object acquisitionLock;

    public ExternalPaymentService(int licensedDealerNumber, String paymentServiceName, String url) {
        this.licensedDealerNumber = licensedDealerNumber;
        this.paymentServiceName = paymentServiceName;
        this.url = url;

        this.httpClient = httpClient;

    }

    // Abstract method for paying with a card
    public Map<Integer, Integer> payWithCard(int price, PaymentDTO payment, int id, Map<Integer, Map<String, Integer>> productList,
                                             int acquisitionIdCounter, int receiptIdCounter) throws Exception {
        // Mocking HTTP request to check if there is enough money in the card
        boolean response = httpClient.get(url + "?creditCard=" + payment.getCreditCardNumber() + "&amount=" + price);
        if(!response){
            throw new Exception("There is not enough money in the credit card");
        }
        acquisitionLock = new Object();
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