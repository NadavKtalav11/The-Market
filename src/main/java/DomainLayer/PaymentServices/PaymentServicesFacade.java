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

    public boolean addExternalService(int licensedDealerNumber, String paymentServiceName, String url){
            int size_before= allPaymentServices.size();
            ExternalPaymentService externalPaymentService = new ExternalPaymentService(licensedDealerNumber,paymentServiceName, url);
            allPaymentServices.put(licensedDealerNumber, externalPaymentService);
            return allPaymentServices.size()==size_before+1;
    }

    public Map<Integer,Integer> pay(int price,int creditCard, int cvv, int month, int year, String holderID, int userId, Map<Integer, Map<String, Integer>> productList){
        ExternalPaymentService externalPaymentService = allPaymentServices.values().iterator().next();
        Map<Integer,Integer> acquisitionAndExternalService = externalPaymentService.payWithCard(price, creditCard, cvv, month, year, holderID, userId, productList, acquisitionIdCounter, receiptIdCounter);
        acquisitionIdCounter++;
        receiptIdCounter += productList.size();
        return acquisitionAndExternalService;
    }



    //todo:Change the function after we move the acquisition map from ExternalPaymentService to here
    public Map<Integer, Integer> getStorePurchaseInfo()
    {
        Map<Integer, Integer> storePurchaseStats = new HashMap<>();

        for (Integer external : allPaymentServices.keySet()) {
            Map<Integer, Acquisition> paymentServiceAcquisitions = allPaymentServices.get(external).getIdAndAcquisition();
            for (Integer acqId : paymentServiceAcquisitions.keySet()) {
                Map<Integer, Receipt> acqReceipts = paymentServiceAcquisitions.get(acqId).getStoreIdAndReceipt();
                for (Integer receiptId : acqReceipts.keySet()) {
                    int storeId = acqReceipts.get(receiptId).getStoreId();
                    storePurchaseStats.put(storeId, storePurchaseStats.getOrDefault(storeId, 0) + 1);
                }
            }

        }

        return storePurchaseStats;
    }

    //todo:Change the function after we move the acquisition map from ExternalPaymentService to here
    public Map<Integer, Integer> getStoreReceiptsAndTotalAmount(int storeId)
    {
        Map<Integer, Integer> receiptAndTotalPrice = new HashMap<>();

        for (Integer external : allPaymentServices.keySet()) {
            Map<Integer, Acquisition> paymentServiceAcquisitions = allPaymentServices.get(external).getIdAndAcquisition();
            for (Integer acqId : paymentServiceAcquisitions.keySet()) {
                Acquisition acq = paymentServiceAcquisitions.get(acqId);
                if (acq.getStoreIdAndReceipt().containsKey(storeId))
                {
                    receiptAndTotalPrice.put(acq.getReceiptIdByStoreId(storeId), acq.getTotalPriceOfStoreInAcquisition(storeId));
                }
            }

        }

        return receiptAndTotalPrice;
    }
}
