package com.user_management.user_management_service.repository;

import com.user_management.user_management_service.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User,Integer> {
    @Query("SELECT u FROM User u WHERE u.role.name = :roleName")
    List<User> findUsersByRoleName(@Param("roleName") String roleName);
}
