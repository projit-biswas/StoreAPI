package com.projit.storeApp.secvices;


import com.projit.storeApp.entities.Role;
import io.jsonwebtoken.Claims;

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

}
