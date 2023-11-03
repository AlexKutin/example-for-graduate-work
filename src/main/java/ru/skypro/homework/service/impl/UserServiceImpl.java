package ru.skypro.homework.service.impl;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
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
    private final UserMapper userMapper;

    public UserServiceImpl(AdUserRepository adUserRepository, PasswordEncoder encoder, UserMapper userMapper) {
        this.adUserRepository = adUserRepository;
        this.encoder = encoder;
        this.userMapper = userMapper;
    }

    /**
     * Производит поиск в БД пользователя по его уникальному идентификатору id
     * @param userId идентификатор пользователя
     * @return Пользователь в виде объекта класса AdUser
     * @throws UserNotFoundException если пользователь не найден в БД по указанному идентификатору
     */
    @Override
    public AdUser getUserById(Integer userId) {
        return adUserRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(String.format(USER_NOT_FOUND_BY_ID_MSG, userId)));
    }

    /**
     * Производит поиск в БД пользователя по его уникальному имени (логину)
     * @param username Логин пользователя, используемый при регистрации и входе в систему
     * @return Пользователь в виде объекта класса AdUser
     * @throws UsernameNotFoundException - если пользователь не найден в БД по указанному имени
     */
    @Override
    public AdUser getUserByUsername(String username) {
        AdUser user = adUserRepository.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException(String.format(USER_NOT_FOUND_BY_USERNAME_MSG, username));
        }
        return user;
    }

    /**
     * Возвращает информационую запись (UserDTO) о текущем авторизованном пользователе
     * @param authentication объект типа Authentication, текущий авторизованный пользователь, предоставляет фронтенд
     * @return Возвращает объект класса UserDTO
     */
    @Override
    public UserDTO getUserInfoByUsername(Authentication authentication) {
        AdUser user = getCurrentUser(authentication);
        return userMapper.toUserDTO(user);
    }

    /**
     * Производит обновление информации о теущем авторизованном пользователе, используя параметр updateUserDTO.
     * В случае успешного обновления сохраняет изменения в БД и возвращает актуальные параметры пользователя
     * @param authentication объект типа Authentication, текущий авторизованный пользователь, предоставляет фронтенд
     * @param updateUserDTO объект класса UpdateUserDTO, содержащий измененные параметры пользователя,
     *                      которые допускается редактировать
     * @return возвращает объект класса UpdateUserDTO с новыми параметрами пользователя
     */
    @Override
    public UpdateUserDTO updateUserInfo(Authentication authentication, UpdateUserDTO updateUserDTO) {
        AdUser user = getCurrentUser(authentication);
        AdUser updatedUser = userMapper.updateUserFromDTO(user, updateUserDTO);
        updatedUser = adUserRepository.save(updatedUser);
        return userMapper.toUpdateUserDTO(updatedUser);
    }

    /**
     * Производит смену пароля для текущего авторизованного пользователя и сохраняет его в БД
     * @param authentication объект типа Authentication, текущий авторизованный пользователь, предоставляет фронтенд
     * @param newPasswordDTO объект содержащий текущий и новый парль для пользователя
     */
    @Override
    public void setNewPassword(Authentication authentication, NewPasswordDTO newPasswordDTO) {
        AdUser user = getCurrentUser(authentication);
        if (encoder.matches(newPasswordDTO.getCurrentPassword(), user.getPassword())) {
            user.setPassword(encoder.encode(newPasswordDTO.getNewPassword()));
            adUserRepository.save(user);
        }
    }

    /**
     * Возвращает объект класса AdUser, соответствующий текущему авторизованному пользователю
     * @param authentication объект типа Authentication, текущий авторизованный пользователь, предоставляет фронтенд
     * @return Пользователь в виде объекта класса AdUser
     */
    @Override
    public AdUser getCurrentUser(Authentication authentication) {
        String username = authentication.getName();
        return getUserByUsername(username);
    }

    /**
     * Производит поиск в БД пользователя по его уникальному имени (логину)
     * @param username the username identifying the user whose data is required.
     * @return объект класса AdUser, реализующий интерфейс UserDetails
     * @throws UsernameNotFoundException если пользователь не найден в БД по указанному имени (логину)
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return getUserByUsername(username);
    }

    /**
     * Производит проверку наличие в БД пользователя, с указанным логином
     * @param username Логин пользователя, используемый при регистрации и входе в систему
     * @return true, если пользователь с указанным логином, существует в БД
     */
    @Override
    public boolean userExists(String username) {
        return adUserRepository.findByUsername(username) != null;
    }

    /**
     * Создает (сохраняет) пользователя в БД
     * @param adUser объект класса AdUser, пользователь
     */
    @Override
    public void createUser(AdUser adUser) {
        adUserRepository.save(adUser);
    }
}
