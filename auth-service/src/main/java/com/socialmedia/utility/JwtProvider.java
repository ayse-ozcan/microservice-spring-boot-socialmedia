package com.socialmedia.utility;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.socialmedia.exception.AuthManagerException;
import com.socialmedia.exception.ErrorType;
import com.socialmedia.repository.enums.ERole;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;

@Service
public class JwtProvider {
    String audience = "socialmedia";
    String issuer = "bilgeadam";
    String secretKey = "secretKey";

    //id ve rol bilgisiyle token uretmek icin kullanilir.
    public Optional<String> createToken(Long id, ERole eRole) {
        String token = null;
        Date date = new Date(System.currentTimeMillis() + (1000L * 60 * 5));
        try {
            token = JWT.create()
                    .withAudience(audience)
                    .withIssuer(issuer)
                    .withIssuedAt(new Date()) // token nin olusturldugu zaman
                    .withExpiresAt(date)
                    .withClaim("id", id)
                    .withClaim("role", eRole.toString())
                    .sign(Algorithm.HMAC512(secretKey));
            return Optional.of(token);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    public Optional<Long> getIdFromToken(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC512(secretKey);
            // Metoda verilen token bizim belirlemis oldugumuz standartlara gore kontrol edilir.
            JWTVerifier verifier = JWT.require(algorithm).withAudience(audience)
                    .withIssuer(issuer).build();
            // token dogrulanirsa decode edilecektir.
            DecodedJWT decodedJWT = verifier.verify(token);
            if (decodedJWT == null) {
                throw new AuthManagerException(ErrorType.INVALID_TOKEN);
            }
            Long id = decodedJWT.getClaim("id").asLong();
            return Optional.of(id);
        } catch (Exception e) {
            throw new AuthManagerException(ErrorType.INVALID_TOKEN);
        }
    }
}
