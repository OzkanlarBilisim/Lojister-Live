package com.lojister.core.security;

import com.lojister.model.entity.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.function.Function;

@Component
public class JwtTokenUtil {

    // 1Gün    MILISANIYE CINSINDEN
    public static final long ACCESS_TOKEN_VALIDITY_SECONDS = 24 * 60 * 60 * 1000;
    public static final String SIGNING_KEY = "aselsis";

    public String getUsernameFromToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }

    public Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }

    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    private Claims getAllClaimsFromToken(String token) {
        return Jwts.parser()
                .setSigningKey(SIGNING_KEY)
                .parseClaimsJws(token)
                .getBody();
    }

    private Boolean isTokenExpired(String token) {
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }

    public String generateToken(User userDetails, boolean rememberMe) {

        return doGenerateToken(userDetails, userDetails.getPhone(), rememberMe);
    }

    private String doGenerateToken(User user, String subject, boolean rememberMe) {

        Claims claims = Jwts.claims().setSubject(subject);
        claims.put("scopes", user.getRole());
        claims.put("id", user.getId());

        long numDays = rememberMe ? 14 : 1;

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuer("http://lojister.com")
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + (ACCESS_TOKEN_VALIDITY_SECONDS * numDays)))
                .signWith(SignatureAlgorithm.HS256, SIGNING_KEY)
                .compact();
    }

    public Boolean validateToken(String token, UserDetails userDetails) {
        final String phone = getUsernameFromToken(token);
        return (
                phone.equals(userDetails.getUsername())
                        && !isTokenExpired(token));
    }
}
