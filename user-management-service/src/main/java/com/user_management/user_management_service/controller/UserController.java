package com.user_management.user_management_service.controller;

import com.user_management.user_management_service.dto.UpdateUserRequest;
import com.user_management.user_management_service.dto.UserRequest;
import com.user_management.user_management_service.model.Role;
import com.user_management.user_management_service.model.User;
import com.user_management.user_management_service.service.RoleService;
import com.user_management.user_management_service.service.UserService;
import com.user_management.user_management_service.utils.ResponseHandler;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    private final RestTemplate restTemplate;
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final RoleService roleService;

    @Autowired
    public UserController(RestTemplate restTemplate,
                          UserService userService,
                          PasswordEncoder passwordEncoder,
                          RoleService roleService) {
        this.restTemplate = restTemplate;
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.roleService = roleService;
    }

    @GetMapping("/hello-from-trainee")
    public ResponseEntity<Object> helloFromTrainee() {
        try {
            String response = restTemplate.getForObject("http://localhost:8093/trainees/hello", String.class);
            return ResponseHandler.responseBuilder("Response from Trainee Management Service", HttpStatus.OK, response);
        } catch (Exception e) {
            return ResponseHandler.responseBuilder("Error calling Trainee Management Service", HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @PostMapping
    @Transactional
    public ResponseEntity<Object> addUser(@Valid @RequestBody UserRequest userInfo) {
        try {
            User user = new User();
            user.setName(userInfo.getName());
            user.setEmail(userInfo.getEmail());
            user.setPassword(passwordEncoder.encode(userInfo.getPassword()));

            Optional<Role> role = roleService.getRoleById(userInfo.getRoleId());
            if (role.isEmpty()) {
                return ResponseHandler.responseBuilder("Role not found", HttpStatus.BAD_REQUEST, null);
            }
            user.setRole(role.get());

            User addedUser = userService.saveUser(user);
            return ResponseHandler.responseBuilder("User added successfully", HttpStatus.CREATED, addedUser);
        } catch (Exception e) {
            return ResponseHandler.responseBuilder("Error adding user", HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteUser(@PathVariable int id) {
        try {
            userService.deleteUser(id);
            return ResponseHandler.responseBuilder("User deleted successfully", HttpStatus.OK, null);
        } catch (Exception e) {
            return ResponseHandler.responseBuilder("Error deleting user", HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> updateUser(@PathVariable int id, @Valid @RequestBody UpdateUserRequest userInfo) {
        try {
            User user = new User();
            user.setName(userInfo.getName());
            user.setEmail(userInfo.getEmail());

            Optional<Role> role = roleService.getRoleById(userInfo.getRoleId());
            if (role.isEmpty()) {
                return ResponseHandler.responseBuilder("Role not found", HttpStatus.BAD_REQUEST, null);
            }
            user.setRole(role.get());

            Optional<User> updatedUser = userService.updateUser(id, user);
            if (updatedUser.isEmpty()) {
                return ResponseHandler.responseBuilder("User not found", HttpStatus.NOT_FOUND, null);
            }
            return ResponseHandler.responseBuilder("User updated successfully", HttpStatus.OK, updatedUser.get());
        } catch (Exception e) {
            return ResponseHandler.responseBuilder("Error updating user", HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getUserById(@PathVariable int id) {
        try {
            Optional<User> user = userService.getUserById(id);
            if (user.isEmpty()) {
                return ResponseHandler.responseBuilder("User not found", HttpStatus.NOT_FOUND, null);
            }
            return ResponseHandler.responseBuilder("User retrieved successfully", HttpStatus.OK, user.get());
        } catch (Exception e) {
            return ResponseHandler.responseBuilder("Error retrieving user", HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<Object> getAllUsers() {
        try {
            List<User> users = userService.getAllUsers();
            return ResponseHandler.responseBuilder("Users retrieved successfully", HttpStatus.OK, users);
        } catch (Exception e) {
            return ResponseHandler.responseBuilder("Error retrieving users", HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @GetMapping("/role/{roleName}")
    public ResponseEntity<Object> getUsersByRole(@PathVariable String roleName) {
        try {
            List<User> users = userService.getUsersByRole(roleName);
            return ResponseHandler.responseBuilder("Users with role " + roleName + " retrieved successfully", HttpStatus.OK, users);
        } catch (Exception e) {
            return ResponseHandler.responseBuilder("Error retrieving users by role", HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }
}