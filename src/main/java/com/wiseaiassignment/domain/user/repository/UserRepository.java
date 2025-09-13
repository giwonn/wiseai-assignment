package com.wiseaiassignment.domain.user.repository;

import com.wiseaiassignment.domain.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
	Optional<User> findByIdAndActive(Long id, boolean active);
}
