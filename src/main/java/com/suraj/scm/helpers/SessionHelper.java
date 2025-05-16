package com.suraj.scm.helpers;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Objects;

@Component
public class SessionHelper {

	public static void removeMessage() {
		// Remove message from session
		// 1. Get the session
		// 2. Remove the message attribute
		// 3. Return the message
		try {
			System.out.println("Removing message from session");
			HttpSession session = ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest().getSession();
			session.removeAttribute("message");
		} catch (Exception e) {
			System.out.println("Error in removing message from session");
			e.printStackTrace();
		}
	}
}
