package com.Ashish.ProjectManagementSystem.AppConfig;


import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;
import java.util.Base64;

public class JwtConstant {

    public static final String SECRET_KEY;

    static {
        // Generate a secure key for HS256 (minimum 256 bits)
        SecretKey key = Keys.secretKeyFor(SignatureAlgorithm.HS256);
        SECRET_KEY = Base64.getEncoder().encodeToString(key.getEncoded());
    }

//
//    public static final String SECRET_KEY = "ajfadjflkj";
    public static final String JWT_HEADER ="Authorization";
}
