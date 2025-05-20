package com.suraj.scm.config;

import com.suraj.scm.entities.User;
import com.suraj.scm.enums.Providers;
import com.suraj.scm.helpers.AppConstants;
import com.suraj.scm.repositories.UserRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Component
public class OAuthSuccessHandler implements AuthenticationSuccessHandler {

	Logger logger = LoggerFactory.getLogger(OAuthSuccessHandler.class);

	@Autowired
	private UserRepository userRepository;

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
		logger.info("OAuthSuccessHandler: User authenticated successfully");

		OAuth2AuthenticationToken oAuth2AuthenticationToken = (OAuth2AuthenticationToken) authentication;
		String clientName = oAuth2AuthenticationToken.getAuthorizedClientRegistrationId();
		logger.info("OAuthSuccessHandler: Client Name: " + clientName);

		// Save the data in DB
		DefaultOAuth2User oauth2User = (DefaultOAuth2User) authentication.getPrincipal();

		User user = new User();
		user.setUserId(UUID.randomUUID().toString());
		user.setPassword("password");
		user.setEnabled(true);
		user.setEmailVerified(true);
		user.setRoles(List.of(AppConstants.ROLE_USER));

		if(clientName.equalsIgnoreCase("google")) {
			user.setEmail(oauth2User.getAttribute("email").toString());
			user.setName(oauth2User.getAttribute("name").toString());
			user.setProfilePic(oauth2User.getAttribute("picture").toString());
			user.setProvider(Providers.GOOGLE);
			user.setProviderId(oauth2User.getName());
			user.setAbout("This is created using google oauth2");
		} else if(clientName.equalsIgnoreCase("github")) {
			String email = oauth2User.getAttribute("email").toString() != null ?
					oauth2User.getAttribute("email").toString() : oauth2User.getAttribute("login").toString() + "@gmail.com";
			String picture = oauth2User.getAttribute("avatar_url").toString();
			String name = oauth2User.getAttribute("login").toString();

			user.setEmail(email);
			user.setName(name);
			user.setProfilePic(picture);
			user.setProvider(Providers.GITHUB);
			user.setProviderId(oauth2User.getName());
			user.setAbout("This is created using github oauth2");
		}

		User savedUser = userRepository.findByEmail(user.getEmail()).orElse(null);
		if (savedUser == null) {
			userRepository.save(user);
			logger.info("OAuthSuccessHandler: User saved successfully");
		} else {
			logger.info("OAuthSuccessHandler: User already exists");
		}

		new DefaultRedirectStrategy().sendRedirect(request, response, "/user/dashboard");
	}
}
