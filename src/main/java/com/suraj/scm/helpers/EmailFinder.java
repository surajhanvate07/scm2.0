package com.suraj.scm.helpers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;

public class EmailFinder {

	private static final Logger logger = LoggerFactory.getLogger(EmailFinder.class);

	public static String getEmailOfLoggedInUser(Authentication authentication) {
		String email = null;

		if (authentication instanceof OAuth2AuthenticationToken) {
			OAuth2AuthenticationToken oauth2Token = (OAuth2AuthenticationToken) authentication;
			String client = oauth2Token.getAuthorizedClientRegistrationId();
			logger.info("Client: {}", client);
			DefaultOAuth2User oauth2User = (DefaultOAuth2User) authentication.getPrincipal();

			if(client.equalsIgnoreCase("google")) {
				logger.info("Google OAuth2 Authentication");
				email = oauth2User.getAttribute("email").toString();
			} else if(client.equalsIgnoreCase("github")) {
				logger.info("GitHub OAuth2 Authentication");
				email = oauth2User.getAttribute("email").toString() != null ?
						oauth2User.getAttribute("email").toString() : oauth2User.getAttribute("login").toString() + "@gmail.com";
			}
		} else {
			email = authentication.getName();
		}
		return email;
	}
}
