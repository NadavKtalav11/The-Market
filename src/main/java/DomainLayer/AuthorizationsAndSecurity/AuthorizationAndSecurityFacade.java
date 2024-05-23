package DomainLayer.AuthorizationsAndSecurity;

import DomainLayer.Market.Market;

public class AuthorizationAndSecurityFacade {


    private PasswordEncryptor passwordEncryptor;
    private TokensService tokensService;
    private static AuthorizationAndSecurityFacade instance;

    public static AuthorizationAndSecurityFacade getInstance() {
        if (instance == null) {
            instance = new AuthorizationAndSecurityFacade();
        }
        return instance;
    }
    private AuthorizationAndSecurityFacade(){
        passwordEncryptor = new PasswordEncryptor();
        tokensService = new TokensService();
    }
    public String generateToken(int userId){
        return tokensService.generateToken(userId);
    }

    public String getToken(int userId){
        return tokensService.getToken(userId);
    }

    public String encodePassword(String password){
        return passwordEncryptor.encryptPassword(password);
    }



    public boolean validateToken(String token){
        return tokensService.validateToken(token);
    }





}