package com.suraj.scm.services.impl;

import com.cloudinary.Cloudinary;
import com.cloudinary.Transformation;
import com.cloudinary.utils.ObjectUtils;
import com.suraj.scm.helpers.AppConstants;
import com.suraj.scm.services.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class ImageServiceImpl implements ImageService {

	private final Cloudinary cloudinary;

	@Override
	public String uploadImage(MultipartFile contactPicture, String fileName) {

		try {
			byte[] data = new byte[contactPicture.getInputStream().available()];
			contactPicture.getInputStream().read(data);
			cloudinary.uploader().upload(data, ObjectUtils.asMap(
					"public_id", fileName
			));
			return getImageUrl(fileName);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public String getImageUrl(String publicId) {
		return cloudinary.url().transformation(
				new Transformation<>()
						.width(AppConstants.CONTACT_IMAGE_WIDTH)
						.height(AppConstants.CONTACT_IMAGE_HEIGHT)
						.crop(AppConstants.CONTACT_IMAGE_CROP)
		).generate(publicId);
	}
}
