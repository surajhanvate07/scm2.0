package com.suraj.scm.controllers;

import com.suraj.scm.entities.User;
import com.suraj.scm.forms.UserForm;
import com.suraj.scm.helpers.Message;
import com.suraj.scm.helpers.MessageType;
import com.suraj.scm.services.UserService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
public class PageController {

	@Autowired
	private UserService userService;

	@GetMapping({"/home", "/"})
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
	public String processRegistration(@Valid @ModelAttribute UserForm userForm, BindingResult bindingResult, HttpSession session) {
		System.out.println("Processing registration");

		// Validate the user form data
		if (bindingResult.hasErrors()) {
			System.out.println("Validation errors occurred");
			return "register";
		}

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
