package com.example.expandapistest.services.api;

import com.example.expandapistest.models.dto.LoginResponseDto;
import com.example.expandapistest.models.dto.UserDto;
import com.example.expandapistest.models.dto.UserLoginDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;

public interface UserService {
    ResponseEntity<Void> addUser(UserDto userDto);
    ResponseEntity<LoginResponseDto> authenticate(UserLoginDto userLoginDto);
}
