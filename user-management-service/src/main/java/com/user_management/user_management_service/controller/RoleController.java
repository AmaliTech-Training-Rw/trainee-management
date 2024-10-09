package com.user_management.user_management_service.controller;

import com.user_management.user_management_service.dto.RoleRequest;
import com.user_management.user_management_service.model.Role;
import com.user_management.user_management_service.service.RoleService;
import com.user_management.user_management_service.utils.ResponseHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/roles")
public class RoleController {

    private final RoleService roleService;

    @Autowired
    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }

    @GetMapping
    public ResponseEntity<Object> getAllRoles() {
        try {
            List<Role> roles = roleService.getAllRoles();
            return ResponseHandler.responseBuilder("Roles retrieved successfully", HttpStatus.OK, roles);
        } catch (Exception e) {
            return ResponseHandler.responseBuilder("Error retrieving roles: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, null);
        }
    }

    @PostMapping
    public ResponseEntity<Object> addRole(@RequestBody RoleRequest roleInfo) {
        try {
            Role role = new Role();
            role.setName(roleInfo.getName());
            role.setDescription(roleInfo.getDescription());

            Role addedRole = roleService.saveRole(role);
            return ResponseHandler.responseBuilder("Role added successfully", HttpStatus.CREATED, addedRole);
        } catch (Exception e) {
            return ResponseHandler.responseBuilder("Error adding role: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, null);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteRole(@PathVariable int id) {
        try {
            roleService.deleteRole(id);
            return ResponseHandler.responseBuilder("Role deleted successfully", HttpStatus.OK, "");
        } catch (Exception e) {
            return ResponseHandler.responseBuilder("Error deleting role: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, null);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> updateRole(@PathVariable int id, @RequestBody RoleRequest roleInfo) {
        try {
            Role role = new Role();
            role.setName(roleInfo.getName());
            role.setDescription(roleInfo.getDescription());

            Optional<Role> updatedRole = roleService.updateRole(id, role);
            return ResponseHandler.responseBuilder("Role updated successfully", HttpStatus.OK, updatedRole);
        } catch (Exception e) {
            return ResponseHandler.responseBuilder("Error updating role: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, null);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getRoleById(@PathVariable int id) {
        try {
            Optional<Role> role = roleService.getRoleById(id);
            return ResponseHandler.responseBuilder("Role retrieved successfully", HttpStatus.OK, role);
        } catch (Exception e) {
            return ResponseHandler.responseBuilder("Error retrieving role: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, null);
        }
    }
}