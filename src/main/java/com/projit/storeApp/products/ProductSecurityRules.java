package com.projit.storeApp.products;

import com.projit.storeApp.common.SecurityRules;
import com.projit.storeApp.users.Role;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AuthorizeHttpRequestsConfigurer;
import org.springframework.stereotype.Component;

@Component
public class ProductSecurityRules implements SecurityRules {
	@Override
	public void configure(AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry registry) {
		registry.requestMatchers(HttpMethod.GET, "/api/products/**").permitAll()
				.requestMatchers(HttpMethod.POST, "/api/products/**").permitAll()
				.requestMatchers(HttpMethod.PUT, "/api/products/**").hasRole(Role.ADMIN.name())
				.requestMatchers(HttpMethod.DELETE, "/api/products/**").hasRole(Role.ADMIN.name());
	}
}
