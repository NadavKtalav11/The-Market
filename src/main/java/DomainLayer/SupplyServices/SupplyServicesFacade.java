package DomainLayer.SupplyServices;


import DomainLayer.PaymentServices.ExternalPaymentService;
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

    public int cancelSupply(int transactionID) throws Exception {
        ExternalSupplyService externalSupplyService1;
        synchronized (externalSupplyServiceLock) {
            externalSupplyService1 = externalSupplyService.values().iterator().next();
        }
        return externalSupplyService1.cancelSupply(transactionID);
    }

//    public boolean addExternalService(String licensedDealerNumber, String supplyServiceName, HashSet<String> countries, HashSet<String> cities){
//        synchronized (externalSupplyServiceLock) {
//            int size_before = externalSupplyService.size();
//            ExternalSupplyService externalPaymentService = new ExternalSupplyService(licensedDealerNumber, supplyServiceName, countries, cities);
//            externalSupplyService.put(licensedDealerNumber, externalPaymentService);
//            return externalSupplyService.size() == size_before + 1;
//        }
//    }

    public boolean addExternalService(String supplyURL){
        synchronized (externalSupplyServiceLock) {
            int size_before = externalSupplyService.size();
            ExternalSupplyService externalPaymentService = new ExternalSupplyService(supplyURL);
            externalSupplyService.put(supplyURL, externalPaymentService);
            return externalSupplyService.size() == size_before + 1;
        }
    }


    public String checkAvailableExternalSupplyService(String country, String city) {
        //   (private Map<Integer, ExternalSupplyService>  ExternalSupplyService)
        if(externalSupplyService.size()<=0){
            return "-1";
        }
        synchronized (externalSupplyServiceLock) {
            for (Map.Entry<String, ExternalSupplyService> entry : externalSupplyService.entrySet()) {
                ExternalSupplyService externalSupplyService1 = entry.getValue();
                if (externalSupplyService1.checkAreaAvailability(country, city)) {
                    return externalSupplyService1.getSupplyURL();
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
    public ExternalSupplyService getExternalSupplyServiceByURL(String supplyURL){
        synchronized (externalSupplyServiceLock) {
            return externalSupplyService.get(supplyURL);
        }
    }


    public void reset() {
        synchronized (externalSupplyServiceLock) {
            externalSupplyService.clear();
        }
    }


   public boolean createShiftingDetails(String externalSupplyServiceId,String userName,String country,String city,String address) throws Exception {
        ExternalSupplyService externalSupplyService = getExternalSupplyServiceById(externalSupplyServiceId);
        int res = externalSupplyService.createSupply(userName,country ,city, address);
        if(res>= 10000 & res<= 100000){
            return true;
        }
        return false;
        // Check if the product exists in the instance's map and if the amount is sufficient
    }
}
