package ServiceLayer;

import DomainLayer.Market.Market;

public class Service_layer {
    private Market market;

    public Service_layer() {
        this.market = new Market(); // Initialize the Market instance
    }

    public void Logout(int memberID){
        try{
            market.Logout(memberID);
        }
        catch (Exception e){
            System.out.println(e);
        }
    }

    public void Exit(int userID){
        try{
            market.Exit(userID);
        }
        catch (Exception e){
            System.out.println(e);
        }
    }

    //todo think about where we get the userID
    public void Register(int userID,String username, String password, String birthday, String address){
        try{
            market.Register(userID, username, password,birthday, address);
        }
        catch (Exception e){
            System.out.println(e);
        }
    }

    //todo think about where we get the userID
    public void Login(int userID, String username, String password){
        try{
            market.Login(userID, username, password);
        }
        catch (Exception e){
            System.out.println(e);
        }
    }


}
