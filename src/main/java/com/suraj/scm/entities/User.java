package com.suraj.scm.entities;

import com.suraj.scm.enums.Providers;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Entity(name = "user")
@Table(name = "users")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class User implements UserDetails {

	@Id
	private String userId;
	@Column(name = "user_name", nullable = false)
	private String name;
	@Column(unique = true, nullable = false)
	private String email;
	@Getter(AccessLevel.NONE)
	private String password;
	@Column(length = 1000)
	private String about;
	private String phoneNumber;
	@Column(length = 1000)
	private String profilePic;
	@Getter(AccessLevel.NONE)
	private boolean enabled = true;
	private boolean emailVerified = false;
	private boolean phoneVerified = false;
	// Signup provider
	@Enumerated(value = EnumType.STRING)
	private Providers provider = Providers.SELF;
	private String providerId;

	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
	private List<Contact> contacts = new ArrayList<>();

	@ElementCollection(fetch = FetchType.EAGER)
	List<String> roles = new ArrayList<>();

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		List<SimpleGrantedAuthority> authorities = new ArrayList<>();
		this.roles.forEach(role -> {
			authorities.add(new SimpleGrantedAuthority(role));
		});

		return authorities;
	}

	@Override
	public String getPassword() {
		return this.password;
	}

	@Override
	public String getUsername() {
		return this.email;
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return this.enabled;
	}
}
