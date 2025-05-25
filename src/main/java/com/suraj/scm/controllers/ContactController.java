package com.suraj.scm.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/user/contacts")
public class ContactController {


	@GetMapping("/add")
	public String addContact() {
		return "user/add_contact";
	}
}
