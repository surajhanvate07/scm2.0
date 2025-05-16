package com.suraj.scm.controllers;

import com.suraj.scm.entities.User;
import com.suraj.scm.forms.UserForm;
import com.suraj.scm.helpers.Message;
import com.suraj.scm.helpers.MessageType;
import com.suraj.scm.services.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class PageController {

	@Autowired
	private UserService userService;

	@GetMapping("/home")
	public String home(Model model) {
		model.addAttribute("title", "Home Page");
		System.out.println("Home page accessed");
		return "home";
	}

	@RequestMapping(value = "/about", method = RequestMethod.GET)
	public String about() {
		return "about";
	}

	@RequestMapping(value = "/services", method = RequestMethod.GET)
	public String service() {
		return "services";
	}

	@RequestMapping(value = "/contact", method = RequestMethod.GET)
	public String contact() {
		return "contact";
	}

	@GetMapping("/login")
	public String login() {
		return "login";
	}

	@GetMapping("/register")
	public String register(Model model) {
		UserForm userForm = new UserForm();
		model.addAttribute("userForm", userForm);
		return "register";
	}

	@PostMapping("/do-register")
	public String processRegistration(@ModelAttribute UserForm userForm, HttpSession session) {
		System.out.println("Processing registration");
//		User user = User.builder()
//				.name(userForm.getName())
//				.email(userForm.getEmail())
//				.password(userForm.getPassword())
//				.phoneNumber(userForm.getPhoneNumber())
//				.about(userForm.getAbout())
//				.build();
		User user = new User();
		user.setName(userForm.getName());
		user.setEmail(userForm.getEmail());
		user.setPassword(userForm.getPassword());
		user.setPhoneNumber(userForm.getPhoneNumber());
		user.setAbout(userForm.getAbout());
		user.setProfilePic("default.png");

		User savedUser = userService.saveUser(user);

		Message message = Message.builder()
				.content("Registration successful")
				.type(MessageType.green)
				.build();

		if (savedUser != null) {
			session.setAttribute("message", message);
		}
		return "redirect:/register";
	}
}
