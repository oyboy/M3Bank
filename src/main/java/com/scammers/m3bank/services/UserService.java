package com.scammers.m3bank.services;

import com.scammers.m3bank.models.User;
import com.scammers.m3bank.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public User getUserById(Long user_id) {
        return userRepository.findById(user_id);
    }
}
