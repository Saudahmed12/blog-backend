package com.saud.blog.Services;

import com.saud.blog.domain.DTOs.RegistrationRequest;
import com.saud.blog.domain.entities.User;

import java.util.UUID;

public interface UserService {
    User getUserById(UUID id);


}
