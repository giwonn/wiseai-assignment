package com.wiseaiassignment.api.user;

import com.wiseaiassignment.api.ApiCustomResponse;
import com.wiseaiassignment.api.reservation.dto.CancelReservationRequest;
import com.wiseaiassignment.api.reservation.dto.CreateReservationRequest;
import com.wiseaiassignment.api.reservation.dto.ReservationResponse;
import com.wiseaiassignment.api.reservation.dto.ReservationSummaryResponse;
import com.wiseaiassignment.api.user.dto.CreateUserRequest;
import com.wiseaiassignment.api.user.dto.CreateUserResponse;
import com.wiseaiassignment.api.user.dto.UserResponse;
import com.wiseaiassignment.application.reservation.ReservationAppService;
import com.wiseaiassignment.application.reservation.dto.ReservationResult;
import com.wiseaiassignment.application.reservation.dto.ReservationSummaryResult;
import com.wiseaiassignment.application.user.UserAppService;
import com.wiseaiassignment.application.user.dto.CreateUserResult;
import com.wiseaiassignment.application.user.dto.UserResult;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController implements UserApiSpec {

	private final ReservationAppService reservationAppService;
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
