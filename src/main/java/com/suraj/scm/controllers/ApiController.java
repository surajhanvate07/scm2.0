package com.suraj.scm.controllers;

import com.suraj.scm.entities.Contact;
import com.suraj.scm.services.ContactService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class ApiController {

	@Autowired
	private ContactService contactService;

	@GetMapping("/contacts/{contactId}")
	public Contact getContactById(@PathVariable("contactId") String contactId) {
		return contactService.getContactById(contactId);
	}
}
