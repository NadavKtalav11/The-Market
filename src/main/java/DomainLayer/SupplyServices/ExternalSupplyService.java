package DomainLayer.SupplyServices;

import DomainLayer.HttpRequestController;
import Util.ExceptionsEnum;
import Util.SupplyServiceDTO;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapKeyColumn;
import jakarta.persistence.*;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Entity
@Table(name = "external_supply_service")
public class ExternalSupplyService {
    //private String licensedDealerNumber;
//    private String supplyServiceName;
//    private Set<String> countries = new HashSet<>();
//    private Set<String> cities = new HashSet<>();
    @Id
    @Column(name = "supply_url", nullable = false)
    private String supplyURL;
    @Transient
    private HttpRequestController httpReqCtrl;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "external_supply_service_url") // This will create a foreign key column in the ShiftingDetails table
    @MapKeyColumn(name = "shift_id") // Column in the ShiftingDetails table for shift_id
    private Map<Integer, ShiftingDetails> shiftIdAndDetails = new HashMap<>();
//    private final Object countriesLock;
//    private final Object citiesLock;
    @Transient
    private final Object shiftLock;

    int ShiftIDCounter= 1;

    public ExternalSupplyService() {
        // JPA requires a no-argument constructor
        this.shiftLock = new Object();
    }

    public ExternalSupplyService(String supplyURL){

        this.supplyURL=supplyURL;
        shiftLock =new Object();

        try
            {
                httpReqCtrl = new HttpRequestController(supplyURL);
            }
            catch (Exception e)
            {
                httpReqCtrl = null;
            }

        }

//    public ExternalSupplyService(String supplyURL){
//
//        this.licensedDealerNumber=supplyServiceDTO.getLicensedDealerNumber();
//        this.supplyServiceName = supplyServiceDTO.getSupplyServiceName();
//        this.countries  = supplyServiceDTO.getCountries();
//        this.cities = supplyServiceDTO.getCities();
//        countriesLock =new Object();
//        citiesLock = new Object();
//        shiftLock = new Object();
//    }

//    public String getLicensedDealerNumber(){
//        return this.licensedDealerNumber;
//    }

    public String getSupplyURL(){
        return this.supplyURL;
    }

    public boolean checkAreaAvailability(String country, String city){
//        synchronized (countriesLock) {
//            if (!countries.contains(country)) {
//                return false;
//            }
//        }
//        synchronized (citiesLock) {
//            if (!cities.contains((city))) {
//                return false;
//            }
//        }
        return true;
    }

    public void addCountries(HashSet<String> countriesToAdd){
//        synchronized (countriesLock) {
//            countries.addAll(countriesToAdd);
//        }

    }

    public void addCities(HashSet<String> citiesToAdd){
//        synchronized (citiesLock) {
//            cities.addAll(citiesToAdd);
//        }

    }

//    public Set<String> getCountries(){
////        synchronized (countriesLock) {
////            return this.countries;
////        }
//    }
//    public Set<String> getCities(){
//        synchronized (citiesLock) {
//            return this.cities;
//        }
//    }


    public boolean createShiftingDetails(String userName, String country,String city,String address){
        synchronized (shiftLock) {
            int size = shiftIdAndDetails.size();
            ShiftingDetails shiftingDetails = new ShiftingDetails(ShiftIDCounter, userName, country, city, address);
            shiftIdAndDetails.put(ShiftIDCounter, shiftingDetails);
            ShiftIDCounter++;
            return shiftIdAndDetails.size() == size + 1;
        }
    }
    public Map<Integer,ShiftingDetails> getShiftIdAndDetails(){
        synchronized (shiftLock) {
            return this.shiftIdAndDetails;
        }
    }

    public int createSupply(String userName, String country,String city,String address) throws Exception {
        try {
            if (this.httpReqCtrl == null) {
                return -1;
            }
            this.httpReqCtrl = new HttpRequestController(supplyURL);
            if (!this.httpReqCtrl.checkHandShake()) {
                return -1;
            }
            Map<String, String> params = new HashMap<>();
            params.put("action_type", "supply");
            params.put("name", userName);
            params.put("address", address);
            params.put("city", city);
            params.put("country", country);
            params.put("zip", "777432");
            this.httpReqCtrl = new HttpRequestController(supplyURL);
            String response = this.httpReqCtrl.sendRequest(params);
            if (response == null) {
                System.out.println("Supply request failed or returned null response.");
                return -1;
            }
            try {
                int transactionId = Integer.parseInt(response);
                if (isValidTransactionID(transactionId)) {
                    ShiftingDetails shiftingDetails = new ShiftingDetails(transactionId, userName, country, city, address);
                    shiftIdAndDetails.put(ShiftIDCounter, shiftingDetails);
                    return transactionId;
                }
                return  -1;

            } catch (NumberFormatException e) {
                System.out.println("Response format error: " + e.getMessage());
                return -1;
            }
        } catch (Exception e) {
            System.out.println("Exception during Supply: " + e.getMessage());
            return -1;
        }
    }

        public int cancelSupply(int transactionID)  throws Exception{

            if(httpReqCtrl == null) //If constructor failed
            {
                throw new Exception("No connection established");
            }

            if(transactionID == 0 || transactionID == -1)
            {
                throw new Exception(ExceptionsEnum.noPayment.toString());
            }

            this.httpReqCtrl = new HttpRequestController(supplyURL);
            this.shiftIdAndDetails.remove(transactionID);
            if(!this.httpReqCtrl.checkHandShake())
            {
                throw new Exception(ExceptionsEnum.checkHandShake.toString());
            }
            Map<String,String> postContent = new HashMap<>();
            postContent.put("action_type", "cancel_supply");
            postContent.put("transaction_id", String.valueOf(transactionID));
            this.httpReqCtrl = new HttpRequestController(supplyURL);
            String response = this.httpReqCtrl.sendRequest(postContent);
            if (response == null)
            {
                throw new Exception("Failed to send request to external supply system");
            }
            int cancelRes = Integer.parseInt(response);
            if(cancelRes != 1)
            {
                throw new Exception(ExceptionsEnum.cancelFailed.toString());
            }

            this.shiftIdAndDetails.remove(transactionID);
            return cancelRes;
        }

        public boolean isValidTransactionID(int transactionID){
            return transactionID >= 10000 && transactionID <= 100000;
        }




}
