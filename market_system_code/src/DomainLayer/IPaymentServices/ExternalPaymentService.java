package DomainLayer.IPaymentServices;


// this class is for external payment service itself

interface ExternalPaymentService {

    // Abstract method for paying with a card
    public abstract boolean payWithCard(int num, int cvv, int month, int year, String holder, int id);

    // Abstract method for refunding to a card
    public abstract boolean refundToCard();

    // Abstract method for checking service availability
    public abstract boolean checkServiceAvailability();
}
