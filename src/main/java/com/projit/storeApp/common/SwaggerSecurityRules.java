package com.projit.storeApp.common;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AuthorizeHttpRequestsConfigurer;
import org.springframework.stereotype.Component;

@Component
public class SwaggerSecurityRules implements SecurityRules {
	@Override
	public void configure(AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry registry) {
		registry.requestMatchers(
				"/swagger-ui/**",
				"/v3/api-docs/**",
				"/swagger-resources/**",
				"/swagger-ui.html",
				"/webjars/**"
		).permitAll();
	}
}
