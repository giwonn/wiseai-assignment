package com.wiseaiassignment.domain.user;

import com.wiseaiassignment.domain.common.exception.DomainException;
import com.wiseaiassignment.domain.common.exception.ExceptionType;
import com.wiseaiassignment.domain.user.model.User;
import com.wiseaiassignment.domain.user.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

	@Mock
	private UserRepository userRepository;

	@InjectMocks
	private UserService userService;

	@Test
	void 존재하는_사용자_ID로_조회시_사용자를_반환한다() {
		// given
		User user = User.create("test@example.com", "테스트사용자");
		given(userRepository.findByIdAndActive(1L, true)).willReturn(Optional.of(user));

		// when
		User result = userService.findActiveUserById(1L);

		// then
		assertThat(result).isEqualTo(user);
	}

	@Test
	void 존재하지_않는_사용자_ID로_조회시_NOT_FOUND_USER_예외가_발생한다() {
		// given
		given(userRepository.findByIdAndActive(1L, true)).willReturn(Optional.empty());

		// when & then
		assertThatThrownBy(() -> userService.findActiveUserById(1L))
				.isInstanceOf(DomainException.class)
				.extracting(ex -> ((DomainException) ex).getType())
				.isEqualTo(ExceptionType.NOT_FOUND_USER);
	}
}
