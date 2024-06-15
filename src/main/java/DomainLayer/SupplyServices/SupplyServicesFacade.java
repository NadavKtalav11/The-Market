package DomainLayer.SupplyServices;


import DomainLayer.Role.RoleFacade;
import Util.SupplyServiceDTO;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class SupplyServicesFacade {
    private static SupplyServicesFacade supplyServicesFacade;
    private Map<String, ExternalSupplyService>  externalSupplyService;
   // private Map<Integer, Receipt> IdAndReceipt = new HashMap<>();
    private final Object externalSupplyServiceLock;

    private SupplyServicesFacade(){
        externalSupplyServiceLock = new Object();
        externalSupplyService=  new HashMap<String, ExternalSupplyService>();
    }

    public void clearPaymentServices() {
        synchronized (externalSupplyServiceLock) {
            externalSupplyService.clear();
        }
    }


    public synchronized static SupplyServicesFacade getInstance() {
        if (supplyServicesFacade == null) {
            supplyServicesFacade = new SupplyServicesFacade();
        }
        return supplyServicesFacade;
    }

    public SupplyServicesFacade newForTest(){
        supplyServicesFacade= new SupplyServicesFacade();
        return supplyServicesFacade;
    }

    public Map<String, ExternalSupplyService>  getAllSupplyServices(){
        return this.externalSupplyService;
    }

    public void removeExternalService(String licensedDealerNumber){
        synchronized (externalSupplyServiceLock) {
            externalSupplyService.remove(licensedDealerNumber);
        }
    }

    public boolean addExternalService(String licensedDealerNumber, String supplyServiceName, HashSet<String> countries, HashSet<String> cities){
        synchronized (externalSupplyServiceLock) {
            int size_before = externalSupplyService.size();
            ExternalSupplyService externalPaymentService = new ExternalSupplyService(licensedDealerNumber, supplyServiceName, countries, cities);
            externalSupplyService.put(licensedDealerNumber, externalPaymentService);
            return externalSupplyService.size() == size_before + 1;
        }
    }

    public boolean addExternalService(SupplyServiceDTO supplyServiceDTO){
        synchronized (externalSupplyServiceLock) {
            int size_before = externalSupplyService.size();
            ExternalSupplyService externalPaymentService = new ExternalSupplyService(supplyServiceDTO);
            externalSupplyService.put(supplyServiceDTO.getLicensedDealerNumber(), externalPaymentService);
            return externalSupplyService.size() == size_before + 1;
        }
    }


    public String checkAvailableExternalSupplyService(String country, String city) {
        //   (private Map<Integer, ExternalSupplyService>  ExternalSupplyService)
        synchronized (externalSupplyServiceLock) {
            if(externalSupplyService.size()<=0){
                return "-1";
            }
            for (Map.Entry<String, ExternalSupplyService> entry : externalSupplyService.entrySet()) {
                ExternalSupplyService externalSupplyService1 = entry.getValue();
                if (externalSupplyService1.checkAreaAvailability(country, city)) {
                    return externalSupplyService1.getLicensedDealerNumber();
                    }
            }
        }
        return "-2";
    }

    public ExternalSupplyService getExternalSupplyServiceById(String externalSupplyServiceId){
        synchronized (externalSupplyServiceLock) {
            return externalSupplyService.get(externalSupplyServiceId);
        }
    }
    public void reset() {
        synchronized (externalSupplyServiceLock) {
            externalSupplyService.clear();
        }
    }


   public boolean createShiftingDetails(String externalSupplyServiceId,String userName,String country,String city,String address){
        ExternalSupplyService externalSupplyService = getExternalSupplyServiceById(externalSupplyServiceId);
        return externalSupplyService.createShiftingDetails(userName,country ,city, address);
        // Check if the product exists in the instance's map and if the amount is sufficient
    }
}
