package PresentationLayer.WAF;

import DomainLayer.Market.Market;
import DomainLayer.Store.StoreFacade;
import DomainLayer.User.UserFacade;
import Util.StoreDTO;
import Util.UserDTO;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class StoreService {


    private final StoreFacade storeFacade;
    private final Market market;


    public StoreService() {
        this.storeFacade = StoreFacade.getInstance();
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
    public List<StoreDTO> getAllStores(){
        return storeFacade.getAllDTOs();
    }

    public StoreDTO getStore(String storeId){
        return storeFacade.getStoreDTOById(storeId);
    }

}


