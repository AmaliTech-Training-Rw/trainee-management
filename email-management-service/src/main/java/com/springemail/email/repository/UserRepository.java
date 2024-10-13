package com.springemail.email.repository;



import com.springemail.email.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);

    Optional<User> findByResetToken(String token);

    // If you are using a reset token

}