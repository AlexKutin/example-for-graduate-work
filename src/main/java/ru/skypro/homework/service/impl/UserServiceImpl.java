package ru.skypro.homework.service.impl;

import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.skypro.homework.dto.NewPasswordDTO;
import ru.skypro.homework.dto.UpdateUserDTO;
import ru.skypro.homework.dto.UserDTO;
import ru.skypro.homework.exception.UserNotFoundException;
import ru.skypro.homework.model.AdUser;
import ru.skypro.homework.repository.AdUserRepository;
import ru.skypro.homework.service.UserMapper;
import ru.skypro.homework.service.UserService;

@Service
public class UserServiceImpl implements UserService {
    private static final String USER_NOT_FOUND_BY_ID_MSG = "Пользователь с id: %d не найден в БД";
    private static final String USER_NOT_FOUND_BY_USERNAME_MSG = "Пользователь с логином: %s не найден в БД";

    private final AdUserRepository adUserRepository;
    private final PasswordEncoder encoder;

    public UserServiceImpl(AdUserRepository adUserRepository, PasswordEncoder encoder) {
        this.adUserRepository = adUserRepository;
        this.encoder = encoder;
    }

    @Override
    public AdUser getUserById(Integer userId) {
        return adUserRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(String.format(USER_NOT_FOUND_BY_ID_MSG, userId)));
    }

    @Override
    public AdUser getUserByUsername(String username) {
        AdUser user = adUserRepository.findByUsername(username);
        if (user == null) {
            throw new UserNotFoundException(String.format(USER_NOT_FOUND_BY_USERNAME_MSG, username));
        }
        return user;
    }

    @Override
    public UserDTO getUserInfoByUsername(Authentication authentication) {
        AdUser user = getCurrentUser(authentication);;
        return UserMapper.toUserDTO(user);
    }

    @Override
    public UpdateUserDTO updateUserInfo(Authentication authentication, UpdateUserDTO updateUserDTO) {
        AdUser user = getCurrentUser(authentication);
        AdUser updatedUser = UserMapper.updateUserFromDTO(user, updateUserDTO);
        updatedUser = adUserRepository.save(updatedUser);
        return UserMapper.toUpdateUserDTO(updatedUser);
    }

    @Override
    public void setNewPassword(Authentication authentication, NewPasswordDTO newPasswordDTO) {
        AdUser user = getCurrentUser(authentication);
        if (encoder.matches(newPasswordDTO.getCurrentPassword(), user.getPasswordHash())) {
            user.setPasswordHash(encoder.encode(newPasswordDTO.getNewPassword()));
            adUserRepository.save(user);
        }
    }

    @Override
    public AdUser getCurrentUser(Authentication authentication) {
        String username = authentication.getName();
        return getUserByUsername(username);
    }
}
