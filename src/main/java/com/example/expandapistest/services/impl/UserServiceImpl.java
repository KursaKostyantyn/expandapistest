package com.example.expandapistest.services.impl;

import com.example.expandapistest.exception.DuplicateException;
import com.example.expandapistest.mapper.UserMapper;
import com.example.expandapistest.models.User;
import com.example.expandapistest.models.dto.LoginResponseDto;
import com.example.expandapistest.models.dto.UserDto;
import com.example.expandapistest.models.dto.UserLoginDto;
import com.example.expandapistest.repositories.UserRepository;
import com.example.expandapistest.services.api.UserService;
import com.example.expandapistest.utils.JwtUtil;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    @Override
    public ResponseEntity<Void> addUser(UserDto userDto) {
        String username =userDto.getUsername();
        Optional<User> existingUser = userRepository.findByUsername(username);
        if (existingUser.isPresent()){
            throw new DuplicateException(String.format("User with username '%s' already exists",username));
        }
        User user = UserMapper.convertUserDtoToUser(userDto);
        String hashedPassword = passwordEncoder.encode(userDto.getPassword());
        user.setPassword(hashedPassword);
        userRepository.save(user);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Override
    public ResponseEntity<LoginResponseDto> authenticate(UserLoginDto userLoginDto) {
        Optional<User> user = userRepository.findByUsername(userLoginDto.getUsername());
        if (user.isEmpty()){
            throw new BadCredentialsException("Дані користувача некоректні");
        }
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userLoginDto.getUsername(), userLoginDto.getPassword()));
        } catch (BadCredentialsException e){
            throw new BadCredentialsException("Дані користувача некоректні");
        }
        String token = JwtUtil.generateToken(userLoginDto.getUsername());
        LoginResponseDto loginResponseDto = new LoginResponseDto(token);
        return ResponseEntity.ok(loginResponseDto);
    }
}
