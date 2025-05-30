package com.suraj.scm.repositories;

import com.suraj.scm.entities.Contact;
import com.suraj.scm.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ContactRepository extends JpaRepository<Contact, String> {

	@Query("SELECT c FROM Contact c WHERE c.user.id = :userId")
	Page<Contact> findByUserId(@Param("userId") String userId, Pageable pageable);

	@Query("SELECT c FROM Contact c WHERE c.user.id = ?1 AND c.isFavorite = true")
	List<Contact> findByUserIdAndIsFavoriteTrue(String userId);

	@Query("SELECT c FROM Contact c WHERE c.name = :name AND c.email = :email AND c.phoneNumber = :phone")
	List<Contact> findByNameAndEmailAndPhoneNumber(@Param("name") String name,
												   @Param("email") String email,
												   @Param("phone") String phoneNumber);

	List<Contact> findByUser(User user);

	Page<Contact> findByNameContainingAndUser(String name, User user,  Pageable pageable);
	Page<Contact> findByEmailContainingAndUser(String email, User user, Pageable pageable);
	Page<Contact> findByPhoneNumberContainingAndUser(String phoneNumber, User user, Pageable pageable);

}
