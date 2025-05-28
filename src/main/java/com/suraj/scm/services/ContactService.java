package com.suraj.scm.services;

import com.suraj.scm.entities.Contact;
import com.suraj.scm.entities.User;

import java.util.List;

public interface ContactService {
	Contact saveContact(Contact contact);

	Contact getContactById(String id);

	Contact updateContact(Contact contact);

	void deleteContact(String id);

	List<Contact> getAllContacts();

	List<Contact> getContactsByUserId(String userId);

	List<Contact> getFavoriteContactsByUserId(String userId);

	List<Contact> searchContacts(String name, String email, String phoneNumber);

	List<Contact> getContactsByUser(User user);
}
