package ru.skypro.homework.service;

import ru.skypro.homework.dto.UpdateUserDTO;
import ru.skypro.homework.dto.UserDTO;
import ru.skypro.homework.model.AdUser;

public class UserMapper {

    public static UserDTO toUserDTO(AdUser adUser) {
        UserDTO userDTO = new UserDTO();

        userDTO.setId(adUser.getUserId());
        userDTO.setEmail(adUser.getUsername());
        userDTO.setFirstName(adUser.getFirstName());
        userDTO.setLastName(adUser.getLastName());
        userDTO.setPhone(adUser.getPhone());
        userDTO.setRole(adUser.getRole());

        return userDTO;
    }

    public static AdUser fromUserDTO(UserDTO userDTO) {
        AdUser adUser = new AdUser();

        adUser.setUserId(userDTO.getId());
        adUser.setUsername(userDTO.getEmail());
        adUser.setFirstName(userDTO.getFirstName());
        adUser.setLastName(userDTO.getLastName());
        adUser.setPhone(userDTO.getPhone());
        adUser.setRole(userDTO.getRole());

        return adUser;
    }

    public static UpdateUserDTO toUpdateUserDTO(AdUser adUser) {
        UpdateUserDTO updateUserDTO = new UpdateUserDTO();

        updateUserDTO.setFirstName(adUser.getFirstName());
        updateUserDTO.setLastName(adUser.getLastName());
        updateUserDTO.setPhone(adUser.getPhone());

        return updateUserDTO;
    }

    public static AdUser updateUserFromDTO(AdUser adUser, UpdateUserDTO updateUserDTO) {
        adUser.setFirstName(updateUserDTO.getFirstName());
        adUser.setLastName(updateUserDTO.getLastName());
        adUser.setPhone(updateUserDTO.getPhone());

        return adUser;
    }
}
