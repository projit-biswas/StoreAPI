package com.projit.storeApp.repositories;

import com.projit.storeApp.entities.User;
import org.springframework.data.domain.Example;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {
	boolean existsByEmail(String email);
}
