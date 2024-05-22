package DomainLayer.SupplyServices;

import java.util.HashMap;
import java.util.Map;

public class ExternalSupplyService {
    private int licensedDealerNumber;
    private String supplyServiceName;
    private String address;
    private HashMap<Integer, Integer> productIdAndAmount;

    public ExternalSupplyService(int licensedDealerNumber, String supplyServiceName, String address){

        this.licensedDealerNumber=licensedDealerNumber;
        this.supplyServiceName = supplyServiceName;
        this.address = address;
    }

    public boolean checkAreaAvailability(String userAddress){
        return true;
    }


    public boolean checkServiceAvailability(HashMap<Integer,Integer> ProductIdAndAmount){
        for (Map.Entry<Integer, Integer> entry : ProductIdAndAmount.entrySet()) {
            Integer productId = entry.getKey();
            Integer requestedAmount = entry.getValue();
            // Check if the product exists in the instance's map and if the amount is sufficient
            if (!productIdAndAmount.containsKey(productId) || productIdAndAmount.get(productId) < requestedAmount) {
                return false; // If any product cannot fulfill the requested amount, return false
            }
        }

        return true;
    }

}
