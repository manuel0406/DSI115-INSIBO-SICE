package com.dsi.insibo.sice.Seguridad.Configuraciones;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import java.util.stream.Collectors;
import java.util.Date;
import java.util.Map;
import java.util.UUID;

@Component
public class JwtUtils {
    
    @Value("${security.jwt.key.private}")
    private String privateKey;          // Firma de autorización (TOKEN)

    @Value("${jwt.time.expiration}")
    private String timeExpiration;     // Tiempo de expiración (1-dia)

    @Value("${security.jwt.user.generator}")
    private String userGenerator;

    public String createToken(Authentication authentication){
        Algorithm algorithm = Algorithm.HMAC256(privateKey); // Algoritmo de encriptamiento.

        // Obtenemos el usuario autorizado
        String username = authentication.getPrincipal().toString();

        // Obtenemos permisos
        String authorities = authentication.getAuthorities().stream()
                                       .map(GrantedAuthority::getAuthority)
                                       .collect(Collectors.joining(","));
        String jwtToken = JWT.create()
                             .withIssuer(this.userGenerator)
                             .withSubject(username)
                             .withClaim("authorities", authorities)
                             .withIssuedAt(new Date())
                             .withExpiresAt(new Date(System.currentTimeMillis() + Long.parseLong(timeExpiration)))
                             .withJWTId(UUID.randomUUID().toString())
                             .withNotBefore(new Date(System.currentTimeMillis()))
                             .sign(algorithm);
        return jwtToken;
    }

    // METODO QUE VALIDA EL TOKEN
    public DecodedJWT validateToken(String token){
        try {
            Algorithm algorithm = Algorithm.HMAC256(privateKey); // Algoritmo de encriptamiento.
            JWTVerifier verifier = JWT.require(algorithm)
                                      .withIssuer(this.userGenerator)
                                      .build();
            DecodedJWT decodedJWT = verifier.verify(token);
            return decodedJWT;
        } catch (JWTVerificationException exception) {
            throw new JWTVerificationException("TOKEN INVALID, not Authorized");
        }
    }

    // OBTENER EL USUARIO DECODIFICADO
    public String extractUsername(DecodedJWT decodedJWT){
        return decodedJWT.getSubject().toString();
    }

    public Claim getSpecificClaim(DecodedJWT decodedJWT, String claimName){
        return decodedJWT.getClaim(claimName);
    }

    public Map<String, Claim> returnAllClaims(DecodedJWT decodedJWT){
        return decodedJWT.getClaims();
    }
}