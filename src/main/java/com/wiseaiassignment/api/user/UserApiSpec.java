package com.wiseaiassignment.api.user;

import com.wiseaiassignment.api.ApiCustomResponse;
import com.wiseaiassignment.api.user.dto.CreateUserRequest;
import com.wiseaiassignment.api.user.dto.CreateUserResponse;
import com.wiseaiassignment.api.user.dto.UserResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;


@Tag(name = "Users", description = "유저 API")
public interface UserApiSpec {

	@Operation(summary = "유저 생성", description = "유저를 생성합니다.")
	@ApiResponse(responseCode = "200", description = "유저 생성 성공")
	ApiCustomResponse<CreateUserResponse> createUser(CreateUserRequest request);

	@Operation(summary = "유저 조회", description = "유저ID로 유저를 조회합니다.")
	@ApiResponse(responseCode = "200", description = "유저 조회 성공")
	ApiCustomResponse<UserResponse> getUserById(long id);

}
