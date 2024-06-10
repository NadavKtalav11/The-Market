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
@RequestMapping("/api")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<APIResponse<String>> createUser() {
        try {
            String userId = userService.addUser();
            return ResponseEntity.status(HttpStatus.CREATED).body(new APIResponse<>(userId, null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new APIResponse<>(null, e.getMessage()));
        }
    }

    @GetMapping("/user")
    public ResponseEntity<APIResponse<String>> getId() {
        try {
            System.out.println("hi\n");
            String userId = userService.enterMarket();
            System.out.println("hi2\n");
            HttpHeaders headers = new HttpHeaders();
            headers.add("accept" , "*/*");

            return ResponseEntity.status(HttpStatus.OK).headers(headers)
                    .body(new APIResponse<String>(userId, null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new APIResponse<>(null, e.getMessage()));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<APIResponse<UserDTO>> updateUser(@PathVariable String id, @RequestBody UserDTO userDetails) {
        try {
            UserDTO user = userService.updateUser(userDetails);
            if (user != null) {
                return ResponseEntity.ok(new APIResponse<>(user, null));
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new APIResponse<>(null, "User not found"));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new APIResponse<>(null, e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<APIResponse<Void>> deleteUser(@PathVariable String id) {
        try {
            userService.delete(id);
            return ResponseEntity.ok(new APIResponse<>(null, null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new APIResponse<>(null, e.getMessage()));
        }
    }
}
