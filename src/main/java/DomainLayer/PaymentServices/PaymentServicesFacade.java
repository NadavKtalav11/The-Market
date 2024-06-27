package DomainLayer.PaymentServices;


import Util.*;

import java.util.*;

public class PaymentServicesFacade {
    private static PaymentServicesFacade paymentServicesFacadeInstance;
    private Map<String, ExternalPaymentService>  allPaymentServices = new HashMap<String, ExternalPaymentService>();
    private Map<String, Acquisition> IdAndAcquisition = new HashMap<>();

    private final Object paymentServiceLock;
    private final Object acquisitionLock;

    public PaymentServicesFacade(){
        paymentServiceLock =new Object();
        acquisitionLock = new Object();
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
        synchronized (paymentServiceLock) {
            allPaymentServices.remove(paymentId);
        }
    }

    public boolean addExternalService(String licensedDealerNumber, String paymentServiceName, String url){
        synchronized (paymentServiceLock) {
            int size_before = allPaymentServices.size();
            ExternalPaymentService externalPaymentService = new ExternalPaymentService(licensedDealerNumber, paymentServiceName, url);
            allPaymentServices.put(licensedDealerNumber, externalPaymentService);
            return allPaymentServices.size() == size_before + 1;
        }
    }

    public boolean addExternalService(PaymentServiceDTO paymentServiceDTO){
        synchronized (paymentServiceLock) {
            int size_before = allPaymentServices.size();
            ExternalPaymentService externalPaymentService = new ExternalPaymentService(paymentServiceDTO);
            allPaymentServices.put(paymentServiceDTO.getLicensedDealerNumber(), externalPaymentService);
            return allPaymentServices.size() == size_before + 1;
        }
    }

    public boolean addExternalService(PaymentServiceDTO paymentServiceDTO, HttpClient httpClient){
        synchronized (paymentServiceLock) {
            int size_before = allPaymentServices.size();
            ExternalPaymentService externalPaymentService = new ExternalPaymentService(paymentServiceDTO, httpClient);
            allPaymentServices.put(paymentServiceDTO.getLicensedDealerNumber(), externalPaymentService);
            return allPaymentServices.size() == size_before + 1;
        }
    }

    public void clearPaymentServices() {
        synchronized (paymentServiceLock) {
            allPaymentServices.clear();
        }
    }

    public String pay(int price, PaymentDTO payment, String userId, Map<String, Map<String, List<Integer>>> productList) throws Exception{
       
        String acquisitionId  = getNewAcquisitionId();
        ExternalPaymentService externalPaymentService;
        synchronized (paymentServiceLock) {
            externalPaymentService = allPaymentServices.values().iterator().next();
        }
        externalPaymentService.payWithCard(price, payment, userId, productList, acquisitionId);

        Acquisition acquisition = new Acquisition(acquisitionId, userId, price, payment, productList);
        synchronized (acquisitionLock) {
            IdAndAcquisition.put(acquisitionId, acquisition);
        }

        externalPaymentService.addAcquisition(acquisitionId, acquisition);
        return acquisition.getAcquisitionId();

    }

    public Map<String, String> getAcquisitionReceipts(String acquisitionId){
        Acquisition acquisition;
        synchronized (acquisitionLock) {
            acquisition = IdAndAcquisition.get(acquisitionId);
        }
        if(acquisition == null){
            throw new IllegalArgumentException(ExceptionsEnum.AcquisitionNotExist.toString());
        }
        return acquisition.getReceiptIdAndStoreIdMap();
    }

    public String getNewAcquisitionId(){
        UUID uuid = UUID.randomUUID();
        String id = "acquisition-"+uuid.toString() ;
        return id;
    }

    public Map<String, ExternalPaymentService> getAllPaymentServices(){
        synchronized (allPaymentServices) {
            return this.allPaymentServices;
        }
    }


    public PaymentServiceDTO getPaymentServiceDTOById(String paymentServiceId){
        ExternalPaymentService externalPaymentService = getPaymentServiceById(paymentServiceId);
        return new PaymentServiceDTO(externalPaymentService.getLicensedDealerNumber(), externalPaymentService.getPaymentServiceName(), externalPaymentService.getUrl());
    }

    public ExternalPaymentService getPaymentServiceById(String paymentServiceId){
        if(allPaymentServices.containsKey(paymentServiceId)){
            return allPaymentServices.get(paymentServiceId);
        }
        else {
            return null;
        }
    }

    public Map<String, Integer> getStorePurchaseInfo()
    {
        Map<String, Integer> storePurchaseStats = new HashMap<>();
        synchronized (acquisitionLock) {
            for (String acqId : IdAndAcquisition.keySet()) {
                Map<String, Receipt> acqReceipts = IdAndAcquisition.get(acqId).getStoreIdAndReceipt();
                for (String receiptId : acqReceipts.keySet()) {
                    String storeId = acqReceipts.get(receiptId).getStoreId();
                    storePurchaseStats.put(storeId, storePurchaseStats.getOrDefault(storeId, 0) + 1);
                }
            }
        }
        return storePurchaseStats;
    }


    public Map<String, Integer> getStoreReceiptsAndTotalAmount(String storeId)
    {
        Map<String, Integer> receiptAndTotalPrice = new HashMap<>();
        synchronized (acquisitionLock) {
            for (String acqId : IdAndAcquisition.keySet()) {
                Acquisition acq = IdAndAcquisition.get(acqId);
                if (acq.getStoreIdAndReceipt().containsKey(storeId)) {
                    receiptAndTotalPrice.put(acq.getReceiptIdByStoreId(storeId), acq.getTotalPriceOfStoreInAcquisition(storeId));
                }
            }
        }

        return receiptAndTotalPrice;
    }

    public Map<String, Acquisition> getIdAndAcquisition() {
        synchronized (acquisitionLock) {
            return IdAndAcquisition;
        }
    }

    public List<AcquisitionDTO> getAcquisitionsDTO(List<String> acquisitions) {
        List<AcquisitionDTO> acquisitionsDTO = new LinkedList<>();
        synchronized (acquisitionLock) {
            for (String acqId : acquisitions) {
                Acquisition acq = IdAndAcquisition.get(acqId);
                if (acq != null) {
                    acquisitionsDTO.add(new AcquisitionDTO(acq.getAcquisitionId(), acq.getUserId(), acq.getTotalPrice(), acq.getDate()));
                }
            }
        }
        return acquisitionsDTO;
    }

    public Map<String, ReceiptDTO> getReceiptsDTOByAcquisition(String acquisitionId) {

        Map<String, ReceiptDTO> receiptsDTO = new HashMap<>();
        Acquisition acq;
        synchronized (acquisitionLock) {
            acq = IdAndAcquisition.get(acquisitionId);
        }
        if (acq != null) {
            Map<String, Receipt> storeReceipts = acq.getStoreIdAndReceipt();
            for (String storeId : storeReceipts.keySet()) {
                Receipt receipt = storeReceipts.get(storeId);
                receiptsDTO.put(receipt.getReceiptId(), new ReceiptDTO(receipt.getReceiptId(), receipt.getStoreId(), receipt.getUserId(), receipt.getProductList()));
            }
        }
        return receiptsDTO;
    }
}
