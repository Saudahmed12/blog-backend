package com.saud.blog.Services.impl;

import com.saud.blog.Repositories.UserRepository;
import com.saud.blog.Services.UserService;
import com.saud.blog.domain.DTOs.RegistrationRequest;
import com.saud.blog.domain.entities.User;
import com.saud.blog.exceptions.EmailAlreadyExistsException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public User getUserById(UUID id) {
            return userRepository
                    .findById(id)
                    .orElseThrow(()-> new EntityNotFoundException("user not found with id: "+ id));
    }




}
