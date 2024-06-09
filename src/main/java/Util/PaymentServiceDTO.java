package Util;

import DomainLayer.PaymentServices.ExternalPaymentService;
import DomainLayer.User.User;

public class PaymentServiceDTO {
    private String licensedDealerNumber;
    private String paymentServiceName;
    private String url;

    public PaymentServiceDTO (ExternalPaymentService externalPaymentService){
        this.licensedDealerNumber = externalPaymentService.getLicensedDealerNumber();
        this.paymentServiceName = externalPaymentService.getPaymentServiceName();
        this.url = externalPaymentService.getUrl();

    }
}
