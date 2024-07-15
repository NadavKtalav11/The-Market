package Util;

import java.util.Date;

public class AcquisitionDTO {

    private String acquisitionId;
    private String memberId;
    private int totalPrice;
    private Date date;

    public AcquisitionDTO(String acquisitionId, String memberId, int totalPrice, Date date) {
        this.acquisitionId = acquisitionId;
        this.memberId = memberId;
        this.totalPrice = totalPrice;
        this.date = date;
    }


    public String getAcquisitionId() {
        return acquisitionId;
    }

    public String getMemberId() {
        return memberId;
    }

    public int getTotalPrice() {
        return totalPrice;
    }

    public Date getDate() {
        return date;
    }
}
