package DomainLayer.IPaymentServices;

 abstract class IPaymentServices {
    protected double amountPayed;

    public IPaymentServices() {
        this.amountPayed = 0;
    }

    //public abstract Response<Boolean> setInformation(List<String> paymentDetails, String holder, int userId);
    public abstract boolean pay(double price);
    public abstract boolean refund(double amountToRefund);
}
