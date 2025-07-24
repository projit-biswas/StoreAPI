package com.projit.storeApp.secvices;

import com.projit.storeApp.config.JwtConfig;
import com.projit.storeApp.entities.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;

@AllArgsConstructor
@Service
public class JwtService {

	private final JwtConfig jwtConfig;

	public String generateAccessToken(User user) {
		return generateToken(user, jwtConfig.getAccessTokenExpiration());
	}

	public String generateRefreshToken(User user) {
		return generateToken(user, jwtConfig.getRefreshTokenExpiration());
	}

	private String generateToken(User user, long tokenExpiration) {
		return Jwts.builder()
				.subject(user.getId().toString())
				.claim("email", user.getEmail())
				.claim("name", user.getName())
				.issuedAt(new Date())
				.expiration(new Date(System.currentTimeMillis() + 1000 * tokenExpiration))
				.signWith(jwtConfig.getSecretKey())
				.compact();
	}

	public boolean validateToken(String token){
		try {
			var claims = getClaims(token);
			return claims.getExpiration().after(new Date());
		} catch (JwtException e) {
			return false;
		}
	}

	private Claims getClaims(String token) {
		return Jwts.parser()
				.verifyWith(jwtConfig.getSecretKey())
				.build()
				.parseClaimsJws(token)
				.getPayload();
	}


	public Long getUserIdFromToken(String token) {
		return Long.valueOf(getClaims(token).getSubject());
	}
}
