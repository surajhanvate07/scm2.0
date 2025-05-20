package com.suraj.scm.controllers;

import com.suraj.scm.helpers.EmailFinder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;


@Controller
@RequestMapping("/user")
public class UserController {

	private static final Logger logger = LoggerFactory.getLogger(UserController.class);

	@RequestMapping(value = "/dashboard", method = RequestMethod.GET)
	public String userDashboard() {
		return "user/userDashboard";
	}

	@RequestMapping(value = "/profile", method = RequestMethod.GET)
	public String userProfile(Authentication authentication) {
		String username = EmailFinder.getEmailOfLoggedInUser(authentication);
		logger.info("Logged in User: {}", username);
		return "user/profile";
	}

}
