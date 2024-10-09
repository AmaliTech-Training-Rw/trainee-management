package com.user_management.user_management_service.service;

import com.user_management.user_management_service.model.Role;
import com.user_management.user_management_service.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RoleService {

    @Autowired
    private RoleRepository roleRepository;

    public List<Role> getAllRoles(){
        return roleRepository.findAll();
    }

    public Role saveRole(Role role){
        return roleRepository.save(role);
    }

    public void deleteRole(int id){
        roleRepository.deleteById(id);
    }

    public Optional<Role> updateRole(int id, Role roleDetails) {
        return roleRepository.findById(id).map(role -> {
            role.setName(roleDetails.getName());
            role.setDescription(roleDetails.getDescription());
            return roleRepository.save(role);
        });
    }

    public Optional<Role> getRoleById(int id){
        return roleRepository.findById(id);
    }
}
