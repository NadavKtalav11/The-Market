package PresentationLayer.WAF;
import DomainLayer.User.User;
import ServiceLayer.Service_layer;
import ServiceLayer.UserService;
import Util.APIResponse;
import Util.UserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


    @RestController
    @RequestMapping("/persons")
    public class UserController {
        @Autowired
        private UserService userService;

        @GetMapping
        public ResponseEntity<APIResponse<List<UserDTO>>> getAllUsers() {
            try {
                List<UserDTO> userDTOList = userService.getAllUsers();
                return ResponseEntity.ok(new APIResponse<>(userDTOList, null));
            }
            catch (Exception e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new APIResponse<>(null, e.getMessage()));
            }

        }

        @GetMapping("/{id}")
        public ResponseEntity<APIResponse<UserDTO>> getUserById(@PathVariable String id) {
            try {
                UserDTO userDTO = userService.getUser(id);
                return ResponseEntity.ok(new APIResponse<>(userDTO, null));
            }
            catch (Exception e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new APIResponse<>(null, e.getMessage()));
            }

        }


        @PostMapping
        public ResponseEntity<APIResponse<String>> createUser() {
            try {
                String userId = userService.addUser();
                return ResponseEntity.ok(new APIResponse<>(userId, null));
            }
            catch (Exception e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new APIResponse<>(null, e.getMessage()));
            }

        }

        @PutMapping("/{id}")
        public ResponseEntity<APIResponse<UserDTO>> updateUser(@RequestBody UserDTO userDetails) {
            try {
                UserDTO user = userService.updateUser(userDetails);
                return ResponseEntity.ok(new APIResponse<>(user, null));
            }
            catch (Exception e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new APIResponse<>(null, e.getMessage()));
            }
        }

        @DeleteMapping("/{userId}")
        public ResponseEntity<APIResponse> deleteUser(@PathVariable String userId) {

            try {
                userService.delete(userId);
                return ResponseEntity.ok(new APIResponse<>(null, null));
            }
            catch (Exception e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new APIResponse<>(null, e.getMessage()));
            }
        }

    }


