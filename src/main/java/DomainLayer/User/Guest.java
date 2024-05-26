package DomainLayer.User;

import java.util.Map;

public class Guest implements State{

    @Override
    public void Logout(User user) {
        //todo throw exception
    }

    @Override
    public void exitMarketSystem(User user) {
        //todo think if we need to do here something
    }

    @Override
    public void Login(String username, String password, Member loginMember) {
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
