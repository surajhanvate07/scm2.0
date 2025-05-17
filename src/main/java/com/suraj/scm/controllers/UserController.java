package com.suraj.scm.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/user")
public class UserController {

	@RequestMapping(value = "/dashboard", method = RequestMethod.GET)
	public String userDashboard() {
		return "user/userDashboard";
	}

	@RequestMapping(value = "/profile", method = RequestMethod.GET)
	public String userProfile() {
		return "user/profile";
	}

}
