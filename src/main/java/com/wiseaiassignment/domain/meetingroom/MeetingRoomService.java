package com.wiseaiassignment.domain.meetingroom;

import com.wiseaiassignment.domain.common.exception.DomainException;
import com.wiseaiassignment.domain.common.exception.ExceptionType;
import com.wiseaiassignment.domain.meetingroom.model.MeetingRoom;
import com.wiseaiassignment.domain.meetingroom.repository.MeetingRoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MeetingRoomService {

	private final MeetingRoomRepository meetingRoomRepository;

	public List<MeetingRoom> findAll() {
		return meetingRoomRepository.findAll();
	}

	public MeetingRoom findActiveMeetingRoomById(Long meetingRoomId) {
		return meetingRoomRepository.findByIdAndActive(meetingRoomId, true)
				.orElseThrow(() -> new DomainException(ExceptionType.NOT_FOUND_MEETING_ROOM));
	}

	public MeetingRoom findById(Long meetingRoomId) {
		return meetingRoomRepository.findById(meetingRoomId)
				.orElseThrow(() -> new DomainException(ExceptionType.NOT_FOUND_MEETING_ROOM));
	}

	public MeetingRoom create(MeetingRoom meetingRoom) {
		return meetingRoomRepository.save(meetingRoom);
	}
}
