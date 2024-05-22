package DomainLayer.PaymentServices;


import DomainLayer.Role.RoleFacade;

import java.util.HashMap;
import java.util.Map;

public class PaymentServicesFacade {
    private static PaymentServicesFacade paymentServicesFacadeInstance;
   private Map<Integer, ExternalPaymentService>  allPaymentServices = new HashMap<Integer, ExternalPaymentService>();
   private Map<Integer, Receipt> IdAndReceipt = new HashMap<>();


    public static PaymentServicesFacade getInstance() {
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

    public Map<Integer, Integer> getStorePurchaseInfo()
    {
        Map<Integer, Integer> storePurchaseStats = new HashMap<>();

        for (Integer receiptId : IdAndReceipt.keySet()) {
            Receipt receipt = IdAndReceipt.get(receiptId);
            for (Integer store : receipt.getStoreIdAndProductDetails().keySet()) {
                storePurchaseStats.put(store, storePurchaseStats.getOrDefault(store, 0) + 1);
            }
        }

        return storePurchaseStats;
    }

    public Map<Integer, Integer> getStoreReceiptsAndTotalAmount(int storeId)
    {
        Map<Integer, Integer> receiptAndTotalPrice = new HashMap<>();
        for (Integer receiptId : IdAndReceipt.keySet()) {
            Receipt receipt = IdAndReceipt.get(receiptId);
            if (receipt.getStoreIdAndProductDetails().containsKey(storeId))
                receiptAndTotalPrice.put(receiptId, receipt.getTotalPriceOfStoreInReceipt(storeId));
        }
        return receiptAndTotalPrice;
    }
}
