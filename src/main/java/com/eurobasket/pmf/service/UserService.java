package com.eurobasket.pmf.service;

import com.eurobasket.pmf.model.User;
import com.eurobasket.pmf.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private UserRepository userRepository;

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Autowired
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

}
