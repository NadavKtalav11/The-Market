package DomainLayer.AuthorizationsAndSecurity;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class TokensService {
    private static final String SECRET_KEY = "your-256-bit-secret"; // Use a strong secret key
    private static final long TOKEN_VALIDITY_DURATION = 3600 * 1000; // 1 hour in milliseconds
    private Map<Integer , String > tokens ;
    final private Object tokensLock= new Object();

    public TokensService() {
        tokens = new HashMap<>();
        // Constructor
    }

    public String generateToken(int userId) {
        long now = System.currentTimeMillis();
        Date expiryDate = new Date(now + TOKEN_VALIDITY_DURATION);
        String currToken = Jwts.builder()
                .setSubject(String.valueOf(userId))
                .setIssuedAt(new Date(now))
                .setExpiration(expiryDate)
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();
        synchronized (tokensLock) {
            tokens.put(userId, currToken);
        }
        return currToken;
    }

    public boolean validateToken(String token) {
        try {
            Claims claims = Jwts.parser().setSigningKey(SECRET_KEY).build().parseClaimsJws(token).getBody();
            Date expiration = claims.getExpiration();
            int userId= getUserId(token);
            if (expiration.after(new Date())){
                synchronized (tokensLock){
                    generateToken(userId);
                }
                return true;
            }
            else{
                return false;
            }
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    public String getToken(int userId){
        synchronized (tokensLock) {
            return tokens.get(userId);
        }
    }
    public void removeToken(int userId){
        synchronized (tokensLock){
            tokens.remove(userId);
        }
    }

    public int getUserId(String token) {
        Claims claims = Jwts.parser().setSigningKey(SECRET_KEY).build().parseClaimsJws(token).getBody();
        return Integer.parseInt(claims.getSubject());
    }
}
