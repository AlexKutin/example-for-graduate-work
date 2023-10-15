package ru.skypro.homework.service;

import org.springframework.security.core.Authentication;
import ru.skypro.homework.dto.NewPasswordDTO;
import ru.skypro.homework.dto.UpdateUserDTO;
import ru.skypro.homework.dto.UserDTO;
import ru.skypro.homework.model.AdUser;

public interface UserService {
    AdUser getUserById(Integer userId);

    AdUser getUserByUsername(String username);

    UserDTO getUserInfoByUsername(Authentication authentication);

    UpdateUserDTO updateUserInfo(Authentication authentication, UpdateUserDTO updateUserDTO);

    void setNewPassword(Authentication authentication, NewPasswordDTO newPasswordDTO);

    AdUser getCurrentUser(Authentication authentication);
}
