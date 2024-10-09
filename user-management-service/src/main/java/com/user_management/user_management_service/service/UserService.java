package com.user_management.user_management_service.service;

import com.user_management.user_management_service.model.User;
import com.user_management.user_management_service.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public Optional<User> getUserById(int id){
        return userRepository.findById(id);
    }

    public List<User> getAllUsers(){
        return userRepository.findAll();
    }

    public User saveUser(User user){
        return userRepository.save(user);
    }

    public void deleteUser(int id){
        userRepository.deleteById(id);
    }

    public Optional<User> updateUser(int id, User userDetails) {
        return userRepository.findById(id).map(user -> {
            user.setName(userDetails.getName());
            user.setEmail(userDetails.getEmail());
            user.setPassword(userDetails.getPassword());
            user.setIsActive(userDetails.getIsActive());
            return userRepository.save(user);
        });
    }

    public List<User> getUsersByRole(String roleName) {
        return userRepository.findUsersByRoleName(roleName);
    }
}
