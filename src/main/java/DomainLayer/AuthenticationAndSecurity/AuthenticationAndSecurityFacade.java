package DomainLayer.AuthenticationAndSecurity;

public class AuthenticationAndSecurityFacade {


    private PasswordEncryptor passwordEncryptor;
    private TokensService tokensService;
    private static AuthenticationAndSecurityFacade instance;

    public synchronized static AuthenticationAndSecurityFacade getInstance() {
        if (instance == null) {
            instance = new AuthenticationAndSecurityFacade();
        }
        return instance;
    }
    private AuthenticationAndSecurityFacade() {
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

    public void removeToken(int userId){
        tokensService.removeToken(userId);
    }



    public boolean validateToken(String token){
        return tokensService.validateToken(token);
    }





}