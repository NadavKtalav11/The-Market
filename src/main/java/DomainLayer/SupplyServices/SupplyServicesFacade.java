package DomainLayer.SupplyServices;


import DomainLayer.Role.RoleFacade;

import java.util.HashMap;
import java.util.Map;

public class SupplyServicesFacade {
    private static SupplyServicesFacade supplyServicesFacade;
    private Map<Integer, ExternalSupplyService>  externalSupplyService;
   // private Map<Integer, Receipt> IdAndReceipt = new HashMap<>();
    private Object externalSupplyServiceLock;

    private SupplyServicesFacade(){
        externalSupplyServiceLock = new Object();
        externalSupplyService=  new HashMap<Integer, ExternalSupplyService>();
    }


    public static SupplyServicesFacade getInstance() {
        if (supplyServicesFacade == null) {
            supplyServicesFacade = new SupplyServicesFacade();
        }
        return supplyServicesFacade;
    }

    public boolean addExternalService(int licensedDealerNumber, String supplyServiceName, String address){
        synchronized (externalSupplyService) {
            int size_before = externalSupplyService.size();
            ExternalSupplyService externalPaymentService = new ExternalSupplyService(licensedDealerNumber, supplyServiceName, address);
            externalSupplyService.put(licensedDealerNumber, externalPaymentService);
            return externalSupplyService.size() == size_before + 1;
        }
    }

    public boolean checkAvailableExternalSupplyService(String userAddress,
                                                       HashMap<Integer,Integer> ProductIdAndAmount) {
        //   (private Map<Integer, ExternalSupplyService>  ExternalSupplyService)
        synchronized (externalSupplyService) {
            for (Map.Entry<Integer, ExternalSupplyService> entry : externalSupplyService.entrySet()) {
                Integer externalSupplyServiceId = entry.getKey();
                ExternalSupplyService externalSupplyService1 = entry.getValue();
                if (externalSupplyService1.checkAreaAvailability(userAddress)) {
                    if (externalSupplyService1.checkServiceAvailability(ProductIdAndAmount)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

        // Check if the product exists in the instance's map and if the amount is sufficient
}
