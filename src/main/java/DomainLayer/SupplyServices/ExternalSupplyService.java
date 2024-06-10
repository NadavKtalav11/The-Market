package DomainLayer.SupplyServices;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class ExternalSupplyService {
    private String licensedDealerNumber;
    private String supplyServiceName;
    private HashSet<String> countries = new HashSet<>();
    private HashSet<String> cities = new HashSet<>();
    private HashMap<Integer, ShiftingDetails> shiftIdAndDetails = new HashMap<>();
    private final Object countriesLock;
    private final Object citiesLock;
    private final Object shiftLock;

    int ShiftIDCounter= 1;

    public ExternalSupplyService(String licensedDealerNumber, String supplyServiceName, HashSet<String> countries, HashSet<String> cities){

        this.licensedDealerNumber=licensedDealerNumber;
        this.supplyServiceName = supplyServiceName;
        this.countries  = countries;
        this.cities = cities;
        countriesLock =new Object();
        citiesLock = new Object();
        shiftLock = new Object();
    }

    public String getLicensedDealerNumber(){
        return this.licensedDealerNumber;
    }

    public boolean checkAreaAvailability(String country, String city){
        synchronized (countriesLock) {
            if (!countries.contains(country)) {
                return false;
            }
        }
        synchronized (citiesLock) {
            if (!cities.contains((city))) {
                return false;
            }
        }
        return true;
    }

    public void addCountries(HashSet<String> countriesToAdd){
        synchronized (countriesLock) {
            countries.addAll(countriesToAdd);
        }

    }

    public void addCities(HashSet<String> citiesToAdd){
        synchronized (citiesLock) {
            cities.addAll(citiesToAdd);
        }

    }

    public HashSet<String> getCountries(){
        synchronized (countriesLock) {
            return this.countries;
        }
    }
    public HashSet<String> getCities(){
        synchronized (citiesLock) {
            return this.cities;
        }
    }


    public boolean createShiftingDetails(String userName, String country,String city,String address){
        synchronized (shiftLock) {
            int size = shiftIdAndDetails.size();
            ShiftingDetails shiftingDetails = new ShiftingDetails(ShiftIDCounter, userName, country, city, address);
            shiftIdAndDetails.put(ShiftIDCounter, shiftingDetails);
            ShiftIDCounter++;
            return shiftIdAndDetails.size() == size + 1;
        }
    }
    public HashMap<Integer,ShiftingDetails> getShiftIdAndDetails(){
        synchronized (shiftLock) {
            return this.shiftIdAndDetails;
        }
    }

}
