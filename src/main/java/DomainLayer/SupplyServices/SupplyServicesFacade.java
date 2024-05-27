package DomainLayer.SupplyServices;


import DomainLayer.Role.RoleFacade;

import java.util.HashMap;
import java.util.HashSet;
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


    public synchronized static SupplyServicesFacade getInstance() {
        if (supplyServicesFacade == null) {
            supplyServicesFacade = new SupplyServicesFacade();
        }
        return supplyServicesFacade;
    }

    public Map<Integer, ExternalSupplyService>  getAllSupplyServices(){
        return this.externalSupplyService;
    }

    public void removeExternalService(int licensedDealerNumber){
        externalSupplyService.remove(licensedDealerNumber);
    }

    public boolean addExternalService(int licensedDealerNumber, String supplyServiceName, HashSet<String> countries, HashSet<String> cities){
        synchronized (externalSupplyService) {
            int size_before = externalSupplyService.size();
            ExternalSupplyService externalPaymentService = new ExternalSupplyService(licensedDealerNumber, supplyServiceName, countries, cities);
            externalSupplyService.put(licensedDealerNumber, externalPaymentService);
            return externalSupplyService.size() == size_before + 1;
        }
    }

    public int checkAvailableExternalSupplyService(String country, String city) {
        //   (private Map<Integer, ExternalSupplyService>  ExternalSupplyService)
        synchronized (externalSupplyService) {
            for (Map.Entry<Integer, ExternalSupplyService> entry : externalSupplyService.entrySet()) {
                ExternalSupplyService externalSupplyService1 = entry.getValue();
                if (externalSupplyService1.checkAreaAvailability(country, city)) {
                    return externalSupplyService1.getLicensedDealerNumber();
                    }
            }
        }
        return -1;
    }

    public ExternalSupplyService getExternalSupplyServiceById(int externalSupplyServiceId){
        return externalSupplyService.get(externalSupplyServiceId);
    }



   public boolean createShiftingDetails(int externalSupplyServiceId,String userName,String country,String city,String address){
        ExternalSupplyService externalSupplyService = getExternalSupplyServiceById(externalSupplyServiceId);
        return externalSupplyService.createShiftingDetails(userName,country ,city, address);
        // Check if the product exists in the instance's map and if the amount is sufficient
    }
}
