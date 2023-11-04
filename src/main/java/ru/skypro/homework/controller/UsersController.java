package ru.skypro.homework.controller;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.NewPasswordDTO;
import ru.skypro.homework.dto.UpdateUserDTO;
import ru.skypro.homework.dto.UserDTO;
import ru.skypro.homework.exception.UserNotFoundException;
import ru.skypro.homework.service.FileService;
import ru.skypro.homework.service.UserService;

import javax.validation.Valid;
import java.io.IOException;

@CrossOrigin(value = "http://localhost:3000")
@RestController
@RequiredArgsConstructor
public class UsersController {
    private final Logger logger = LoggerFactory.getLogger(UsersController.class);
    private final UserService userService;
    private final FileService fileService;

    @PostMapping("/users/set_password")
    public ResponseEntity<Void> setPassword(@Valid @RequestBody NewPasswordDTO newPasswordDTO,
                                            Authentication authentication) {
        userService.setNewPassword(authentication, newPasswordDTO);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/users/me")
    public ResponseEntity<UserDTO> getUser(Authentication authentication) {
        return ResponseEntity.ok(userService.getUserInfoByUsername(authentication));
    }

    @PatchMapping("/users/me")
    public ResponseEntity<UpdateUserDTO> updateUser(@Valid @RequestBody UpdateUserDTO updateUserDTO,
                                                    Authentication authentication) {
        return ResponseEntity.ok(userService.updateUserInfo(authentication, updateUserDTO));
    }

    @PatchMapping(value = "/users/me/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Void> updateUserImage(@RequestPart("image") MultipartFile multipartFile,
                                                Authentication authentication) throws IOException {
        fileService.updateCurrentUserAvatar(authentication, multipartFile);
        return ResponseEntity.ok().build();
    }

    @ExceptionHandler
    ResponseEntity<?> handleUserNotFound(UserNotFoundException e) {
        logger.error(e.getMessage());
        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler
    ResponseEntity<?> handleUsernameNotFound(UsernameNotFoundException e) {
        logger.error(e.getMessage());
        return ResponseEntity.notFound().build();
    }

}
