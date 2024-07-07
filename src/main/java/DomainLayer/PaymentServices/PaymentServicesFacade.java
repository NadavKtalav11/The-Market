package DomainLayer.PaymentServices;


import DomainLayer.Repositories.AcquisitionMemoryRepository;
import DomainLayer.Repositories.AcquisitionRepository;
import DomainLayer.Repositories.ExternalPaymentMemoryRepository;
import DomainLayer.Repositories.ExternalPaymentRepository;
import Util.*;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class PaymentServicesFacade {
    private static PaymentServicesFacade paymentServicesFacadeInstance;
    private ExternalPaymentRepository externalPaymentRepository;
    private AcquisitionRepository acquisitionRepository;


    //db constructor
    @Autowired
    public PaymentServicesFacade(ExternalPaymentRepository externalPaymentRepository, AcquisitionRepository acquisitionRepository) throws Exception {
        this.externalPaymentRepository = externalPaymentRepository;
        this.acquisitionRepository = acquisitionRepository;

//        //TEST
//        Map<String, Map<String, List<Integer>>> productList = new HashMap<>();
//        List<Integer> priceQuantity = new ArrayList<>();
//        priceQuantity.add(12);
//        priceQuantity.add(12);
//        Map<String, List<Integer>> productNames = new HashMap<>();
//        productNames.put("candle", priceQuantity);
//        productList.put("candleStore", productNames);
//        pay(99, new PaymentDTO("123", "noa", "curr", "123456789", 123,11,
//                26),"userID", productList);
    }

    //memory constructor
    public PaymentServicesFacade(){
        this.externalPaymentRepository = new ExternalPaymentMemoryRepository();
        this.acquisitionRepository = new AcquisitionMemoryRepository();
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
        externalPaymentRepository.deleteById(paymentId);
    }

    public boolean checkHandShake(){
        Optional<ExternalPaymentService> externalPaymentService = externalPaymentRepository.findById("https://damp-lynna-wsep-1984852e.koyeb.app/");
        ExternalPaymentService externalPaymentService1 = externalPaymentService.orElse(null);
        if (externalPaymentService1 != null){
            return externalPaymentService1.checkHandShake();
        }
        return false;
    }

//    public boolean addExternalService(String licensedDealerNumber, String paymentServiceName, String url){
//        synchronized (paymentServiceLock) {
//            int size_before = allPaymentServices.size();
//            ExternalPaymentService externalPaymentService = new ExternalPaymentService(licensedDealerNumber, paymentServiceName, url);
//            allPaymentServices.put(licensedDealerNumber, externalPaymentService);
//            return allPaymentServices.size() == size_before + 1;
//        }
//    }

    public boolean addExternalService(String paymentURL){
        List<ExternalPaymentService> externalPaymentServices = externalPaymentRepository.findAll();
        int size_before = externalPaymentServices.size();
        ExternalPaymentService externalPaymentService = new ExternalPaymentService(paymentURL);
        externalPaymentRepository.save(externalPaymentService);
        return externalPaymentRepository.findAll().size() == size_before + 1;

    }

//    public boolean addExternalService(PaymentServiceDTO paymentServiceDTO, HttpClient httpClient){
//        synchronized (paymentServiceLock) {
//            int size_before = allPaymentServices.size();
//            ExternalPaymentService externalPaymentService = new ExternalPaymentService(paymentServiceDTO, httpClient);
//            allPaymentServices.put(paymentServiceDTO.getLicensedDealerNumber(), externalPaymentService);
//            return allPaymentServices.size() == size_before + 1;
//        }
//    }

    public void clearPaymentServices() {
        externalPaymentRepository.deleteAll();
    }

    @Transactional
    public String pay(int price, PaymentDTO payment, String userId, Map<String, Map<String, List<Integer>>> productList) throws Exception{
       
        String acquisitionId  = getNewAcquisitionId();
        ExternalPaymentService externalPaymentService = getPaymentServiceByURL("https://damp-lynna-wsep-1984852e.koyeb.app/");
       int transactionId  = externalPaymentService.payWithCard(price, payment, userId, productList, acquisitionId);
        System.out.println("transactionId is " + transactionId);
        Acquisition acquisition = new Acquisition(String.valueOf(transactionId), userId, price, payment, productList);
        acquisitionRepository.save(acquisition);
        externalPaymentService.addAcquisition(String.valueOf(transactionId), acquisition);
        return acquisition.getAcquisitionId();

    }

    public int cancelPayment(int transactionID) throws Exception {
        ExternalPaymentService externalPaymentService = getPaymentServiceByURL("https://damp-lynna-wsep-1984852e.koyeb.app/");
         return externalPaymentService.cancelPayment(transactionID);
    }

    public Map<String, String> getAcquisitionReceipts(String acquisitionId){
        Optional<Acquisition> acquisition = acquisitionRepository.findById(acquisitionId);
        Acquisition acquisition1 = acquisition.orElse(null);
        if(acquisition1 != null){
            return acquisition1.getReceiptIdAndStoreIdMap();
        }
        throw new IllegalArgumentException(ExceptionsEnum.AcquisitionNotExist.toString());

    }

    public String getNewAcquisitionId(){
        UUID uuid = UUID.randomUUID();
        String id = "acquisition-"+uuid.toString() ;
        return id;
    }

    public Map<String, ExternalPaymentService> getAllPaymentServices(){
        List<ExternalPaymentService> externalPaymentServices = externalPaymentRepository.findAll();
        Map<String,ExternalPaymentService> externalPaymentServiceMap = new HashMap<>();
        for(ExternalPaymentService externalPaymentService: externalPaymentServices){
            externalPaymentServiceMap.put(externalPaymentService.getUrl(), externalPaymentService);
        }
        return externalPaymentServiceMap;


    }


//    public PaymentServiceDTO getPaymentServiceDTOById(String paymentServiceId){
//        ExternalPaymentService externalPaymentService = getPaymentServiceById(paymentServiceId);
//        return new PaymentServiceDTO(externalPaymentService.getLicensedDealerNumber(), externalPaymentService.getPaymentServiceName(), externalPaymentService.getUrl());
//    }

//    public ExternalPaymentService getPaymentServiceById(String paymentServiceId){
//        if(allPaymentServices.containsKey(paymentServiceId)){
//            return allPaymentServices.get(paymentServiceId);
//        }
//        else {
//            return null;
//        }
//    }

    public ExternalPaymentService getPaymentServiceByURL(String paymentURL){

        Optional<ExternalPaymentService> externalPaymentService = externalPaymentRepository.findById(paymentURL);
        ExternalPaymentService externalPaymentService1 = externalPaymentService.orElse(null);
        if (externalPaymentService1 != null){
            return new ExternalPaymentService(externalPaymentService1.getUrl());
        }
        return null;
    }

    public Map<String, Integer> getStorePurchaseInfo()
    {
        Map<String, Integer> storePurchaseStats = new HashMap<>();
        List<Acquisition> acquisitions = acquisitionRepository.findAll();
            for (Acquisition acquisition : acquisitions) {
                Map<String, Receipt> acqReceipts = acquisition.getStoreIdAndReceipt();
                for (String receiptId : acqReceipts.keySet()) {
                    String storeId = acqReceipts.get(receiptId).getStoreId();
                    storePurchaseStats.put(storeId, storePurchaseStats.getOrDefault(storeId, 0) + 1);
                }
            }
        return storePurchaseStats;
    }


    public Map<String, Integer> getStoreReceiptsAndTotalAmount(String storeId)
    {
        Map<String, Integer> receiptAndTotalPrice = new HashMap<>();
        List<Acquisition> acquisitions = acquisitionRepository.findAll();
            for (Acquisition acquisition : acquisitions) {
                if (acquisition.getStoreIdAndReceipt().containsKey(storeId)) {
                    receiptAndTotalPrice.put(acquisition.getReceiptIdByStoreId(storeId), acquisition.getTotalPriceOfStoreInAcquisition(storeId));
                }
            }


        return receiptAndTotalPrice;
    }

    public Map<String, Acquisition> getIdAndAcquisition() {
        List<Acquisition> acquisitions = acquisitionRepository.findAll();
        Map<String, Acquisition> acquisitionMap = new HashMap<>();
        for (Acquisition acquisition: acquisitions){
            acquisitionMap.put(acquisition.getAcquisitionId(),acquisition);
        }
        return acquisitionMap;
    }

    public List<AcquisitionDTO> getAcquisitionsDTO(List<String> acquisitions) {
        List<AcquisitionDTO> acquisitionsDTO = new LinkedList<>();
        for (String acqId : acquisitions) {
            Optional<Acquisition> acq = acquisitionRepository.findById(acqId);
            Acquisition acq1 = acq.orElse(null);
            if (acq1 != null) {
                acquisitionsDTO.add(new AcquisitionDTO(acq1.getAcquisitionId(), acq1.getUserId(), acq1.getTotalPrice(), acq1.getDate()));
            }
        }

        return acquisitionsDTO;
    }

    public Map<String, ReceiptDTO> getReceiptsDTOByAcquisition(String acquisitionId) {

        Map<String, ReceiptDTO> receiptsDTO = new HashMap<>();
        Optional<Acquisition> acq = acquisitionRepository.findById(acquisitionId);
        Acquisition acq1 = acq.orElse(null);
        if (acq1 != null) {
            Map<String, Receipt> storeReceipts = acq1.getStoreIdAndReceipt();
            for (String storeId : storeReceipts.keySet()) {
                Receipt receipt = storeReceipts.get(storeId);
                receiptsDTO.put(receipt.getReceiptId(), new ReceiptDTO(receipt.getReceiptId(), receipt.getStoreId(), receipt.getUserId(), receipt.getProductList()));
            }
        }
        return receiptsDTO;
    }
}
