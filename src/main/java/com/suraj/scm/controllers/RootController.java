package com.suraj.scm.controllers;

import com.suraj.scm.entities.User;
import com.suraj.scm.helpers.EmailFinder;
import com.suraj.scm.services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class RootController {

	private static final Logger logger = LoggerFactory.getLogger(RootController.class);

	@Autowired
	private UserService userService;

	@ModelAttribute
	public void addLoggedInUserToModel(Model model, Authentication authentication) {
		if (authentication == null || !authentication.isAuthenticated()) {
			logger.info("No user is logged in");
			return;
		}
		String username = EmailFinder.getEmailOfLoggedInUser(authentication);
		logger.info("Logged in User: {}", username);

		User user = userService.getUserByEmail(username);
		if (user == null) {
			logger.error("User not found with email: {}", username);
		} else {
			logger.info("User Name: {}", user.getName());
			logger.info("User Email: {}", user.getEmail());
		}
		model.addAttribute("loggedInUser", user);
	}
}
