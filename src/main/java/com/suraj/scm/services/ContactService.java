package com.suraj.scm.services;

import com.suraj.scm.entities.Contact;
import com.suraj.scm.entities.User;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ContactService {
	Contact saveContact(Contact contact);

	Contact getContactById(String id);

	Contact updateContact(Contact contact);

	void deleteContact(String id);

	List<Contact> getAllContacts();

	Page<Contact> getContactsByUserId(String userId, int page, int size, String sortBy, String sortDir);

	List<Contact> getFavoriteContactsByUserId(String userId);

	List<Contact> searchContacts(String name, String email, String phoneNumber);

	List<Contact> getContactsByUser(User user);

	Page<Contact> searchByField(String field, String value, int page, int size, String sortBy, String sortDir, User loggedUser);
}
