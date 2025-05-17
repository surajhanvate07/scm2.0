package com.suraj.scm.services.impl;

import com.suraj.scm.entities.User;
import com.suraj.scm.exceptions.ResourceNotFoundException;
import com.suraj.scm.helpers.AppConstants;
import com.suraj.scm.repositories.UserRepository;
import com.suraj.scm.services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Override
	public User saveUser(User user) {
		//  Generate a unique ID for the user
		String userId = UUID.randomUUID().toString();
		user.setUserId(userId);
		// Encode the password before saving
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		user.setRoles(List.of(AppConstants.ROLE_USER));
		return userRepository.save(user);
	}

	@Override
	public Optional<User> getUserById(String id) {
		return userRepository.findById(id);
	}

	@Override
	public Optional<User> updateUser(User user) {
		User fetchedUser = userRepository.findById(user.getUserId()).orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + user.getUserId()));
		fetchedUser.setName(user.getName());
		fetchedUser.setEmail(user.getEmail());
		fetchedUser.setPassword(passwordEncoder.encode(user.getPassword()));
		fetchedUser.setPhoneNumber(user.getPhoneNumber());
		fetchedUser.setAbout(user.getAbout());
		fetchedUser.setProfilePic(user.getProfilePic());
		fetchedUser.setEnabled(user.isEnabled());
		fetchedUser.setEmailVerified(user.isEmailVerified());
		fetchedUser.setPhoneVerified(user.isPhoneVerified());
		fetchedUser.setProvider(user.getProvider());
		fetchedUser.setProviderId(user.getProviderId());

		User savedUser = userRepository.save(fetchedUser);
		return Optional.of(savedUser);
	}

	@Override
	public void deleteUser(String id) {
		User fetchedUser = userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
		userRepository.delete(fetchedUser);
	}

	@Override
	public boolean isUserExists(String id) {
		User fetchedUser = userRepository.findById(id).orElse(null);
		if (fetchedUser != null) {
			logger.info("User exists with id: " + id);
			return true;
		}
		return false;
	}

	@Override
	public boolean isUserExistsByEmail(String email) {
		User fetchedUser = userRepository.findByEmail(email).orElse(null);
		if (fetchedUser != null) {
			logger.info("User exists with email: " + email);
			return true;
		}
		return false;
	}

	@Override
	public List<User> getAllUsers() {
		return userRepository.findAll();
	}
}
