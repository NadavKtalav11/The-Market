package DomainLayer.SupplyServices;

public class ExternalSupplyService {
    private int licensedDealerNumber;
    private String supplyServiceName;
    private String address;

    public ExternalSupplyService(int licensedDealerNumber, String supplyServiceName, String address){

        this.licensedDealerNumber=licensedDealerNumber;
        this.supplyServiceName = supplyServiceName;
        this.address = address;
    }

    public boolean checkServiceAvailability(int ProductId, int amount){
        return true;
    }

}
