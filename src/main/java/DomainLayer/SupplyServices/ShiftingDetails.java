package DomainLayer.SupplyServices;

import java.util.Date;
import jakarta.persistence.*;

@Entity
@Table(name = "shifting_details")
public class ShiftingDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "shifting_id")
    private int shiftingId;
    @Column(name = "user_name", nullable = false)
    private String userName;
    @Column(name = "country", nullable = false)
    private String country;
    @Column(name = "city", nullable = false)
    private String city;
    @Column(name = "address", nullable = false)
    private String address;
    @Column(name = "zip")
    private String zip;
    @Column(name = "date", nullable = false)
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

    // No-argument constructor required by JPA
    public ShiftingDetails() {
    }

    public int getShiftingId() {
        return shiftingId;
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
