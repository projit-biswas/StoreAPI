package com.projit.storeApp.payments;

import com.projit.storeApp.common.SecurityRules;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AuthorizeHttpRequestsConfigurer;
import org.springframework.stereotype.Component;

@Component
public class PaymentSecurityRules implements SecurityRules {
	@Override
	public void configure(AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry registry) {
		registry.requestMatchers(HttpMethod.POST, "/api/checkout/webhook").permitAll();
	}
}
