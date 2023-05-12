package com.asb.playgroundJava11.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.Serializable;
import java.io.StringReader;
import java.nio.file.Files;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;
import java.util.Date;
import java.util.Map;
import java.util.function.Function;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class JwtTokenUtil implements Serializable {

	private static final long serialVersionUID = -2550185165626007488L;

	public static final long JWT_TOKEN_VALIDITY = 5 * 60 * 60;

	public String getSubjectFromToken(String token) throws Exception {
		return getClaimFromToken(token, Claims::getSubject);
	}

	//retrieve expiration date from jwt token
	public Date getExpirationDateFromToken(String token) throws Exception {
		return getClaimFromToken(token, Claims::getExpiration);
	}

	public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) throws Exception {
		final Claims claims = getAllClaimsFromToken(token);
		return claimsResolver.apply(claims);
	}
    //for retrieveing any information from token we will need the secret key
	public Claims getAllClaimsFromToken(String token) throws Exception {
		return Jwts.parser().setSigningKey(getPrivateKey()).parseClaimsJws(token).getBody();
	}

	//check if the token has expired
	private Boolean isTokenExpired(String token) throws Exception {
		final Date expiration = getExpirationDateFromToken(token);
		return expiration.before(new Date());
	}

	//while creating the token -
	//1. Define  claims of the token, like Issuer, Expiration, Subject, and the ID
	//2. Sign the JWT using the HS512 algorithm and secret key.
	//3. According to JWS Compact Serialization(https://tools.ietf.org/html/draft-ietf-jose-json-web-signature-41#section-3.1)
	//   compaction of the JWT to a URL-safe string 
	public String doGenerateToken(Map<String, Object> claims, String subject) throws Exception {

		return Jwts.builder().setClaims(claims).setSubject(subject).setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(new Date(System.currentTimeMillis() + JWT_TOKEN_VALIDITY * 1000))
				.signWith(SignatureAlgorithm.RS256, getPrivateKey()).compact();
	}

	//validate token
	public Boolean validateToken(String token) throws Exception {
		final String username = getSubjectFromToken(token);
		return (!username.isBlank() && !isTokenExpired(token));
	}

    private PrivateKey getPrivateKey() throws Exception {
        File privateKeyFile = new File("private.key");
        byte[] privateKeyBytes = Files.readAllBytes(privateKeyFile.toPath());
        // Read in the key into a String
        StringBuilder pkcs8Lines = new StringBuilder();
        BufferedReader rdr = new BufferedReader(new StringReader(
            new String(privateKeyBytes, "UTF-8")
            ));
        String line;
        while ((line = rdr.readLine()) != null) {
            pkcs8Lines.append(line);
        }
        // Remove the "BEGIN" and "END" lines, as well as any whitespace
        String pkcs8Pem = pkcs8Lines.toString();
        pkcs8Pem = pkcs8Pem.replace("-----BEGIN PRIVATE KEY-----", "");
        pkcs8Pem = pkcs8Pem.replace("-----END PRIVATE KEY-----", "");
        pkcs8Pem = pkcs8Pem.replaceAll("\\s+", "");
        // Base64 decode the result
        byte[] pkcs8EncodedBytes = Base64.getDecoder().decode(pkcs8Pem);
        // extract the private key
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(pkcs8EncodedBytes);
        KeyFactory kf = KeyFactory.getInstance("RSA");
        PrivateKey privKey = kf.generatePrivate(keySpec);
        return privKey;
    }

    private PublicKey getPublicKey() throws Exception {
        File publicKeyFile = new File("public.key");
        byte[] publicKeyBytes = Files.readAllBytes(publicKeyFile.toPath());
        // Read in the key into a String
        StringBuilder pkcs8Lines = new StringBuilder();
        BufferedReader rdr = new BufferedReader(new StringReader(            
            new String(publicKeyBytes, "UTF-8")
        ));
        String line;
        while ((line = rdr.readLine()) != null) {
            pkcs8Lines.append(line);
        }
        // Remove the "BEGIN" and "END" lines, as well as any whitespace
        String pkcs8Pem = pkcs8Lines.toString();
        pkcs8Pem = pkcs8Pem.replace("-----BEGIN PUBLIC KEY-----", "");
        pkcs8Pem = pkcs8Pem.replace("-----END PUBLIC KEY-----", "");
        pkcs8Pem = pkcs8Pem.replaceAll("\\s+", "");
        // Base64 decode the result
        byte[] pkcs8EncodedBytes = Base64.getDecoder().decode(pkcs8Pem);
        // extract the private key
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(pkcs8EncodedBytes);
        KeyFactory kf = KeyFactory.getInstance("RSA");
        PublicKey publicKey = kf.generatePublic(keySpec);
        return publicKey;
    }

    public String removeBearer(String bearerToken) {
        return bearerToken.replaceAll("Bearer ", "");
    }
}
