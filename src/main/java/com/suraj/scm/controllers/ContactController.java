package com.suraj.scm.controllers;

import com.suraj.scm.entities.Contact;
import com.suraj.scm.entities.User;
import com.suraj.scm.forms.ContactForm;
import com.suraj.scm.forms.ContactSearchForm;
import com.suraj.scm.helpers.AppConstants;
import com.suraj.scm.helpers.EmailFinder;
import com.suraj.scm.helpers.Message;
import com.suraj.scm.helpers.MessageType;
import com.suraj.scm.services.ContactService;
import com.suraj.scm.services.ImageService;
import com.suraj.scm.services.UserService;
import com.suraj.scm.validator.OnCreate;
import com.suraj.scm.validator.OnUpdate;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Controller
@RequestMapping("/user/contacts")
public class ContactController {

	private final Logger logger = LoggerFactory.getLogger(ContactController.class);
	@Autowired
	private ContactService contactService;
	@Autowired
	private UserService userService;
	@Autowired
	private ImageService imageService;

	private static ContactForm getUpdateContactForm(Contact contact) {
		ContactForm contactForm = new ContactForm();
		contactForm.setName(contact.getName());
		contactForm.setEmail(contact.getEmail());
		contactForm.setPhoneNumber(contact.getPhoneNumber());
		contactForm.setAddress(contact.getAddress());
		contactForm.setDescription(contact.getDescription());
		contactForm.setFavorite(contact.isFavorite());
		contactForm.setWebsiteLink(contact.getWebsiteLink());
		contactForm.setLinkedInLink(contact.getLinkedInLink());
		contactForm.setPicture(contact.getPicture());
		return contactForm;
	}

	@GetMapping("/add")
	public String addContact(Model model) {
		ContactForm contactForm = new ContactForm();
		model.addAttribute("contactForm", contactForm);
		return "user/add_contact";
	}

	@PostMapping("/add")
	public String saveContact(@Validated(OnCreate.class) @ModelAttribute ContactForm contactForm, BindingResult bindingResult, Authentication authentication, HttpSession session) {
		logger.info("Received contact form submission: {}", contactForm);
		if (bindingResult.hasErrors()) {
			logger.info("Validation errors occurred");
			return "user/add_contact";
		}

		String userName = EmailFinder.getEmailOfLoggedInUser(authentication);
		User loggedUser = userService.getUserByEmail(userName);

		Contact contact = new Contact();
		contact.setName(contactForm.getName());
		contact.setEmail(contactForm.getEmail());
		contact.setPhoneNumber(contactForm.getPhoneNumber());
		contact.setAddress(contactForm.getAddress());
		contact.setDescription(contactForm.getDescription());
		contact.setFavorite(contactForm.isFavorite());
		contact.setWebsiteLink(contactForm.getWebsiteLink());
		contact.setLinkedInLink(contactForm.getLinkedInLink());
		contact.setUser(loggedUser);
		// Handle profile picture upload if provided
		if (contactForm.getContactPicture() != null && !contactForm.getContactPicture().isEmpty()) {
			String fileName = UUID.randomUUID().toString();
			String imageUploadedUrl = imageService.uploadImage(contactForm.getContactPicture(), fileName);
			contact.setCloudinaryImagePublicId(fileName);
			contact.setPicture(imageUploadedUrl);
		}

		Contact savedContact = contactService.saveContact(contact);

		Message message = null;
		if (savedContact != null) {
			logger.info("Contact saved successfully: {}", savedContact);
			message = Message.builder()
					.content("Contact added successfully!")
					.type(MessageType.green)
					.build();
			session.setAttribute("message", message);
			return "redirect:/user/contacts/add";
		} else {
			logger.error("Failed to save contact");
			message = Message.builder()
					.content("Failed to add contact. Please try again.")
					.type(MessageType.red)
					.build();
			session.setAttribute("message", message);
		}
		return "redirect:/user/contacts/add";
	}

	// Search Handler

	@GetMapping
	public String viewContacts(@RequestParam(value = "page", defaultValue = "0") int page,
							   @RequestParam(value = "size", defaultValue = "5") int size,
							   @RequestParam(value = "sortBy", defaultValue = "name") String sortBy,
							   @RequestParam(value = "sortDir", defaultValue = "asc") String sortDir,
							   Model model, Authentication authentication) {

		String userName = EmailFinder.getEmailOfLoggedInUser(authentication);
		User loggedUser = userService.getUserByEmail(userName);

		Page<Contact> pageContacts = contactService.getContactsByUserId(loggedUser.getUserId(), page, size, sortBy, sortDir);

		model.addAttribute("pageContacts", pageContacts);
		model.addAttribute("currentPage", page);
		model.addAttribute("pageSize", AppConstants.PAGE_SIZE);

		ContactSearchForm contactSearchForm = new ContactSearchForm();
		model.addAttribute("contactSearchForm", contactSearchForm);

		return "user/view_contacts";
	}

	@GetMapping("/search")
	public String searchHandler(@ModelAttribute ContactSearchForm contactSearchForm,
								@RequestParam(value = "page", defaultValue = "0") int page,
								@RequestParam(value = "size", defaultValue = "5") int size,
								@RequestParam(value = "sortBy", defaultValue = "name") String sortBy,
								@RequestParam(value = "sortDir", defaultValue = "asc") String sortDir,
								Model model, Authentication authentication) {

		User loggedUser = userService.getUserByEmail(EmailFinder.getEmailOfLoggedInUser(authentication));

		Page<Contact> searchedList = contactService.searchByField(contactSearchForm.getField(), contactSearchForm.getValue(), page, size, sortBy, sortDir, loggedUser);
		logger.info("Total contacts found: {}", searchedList.getTotalElements());

		model.addAttribute("pageContacts", searchedList);
		model.addAttribute("currentPage", page);
		model.addAttribute("pageSize", AppConstants.PAGE_SIZE);
		model.addAttribute("contactSearchForm", contactSearchForm);
		return "user/search_contacts";
	}

	@RequestMapping("/delete/{contactId}")
	public String deleteContact(@PathVariable("contactId") String contactId, HttpSession session) {
		logger.info("Attempting to delete contact with ID: {}", contactId);
		boolean isDeleted = contactService.deleteContact(contactId);
		Message message;

		if (isDeleted) {
			logger.info("Contact deleted successfully");
			message = Message.builder()
					.content("Contact deleted successfully!")
					.type(MessageType.green)
					.build();
		} else {
			logger.error("Failed to delete contact with ID: {}", contactId);
			message = Message.builder()
					.content("Failed to delete contact. Please try again.")
					.type(MessageType.red)
					.build();
		}

		session.setAttribute("message", message);
		return "redirect:/user/contacts";
	}

	@GetMapping("/edit/{contactId}")
	public String editContact(@PathVariable("contactId") String contactId, Model model) {
		logger.info("Editing contact with ID: {}", contactId);
		Contact contact = contactService.getContactById(contactId);
		if (contact == null) {
			logger.error("Contact not found with ID: {}", contactId);
			return "error";
		}

		ContactForm contactForm = getUpdateContactForm(contact);

		model.addAttribute("contactForm", contactForm);
		model.addAttribute("contactId", contactId);

		return "user/update_contact_modal";
	}

	@PostMapping("/update/{contactId}")
	public String updateContact(@PathVariable("contactId") String contactId, @Validated(OnUpdate.class) @ModelAttribute ContactForm contactForm, BindingResult result, Model model, HttpSession session) {
		if (result.hasErrors()) {
			logger.error("Validation errors occurred while updating contact");
			model.addAttribute("contactForm", contactForm);
			return "user/update_contact_modal";
		}

		Contact contact = contactService.getContactById(contactId);
		contact.setName(contactForm.getName());
		contact.setEmail(contactForm.getEmail());
		contact.setPhoneNumber(contactForm.getPhoneNumber());
		contact.setAddress(contactForm.getAddress());
		contact.setDescription(contactForm.getDescription());
		contact.setFavorite(contactForm.isFavorite());
		contact.setWebsiteLink(contactForm.getWebsiteLink());
		contact.setLinkedInLink(contactForm.getLinkedInLink());

		// Processing the contact image
		if (contactForm.getContactPicture() != null && !contactForm.getContactPicture().isEmpty()) {
			String fileName = UUID.randomUUID().toString();
			String imageUploadedUrl = imageService.uploadImage(contactForm.getContactPicture(), fileName);
			contact.setCloudinaryImagePublicId(fileName);
			contact.setPicture(imageUploadedUrl);
			contactForm.setPicture(imageUploadedUrl);
		}

		Contact updatedContact = contactService.updateContact(contact);
		Message message;
		if (updatedContact != null) {
			logger.info("Contact updated successfully: {}", updatedContact);
			message = Message.builder()
					.content("Contact updated successfully!")
					.type(MessageType.green)
					.build();
		} else {
			logger.error("Failed to update contact with ID: {}", contactId);
			message = Message.builder()
					.content("Failed to update contact. Please try again.")
					.type(MessageType.red)
					.build();
		}
		session.setAttribute("message", message);
		return "redirect:/user/contacts";
	}

}
