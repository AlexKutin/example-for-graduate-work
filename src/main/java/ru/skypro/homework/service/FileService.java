package ru.skypro.homework.service;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
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
    private String adPhotoFilePath;

    private final UserService userService;
    private final AdUserRepository adUserRepository;
    private final AdRepository adRepository;

    public FileService(UserService userService, AdUserRepository adUserRepository, AdRepository adRepository) {
        this.userService = userService;
        this.adUserRepository = adUserRepository;
        this.adRepository = adRepository;
    }

    /**
     * Обновляет фото профиля (аватар) текущего авторизованного пользователя из параметра file.
     * Производит сохранение информации в БД и нового фото на жесткий диск
     * @param authentication объект типа Authentication, текущий авторизованный пользователь, предоставляет фронтенд
     * @param file графический файл с фото профиля (аватаром) пользователя
     * @throws IOException - если не удалось сохранить на диске файл с фото профиля (аватар) пользователя
     */
    public void updateCurrentUserAvatar(Authentication authentication, MultipartFile file) throws IOException {
        AdUser user = userService.getCurrentUser(authentication);
        String avatarFileName = saveFileToDisk(file, avatarFilePath, "avatar_" + user.getUserId());
        user.setAvatarFileName(avatarFileName);
        adUserRepository.save(user);
    }

    /**
     * Возвращает фото профиля (аватар) текущего авторизованного пользователя в виде объекта класса File
     * @param authentication объект типа Authentication, текущий авторизованный пользователь, предоставляет фронтенд
     * @return объект класса File
     */
    public File getAvatarFileByCurrentUser(Authentication authentication) {
        AdUser adUser = userService.getCurrentUser(authentication);
        return getFileByPathAndImageName(this.avatarFilePath, adUser.getAvatarFileName());
    }

    /**
     * Возвращает фото профиля (аватар) для пользователя с идентификатором userId в виде объекта класса File
     * @param userId идентификатор пользователя
     * @return объект класса File
     */
    public File getAvatarFileByUserId(Integer userId) {
        AdUser adUser = userService.getUserById(userId);
        return getFileByPathAndImageName(this.avatarFilePath, adUser.getAvatarFileName());
    }

    /**
     * Возвращает картинку объявления в виде объекта класса File
     * @param ad объявление в виде объекта класса Ad
     * @return объект класса File
     */
    public File getPhotoByAd(Ad ad) {
        String photoName = ad.getImage();
        return getFileByPathAndImageName(this.adPhotoFilePath, photoName);
    }

    /**
     * Конструирует полный путь из переданных параметров и возвращает объект класса File
     * @param dirPath каталог на диске, в котором находятся графические файлы
     * @param imageName имя файла
     * @return объект класса File
     */
    public File getFileByPathAndImageName(String dirPath, String imageName) {
        if (imageName == null) {
            return null;
        }
        Path imagePath = Path.of(dirPath, imageName);
        return imagePath.toFile();
    }

    /**
     * Обновляет фото в объявлении, производит сохранение информации в БД и нового фото на жесткий диск
     * @param ad объявление, объект класса Ad
     * @param file графический файл с картинкой (фото) объявления
     * @throws IOException - если не удалось сохранить на диске файл с фото объявления
     */
    @PreAuthorize("hasRole('ADMIN') or @securityService.isOwnerAd(authentication, #ad.adId)")
    public void updateAdPhoto(Ad ad, MultipartFile file, Authentication authentication) throws IOException {
        String photoFileName = saveFileToDisk(file, adPhotoFilePath, "photo_" + ad.getAdId());
        ad.setImage(photoFileName);
        adRepository.save(ad);
    }

    /**
     * Сохраняет на диск графический файл с картинкой, указанный в параметре file.
     * Сохранение производится в каталог dirPath под именем, указанном в параметре fileName
     * @param file графический файл с картинкой
     * @param dirPath путь к каталогу на диске для сохранения файла
     * @param fileName имя файла, под которым он будет записан на диск
     * @return имя файла, под которым файл был сохранен на диск
     * @throws IOException если не удалось сохранить на диске файл
     */
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

    /**
     * Удаляет файл с фотографией с именем adPhotoName с жесткого диска
     * @param adPhotoName имя файла с фотографией объявления
     * @throws IOException если не удалось удалить файл
     */
    public void deleteAdPhoto(String adPhotoName) throws IOException {
        File adPhotoFile = getFileByPathAndImageName(adPhotoFilePath, adPhotoName);
        FileUtils.delete(adPhotoFile);
    }
}
