package com.synchrony.security;

import java.security.SignatureException;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import com.synchrony.entity.User;

import io.jsonwebtoken.*;
import lombok.extern.log4j.Log4j;

@Component
@Log4j
public class JwtTokenProvider {

	private static final Logger log = LoggerFactory.getLogger(JwtTokenProvider.class);
	@Value("${app.jwtSecret}")
	private String jwtSecret;

	@Value("${app.jwtExpirationInMs}")
	private int jwtExpirationInMs;
	@SuppressWarnings("unused")
	private UserPrincipal userPrincipal;

	public String generateToken(Authentication authentication, User user) {

		userPrincipal = (UserPrincipal) authentication.getPrincipal();
		Date expiryDate = new Date(System.currentTimeMillis() + jwtExpirationInMs);
		return Jwts.builder().setSubject(Long.toString(System.currentTimeMillis() + jwtExpirationInMs))
				.setId(user.getUserName()).setIssuedAt(new Date()).setExpiration(expiryDate)
				.signWith(SignatureAlgorithm.HS512, jwtSecret).compact();
	}

	public Long getUserIdFromJWT(String token) {
		Claims claims = Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody();

		return Long.parseLong(claims.getSubject());
	}

	public boolean validateToken(String authToken) {
		try {
			Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(authToken);
			return true;
		} catch (io.jsonwebtoken.SignatureException ex) {
			log.error("Invalid JWT signature");
		} catch (MalformedJwtException ex) {
			log.error("Invalid JWT token");
		} catch (ExpiredJwtException ex) {
			log.error("Expired JWT token");
		} catch (UnsupportedJwtException ex) {
			log.error("Unsupported JWT token");
		} catch (IllegalArgumentException ex) {
			log.error("JWT claims string is empty.");
		}
		return false;
	}
}
