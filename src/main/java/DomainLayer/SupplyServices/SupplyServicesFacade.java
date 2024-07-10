package DomainLayer.SupplyServices;


import DomainLayer.PaymentServices.ExternalPaymentService;
import DomainLayer.Repositories.ExternalSupplyMemoryRepository;
import DomainLayer.Repositories.ExternalSupplyRepository;
import DomainLayer.Repositories.MemberMemoryRepository;
import DomainLayer.Repositories.UserMemoryRepository;
import DomainLayer.Role.RoleFacade;
import Util.SupplyServiceDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class SupplyServicesFacade {
    private static SupplyServicesFacade supplyServicesFacade;
    private ExternalSupplyRepository externalSupplyRepository;
    //private Map<String, ExternalSupplyService>  externalSupplyService; moved to memoryRepo
   // private Map<Integer, Receipt> IdAndReceipt = new HashMap<>();

    //db constructor
    @Autowired
    public SupplyServicesFacade(ExternalSupplyRepository externalSupplyRepository){
        this.externalSupplyRepository = externalSupplyRepository;
        //externalSupplyService=  new HashMap<String, ExternalSupplyService>();
        //TEST FOR SAVING
        //externalSupplyRepository.save(new ExternalSupplyService("www.noaaboody.co.il"));
    }

    //memory constructor
    public SupplyServicesFacade()
    {
        externalSupplyRepository = new ExternalSupplyMemoryRepository();
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
        List<ExternalSupplyService> externalSupplyServices = externalSupplyRepository.findAll();
        Map<String, ExternalSupplyService> externalSupplyServicesMap = new HashMap<>();
        for (ExternalSupplyService externalSupplyService : externalSupplyServices) {
            externalSupplyServicesMap.put(externalSupplyService.getSupplyURL(), externalSupplyService);
        }
        return externalSupplyServicesMap;
    }

    public void removeExternalService(String SupplyServiceUrl){
        externalSupplyRepository.deleteById(SupplyServiceUrl);
    }

    public int cancelSupply(String shippingId) throws Exception {
        //Optional<ExternalSupplyService> externalSupplyService1 = externalSupplyRepository.findById("https://damp-lynna-wsep-1984852e.koyeb.app/");
        List<ExternalSupplyService> externalSupplyServiceList = externalSupplyRepository.findAll();
        for (ExternalSupplyService externalSupplyService: externalSupplyServiceList ) {
            if (externalSupplyService.hasShipment(shippingId)) {
                return externalSupplyService.cancelSupply(shippingId);
            }
        }
        return -1;
    }

    public void clearSupplyServices() {
        externalSupplyRepository.deleteAll();
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
        List<ExternalSupplyService> externalSupplyServices = externalSupplyRepository.findAll();
        int size_before = externalSupplyServices.size();
        ExternalSupplyService externalPaymentService = new ExternalSupplyService(supplyURL);
        externalSupplyRepository.save(externalPaymentService);
        externalSupplyServices = externalSupplyRepository.findAll();
        return externalSupplyServices.size() == size_before + 1;
    }


    public String checkAvailableExternalSupplyService(String country, String city) {
        //   (private Map<Integer, ExternalSupplyService>  ExternalSupplyService)
        List<ExternalSupplyService> externalSupplyServices = externalSupplyRepository.findAll();
        if(externalSupplyServices.size()<=0){
            return "-1";
        }
        for (ExternalSupplyService externalSupplyService : externalSupplyServices) {
            if (externalSupplyService.checkAreaAvailability(country, city)) {
                return externalSupplyService.getSupplyURL();
            }
        }
        return "-2";
    }

//    public ExternalSupplyService getExternalSupplyServiceById(String externalSupplyServiceId){
//        synchronized (externalSupplyServiceLock) {
//            return externalSupplyService.get(externalSupplyServiceId);
//        }
//    }

    public ExternalSupplyService getExternalSupplyServiceByURL(String supplyURL){
       Optional<ExternalSupplyService> externalSupplyService1 = externalSupplyRepository.findById(supplyURL);
       return externalSupplyService1.orElse(null);
    }


    public void reset() {
        externalSupplyRepository.deleteAll();
    }


   public boolean createShiftingDetails(String externalSupplyServiceUrl,String userName,String country,String city,String address, String acquisitionId) throws Exception {
        ExternalSupplyService externalSupplyService = getExternalSupplyServiceByURL(externalSupplyServiceUrl);
        int res = externalSupplyService.createSupply(userName,country ,city, address, acquisitionId);
        if(res>= 10000 & res<= 100000){
            return true;
        }
        return false;
        // Check if the product exists in the instance's map and if the amount is sufficient
    }
}
