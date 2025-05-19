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

		// Save the data in DB
		DefaultOAuth2User oauth2User = (DefaultOAuth2User) authentication.getPrincipal();
		String email = oauth2User.getAttribute("email").toString();
		String name = oauth2User.getAttribute("name").toString();
		String picture = oauth2User.getAttribute("picture").toString();

		User user = new User();
		user.setEmail(email);
		user.setName(name);
		user.setProfilePic(picture);
		user.setPassword("password");
		user.setUserId(UUID.randomUUID().toString());
		user.setEnabled(true);
		user.setEmailVerified(true);
		user.setProvider(Providers.GOOGLE);
		user.setProviderId(oauth2User.getName());
		user.setRoles(List.of(AppConstants.ROLE_USER));
		user.setAbout("This is created using google oauth2");

		User savedUser = userRepository.findByEmail(email).orElse(null);
		if (savedUser == null) {
			userRepository.save(user);
			logger.info("OAuthSuccessHandler: User saved successfully");
		} else {
			logger.info("OAuthSuccessHandler: User already exists");
		}

		new DefaultRedirectStrategy().sendRedirect(request, response, "/user/dashboard");
	}
}
