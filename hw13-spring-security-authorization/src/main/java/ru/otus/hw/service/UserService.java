package ru.otus.hw.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.dto.user.UserDto;
import ru.otus.hw.dto.user.UserRegistrationDto;
import ru.otus.hw.exception.BusinessLogicException;
import ru.otus.hw.exception.EntityNotFoundException;
import ru.otus.hw.mapper.UserMapper;
import ru.otus.hw.model.MyUser;
import ru.otus.hw.repository.UserRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final UserMapper userMapper;

    @Transactional(readOnly = true)
    public UserDto getById(Long id) {
        MyUser user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User with id %d not found".formatted(id)));

        return userMapper.toDto(user);
    }

    @Transactional(readOnly = true)
    public List<UserDto> findAllUsers() {
        List<MyUser> users = userRepository.findAll();

        return userMapper.toDtoList(users);
    }

    @Transactional
    public void createUser(UserRegistrationDto registrationDto) {

        if (userRepository.existsByUsername(registrationDto.username())) {
            throw new BusinessLogicException("Пользователь с таким именем уже существует");
        }

        MyUser user = new MyUser();
        user.setUsername(registrationDto.username());
        user.setPassword(passwordEncoder.encode(registrationDto.password()));

        if (registrationDto.role() != null) {
            user.setRole(registrationDto.role());
        }

        userRepository.save(user);
    }
}
