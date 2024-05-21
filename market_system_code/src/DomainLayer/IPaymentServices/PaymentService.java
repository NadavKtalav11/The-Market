package DomainLayer.IPaymentServices;

class PaymentService extends IPaymentServices {
    private int cardNumber;
    private int cvv;
    private String month;
    private String year;
    private String holder;
    private int userId;
    private ExternalPaymentService externalPaymentService;
   // private static final Logger logger = Logger.getLogger(PaymentService.class.getName());

    public PaymentService(ExternalPaymentService externalPaymentService) {
        super();
        this.externalPaymentService = externalPaymentService;
        this.cardNumber = -1;
        this.cvv = -1;
        this.month = "";
        this.year = "";
        this.holder = "";
        this.userId = -1;
    }



    @Override
    public boolean pay(double price) {
        this.amountPayed = price;
        return this.externalPaymentService.payWithCard(this.cardNumber, this.cvv, this.month, this.year, this.holder, this.userId);
    }

    @Override
    public boolean refund(double amountToRefund) {
        return this.externalPaymentService.refundToCard();
    }




}
