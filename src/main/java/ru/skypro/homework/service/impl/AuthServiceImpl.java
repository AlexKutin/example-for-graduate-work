package ru.skypro.homework.service.impl;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.skypro.homework.dto.RegisterDTO;
import ru.skypro.homework.model.AdUser;
import ru.skypro.homework.service.AuthService;
import ru.skypro.homework.service.UserMapper;
import ru.skypro.homework.service.UserService;

@Service
public class AuthServiceImpl implements AuthService {

    private final UserService userService;
    private final PasswordEncoder encoder;
    private final UserMapper userMapper;

    public AuthServiceImpl(UserService userService, PasswordEncoder passwordEncoder, UserMapper userMapper) {
        this.encoder = passwordEncoder;
        this.userService = userService;
        this.userMapper = userMapper;
    }

    @Override
    public boolean login(String userName, String password) {
        try {
            UserDetails userDetails = userService.loadUserByUsername(userName);
            return encoder.matches(password, userDetails.getPassword());
        } catch (UsernameNotFoundException e) {
            return false;
        }
    }

    @Override
    public boolean register(RegisterDTO registerDTO) {
        if (userService.userExists(registerDTO.getUsername())) {
            return false;
        }
        AdUser adUser = userMapper.fromRegisterDTO(registerDTO);
        userService.createUser(adUser);
        return true;
    }

}
