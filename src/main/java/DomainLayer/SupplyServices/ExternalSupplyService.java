package DomainLayer.SupplyServices;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class ExternalSupplyService {
    private int licensedDealerNumber;
    private String supplyServiceName;
    private HashSet<String> countries = new HashSet<>();
    private HashSet<String> cities = new HashSet<>();
    private HashMap<Integer, ShiftingDetails> shiftIdAndDetails = new HashMap<>();
    int ShiftIDCounter= 1;

    public ExternalSupplyService(int licensedDealerNumber, String supplyServiceName, HashSet<String> countries, HashSet<String> cities){

        this.licensedDealerNumber=licensedDealerNumber;
        this.supplyServiceName = supplyServiceName;
        this.countries  = countries;
        this.cities = cities;
    }

    public int getLicensedDealerNumber(){
        return this.licensedDealerNumber;
    }

    public boolean checkAreaAvailability(String country, String city){
        if(!countries.contains(country)){
            return false;
        }
        if(!cities.contains((city))){
            return false;
        }
        return true;
    }


    public boolean createShiftingDetails(String userName, String country,String city,String address){
        int size = shiftIdAndDetails.size();
        ShiftingDetails shiftingDetails = new ShiftingDetails(ShiftIDCounter, userName, country, city, address);
        shiftIdAndDetails.put(ShiftIDCounter,shiftingDetails);
        ShiftIDCounter++;
        return shiftIdAndDetails.size() == size+1;
    }
    public HashMap<Integer,ShiftingDetails> getShiftIdAndDetails(){
        return this.shiftIdAndDetails;
    }

}
