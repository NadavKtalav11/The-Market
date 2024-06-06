package ServiceLayer;

import DomainLayer.User.UserFacade;
import Util.UserDTO;

import java.util.List;

public class UserService {

    private UserFacade userFacade;
    public UserService (){
        UserFacade.getInstance();
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
}
