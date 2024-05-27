package DomainLayer.PaymentServices;


import java.util.HashMap;
import java.util.Map;

public class PaymentServicesFacade {
    private static PaymentServicesFacade paymentServicesFacadeInstance;
    private Map<Integer, ExternalPaymentService>  allPaymentServices = new HashMap<Integer, ExternalPaymentService>();
    private Map<Integer, Acquisition> IdAndAcquisition = new HashMap<>();
    private int acquisitionIdCounter = 1;
    private int receiptIdCounter = 1;



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

    public void removeExternalService(int paymentId){
        allPaymentServices.remove(paymentId);
    }

    public boolean addExternalService(int licensedDealerNumber, String paymentServiceName, String url){
            int size_before= allPaymentServices.size();
            ExternalPaymentService externalPaymentService = new ExternalPaymentService(licensedDealerNumber,paymentServiceName, url);
            allPaymentServices.put(licensedDealerNumber, externalPaymentService);
            return allPaymentServices.size()==size_before+1;
    }
    public void clearPaymentServices() {
        allPaymentServices.clear();
    }

    public Map<Integer,Integer> pay(int price,String creditCard, int cvv, int month, int year, String holderID, int userId, Map<Integer, Map<String, Integer>> productList){
        ExternalPaymentService externalPaymentService = allPaymentServices.values().iterator().next();
        Map<Integer,Integer> paymentSucceeded = externalPaymentService.payWithCard(price, creditCard, cvv, month, year, holderID, userId, productList, acquisitionIdCounter, receiptIdCounter);
        if (paymentSucceeded!=null)
        {
            Acquisition acquisition = new Acquisition(acquisitionIdCounter, userId, price, holderID, creditCard, cvv, month, year, productList, receiptIdCounter);
            IdAndAcquisition.put(acquisitionIdCounter, acquisition);
            acquisitionIdCounter++;
            receiptIdCounter += productList.size();
            return acquisition.getReceiptIdAndStoreIdMap();
        }
        else
        {
            throw new IllegalArgumentException("Payment failed");
        }
    }

    public Map<Integer, ExternalPaymentService> getAllPaymentServices(){
        return this.allPaymentServices;
    }

    public int getAcquisitionIdCounter(){
        return this.acquisitionIdCounter;
    }

    public int getReceiptIdCounter(){
        return this.receiptIdCounter;
    }



    public Map<Integer, Integer> getStorePurchaseInfo()
    {
        Map<Integer, Integer> storePurchaseStats = new HashMap<>();
        for (Integer acqId : IdAndAcquisition.keySet()) {
            Map<Integer, Receipt> acqReceipts = IdAndAcquisition.get(acqId).getStoreIdAndReceipt();
            for (Integer receiptId : acqReceipts.keySet()) {
                int storeId = acqReceipts.get(receiptId).getStoreId();
                storePurchaseStats.put(storeId, storePurchaseStats.getOrDefault(storeId, 0) + 1);
            }
        }
        return storePurchaseStats;
    }


    public Map<Integer, Integer> getStoreReceiptsAndTotalAmount(int storeId)
    {
        Map<Integer, Integer> receiptAndTotalPrice = new HashMap<>();

        for (Integer acqId : IdAndAcquisition.keySet()) {
            Acquisition acq = IdAndAcquisition.get(acqId);
            if (acq.getStoreIdAndReceipt().containsKey(storeId))
            {
                receiptAndTotalPrice.put(acq.getReceiptIdByStoreId(storeId), acq.getTotalPriceOfStoreInAcquisition(storeId));
            }
        }

        return receiptAndTotalPrice;
    }

    public Map<Integer, Acquisition> getIdAndAcquisition() {
        return IdAndAcquisition;
    }
}
