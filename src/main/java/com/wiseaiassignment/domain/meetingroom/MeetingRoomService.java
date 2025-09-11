package com.wiseaiassignment.domain.meetingroom;

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
}
