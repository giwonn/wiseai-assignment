package com.wiseaiassignment.api.reservation;

import com.wiseaiassignment.api.ApiCustomResponse;
import com.wiseaiassignment.api.reservation.dto.CreateReservationRequest;
import com.wiseaiassignment.api.reservation.dto.ReservationResponse;
import com.wiseaiassignment.domain.meetingroom.model.MeetingRoom;
import com.wiseaiassignment.domain.meetingroom.repository.MeetingRoomRepository;
import com.wiseaiassignment.domain.user.model.User;
import com.wiseaiassignment.domain.user.repository.UserRepository;
import com.wiseaiassignment.helper.util.DatabaseCleanUp;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ReservationApiE2ETest {

	private static final String ENDPOINT = "/reservations";

	@Autowired
	TestRestTemplate testRestTemplate;

	@Autowired
	MeetingRoomRepository meetingRoomRepository;

	@Autowired
	UserRepository userRepository;

	@Autowired
	DatabaseCleanUp databaseCleanUp;

	User savedUser;
	MeetingRoom savedMeetingRoom;

	@BeforeEach
	void setUp() {
		savedUser = userRepository.save(User.create("test@example.com", "테스트사용자"));
		savedMeetingRoom = meetingRoomRepository.save(MeetingRoom.create("회의실1", 10));
	}

	@AfterEach
	void tearDown() {
		databaseCleanUp.truncateAllTables();
	}

	@DisplayName("POST /reservations - 회의실 예약")
	@Nested
	class CreateReservation {

		@Test
		void 회의실_예약에_성공한다() {
			// given
			CreateReservationRequest requestBody = new CreateReservationRequest(
					savedMeetingRoom.getId(),
					"주간회의",
					LocalDateTime.of(2025,1,1,10,0),
					LocalDateTime.of(2025,1,1,11,0),
					savedUser.getId(),
					List.of()
			);
			HttpEntity request = new HttpEntity<>(requestBody);

			// when
			ParameterizedTypeReference<ApiCustomResponse<ReservationResponse>> responseType =
					new ParameterizedTypeReference<>() {};
			ResponseEntity<ApiCustomResponse<ReservationResponse>> response =
					testRestTemplate.exchange(ENDPOINT, HttpMethod.POST, request, responseType);

			// then
			System.out.println(response.getStatusCode());
			assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
			assertThat(response.getBody()).isNotNull();
			ReservationResponse reservation = response.getBody().data();
			assertThat(reservation.reservationId()).isEqualTo(1L);
			assertThat(reservation.roomId()).isEqualTo(savedMeetingRoom.getId());
			assertThat(reservation.roomName()).isEqualTo(savedMeetingRoom.getName());
			assertThat(reservation.reserverName()).isEqualTo(savedUser.getName());
			assertThat(reservation.reserverEmail()).isEqualTo(savedUser.getEmail());
			assertThat(reservation.startTime()).isEqualTo(requestBody.startTime());
			assertThat(reservation.endTime()).isEqualTo(requestBody.endTime());
		}

		void 겹치는_시간에_예약을_시도하면_409에러코드를_반환한다() {
			throw new Error("구현 예정");
		}

	}

}
