package DomainLayer.AuthorizationsAndSecurity;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Date;

public class TokensService {
    private static final String SECRET_KEY = "your-256-bit-secret"; // Use a strong secret key
    private static final long TOKEN_VALIDITY_DURATION = 3600 * 1000; // 1 hour in milliseconds

    public TokensService() {
        // Constructor
    }

    public String generateToken(int guestId) {
        long now = System.currentTimeMillis();
        Date expiryDate = new Date(now + TOKEN_VALIDITY_DURATION);

        return Jwts.builder()
                .setSubject(String.valueOf(guestId))
                .setIssuedAt(new Date(now))
                .setExpiration(expiryDate)
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();
    }

    public boolean validateToken(String token) {
        try {
            Claims claims = Jwts.parser().setSigningKey(SECRET_KEY).build().parseClaimsJws(token).getBody();
            Date expiration = claims.getExpiration();
            return expiration.after(new Date());
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    public int getGuestId(String token) {
        Claims claims = Jwts.parser().setSigningKey(SECRET_KEY).build().parseClaimsJws(token).getBody();
        return Integer.parseInt(claims.getSubject());
    }
}
