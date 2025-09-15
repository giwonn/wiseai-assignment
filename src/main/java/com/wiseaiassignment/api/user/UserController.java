package com.wiseaiassignment.api.user;

import com.wiseaiassignment.api.ApiCustomResponse;
import com.wiseaiassignment.api.user.dto.CreateUserRequest;
import com.wiseaiassignment.api.user.dto.CreateUserResponse;
import com.wiseaiassignment.api.user.dto.UserResponse;
import com.wiseaiassignment.application.user.UserAppService;
import com.wiseaiassignment.application.user.dto.CreateUserResult;
import com.wiseaiassignment.application.user.dto.UserResult;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController implements UserApiSpec {

	private final UserAppService userAppService;

	@Override
	@PostMapping(path = "")
	public ApiCustomResponse<CreateUserResponse> createUser(
			@Valid @RequestBody CreateUserRequest request
	) {
		CreateUserResult result = userAppService.createUser(request.toCommand());
		return ApiCustomResponse.of(CreateUserResponse.from(result));
	}

	@Override
	@GetMapping(path = "/{id}")
	public ApiCustomResponse<UserResponse> getUserById(
			@PathVariable long id
	) {
		UserResult result = userAppService.findById(id);
		return ApiCustomResponse.of(UserResponse.from(result));
	}
}
