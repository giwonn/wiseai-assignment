package com.wiseaiassignment.domain.reservation;

import com.wiseaiassignment.application.reservation.dto.CreateReservationCommand;
import com.wiseaiassignment.domain.common.exception.DomainException;
import com.wiseaiassignment.domain.common.exception.ExceptionType;
import com.wiseaiassignment.domain.meetingroom.model.MeetingRoom;
import com.wiseaiassignment.domain.meetingroom.repository.MeetingRoomRepository;
import com.wiseaiassignment.domain.reservation.model.Reservation;
import com.wiseaiassignment.domain.reservation.model.ReservationSlot;
import com.wiseaiassignment.domain.reservation.repository.ReservationRepository;
import com.wiseaiassignment.domain.reservation.repository.ReservationSlotRepository;
import com.wiseaiassignment.domain.user.model.User;
import com.wiseaiassignment.domain.user.repository.UserRepository;
import com.wiseaiassignment.helper.util.DatabaseCleanUp;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
class ReservationServiceIntegrationTest {

	@Autowired
	private ReservationService reservationService;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private MeetingRoomRepository meetingRoomRepository;

	@Autowired
	private ReservationRepository reservationRepository;

	@Autowired
	private ReservationSlotRepository reservationSlotRepository;

	@Autowired
	private DatabaseCleanUp databaseCleanUp;

	private User savedUser;
	private MeetingRoom savedMeetingRoom;
	private CreateReservationCommand command;

	@BeforeEach
	void setUp() {
		savedUser = userRepository.save(User.create("test@example.com", "테스트사용자"));
		savedMeetingRoom = meetingRoomRepository.save(MeetingRoom.create("회의실1", 10));

		command = new CreateReservationCommand(
				"주간회의",
				savedUser.getId(),
				savedMeetingRoom.getId(),
				LocalDateTime.of(2024, 1, 1, 10, 0),
				LocalDateTime.of(2024, 1, 1, 11, 0),
				List.of("test1@email.com", "test2@email.com")
		);
	}

	@AfterEach
	void tearDown() {
		databaseCleanUp.truncateAllTables();
	}

	@Nested
	@DisplayName("회의실 예약 테스트")
	class ReservationMeetingRoom {

		@Test
		void 정상적으로_예약이_생성된다() {
			// when
			Reservation result = reservationService.reserve(command.toEntity());

			// then
			assertThat(result).isNotNull();

			// 예약 데이터 확인
			List<Reservation> reservations = reservationRepository.findAll();
			assertThat(reservations).hasSize(1);

			Reservation reservation = reservations.get(0);
			assertThat(reservation.getUserId()).isEqualTo(command.userId());
			assertThat(reservation.getMeetingRoomId()).isEqualTo(command.meetingRoomId());
			assertThat(reservation.getStartTime()).isEqualTo(command.startTime());
			assertThat(reservation.getEndTime()).isEqualTo(command.endTime());
			assertThat(reservation.getAttendeeEmails())
					.hasSize(command.attendeeEmails().size())
					.containsAll(command.attendeeEmails());

			// 슬롯 데이터 확인
			List<ReservationSlot> slots = reservationSlotRepository.findAll();

			assertThat(slots).hasSize(2).allSatisfy(slot -> {
				assertThat(slot.getMeetingRoomId()).isEqualTo(savedMeetingRoom.getId());
				assertThat(slot.getReservationId()).isEqualTo(reservation.getId());
			});

			// 슬롯 시간 확인
			assertThat(slots.get(0).getSlotTime()).isEqualTo(command.startTime());
			assertThat(slots.get(1).getSlotTime()).isEqualTo(command.startTime().plusMinutes(ReservationSlot.SLOT_INTERVAL_MINUTES));
		}

		@Test
		void 같은_회의실_다른_시간대_예약은_성공한다() {
			// given
			// 10:00-11:00 예약
			reservationService.reserve(command.toEntity());
			// 11:00-12:00 예약 (겹치지 않는 시간)
			CreateReservationCommand secondCommand = new CreateReservationCommand(
					"월간회의",
					savedUser.getId(),
					savedMeetingRoom.getId(),
					LocalDateTime.of(2024, 1, 1, 11, 0),
					LocalDateTime.of(2024, 1, 1, 12, 0),
					List.of()
			);

			// when
			reservationService.reserve(secondCommand.toEntity());

			// 총 2개의 예약과 4개의 슬롯
			assertThat(reservationRepository.findAll()).hasSize(2);
			assertThat(reservationSlotRepository.findAll()).hasSize(4);
		}

		@Test
		void 다른_회의실_같은_시간_예약은_성공한다() {
			// given
			// 10:00-11:00 회의실1 예약
			reservationService.reserve(command.toEntity());

			// 다른 회의실 생성
			MeetingRoom anotherRoom = meetingRoomRepository.save(MeetingRoom.create("회의실2", 8));
			// 회의실1 예약과 같은 시간으로 생성
			CreateReservationCommand anotherRoomCommand = new CreateReservationCommand(
					"월간회의",
					savedUser.getId(),
					anotherRoom.getId(),
					LocalDateTime.of(2024, 1, 1, 10, 0),
					LocalDateTime.of(2024, 1, 1, 11, 0),
					List.of()
			);

			// when
			reservationService.reserve(anotherRoomCommand.toEntity());

			// then
			// 총 2개의 예약과 4개의 슬롯
			assertThat(reservationRepository.findAll()).hasSize(2);
			assertThat(reservationSlotRepository.findAll()).hasSize(4);
		}

		@Test
		void 예약시간이_겹치면_MEETING_ROOM_ALREADY_RESERVED_예외가_발생한다() {
			// given
			reservationService.reserve(command.toEntity());
			// 10:30-11:30 예약
			CreateReservationCommand secondCommand = new CreateReservationCommand(
					"월간회의",
					savedUser.getId(),
					savedMeetingRoom.getId(),
					LocalDateTime.of(2024, 1, 1, 10, 30),
					LocalDateTime.of(2024, 1, 1, 11, 30),
					List.of()
			);

			// when & then
			assertThatThrownBy(() -> reservationService.reserve(secondCommand.toEntity()))
					.isInstanceOf(DomainException.class)
					.extracting(ex -> ((DomainException) ex).getType())
					.isEqualTo(ExceptionType.MEETING_ROOM_ALREADY_RESERVED);
		}

	}

}
