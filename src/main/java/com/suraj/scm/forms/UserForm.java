package com.suraj.scm.forms;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class UserForm {

	private String name;
	private String email;
	private String password;
	private String phoneNumber;
	private String about;
}
