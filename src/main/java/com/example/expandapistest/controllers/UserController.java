package com.example.expandapistest.controllers;

import com.example.expandapistest.models.dto.LoginResponseDto;
import com.example.expandapistest.models.dto.UserDto;
import com.example.expandapistest.models.dto.UserLoginDto;
import com.example.expandapistest.services.api.UserService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/user")
public class UserController {
    private final UserService userService;

    @PostMapping("/add")
    public ResponseEntity<Void> addUser(@Valid @RequestBody UserDto userDto) {
        return userService.addUser(userDto);
    }

    @PostMapping("/authenticate")
    public ResponseEntity<LoginResponseDto> authenticate(@Valid @RequestBody UserLoginDto userLoginDto){
        return userService.authenticate(userLoginDto);
    }

    @GetMapping("/secret")
    public ResponseEntity<String> secret(){
        return ResponseEntity.ok("Secret information");
    }
}
