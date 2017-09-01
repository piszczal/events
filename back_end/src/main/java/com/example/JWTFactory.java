package com.example;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.TextCodec;

import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Szymon on 23.04.2017.
 */
public class JWTFactory {

    public String createHS256Token(String issuer, String subject, String moduleSecret) {
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

        byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary(TextCodec.BASE64.encode(moduleSecret));
        Key signingKey = new SecretKeySpec(apiKeySecretBytes, signatureAlgorithm.getJcaName());

        Map<String, Object> headerClaims = new HashMap();
        headerClaims.put("typ", "JWT");

        JwtBuilder builder = Jwts.builder()
                .setHeader(headerClaims)
                .setExpiration(new Date(System.currentTimeMillis() + 10000 * 6000))
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setSubject(subject)
                .setIssuer(issuer)
                .signWith(signatureAlgorithm, signingKey);

        return builder.compact();
    }

    public void parseJWTToken(String token, String moduleSecret) {
        Claims claims = Jwts.parser()
                .setSigningKey(DatatypeConverter.parseBase64Binary(TextCodec.BASE64.encode(moduleSecret)))
                .parseClaimsJws(token).getBody();
    }
}
