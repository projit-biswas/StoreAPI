package com.projit.storeApp.services;


import com.projit.storeApp.entities.Role;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

import javax.crypto.SecretKey;
import java.util.Date;

public class Jwt {
	private final Claims claims;
	private final SecretKey secret;

	public Jwt(Claims claims, SecretKey secret) {
		this.claims = claims;
		this.secret = secret;
	}

	public boolean isExpired() {
		return claims.getExpiration().before(new Date());
	}

	public Long getUserId() {
		return Long.valueOf(claims.getSubject());
	}

	public Role getRole() {
		return Role.valueOf(claims.get("role", String.class));
	}

	public String toString() {
		return Jwts.builder().claims(claims).signWith(secret).compact();
	}

}
