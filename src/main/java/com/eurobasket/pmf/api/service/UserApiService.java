package com.eurobasket.pmf.api.service;

import com.eurobasket.pmf.dto.UserDTO;
import com.eurobasket.pmf.model.Role;
import com.eurobasket.pmf.model.User;
import com.eurobasket.pmf.repository.RoleRepository;
import com.eurobasket.pmf.repository.UserRepository;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;

@Service
public class UserApiService {

    private static final String USER_ROLE = "ROLE_USER";

    private UserRepository userRepository;
    private RoleRepository roleRepository;

    public JSONObject getAllUsers() {
        JSONObject result = new JSONObject();
        JSONArray users = new JSONArray();
        userRepository.findAll().forEach(user -> users.put(createJsonResponse(user)));
        result.put("users", users);
        return result;
    }

    public JSONObject getUserById(int id) {
        return userRepository.findById(id).map(this::createJsonResponse).orElse(null);
    }

    public JSONObject getCurrentUser(User currentUser) {
        return createJsonResponse(currentUser);
    }

    public JSONObject createUser(UserDTO userDTO) {
        Optional<Role> role = roleRepository.findByName(USER_ROLE);
        if (role.isPresent() && userRepository.findByUsername(userDTO.getUsername()).isEmpty()) {
            User user = new User(userDTO.getUsername(), new BCryptPasswordEncoder().encode(userDTO.getPassword()), new HashSet<>(Collections.singletonList(role.get())));
            userRepository.save(user);
            return createJsonResponse(user);
        }
        return null;
    }

    public JSONObject changePassword(Optional<User> currentUser, String newPassword) {
        if (currentUser.isPresent()) {
            User user = currentUser.get();
            user.setPassword(new BCryptPasswordEncoder().encode(newPassword));
            userRepository.save(user);
            return createJsonResponse(user);
        }
        return null;
    }

    public JSONObject deleteUserById(int id) {
        Optional<User> user = userRepository.findById(id);
        if (user.isPresent()) {
            JSONObject response = new JSONObject();
            response.put("deleted user", createJsonResponse(user.get()));
            userRepository.delete(user.get());
            return response;
        }
        return null;
    }

    private JSONObject createJsonResponse(User user) {
        JSONObject response = new JSONObject();
        response.put("id", user.getId());
        response.put("username", user.getUsername());
        response.put("roles", user.getRoles());
        return response;
    }

    @Autowired
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Autowired
    public void setRoleRepository(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }
}
