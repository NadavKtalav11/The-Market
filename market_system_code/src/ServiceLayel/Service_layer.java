package com.company.ServiceLayel;

import DomainLayer.Market.Market;

public class Service_layer {
    private Market market;

    public Service_layer() {
        this.market = new Market(); // Initialize the Market instance
    }

    public void Logout(int memberID){
        try{
            market.getUserFacade().getUserByID(memberID).Logout();
        }
        catch (Exception e){
            System.out.println(e);
        }
    }


}
