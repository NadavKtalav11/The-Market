package DomainLayer.PaymentServices;


import Util.PaymentDTO;
import Util.PaymentServiceDTO;
import Util.UserDTO;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PaymentServicesFacade {
    private static PaymentServicesFacade paymentServicesFacadeInstance;
    private Map<String, ExternalPaymentService>  allPaymentServices = new HashMap<String, ExternalPaymentService>();
    private Map<String, Acquisition> IdAndAcquisition = new HashMap<>();

    private final Object paymentServiceLock;
    private final Object acquisitionLock;

    public PaymentServicesFacade(){
        paymentServiceLock =new Object();
        acquisitionLock = new Object();
    }



    //private int acquisitionIdCounter = 1;
    //private int receiptIdCounter = 1;



    public synchronized static PaymentServicesFacade getInstance() {
        if (paymentServicesFacadeInstance == null) {
            paymentServicesFacadeInstance = new PaymentServicesFacade();
        }
        return paymentServicesFacadeInstance;
    }

    public PaymentServicesFacade newForTest(){
        paymentServicesFacadeInstance= new PaymentServicesFacade();
        return paymentServicesFacadeInstance;
    }

    public void removeExternalService(String paymentId){
        synchronized (paymentServiceLock) {
            allPaymentServices.remove(paymentId);
        }
    }

    public boolean addExternalService(String licensedDealerNumber, String paymentServiceName, String url){
        synchronized (paymentServiceLock) {
            int size_before = allPaymentServices.size();
            ExternalPaymentService externalPaymentService = new ExternalPaymentService(licensedDealerNumber, paymentServiceName, url);
            allPaymentServices.put(licensedDealerNumber, externalPaymentService);
            return allPaymentServices.size() == size_before + 1;
        }
    }
    public void clearPaymentServices() {
        synchronized (paymentServiceLock) {
            allPaymentServices.clear();
        }
    }

    public Map<String,String> pay(int price, PaymentDTO payment, String userId, Map<String, Map<String, Integer>> productList) throws Exception{
       
        String acquisitionId  = getNewAcquisitionId();
        String receiptId = getNewReceiptId();
        ExternalPaymentService externalPaymentService;
        synchronized (paymentServiceLock) {
            externalPaymentService = allPaymentServices.values().iterator().next();
        }
        Map<String,String> paymentSucceeded = externalPaymentService.payWithCard(price, payment, userId, productList, acquisitionId, receiptId);
        if (paymentSucceeded!=null)
        {
            //String acquisitionId1  = getNewAcquisitionId();
            Acquisition acquisition = new Acquisition(acquisitionId, userId, price, payment, productList, getNewReceiptId());
            synchronized (acquisitionLock) {
                IdAndAcquisition.put(acquisitionId, acquisition);
            }
            //acquisitionIdCounter++;
            //receiptIdCounter += productList.size();
            return acquisition.getReceiptIdAndStoreIdMap();
        }
        else
        {
            throw new IllegalArgumentException("Payment failed");
        }
    }

    public String getNewAcquisitionId(){
        UUID uuid = UUID.randomUUID();
        String id = "acquisition-"+uuid.toString() ;
        return id;
    }

    public String getNewReceiptId(){
        UUID uuid = UUID.randomUUID();
        String id = "receipt-"+uuid.toString() ;
        return id;
    }

    public Map<String, ExternalPaymentService> getAllPaymentServices(){
        synchronized (allPaymentServices) {
            return this.allPaymentServices;
        }
    }

    //public int getAcquisitionIdCounter(){
       // return this.acquisitionIdCounter;
    //}

    //public int getReceiptIdCounter(){
    //    return this.receiptIdCounter;
    //}

    public PaymentServiceDTO getPaymentServiceDTOById(String paymentServiceId){
        return new PaymentServiceDTO(getPaymentServiceById(paymentServiceId));
    }

    public ExternalPaymentService getPaymentServiceById(String paymentServiceId){
        if(allPaymentServices.containsKey(paymentServiceId)){
            return allPaymentServices.get(paymentServiceId);
        }
        else {
            return null;
        }
    }

    public Map<String, Integer> getStorePurchaseInfo()
    {
        Map<String, Integer> storePurchaseStats = new HashMap<>();
        synchronized (acquisitionLock) {
            for (String acqId : IdAndAcquisition.keySet()) {
                Map<String, Receipt> acqReceipts = IdAndAcquisition.get(acqId).getStoreIdAndReceipt();
                for (String receiptId : acqReceipts.keySet()) {
                    String storeId = acqReceipts.get(receiptId).getStoreId();
                    storePurchaseStats.put(storeId, storePurchaseStats.getOrDefault(storeId, 0) + 1);
                }
            }
        }
        return storePurchaseStats;
    }


    public Map<String, Integer> getStoreReceiptsAndTotalAmount(String storeId)
    {
        Map<String, Integer> receiptAndTotalPrice = new HashMap<>();
        synchronized (acquisitionLock) {
            for (String acqId : IdAndAcquisition.keySet()) {
                Acquisition acq = IdAndAcquisition.get(acqId);
                if (acq.getStoreIdAndReceipt().containsKey(storeId)) {
                    receiptAndTotalPrice.put(acq.getReceiptIdByStoreId(storeId), acq.getTotalPriceOfStoreInAcquisition(storeId));
                }
            }
        }

        return receiptAndTotalPrice;
    }

    public Map<String, Acquisition> getIdAndAcquisition() {
        synchronized (acquisitionLock) {
            return IdAndAcquisition;
        }
    }
}
