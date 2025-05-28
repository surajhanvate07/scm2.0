package com.suraj.scm.services.impl;

import com.suraj.scm.entities.Contact;
import com.suraj.scm.entities.User;
import com.suraj.scm.exceptions.ResourceNotFoundException;
import com.suraj.scm.repositories.ContactRepository;
import com.suraj.scm.services.ContactService;
import org.springframework.beans.factory.annotation.Autowired;
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
		return null;
	}

	@Override
	public void deleteContact(String id) {
		Contact contact = getContactById(id);
		if (contact != null) {
			contactRepository.delete(contact);
		} else {
			throw new ResourceNotFoundException("Contact not found with id: " + id);
		}
	}

	@Override
	public List<Contact> getAllContacts() {
		return contactRepository.findAll();
	}

	@Override
	public List<Contact> getContactsByUserId(String userId) {
		if (userId == null || userId.isEmpty()) {
			throw new IllegalArgumentException("User ID cannot be null or empty");
		}
		return contactRepository.findByUserId(userId);
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
}
