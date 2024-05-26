package DomainLayer.AuthorizationsAndSecurity;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.nio.charset.Charset;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.*;

public class TokensService {
    private final String SECRET_KEY  ; // Use a strong secret key
    private final long TOKEN_VALIDITY_DURATION = 3600 * 1000; // 1 hour in milliseconds
    private Map<Integer , String > tokens ;
    final private Object tokensLock= new Object();

    public TokensService() {
        tokens = new HashMap<>();
        SECRET_KEY =generateSecretKey(32);

        // Constructor
    }

    private String generateSecretKey(int keyLength) {
        try {
            SecureRandom secureRandom = SecureRandom.getInstanceStrong(); // Use a strong instance
            byte[] key = new byte[keyLength];
            secureRandom.nextBytes(key);
            return Base64.getEncoder().encodeToString(key);
        }// Encode the key as a Base64 string
        catch (NoSuchAlgorithmException E){
            byte[] array = new byte[32]; // length is bounded by 7
            new Random().nextBytes(array);
            return new String(array, Charset.forName("UTF-8"));
        }
    }



    public String generateToken(int memberId) {
        long now = System.currentTimeMillis();
        Date expiryDate = new Date(now + TOKEN_VALIDITY_DURATION);
        String currToken = Jwts.builder()
                .setSubject(String.valueOf(memberId))
                .setIssuedAt(new Date(now))
                .setExpiration(expiryDate)
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();
        synchronized (tokensLock) {
            tokens.put(memberId, currToken);
        }
        return currToken;
    }

    public boolean validateToken(String token) {
        try {
            Claims claims = Jwts.parser().setSigningKey(SECRET_KEY).build().parseClaimsJws(token).getBody();
            Date expiration = claims.getExpiration();
            int memberId= getMemberId(token);
            if (expiration.after(new Date())){
                synchronized (tokensLock){
                    generateToken(memberId);
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

    public String getToken(int memberId){
        synchronized (tokensLock) {
            return tokens.get(memberId);
        }
    }
    public void removeToken(int memberId){
        synchronized (tokensLock){
            tokens.remove(memberId);
        }
    }

    public int getMemberId(String token) {
        Claims claims = Jwts.parser().setSigningKey(SECRET_KEY).build().parseClaimsJws(token).getBody();
        return Integer.parseInt(claims.getSubject());
    }
}
