package com.Ashish.ProjectManagementSystem.AppConfig;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.Authentication;

import javax.crypto.SecretKey;
import java.util.Base64;
import java.util.Date;

public class JwtProvider {

    static SecretKey keys = Keys.hmacShaKeyFor(Base64.getDecoder().decode(JwtConstant.SECRET_KEY));



    public static String generateToken(Authentication auth){

        return Jwts.builder().setIssuedAt(new Date())
                .setExpiration(new Date(new Date().getTime()+86400000))
                .claim("email", auth.getName())
                .signWith(keys)
                .compact();
    }

    public static String getGetEmailFromToken(String jwt){

         Claims claims;
         claims = Jwts.parser().setSigningKey(keys).build().parseClaimsJws(jwt).getBody();

        return String.valueOf(claims.get("email"));
    }
}
