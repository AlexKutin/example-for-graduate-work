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

    /**
     * Производит проверку логина/пароля пользователя на соответствие данным, хранящимся в БД
     * @param userName уникальное имя, логин пользователя (его email), предоставляет фронтенд
     * @param password пароль пользователя, предоставляет фронтенд
     * @return true - если пароль, переданный в параметре password соответствует закодированному паролю, хранящемуся в БД
     * @throws UsernameNotFoundException - если пользователь с указанным userName не найден в БД
     */
    @Override
    public boolean login(String userName, String password) {
        try {
            UserDetails userDetails = userService.loadUserByUsername(userName);
            return encoder.matches(password, userDetails.getPassword());
        } catch (UsernameNotFoundException e) {
            return false;
        }
    }

    /**
     * Создает нового пользователя в соответствии с переданными параметрами и сохраняет его в БД.
     * Если пользователь с указанным логином уже существует, то создание не происходит, возвращает false
     * @param registerDTO - объект класса RegisterDTO, содержащий регистрационную информацию о новом пользователе,
     *                    предоставляет фронтенд
     * @return true - если пользователь был успешно зарегистрирован и сохранен в БД
     */
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
