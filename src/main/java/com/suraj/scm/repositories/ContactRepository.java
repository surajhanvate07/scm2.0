package com.suraj.scm.repositories;

import com.suraj.scm.entities.Contact;
import com.suraj.scm.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ContactRepository extends JpaRepository<Contact, String> {

	@Query("SELECT c FROM Contact c WHERE c.user.id = :userId")
	List<Contact> findByUserId(@Param("userId") String userId);

	@Query("SELECT c FROM Contact c WHERE c.user.id = ?1 AND c.isFavorite = true")
	List<Contact> findByUserIdAndIsFavoriteTrue(String userId);

	@Query("SELECT c FROM Contact c WHERE c.name = :name AND c.email = :email AND c.phoneNumber = :phone")
	List<Contact> findByNameAndEmailAndPhoneNumber(@Param("name") String name,
												   @Param("email") String email,
												   @Param("phone") String phoneNumber);

	List<Contact> findByUser(User user);
}
