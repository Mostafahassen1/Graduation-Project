package com.codemeet.service;

import com.cloudinary.Cloudinary;
import com.codemeet.utils.dto.cloudinary.CloudinaryInfoResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CloudinaryService {

    private final Cloudinary cloudinary;

    @Transactional
    public CloudinaryInfoResponse updateProfilePicture(MultipartFile file, Integer userId) {
        try {
            Map<?, ?> result = cloudinary.uploader()
                .upload(file.getBytes(), Map.of(
                    "public_id", "code-meet/profile-picture/" + userId,
                    "overwrite", true
                ));
            String url = (String) result.get("secure_url");
            String publicId = (String) result.get("public_id");

            return new CloudinaryInfoResponse(url, publicId);
        } catch (IOException e) {
            throw new RuntimeException("Image upload failed.");
        }
    }

    @Transactional
    public CloudinaryInfoResponse updateRoomPicture(MultipartFile file, Integer rooId) {
        try {
            Map<?, ?> result = cloudinary.uploader()
                .upload(file.getBytes(), Map.of(
                    "public_id", "code-meet/room-picture/" + rooId,
                    "overwrite", true
                ));
            String url = (String) result.get("secure_url");
            String publicId = (String) result.get("public_id");

            return new CloudinaryInfoResponse(url, publicId);
        } catch (IOException e) {
            throw new RuntimeException("Image upload failed.");
        }
    }
}
