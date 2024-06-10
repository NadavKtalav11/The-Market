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
public class MarketService {



    private final Market market;


    public MarketService (){

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


    public String enterMarket(){
        return market.enterMarketSystem();
    }

}
