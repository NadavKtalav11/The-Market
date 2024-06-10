package PresentationLayer.WAF;

import DomainLayer.Market.Market;
import DomainLayer.Role.RoleFacade;
import DomainLayer.Store.StoreFacade;
import DomainLayer.User.UserFacade;
import Util.StoreDTO;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestOperations;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class MemberService {



    private final UserFacade userFacade;
    private final Market market;
    private final RoleFacade roleFacade;


    public MemberService() {
        this.userFacade = UserFacade.getInstance();
        market = Market.getInstance();
        roleFacade= RoleFacade.getInstance();
    }

        @Bean
        public RestTemplate restTemplate() {
            return new RestTemplate();
        }

        @Bean
        public RestTemplate restTemplate(RestTemplateBuilder builder) {
            return builder.build();
        }
        public boolean isMember(String userId){
            return userFacade.isMember(userId);
        }

    public boolean isStoreOwner(String userId,String storeId){
        return roleFacade.verifyStoreOwner(userId,storeId);
    }

    public boolean isStoreManager(String memberId,String storeId){
        return roleFacade.verifyStoreManager(memberId,storeId);
    }

    public boolean hasInventoryPermission(String memberID, String storeId){
        return roleFacade.managerHasInventoryPermissions(memberID, storeId);
    }

    public boolean hasPurchasePermission(String memberID, String storeId){
        return roleFacade.managerHasPurchasePermissions(memberID, storeId);
    }


    }

