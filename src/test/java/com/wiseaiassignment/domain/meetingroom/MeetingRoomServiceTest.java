package com.wiseaiassignment.domain.meetingroom;

import com.wiseaiassignment.domain.common.exception.DomainException;
import com.wiseaiassignment.domain.common.exception.ExceptionType;
import com.wiseaiassignment.domain.meetingroom.model.MeetingRoom;
import com.wiseaiassignment.domain.meetingroom.repository.MeetingRoomRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class MeetingRoomServiceTest {

	@Mock
	private MeetingRoomRepository meetingRoomRepository;

	@InjectMocks
	private MeetingRoomService meetingRoomService;

	@Test
	void ID로_회의실을_조회시_회의실을_반환한다() {
		// given
		MeetingRoom meetingRoom = MeetingRoom.create("회의실1", 10);
		given(meetingRoomRepository.findByIdAndActive(1L, true)).willReturn(Optional.of(meetingRoom));

		// when
		MeetingRoom result = meetingRoomService.findActiveMeetingRoomById(1L);

		// then
		assertThat(result).isEqualTo(meetingRoom);
	}

	@Test
	void ID로_존재하지_않는_회의실을_조회하면_NOT_FOUND_MEETING_ROOM_예외가_발생한다() {
		// given
		given(meetingRoomRepository.findByIdAndActive(1L, true)).willReturn(Optional.empty());

		// when & then
		assertThatThrownBy(() -> meetingRoomService.findActiveMeetingRoomById(1L))
				.isInstanceOf(DomainException.class)
				.extracting(ex -> ((DomainException) ex).getType())
				.isEqualTo(ExceptionType.NOT_FOUND_MEETING_ROOM);
	}
}
