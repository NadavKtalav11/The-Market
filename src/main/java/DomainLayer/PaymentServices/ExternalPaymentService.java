package DomainLayer.PaymentServices;


// this class is for external payment service itself

public  class ExternalPaymentService {
    private int licensedDealerNumber;
    private String paymentServiceName;
    private String url;

    public ExternalPaymentService(int licensedDealerNumber, String paymentServiceName,String url){
        this.licensedDealerNumber=licensedDealerNumber;
        this.paymentServiceName= paymentServiceName;
        this.url= url;
    }

    // Abstract method for paying with a card
    public boolean payWithCard(int price, int cvv, int month, int year, String holder, int id){
        return true;
    }

    // Abstract method for refunding to a card
    public  boolean refundToCard(){
     return  true;
    }

    // Abstract method for checking service availability
    public  boolean checkServiceAvailability(){
        return true;
    }
}
