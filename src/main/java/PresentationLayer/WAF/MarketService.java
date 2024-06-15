package PresentationLayer.WAF;

import DomainLayer.Market.Market;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class MarketService {



    private final Service_layer service;


    public MarketService (){
        service = new Service_layer();
        //market = Market.getInstance();

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
        return service.enterMarketSystem().getData();
    }





}
