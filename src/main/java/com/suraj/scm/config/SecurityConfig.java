package com.suraj.scm.config;

import com.suraj.scm.services.impl.SecurityCustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

	@Autowired
	private SecurityCustomUserDetailsService securityCustomUserDetailsService;

	@Autowired
	private OAuthSuccessHandler oAuthSuccessHandler;

	@Bean
	public AuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
		daoAuthenticationProvider.setUserDetailsService(securityCustomUserDetailsService);
		daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
		return daoAuthenticationProvider;
	}

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
		httpSecurity
				.authorizeHttpRequests(authorize -> authorize
						.requestMatchers("/admin/**").hasRole("ADMIN")
						.requestMatchers("/user/**").authenticated()
						.requestMatchers("/login", "/css/**", "/js/**", "/images/**", "/**").permitAll()
				)
				.formLogin(form -> form
						.loginPage("/login")                    // Your custom login page
						.loginProcessingUrl("/authenticate")    // URL for login form POST
						.defaultSuccessUrl("/user/dashboard", false)
						.failureUrl("/login?error=true")
						.usernameParameter("email")
						.passwordParameter("password")
						.permitAll()
				);
		httpSecurity.csrf(AbstractHttpConfigurer::disable);
		httpSecurity.logout(logout-> {
			logout
					.logoutUrl("/logout")
					.logoutSuccessUrl("/login?logout=true")
					.deleteCookies("JSESSIONID")
					.permitAll();
		});

		// OAuth2 login configuration
		httpSecurity.oauth2Login(oauth -> {
			oauth.loginPage("/login")
					.successHandler(oAuthSuccessHandler)
					.permitAll();
		});
		return httpSecurity.build();
	}


	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
}
