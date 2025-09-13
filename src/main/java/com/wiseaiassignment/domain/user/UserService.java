package com.wiseaiassignment.domain.user;

import com.wiseaiassignment.domain.common.exception.DomainException;
import com.wiseaiassignment.domain.common.exception.ExceptionType;
import com.wiseaiassignment.domain.user.model.User;
import com.wiseaiassignment.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

	private final UserRepository userRepository;

	public User findActiveUserById(Long userId) {
		return userRepository.findByIdAndActive(userId, true)
				.orElseThrow(() -> new DomainException(ExceptionType.NOT_FOUND_USER));
	}

	public User findById(Long userId) {
		return userRepository.findById(userId)
				.orElseThrow(() -> new DomainException(ExceptionType.NOT_FOUND_USER));
	}

}
