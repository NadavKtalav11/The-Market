package ServiceLayer;

import DomainLayer.Market.Market;

public class Service_layer {
    private Market market;

    public Service_layer() {

        this.market =  Market.getInstance(); // Initialize the Market instance
    }


    public void init(String userName, String password, int licensedDealerNumber,
                     String paymentServiceName, String url, int licensedDealerNumber1, String supplyServiceName, String address){
        try{
            market.init(userName, password,licensedDealerNumber,paymentServiceName,
                    url, licensedDealerNumber1, supplyServiceName, address);
        }
        catch (Exception e){
            System.out.println(e);
        }
    }

    public void payWithExternalPaymentService(){
        try {
            market.payWithExternalPaymentService();
        }

        catch (Exception e){
        System.out.println(e);
        }
    }



    public void Logout(int memberID){
        try{
            market.Logout(memberID);
        }
        catch (Exception e){
            System.out.println(e);
        }
    }


}
