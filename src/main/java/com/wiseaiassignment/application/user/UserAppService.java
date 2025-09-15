package com.wiseaiassignment.application.user;

import com.wiseaiassignment.application.user.dto.CreateUserCommand;
import com.wiseaiassignment.application.user.dto.CreateUserResult;
import com.wiseaiassignment.application.user.dto.UserResult;
import com.wiseaiassignment.domain.user.UserService;
import com.wiseaiassignment.domain.user.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserAppService {

	private final UserService userService;

	public CreateUserResult createUser(CreateUserCommand command) {
		User user = userService.create(command.toEntity());
		return CreateUserResult.from(user);
	}

	public UserResult findById(Long userId) {
		User user = userService.findById(userId);
		return UserResult.from(user);
	}
}
