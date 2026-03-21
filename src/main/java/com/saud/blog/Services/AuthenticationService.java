package com.saud.blog.Services;

import com.saud.blog.domain.DTOs.RegistrationRequest;
import org.springframework.security.core.userdetails.UserDetails;

public interface AuthenticationService {

    UserDetails authenticate(String email, String password);
    String generateToken(UserDetails userDetails);

    UserDetails validateToken(String token);

    void registerUser(RegistrationRequest request);
}
