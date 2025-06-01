package com.suraj.scm.services.impl;

import com.suraj.scm.entities.Contact;
import com.suraj.scm.entities.User;
import com.suraj.scm.exceptions.ResourceNotFoundException;
import com.suraj.scm.repositories.ContactRepository;
import com.suraj.scm.services.ContactService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class ContactServiceImpl implements ContactService {

	@Autowired
	private ContactRepository contactRepository;

	@Override
	public Contact saveContact(Contact contact) {
		String contactId = UUID.randomUUID().toString();
		contact.setId(contactId);
		return contactRepository.save(contact);
	}

	@Override
	public Contact getContactById(String id) {
		if (id != null && !id.isEmpty()) {
			return contactRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Contact not found with id: " + id));
		}
		throw new IllegalArgumentException("Contact ID cannot be null or empty");
	}

	@Override
	public Contact updateContact(Contact contact) {
		Contact existingContact = getContactById(contact.getId());
		if (existingContact != null) {
			// Update the existing contact with new values
			existingContact.setName(contact.getName());
			existingContact.setEmail(contact.getEmail());
			existingContact.setPhoneNumber(contact.getPhoneNumber());
			existingContact.setAddress(contact.getAddress());
			existingContact.setFavorite(contact.isFavorite());
			if(contact.getDescription() != null) {
				existingContact.setDescription(contact.getDescription());
			}
			if(contact.getWebsiteLink() != null) {
				existingContact.setWebsiteLink(contact.getWebsiteLink());
			}
			if(contact.getLinkedInLink() != null) {
				existingContact.setLinkedInLink(contact.getLinkedInLink());
			}
			existingContact.setPicture(contact.getPicture());
			if(contact.getCloudinaryImagePublicId() != null) {
				existingContact.setCloudinaryImagePublicId(contact.getCloudinaryImagePublicId());
			}
			return contactRepository.save(existingContact);
		}
		return null;
	}

	@Override
	public boolean deleteContact(String id) {
		Contact contact = getContactById(id);
		if (contact != null) {
			contactRepository.delete(contact);
			return true;
		} else {
			throw new ResourceNotFoundException("Contact not found with id: " + id);
		}
	}

	@Override
	public List<Contact> getAllContacts() {
		return contactRepository.findAll();
	}

	@Override
	public Page<Contact> getContactsByUserId(String userId, int page, int size, String sortBy, String sortDir) {
		if (sortBy == null || sortBy.isEmpty()) {
			sortBy = "name"; // Default sorting by name
		}
		if (sortDir == null || sortDir.isEmpty()) {
			sortDir = "asc"; // Default sorting direction
		}
		if (!sortDir.equalsIgnoreCase("asc") && !sortDir.equalsIgnoreCase("desc")) {
			throw new IllegalArgumentException("Sort direction must be either 'asc' or 'desc'");
		}
		// Create a sort object based on the provided sortBy and sortDir
		Sort sort = sortDir.equalsIgnoreCase("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();

		// Create a Pageable object with the specified page, size, and sort
		Pageable pageable = PageRequest.of(page, size, sort);

		if (userId == null || userId.isEmpty()) {
			throw new IllegalArgumentException("User ID cannot be null or empty");
		}
		return contactRepository.findByUserId(userId, pageable);
	}

	@Override
	public List<Contact> getFavoriteContactsByUserId(String userId) {
		if (userId == null || userId.isEmpty()) {
			throw new IllegalArgumentException("User ID cannot be null or empty");
		}
		return contactRepository.findByUserIdAndIsFavoriteTrue(userId);
	}

	@Override
	public List<Contact> searchContacts(String name, String email, String phoneNumber) {
		return contactRepository.findByNameAndEmailAndPhoneNumber(name, email, phoneNumber);
	}

	@Override
	public List<Contact> getContactsByUser(User user) {
		return contactRepository.findByUser(user);
	}

	@Override
	public Page<Contact> searchByField(String field, String value, int page, int size, String sortBy, String sortDir, User user) {
		if (sortBy == null || sortBy.isEmpty()) {
			sortBy = "name"; // Default sorting by name
		}
		if (sortDir == null || sortDir.isEmpty()) {
			sortDir = "asc"; // Default sorting direction
		}
		if (!sortDir.equalsIgnoreCase("asc") && !sortDir.equalsIgnoreCase("desc")) {
			throw new IllegalArgumentException("Sort direction must be either 'asc' or 'desc'");
		}
		// Create a sort object based on the provided sortBy and sortDir
		Sort sort = sortDir.equalsIgnoreCase("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();

		// Create a Pageable object with the specified page, size, and sort
		Pageable pageable = PageRequest.of(page, size, sort);

		return switch (field.toLowerCase()) {
			case "name" -> contactRepository.findByNameContainingAndUser(value, user, pageable);
			case "email" -> contactRepository.findByEmailContainingAndUser(value, user, pageable);
			case "phone" -> contactRepository.findByPhoneNumberContainingAndUser(value, user, pageable);
			default -> throw new IllegalArgumentException("Invalid search field: " + field);
		};
	}
}
