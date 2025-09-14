package com.wiseaiassignment.domain.reservation;

import com.wiseaiassignment.domain.meetingroom.model.MeetingRoom;
import com.wiseaiassignment.domain.meetingroom.repository.MeetingRoomRepository;
import com.wiseaiassignment.domain.reservation.model.Reservation;
import com.wiseaiassignment.domain.reservation.model.ReservationSlot;
import com.wiseaiassignment.domain.reservation.model.ReservationStatus;
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
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class ReservationServiceConcurrencyTest {

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

	private MeetingRoom savedMeetingRoom;

	@BeforeEach
	void setUp() {
		savedMeetingRoom = meetingRoomRepository.save(MeetingRoom.create("회의실1", 10));
	}

	@AfterEach
	void tearDown() {
		databaseCleanUp.truncateAllTables();
	}

	@Nested
	@DisplayName("회의실 예약 동시성 테스트")
	class ReserveConcurrencyTest {

		@Test
		void 같은_회의실로_동일_시간에_예약하면_하나만_성공한다() throws InterruptedException {
			// given
			final int TRY_COUNT = 10;
			ExecutorService executor = Executors.newFixedThreadPool(3);
			CountDownLatch latch = new CountDownLatch(TRY_COUNT);
			AtomicInteger successCount = new AtomicInteger(0);
			AtomicInteger failureCount = new AtomicInteger(0);

			long[] userIds = new long[TRY_COUNT];
			for (int i = 0; i < TRY_COUNT; i++) {
				User user = userRepository.save(User.create("test"+i+"@example.com", "사용자"+i));
				userIds[i] = user.getId();
			}

			// when
			for (int i = 0; i < TRY_COUNT; i++) {
				final int index = i;
				executor.submit(() -> {
					try {
						Reservation reservation = Reservation.create(
								"주간회의",
								userIds[index],
								"test@email.com",
								savedMeetingRoom.getId(),
								savedMeetingRoom.getName(),
								LocalDateTime.of(2024, 1, 1, 10, 0),
								LocalDateTime.of(2024, 1, 1, 10, 30),
								List.of()
						);
						reservationService.reserve(reservation);
						successCount.incrementAndGet();
					} catch (Exception e) {
						failureCount.incrementAndGet();
					} finally {
						latch.countDown();
					}
				});
			}

			latch.await();

			// then
			assertThat(successCount.get()).isEqualTo(1);
			assertThat(failureCount.get()).isEqualTo(TRY_COUNT - 1);

			// 예약 상태가 RESERVED인 것을 필터링한 후 해당 List의 크기가 1인지 확인
			List<Reservation> reservedList = reservationRepository.findAll().stream()
					.filter(r -> r.getStatus() == ReservationStatus.RESERVED)
					.toList();
			assertThat(reservedList).hasSize(1);

			List<ReservationSlot> slots = reservationSlotRepository.findAll();
			assertThat(slots).hasSize(1);
		}
	}
}
