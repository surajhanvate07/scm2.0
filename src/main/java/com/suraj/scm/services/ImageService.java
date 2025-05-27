package com.suraj.scm.services;

import org.springframework.web.multipart.MultipartFile;

public interface ImageService {
	String uploadImage(MultipartFile contactPicture, String fileName);

	String getImageUrl(String publicId);
}
