package com.wiseaiassignment.api.reservation;

import com.wiseaiassignment.api.ApiCustomResponse;
import com.wiseaiassignment.api.ApiResponseType;
import com.wiseaiassignment.api.reservation.dto.CreateReservationRequest;
import com.wiseaiassignment.api.reservation.dto.ReservationResponse;
import com.wiseaiassignment.api.reservation.dto.ReservationSummaryResponse;
import com.wiseaiassignment.domain.meetingroom.model.MeetingRoom;
import com.wiseaiassignment.domain.meetingroom.repository.MeetingRoomRepository;
import com.wiseaiassignment.domain.reservation.model.Reservation;
import com.wiseaiassignment.domain.reservation.repository.ReservationRepository;
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
import java.util.Comparator;
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
	ReservationRepository reservationRepository;

	@Autowired
	DatabaseCleanUp databaseCleanUp;

	User savedUser;
	MeetingRoom savedMeetingRoom;
	List<Reservation> savedReservations;

	@BeforeEach
	void setUp() {
		savedUser = userRepository.save(User.create("test@example.com", "테스트사용자"));
		savedMeetingRoom = meetingRoomRepository.save(MeetingRoom.create("회의실1", 10));
		savedReservations = reservationRepository.saveAll(
				List.of(
					Reservation.create(
							"회의1",
							savedUser.getId(),
							savedUser.getEmail(),
							savedMeetingRoom.getId(),
							savedMeetingRoom.getName(),
							LocalDateTime.of(2025,1,1,8,0),
							LocalDateTime.of(2025,1,1,9,0),
							List.of("test1@email.com", "test2@email.com")
					),
					Reservation.create(
							"회의2",
							savedUser.getId(),
							savedUser.getEmail(),
							savedMeetingRoom.getId(),
							savedMeetingRoom.getName(),
							LocalDateTime.of(2025,1,1,9,0),
							LocalDateTime.of(2025,1,1,10,0),
							List.of("test3@email.com", "test4@email.com")
					)
				)
		);
	}

	@AfterEach
	void tearDown() {
		databaseCleanUp.truncateAllTables();
	}

	@DisplayName("GET /reservations/summary - 일단위 회의실 예약 리스트 조회")
	@Nested
	class getReservationsByDate {

		@Test
		void 회의실_예약_리스트를_조회한다() {
			// given
			String url = ENDPOINT + "/summary?date=2025-01-01";

			// when
			ParameterizedTypeReference<ApiCustomResponse<List<ReservationSummaryResponse>>> responseType =
					new ParameterizedTypeReference<>() {};
			ResponseEntity<ApiCustomResponse<List<ReservationSummaryResponse>>> response =
					testRestTemplate.exchange(url, HttpMethod.GET, null, responseType);

			// then
			assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
			assertThat(response.getBody()).isNotNull();
			assertThat(response.getBody().result()).isEqualTo(ApiResponseType.SUCCESS);

			List<ReservationSummaryResponse> reservations = response.getBody().data().stream()
					.sorted(Comparator.comparingLong(ReservationSummaryResponse::reservationId))
					.toList();
			assertThat(reservations).hasSize(2);
			assertThat(reservations.get(0).reservationId()).isEqualTo(savedReservations.get(0).getId());
			assertThat(reservations.get(0).reservationTitle()).isEqualTo(savedReservations.get(0).getTitle());
			assertThat(reservations.get(0).roomId()).isEqualTo(savedMeetingRoom.getId());
			assertThat(reservations.get(0).startTime()).isEqualTo(savedReservations.get(0).getStartTime());
			assertThat(reservations.get(0).endTime()).isEqualTo(savedReservations.get(0).getEndTime());
			assertThat(reservations.get(0).status()).isEqualTo(savedReservations.get(0).getStatus());
		}
	}

	@DisplayName("GET /reservations/{id} - 회의실 예약 단건 조회")
	@Nested
	class getReservation {
		@Test
		void 회의실_예약_단건을_조회한다() {
			// given
			String url = ENDPOINT + "/" + savedReservations.get(0).getId();

			// when
			ParameterizedTypeReference<ApiCustomResponse<ReservationResponse>> responseType =
					new ParameterizedTypeReference<>() {};
			ResponseEntity<ApiCustomResponse<ReservationResponse>> response =
					testRestTemplate.exchange(url, HttpMethod.GET, null, responseType);

			// then
			assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
			assertThat(response.getBody()).isNotNull();
			assertThat(response.getBody().result()).isEqualTo(ApiResponseType.SUCCESS);

			ReservationResponse reservation = response.getBody().data();
			assertThat(reservation.reservationId()).isEqualTo(savedReservations.get(0).getId());
			assertThat(reservation.roomId()).isEqualTo(savedMeetingRoom.getId());
			assertThat(reservation.roomName()).isEqualTo(savedMeetingRoom.getName());
			assertThat(reservation.reserverEmail()).isEqualTo(savedUser.getEmail());
			assertThat(reservation.startTime()).isEqualTo(savedReservations.get(0).getStartTime());
			assertThat(reservation.endTime()).isEqualTo(savedReservations.get(0).getEndTime());
		}
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
			assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
			assertThat(response.getBody()).isNotNull();
			assertThat(response.getBody().result()).isEqualTo(ApiResponseType.SUCCESS);

			ReservationResponse reservation = response.getBody().data();
			assertThat(reservation.roomId()).isEqualTo(savedMeetingRoom.getId());
			assertThat(reservation.roomName()).isEqualTo(savedMeetingRoom.getName());
			assertThat(reservation.reserverEmail()).isEqualTo(savedUser.getEmail());
			assertThat(reservation.startTime()).isEqualTo(requestBody.startTime());
			assertThat(reservation.endTime()).isEqualTo(requestBody.endTime());
		}

		void 겹치는_시간에_예약을_시도하면_409에러코드를_반환한다() {
			throw new Error("구현 예정");
		}

	}

	@DisplayName("POST /reservations/{id}/cancel - 회의실 예약 취소")
	@Nested
	class CancelReservation {

		@Test
		void 회의실_예약_취소_성공() {
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
			String endpoint = ENDPOINT + "/" + savedReservations.get(0).getId() + "/cancel";

			// when
			ParameterizedTypeReference<ApiCustomResponse<Void>> responseType =
					new ParameterizedTypeReference<>() {};
			ResponseEntity<ApiCustomResponse<Void>> response =
					testRestTemplate.exchange(endpoint, HttpMethod.POST, request, responseType);

			// then
			assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
			assertThat(response.getBody()).isNotNull();
			assertThat(response.getBody().result()).isEqualTo(ApiResponseType.SUCCESS);
			assertThat(response.getBody().data()).isNull();
		}

	}

}
