package ru.skypro.homework.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.skypro.homework.dto.RegisterDTO;
import ru.skypro.homework.dto.UpdateUserDTO;
import ru.skypro.homework.dto.UserDTO;
import ru.skypro.homework.model.AdUser;

@Service
public class UserMapper {
    private final PasswordEncoder encoder;

    public UserMapper(PasswordEncoder encoder) {
        this.encoder = encoder;
    }

    public UserDTO toUserDTO(AdUser adUser) {
        UserDTO userDTO = new UserDTO();

        userDTO.setId(adUser.getUserId());
        userDTO.setEmail(adUser.getUsername());
        userDTO.setFirstName(adUser.getFirstName());
        userDTO.setLastName(adUser.getLastName());
        userDTO.setPhone(adUser.getPhone());
        userDTO.setRole(adUser.getRole());

        return userDTO;
    }

    public AdUser fromRegisterDTO(RegisterDTO registerDTO) {
        AdUser adUser = new AdUser();

        adUser.setUsername(registerDTO.getUsername());
        adUser.setFirstName(registerDTO.getFirstName());
        adUser.setLastName(registerDTO.getLastName());
        adUser.setPhone(registerDTO.getPhone());
        adUser.setRole(registerDTO.getRole());
        adUser.setPassword(encoder.encode(registerDTO.getPassword()));

        return adUser;
    }

    public UpdateUserDTO toUpdateUserDTO(AdUser adUser) {
        UpdateUserDTO updateUserDTO = new UpdateUserDTO();

        updateUserDTO.setFirstName(adUser.getFirstName());
        updateUserDTO.setLastName(adUser.getLastName());
        updateUserDTO.setPhone(adUser.getPhone());

        return updateUserDTO;
    }

    public AdUser updateUserFromDTO(AdUser adUser, UpdateUserDTO updateUserDTO) {
        adUser.setFirstName(updateUserDTO.getFirstName());
        adUser.setLastName(updateUserDTO.getLastName());
        adUser.setPhone(updateUserDTO.getPhone());

        return adUser;
    }
}
