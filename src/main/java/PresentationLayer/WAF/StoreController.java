package PresentationLayer.WAF;

import Util.APIResponse;
import Util.StoreDTO;
import Util.UserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/store")
public class StoreController {

    private final StoreService storeService;

    @Autowired
    public StoreController(StoreService storeService) {
        this.storeService = storeService;
    }

    @GetMapping("/getStore/{storeId}")
    public ResponseEntity<APIResponse<StoreDTO>> getStore(@PathVariable String storeId) {
        try {
            StoreDTO storeDTO = storeService.getStore(storeId);
            return ResponseEntity.status(HttpStatus.CREATED).body(new APIResponse<>(storeDTO, null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new APIResponse<>(null, e.getMessage()));
        }
    }

    @GetMapping("/getAllStores")
    public ResponseEntity<APIResponse<List<StoreDTO>>> getId() {
        try {
            System.out.println("hi\n");
            List<StoreDTO> allStores = storeService.getAllStores();
            System.out.println("hi2\n");
            HttpHeaders headers = new HttpHeaders();
            headers.add("accept", "*/*");

            return ResponseEntity.status(HttpStatus.OK).headers(headers)
                    .body(new APIResponse<>(allStores, null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new APIResponse<>(null, e.getMessage()));
        }
    }
}

