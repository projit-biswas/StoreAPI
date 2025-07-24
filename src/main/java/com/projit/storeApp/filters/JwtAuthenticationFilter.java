package com.projit.storeApp.filters;

import com.projit.storeApp.secvices.Jwt;
import com.projit.storeApp.secvices.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
@AllArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
	private final JwtService jwtService;
	private final Jwt jwt;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
		var authHeader = request.getHeader("Authorization");
		if (authHeader == null || !authHeader.startsWith("Bearer ")) {
			filterChain.doFilter(request,response);
			return;
		}

		var token = authHeader.replace("Bearer ", "");
		var jwt = jwtService.parseToken(token);
		if (jwt == null || jwt.isExpired()) {
			filterChain.doFilter(request,response);
			return;
		}

		var authentication = new UsernamePasswordAuthenticationToken(
				this.jwt.getUserId(),
				null,
				List.of(new SimpleGrantedAuthority("ROLE_" + this.jwt.getRole()))
		);

		authentication.setDetails(
				new WebAuthenticationDetailsSource().buildDetails(request)
		);
		SecurityContextHolder.getContext().setAuthentication(authentication);
		filterChain.doFilter(request,response);
	}
}
