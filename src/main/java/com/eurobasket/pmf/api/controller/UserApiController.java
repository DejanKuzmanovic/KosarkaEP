package com.eurobasket.pmf.api.controller;

import com.eurobasket.pmf.api.service.UserApiService;
import com.eurobasket.pmf.dto.MatchingPasswordDTO;
import com.eurobasket.pmf.dto.UserDTO;
import com.eurobasket.pmf.model.User;
import com.eurobasket.pmf.service.AuthenticationService;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Optional;

@RestController
@RequestMapping(value = "/api/v1")
public class UserApiController {

    private UserApiService userApiService;
    private AuthenticationService authenticationService;

    @GetMapping(value = "/users", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getAllUsers() {
        if (authenticationService.isAdmin()) {
            return ResponseEntity.ok(userApiService.getAllUsers().toString());
        }
        return ResponseEntity.badRequest().build();
    }

    @GetMapping(value = "/user/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getUserById(@PathVariable int id) {
        JSONObject response = userApiService.getUserById(id);
        return response != null ? ResponseEntity.ok(response.toString()) : ResponseEntity.notFound().build();
    }

    @GetMapping(value = "/user/current", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getCurrentUser() {
        Optional<User> currentUser = authenticationService.getCurrentUser();
        JSONObject response = new JSONObject();
        if (currentUser.isPresent()) {
            response = userApiService.getCurrentUser(currentUser.get());
        }
        return response != null ? ResponseEntity.ok(response.toString()) : ResponseEntity.notFound().build();
    }

    @PostMapping(value = "/user/create", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> createUser(@Valid @RequestBody UserDTO userDTO) {
        JSONObject response = userApiService.createUser(userDTO);
        return response != null ? ResponseEntity.ok(response.toString()) : ResponseEntity.badRequest().build();
    }

    @PutMapping(value = "/user/edit", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> changePassword(@Valid @RequestBody MatchingPasswordDTO passwordDTO) {
        JSONObject response = userApiService.changePassword(authenticationService.getCurrentUser(), passwordDTO.getPassword());
        return response != null ? ResponseEntity.ok(response.toString()) : ResponseEntity.notFound().build();
    }

    @DeleteMapping(value = "/user/delete/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable int id) {
        if (authenticationService.isAdmin()) {
            JSONObject response = userApiService.deleteUserById(id);
            return response != null ? ResponseEntity.ok(response.toString()) : ResponseEntity.notFound().build();
        }
        return ResponseEntity.badRequest().build();
    }

    @Autowired
    public void setUserApiService(UserApiService userApiService) {
        this.userApiService = userApiService;
    }

    @Autowired
    public void setAuthenticationService(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }
}

