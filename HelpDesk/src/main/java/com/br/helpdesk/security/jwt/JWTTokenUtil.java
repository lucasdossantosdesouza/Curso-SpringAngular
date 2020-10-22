package com.br.helpdesk.security.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Value;

import java.io.Serializable;
import java.util.Date;

public class JWTTokenUtil implements Serializable {

    private static String CLEAN_KEY_USERNAME= "sub";
    private static String CLEAN_KEY_CREATE= "created";
    private static String CLEAN_KEY_EXPIRED= "exp";

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private Long expiration;

    public String getUserNameFromToken(String token){
        String username = null;
        try{
            final Claims claims = getClaimsFromToken(token);
            username = claims.getSubject();
        }catch (Exception e){

        }
        return username;
    }

    public Date getExpirationDataFromToken(String token){
        Date expiration = null;
        try{
            final Claims claims = getClaimsFromToken(token);
            expiration = claims.getExpiration();
        }catch (Exception e){

        }
        return expiration;
    }

    private Claims getClaimsFromToken(String token){
        Claims claims = null;
        try{
            claims = Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
        }catch (Exception e){

        }
        return claims;
    }
}
