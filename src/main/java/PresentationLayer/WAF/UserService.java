package PresentationLayer.WAF;

import DomainLayer.Market.Market;
import DomainLayer.User.UserFacade;
import Util.UserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class UserService {



    private final UserFacade userFacade;
    private final Market market;


    public UserService (){
        this.userFacade = UserFacade.getInstance();
        market = Market.getInstance();
    }


    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder.build();
    }

    public List<UserDTO> getAllUsers(){
        return userFacade.getAllUsers();
    }

    public UserDTO getUser(String userID){
        return userFacade.getUserDTOById(userID);
    }

    public String addUser() {
        return userFacade.addUser();
    }

    public UserDTO updateUser(UserDTO userDTO){
        return userFacade.updateUser(userDTO);
    }

    public void delete(String userId){
        userFacade.removeUser(userId);
    }

    public String enterMarket(){
        return market.enterMarketSystem();
    }

}
