package PresentationLayer.WAF;

import Util.APIResponse;
import Util.UserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/market")
public class MarketController {

    private final MarketService marketService;

    @Autowired
    public MarketController(MarketService marketService) {
        this.marketService = marketService;
    }



    @GetMapping("/enterSystem")
    public ResponseEntity<APIResponse<String>> enterMarket() {
        try {
            String userId = marketService.enterMarket();
            HttpHeaders headers = new HttpHeaders();
            headers.add("accept" , "*/*");

            return ResponseEntity.status(HttpStatus.OK).headers(headers)
                    .body(new APIResponse<String>(userId, null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new APIResponse<>(null, e.getMessage()));
        }
    }

}
