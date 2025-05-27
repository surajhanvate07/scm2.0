package com.suraj.scm.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class FileValidator implements ConstraintValidator<ValidFile, MultipartFile> {

	private static final long MAX_FILE_SIZE = 2 * 1024 * 1024; // 2 MB

	@Override
	public boolean isValid(MultipartFile file, ConstraintValidatorContext context) {
		if (file == null || file.isEmpty()) {
			context.disableDefaultConstraintViolation();
			context.buildConstraintViolationWithTemplate("File must not be empty").addConstraintViolation();
			return false;
		}

		if (file.getSize() > MAX_FILE_SIZE) {
			context.disableDefaultConstraintViolation();
			context.buildConstraintViolationWithTemplate("File size should be less than 2 MB").addConstraintViolation();
			return false;
		}

//		try {
//			BufferedImage bufferedImage = ImageIO.read(file.getInputStream()); // This will throw an exception if the file is not a valid image
//			bufferedImage.getHeight()
//		} catch (IOException e) {
//			throw new RuntimeException(e);
//		}
		return true;
	}
}
