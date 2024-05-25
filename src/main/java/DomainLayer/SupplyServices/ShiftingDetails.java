package DomainLayer.SupplyServices;

import java.util.Date;

public class ShiftingDetails {
    private int shiftingId;
    private String userName;
    private String country;
    private String city;
    private String address;
    private String zip;
    private Date date;

    // Constructor
    public ShiftingDetails(int shiftingId,String userName, String country, String city,
                           String address) {
        this.shiftingId = shiftingId;
        this.userName = userName;
        this.country = country;
        this.city = city;
        this.address = address;
        this.date = new Date(); // Current date and time
    }

    // Getters
    public String getUserName() {
        return userName;
    }

    public String getCountry() {
        return country;
    }

    public String getCity() {
        return city;
    }

    public String getAddress() {
        return address;
    }

    public String getZip() {
        return zip;
    }

    public Date getDate() {
        return date;
    }


}
