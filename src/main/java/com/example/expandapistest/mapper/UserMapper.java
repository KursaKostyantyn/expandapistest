package com.example.expandapistest.mapper;

import com.example.expandapistest.models.User;
import com.example.expandapistest.models.dto.UserDto;
import org.modelmapper.ModelMapper;

public class UserMapper {

    public static User convertUserDtoToUser(UserDto userDto) {
        ModelMapper mapper = new ModelMapper();

        return mapper.map(userDto, User.class);
    }

}
