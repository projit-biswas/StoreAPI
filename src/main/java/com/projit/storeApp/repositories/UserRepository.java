package com.projit.storeApp.repositories;

import com.projit.storeApp.entities.User;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.domain.Example;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {
	boolean existsByEmail(String email);
	Optional<User> findByEmail(String email);
}
