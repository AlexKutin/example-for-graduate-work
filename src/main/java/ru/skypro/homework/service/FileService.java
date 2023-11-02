package ru.skypro.homework.service;

import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.model.Ad;
import ru.skypro.homework.model.AdUser;
import ru.skypro.homework.repository.AdRepository;
import ru.skypro.homework.repository.AdUserRepository;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

@Service
public class FileService {
    @Value("${path.to.user.avatar.file.disk}")
    private String avatarFilePath;

    @Value("${path.to.ad.photo.file.disk}")
    private String photoFilePath;

    private final UserService userService;
    private final AdUserRepository adUserRepository;
    private final AdRepository adRepository;

    public FileService(UserService userService, AdUserRepository adUserRepository, AdRepository adRepository) {
        this.userService = userService;
        this.adUserRepository = adUserRepository;
        this.adRepository = adRepository;
    }

    public void updateCurrentUserAvatar(Authentication authentication, MultipartFile file) throws IOException {
        AdUser user = userService.getCurrentUser(authentication);
        String avatarFileName = saveFileToDisk(file, avatarFilePath, "avatar_" + user.getUserId());
        user.setAvatarFileName(avatarFileName);
        adUserRepository.save(user);
    }

    public File getAvatarFileByCurrentUser(Authentication authentication) {
        AdUser adUser = userService.getCurrentUser(authentication);
        return getFileByPathAndImageName(this.avatarFilePath, adUser.getAvatarFileName());
    }

    public File getAvatarFileByUSerId(Integer userId) {
        AdUser adUser = userService.getUserById(userId);
        return getFileByPathAndImageName(this.avatarFilePath, adUser.getAvatarFileName());
    }

    public File getPhotoByAd(Ad ad) {
        String photoName = ad.getImage();
        return getFileByPathAndImageName(this.photoFilePath, photoName);
    }

    public File getFileByPathAndImageName(String dirPath, String imageName) {
        if (imageName == null) {
            return null;
        }
        Path imagePath = Path.of(dirPath, imageName);
        return imagePath.toFile();
    }

    public void updateAdPhoto(Ad ad, MultipartFile file) throws IOException {
        String photoFileName = saveFileToDisk(file, photoFilePath, "photo_" + ad.getAdId());
        ad.setImage(photoFileName);
        adRepository.save(ad);
    }

    public String saveFileToDisk(MultipartFile file, String dirPath, String fileName) throws IOException {
        String fileExtension = FilenameUtils.getExtension(file.getOriginalFilename());
        Path filePath = Path.of(dirPath, fileName + "." + fileExtension);
        Files.createDirectories(filePath.getParent());
        Files.deleteIfExists(filePath);

        try (InputStream is = file.getInputStream()) {
            Files.copy(is, filePath, StandardCopyOption.REPLACE_EXISTING);
        }
        return filePath.getFileName().toString();
    }
}
