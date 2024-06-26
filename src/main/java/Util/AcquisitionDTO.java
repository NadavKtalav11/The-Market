package Util;

import java.util.Date;

public class AcquisitionDTO {

    private String acquisitionId;
    private String userId;
    private int totalPrice;
    private Date date;

    public AcquisitionDTO(String acquisitionId, String userId, int totalPrice, Date date) {
        this.acquisitionId = acquisitionId;
        this.userId = userId;
        this.totalPrice = totalPrice;
        this.date = date;
    }


    public String getAcquisitionId() {
        return acquisitionId;
    }

    public String getUserId() {
        return userId;
    }

    public int getTotalPrice() {
        return totalPrice;
    }

    public Date getDate() {
        return date;
    }
}
