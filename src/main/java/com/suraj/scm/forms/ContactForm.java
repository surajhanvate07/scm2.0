package com.suraj.scm.forms;

import com.suraj.scm.validator.OnCreate;
import com.suraj.scm.validator.OnUpdate;
import com.suraj.scm.validator.ValidFile;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ContactForm {

	@NotBlank(message = "Name is required", groups = {OnCreate.class, OnUpdate.class})
	@Size(min = 3, max = 20, message = "Name must be between 3 and 20 characters")
	private String name;
	@NotBlank(message = "Email is required", groups = {OnCreate.class, OnUpdate.class})
	@Email(message = "Email is not valid")
	private String email;
	@NotBlank(message = "Phone number is required", groups = {OnCreate.class, OnUpdate.class})
	@Pattern(regexp = "^[0-9]{10}$", message = "Phone number must be 10 digits")
	private String phoneNumber;
	@NotBlank(message = "Address is required", groups = {OnCreate.class, OnUpdate.class})
	private String address;
	private String description;
	private boolean favorite;
	private String websiteLink;
	private String linkedInLink;

	@ValidFile(message = "Invalid file. File must be an image and size must not exceed 5 MB")
	private MultipartFile contactPicture;
	private String picture;
}
