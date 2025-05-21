package com.suraj.scm.controllers;

import com.suraj.scm.entities.User;
import com.suraj.scm.helpers.EmailFinder;
import com.suraj.scm.services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;


@Controller
@RequestMapping("/user")
public class UserController {

	@Autowired
	private UserService userService;

	private static final Logger logger = LoggerFactory.getLogger(UserController.class);

	@RequestMapping(value = "/dashboard", method = RequestMethod.GET)
	public String userDashboard() {
		return "user/user_dashboard";
	}

	@RequestMapping(value = "/profile", method = RequestMethod.GET)
	public String userProfile() {
		return "user/profile";
	}

}
