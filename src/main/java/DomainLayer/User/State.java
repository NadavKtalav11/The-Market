package DomainLayer.User;

import java.util.Map;

public interface State {


    void Logout(User user);
    void exitMarketSystem(User user);
    //void Register(User user, String username, String password, String birthday, String address) throws Exception;
    void Login(String username, String password, Member loginMember) throws Exception;
    boolean isMember();
    void addReceipt(Map<Integer, Integer> receiptIdAndStoreId);
}
