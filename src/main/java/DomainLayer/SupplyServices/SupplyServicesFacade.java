package DomainLayer.SupplyServices;


import DomainLayer.Role.RoleFacade;

import java.util.HashMap;
import java.util.Map;

public class SupplyServicesFacade {
    private static SupplyServicesFacade supplyServicesFacade;
    private Map<Integer, ExternalSupplyService>  ExternalSupplyService = new HashMap<Integer, ExternalSupplyService>();
   // private Map<Integer, Receipt> IdAndReceipt = new HashMap<>();


    public static SupplyServicesFacade getInstance() {
        if (supplyServicesFacade == null) {
            supplyServicesFacade = new SupplyServicesFacade();
        }
        return supplyServicesFacade;
    }

    public boolean addExternalService(int licensedDealerNumber, String supplyServiceName, String address){
        int size_before= ExternalSupplyService.size();
        ExternalSupplyService externalPaymentService = new ExternalSupplyService(licensedDealerNumber,supplyServiceName, address);
        ExternalSupplyService.put(licensedDealerNumber, externalPaymentService);
        return ExternalSupplyService.size()==size_before+1;
    }
}
