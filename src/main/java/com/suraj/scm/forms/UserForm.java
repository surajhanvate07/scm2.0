package com.suraj.scm.forms;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class UserForm {

	@NotBlank(message = "Name is required")
	@Size(min = 3, max = 20, message = "Name must be between 3 and 20 characters")
	private String name;

	@NotBlank(message = "Email is required")
	@Email(message = "Email is not valid")
	private String email;

	@NotBlank(message = "Password is required")
	@Size(min = 6, max = 20, message = "Password must be between 6 and 20 characters")
	private String password;

	@NotBlank(message = "Phone number is required")
	@Size(min = 10, max = 13, message = "Phone number must be between 10 and 13 digits")
	private String phoneNumber;

	@NotBlank(message = "About section must not be blank")
	private String about;
}
