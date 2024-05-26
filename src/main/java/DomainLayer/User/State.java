package DomainLayer.User;

import java.util.Map;

public interface State {


    void Logout();
    void exitMarketSystem();
    //void Register(User user, String username, String password, String birthday, String address) throws Exception;
    void Login() throws Exception;
    boolean isMember();
    void addReceipt(Map<Integer, Integer> receiptIdAndStoreId);
}
