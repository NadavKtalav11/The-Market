package DomainLayer.User;

import java.util.Map;

public class Guest extends State{


    public Guest(){
        super();

    }
    @Override
    public void Logout() {
        throw new IllegalArgumentException("only member can log out");
    }

    @Override
    public void exitMarketSystem() {
        //todo think if we need to do here something
    }

    @Override
    public void Login() {
        //do nothing
        return;
    }

    @Override
    public boolean isMember() {
        return false;
    }

    @Override
    public void addReceipt(Map<String, String> receiptIdAndStoreId) {
        return;
    }

    @Override
    public String getUsername() {
        return null;
    }


}
