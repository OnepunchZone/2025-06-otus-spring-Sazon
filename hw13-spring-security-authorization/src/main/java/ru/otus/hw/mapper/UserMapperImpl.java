package ru.otus.hw.mapper;

import org.springframework.stereotype.Component;
import ru.otus.hw.dto.user.UserDto;
import ru.otus.hw.model.MyUser;

import java.util.List;

@Component
public class UserMapperImpl implements UserMapper {

    @Override
    public UserDto toDto(MyUser user) {
        return new UserDto(user.getId(), user.getUsername(), user.getRole());
    }

    @Override
    public List<UserDto> toDtoList(List<MyUser> users) {
        return users.stream()
                .map(this::toDto)
                .toList();
    }
}
