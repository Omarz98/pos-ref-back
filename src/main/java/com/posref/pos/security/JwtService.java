package com.posref.pos.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {

    @Value("${security.jwt.secret}")
    private String secret;

    @Value("${security.jwt.expiration}")
    private long expiration;

    public String generarToken(UsuarioPrincipal usuario) {

        return Jwts.builder()
                .claims(Map.of(
                        "usuarioId", usuario.getId(),
                        "nombre", usuario.getNombre(),
                        "authorities",
                        usuario.getAuthorities()
                                .stream()
                                .map(Object::toString)
                                .toList()
                ))
                .subject(usuario.getUsername())
                .issuedAt(new Date())
                .expiration(
                        new Date(
                                System.currentTimeMillis() + expiration
                        )
                )
                .signWith(getSigningKey())
                .compact();
    }

    public String obtenerUsername(String token) {
        return obtenerClaim(
                token,
                Claims::getSubject
        );
    }

    public boolean tokenValido(
            String token,
            UserDetails userDetails
    ) {

        String username = obtenerUsername(token);

        return username.equals(userDetails.getUsername())
                && !tokenExpirado(token);
    }

    private boolean tokenExpirado(String token) {
        return obtenerExpiracion(token)
                .before(new Date());
    }

    private Date obtenerExpiracion(String token) {
        return obtenerClaim(
                token,
                Claims::getExpiration
        );
    }

    private <T> T obtenerClaim(
            String token,
            Function<Claims, T> resolver
    ) {

        Claims claims = Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();

        return resolver.apply(claims);
    }

    private SecretKey getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secret);

        return Keys.hmacShaKeyFor(keyBytes);
    }
}
