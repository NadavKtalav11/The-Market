package DomainLayer.PaymentServices;


// this class is for external payment service itself

import Util.ExceptionsEnum;
import Util.PaymentDTO;
import Util.PaymentServiceDTO;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public  class ExternalPaymentService {
    private String licensedDealerNumber;
    private String paymentServiceName;
    private String url;
    private Map<String, Acquisition> idAndAcquisition = new HashMap<>();
    private HttpClient httpClient=new SimpleHttpClient();
    private final Object acquisitionLock= new Object();

    public ExternalPaymentService(String licensedDealerNumber, String paymentServiceName, String url) {
        this.licensedDealerNumber = licensedDealerNumber;
        this.paymentServiceName = paymentServiceName;
        this.url = url;


    }


    public ExternalPaymentService(PaymentServiceDTO paymentServiceDTO) {
        this.licensedDealerNumber = paymentServiceDTO.getLicensedDealerNumber();
        this.paymentServiceName = paymentServiceDTO.getPaymentServiceName();
        this.url = paymentServiceDTO.getUrl();

        //this.httpClient = httpClient;
    }

    public ExternalPaymentService(PaymentServiceDTO paymentServiceDTO, HttpClient httpClient) {
        this.licensedDealerNumber = paymentServiceDTO.getLicensedDealerNumber();
        this.paymentServiceName = paymentServiceDTO.getPaymentServiceName();
        this.url = paymentServiceDTO.getUrl();

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
    public void payWithCard(int price, PaymentDTO payment, String id, Map<String, Map<String, List<Integer>>> productList,
                                             String acquisitionIdCounter) throws Exception {
        // Mocking HTTP request to check if there is enough money in the card
        boolean response = httpClient.checkCreditCard( url, payment );
        if(!response){
            throw new Exception(ExceptionsEnum.CreditCardIssue.toString());
        }
    }

    public void addAcquisition(String acquisitionId, Acquisition acquisition){
        synchronized (acquisitionLock) {
            idAndAcquisition.put(acquisitionId, acquisition);
        }
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