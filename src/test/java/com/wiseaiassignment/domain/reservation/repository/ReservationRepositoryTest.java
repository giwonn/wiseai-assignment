package com.wiseaiassignment.domain.reservation.repository;

import com.wiseaiassignment.domain.meetingroom.model.MeetingRoom;
import com.wiseaiassignment.domain.meetingroom.repository.MeetingRoomRepository;
import com.wiseaiassignment.domain.reservation.model.Reservation;
import com.wiseaiassignment.domain.reservation.model.ReservationAttendee;
import com.wiseaiassignment.domain.reservation.model.ReservationStatus;
import com.wiseaiassignment.domain.user.model.User;
import com.wiseaiassignment.domain.user.repository.UserRepository;
import com.wiseaiassignment.helper.util.DatabaseCleanUp;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class ReservationRepositoryTest {

	@Autowired
	ReservationRepository reservationRepository;

	@Autowired
	ReservationAttendeeRepository reservationAttendeeRepository;

	@Autowired
	UserRepository userRepository;

	@Autowired
	MeetingRoomRepository meetingRoomRepository;

	@Autowired
	private DatabaseCleanUp databaseCleanUp;

	private User savedUser;
	private MeetingRoom savedMeetingRoom;

	@BeforeEach
	void setUp() {
		savedUser = userRepository.save(User.create("test@example.com", "테스트사용자"));
		savedMeetingRoom = meetingRoomRepository.save(MeetingRoom.create("회의실1", 10));
	}

	@AfterEach
	void tearDown() {
		databaseCleanUp.truncateAllTables();
	}

	@Nested
	@DisplayName("ID로 참여자를 함께 조회")
	class findByIdWithAttendees {

		@Test
		void 참여자와_함께_예약이_조회된다() {
			// given
			Reservation reservation = Reservation.create(
					"주간회의",
					LocalDateTime.of(2025,1,1,14,0),
					LocalDateTime.of(2025,1,1,15,0),
					savedMeetingRoom.getId(),
					savedUser.getId()
			);
			reservationRepository.save(reservation);

			User secondUser = userRepository.save(User.create("test2@example.com", "테스트사용자2"));
			List<ReservationAttendee> attendees = List.of(
					ReservationAttendee.of(reservation.getId(), secondUser.getId()),
					ReservationAttendee.of(reservation.getId(), savedUser.getId())
			);
			reservationAttendeeRepository.saveAll(attendees);

			// when
			Optional<Reservation> result = reservationRepository.findByIdWithAttendees(reservation.getId());

			// then
			assertThat(result).isNotEmpty();
			assertThat(result.get().getId()).isEqualTo(reservation.getId());
			assertThat(result.get().getAttendees()).hasSize(2);
		}
	}

	@Nested
	@DisplayName("상태와 예약시간 기준 조회")
	class findByStatusAndDate {

		@Test
		void RESERVED인_예약이_조회된다() {
			// given
			Reservation reservation = Reservation.create(
					"주간회의",
					LocalDateTime.of(2025,1,1,14,0),
					LocalDateTime.of(2025,1,1,15,0),
					savedMeetingRoom.getId(),
					savedUser.getId()
			);
			reservationRepository.save(reservation);

			// when
			List<Reservation> result = reservationRepository.findByStatusAndDate(
					ReservationStatus.RESERVED,
					LocalDateTime.of(2025, 1, 1, 0, 0),
					LocalDateTime.of(2025, 1, 2, 0, 0)
			);

			// then
			assertThat(result).hasSize(1);
			assertThat(result.get(0).getId()).isEqualTo(reservation.getId());
			assertThat(result.get(0).getStatus()).isEqualTo(ReservationStatus.RESERVED);
		}
	}

	@Nested
	@DisplayName("같은 회의실의 겹치는 예약시간 조회")
	class findConflictReservations {

		@Test
		void 예약시작시간에_종료되는_회의는_조회되지_않는다() {
			// given
			Reservation reservation = Reservation.create(
					"주간회의",
					LocalDateTime.of(2025,1,1,14,0),
					LocalDateTime.of(2025,1,1,15,0),
					savedMeetingRoom.getId(),
					savedUser.getId()
			);
			reservationRepository.save(reservation);

			// when
			List<Reservation> result = reservationRepository.findConflictReservations(
					1L,
					LocalDateTime.of(2025, 1, 1, 15, 0), // 15:00 ~ 16:00
					LocalDateTime.of(2025, 1, 1, 16, 0)
			);

			// then
			assertThat(result).isEmpty();
		}

		@Test
		void 예약시간이_겹치면_조회된다() {
			// given
			Reservation reservation = Reservation.create(
					"주간회의",
					LocalDateTime.of(2025,1,1,14,0),
					LocalDateTime.of(2025,1,1,15,0),
					savedMeetingRoom.getId(),
					savedUser.getId()
			);
			reservationRepository.save(reservation);

			// when
			List<Reservation> result = reservationRepository.findConflictReservations(
					savedMeetingRoom.getId(),
					LocalDateTime.of(2025, 1, 1, 14, 30), // 14:30 ~ 15:30
					LocalDateTime.of(2025, 1, 1, 15, 30)
			);

			// then
			assertThat(result).hasSize(1);
			assertThat(result.get(0).getId()).isEqualTo(reservation.getId());
		}

		@Test
		void 예약종료시간에_시작하는_회의는_겹치지_않는다() {
			// given
			Reservation reservation = Reservation.create(
					"주간회의",
					LocalDateTime.of(2025,1,1,14,0),
					LocalDateTime.of(2025,1,1,15,0),
					savedMeetingRoom.getId(),
					savedUser.getId()
			);
			reservationRepository.save(reservation);

			// when
			List<Reservation> result = reservationRepository.findConflictReservations(
					1L,
					LocalDateTime.of(2025, 1, 1, 13, 0), // 13:00 ~ 14:00
					LocalDateTime.of(2025, 1, 1, 14, 0)
			);

			// then
			assertThat(result).isEmpty();
		}

		@Test
		void 다른_회의실의_예약은_겹치지_않는다() {
			// given
			MeetingRoom secondMeetingRoom = meetingRoomRepository.save(MeetingRoom.create("회의실2", 5));
			Reservation reservation = Reservation.create(
					"주간회의",
					LocalDateTime.of(2025,1,1,14,0),
					LocalDateTime.of(2025,1,1,15,0),
					savedMeetingRoom.getId(),
					savedUser.getId()
			);
			reservationRepository.save(reservation);

			// when
			List<Reservation> result = reservationRepository.findConflictReservations(
					secondMeetingRoom.getId(), // 다른 회의실
					LocalDateTime.of(2025, 1, 1, 14, 30), // 14:30 ~ 15:30
					LocalDateTime.of(2025, 1, 1, 15, 30)
			);

			// then
			assertThat(result).isEmpty();
		}
	}


}
