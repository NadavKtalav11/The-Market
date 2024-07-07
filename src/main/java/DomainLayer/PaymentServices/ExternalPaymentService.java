package DomainLayer.PaymentServices;


// this class is for external payment service itself

import DomainLayer.HttpRequestController;
import Util.ExceptionsEnum;
import Util.PaymentDTO;
import Util.PaymentServiceDTO;
import jakarta.persistence.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Entity
@Table(name = "external_payment_service")
public  class ExternalPaymentService {
    @Id
    @Column(name = "url", nullable = false)
    private String url;
    @Transient
    private HttpRequestController httpReqCtrl;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "external_payment_service_url") // This will create a foreign key column in the Acquisition table
    @MapKeyColumn(name = "acquisition_id") // Column in the Acquisition table for acquisition_id
    private Map<String, Acquisition> idAndAcquisition = new HashMap<>();
    // private HttpClient httpClient=new SimpleHttpClient();
    @Transient
    private final Object acquisitionLock= new Object();

    public ExternalPaymentService() {
        // JPA requires a no-argument constructor
    }

    public ExternalPaymentService(String url) {
        this.url = url;
            try
            {
                httpReqCtrl = new HttpRequestController(url);
            }
            catch (Exception e)
            {
                httpReqCtrl = null;
            }
        }


//    public ExternalPaymentService(PaymentServiceDTO paymentServiceDTO) {
//        this.licensedDealerNumber = paymentServiceDTO.getLicensedDealerNumber();
//        this.paymentServiceName = paymentServiceDTO.getPaymentServiceName();
//        this.url = paymentServiceDTO.getUrl();
//
//        //this.httpClient = httpClient;
//    }

//    public ExternalPaymentService(PaymentServiceDTO paymentServiceDTO, HttpClient httpClient) {
//        this.licensedDealerNumber = paymentServiceDTO.getLicensedDealerNumber();
//        this.paymentServiceName = paymentServiceDTO.getPaymentServiceName();
//        this.url = paymentServiceDTO.getUrl();
//
//        this.httpClient = httpClient;
//    }


//    public String getLicensedDealerNumber(){
//        return licensedDealerNumber;
//    }

//    public String getPaymentServiceName(){
//        return paymentServiceName;
//    }
    public String getUrl(){
        return url;
    }

    // Abstract method for paying with a card
    public int payWithCard(int price, PaymentDTO payment, String id, Map<String, Map<String, List<Integer>>> productList,
                                             String acquisitionIdCounter) throws Exception {
        try {
            if (this.httpReqCtrl == null) {
                return -1;
            }
            this.httpReqCtrl = new HttpRequestController(url);
            if (!this.httpReqCtrl.checkHandShake()) {
                return -1;
            }
            Map<String,String> postContent = new HashMap<>();
            postContent.put("action_type", "pay");
            postContent.put("amount", String.valueOf(price));
            postContent.put("currency", payment.getCurrency());
            postContent.put("card_number", payment.getCreditCardNumber());
            postContent.put("month", String.valueOf(payment.getMonth()));
            postContent.put("year", String.valueOf(payment.getYear()));
            postContent.put("holder", payment.getHolderName());
            postContent.put("cvv", String.valueOf(payment.getCvv()));
            postContent.put("id", payment.getHolderId());
            this.httpReqCtrl = new HttpRequestController(url);
            String response = this.httpReqCtrl.sendRequest(postContent);
            if (response == null) {
                System.out.println("Payment request failed or returned null response.");
                return -1;
            }
            try {
                int transactionId = Integer.parseInt(response);
                return transactionId;
            } catch (NumberFormatException e) {
                System.out.println("Response format error: " + e.getMessage());
                return -1;
            }
        } catch (Exception e) {
            System.out.println("Exception during payment: " + e.getMessage());
            return -1;
        }

    }

    public void addAcquisition(String acquisitionId, Acquisition acquisition){
        synchronized (acquisitionLock) {
            idAndAcquisition.put(acquisitionId, acquisition);
        }
    }



    public int cancelPayment(int transactionID)  throws Exception{

        if(httpReqCtrl == null) //If constructor failed
        {
            throw new Exception("No connection established");
        }

        if(transactionID == 0 || transactionID == -1)
        {
            throw new Exception(ExceptionsEnum.noPayment.toString());
        }

        this.httpReqCtrl = new HttpRequestController(url);
        if(!this.httpReqCtrl.checkHandShake())
        {
            throw new Exception(ExceptionsEnum.checkHandShake.toString());
        }
        Map<String,String> postContent = new HashMap<>();
        postContent.put("action_type", "cancel_pay");
        postContent.put("transaction_id", String.valueOf(transactionID));
        this.httpReqCtrl = new HttpRequestController(url);
        String response = this.httpReqCtrl.sendRequest(postContent);
        if (response == null)
        {
            throw new Exception("Failed to send request to external payment system");
        }
        int cancelRes = Integer.parseInt(response);
        if(cancelRes != 1)
        {
            throw new Exception(ExceptionsEnum.cancelFailed.toString());
        }
        return cancelRes;
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


    public boolean checkHandShake() {
        return this.httpReqCtrl.checkHandShake();
    }
}