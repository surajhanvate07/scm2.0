package com.suraj.scm.entities;

import com.suraj.scm.enums.Providers;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity(name = "user")
@Table(name = "users")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class User {
	@Id
	private String userId;
	@Column(name = "user_name", nullable = false)
	private String name;
	@Column(unique = true, nullable = false)
	private String email;
	private String password;
	@Column(length = 1000)
	private String about;
	private String phoneNumber;
	@Column(length = 1000)
	private String profilePic;
	private boolean enabled = false;
	private boolean emailVerified = false;
	private boolean phoneVerified = false;

	// Signup provider
	@Enumerated(value = EnumType.STRING)
	private Providers provider = Providers.SELF;
	private String providerId;

	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
	private List<Contact> contacts = new ArrayList<>();

}
