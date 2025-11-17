package ru.otus.hw.mappers;

import org.springframework.stereotype.Component;
import ru.otus.hw.dtos.userdtos.UserDto;
import ru.otus.hw.models.MyUser;

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
