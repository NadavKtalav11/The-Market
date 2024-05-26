package DomainLayer.User;

import java.util.Map;

public class Guest implements State{

    @Override
    public void Logout() {
        //todo throw exception
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
    public void addReceipt(Map<Integer, Integer> receiptIdAndStoreId) {
        return;
    }

}
