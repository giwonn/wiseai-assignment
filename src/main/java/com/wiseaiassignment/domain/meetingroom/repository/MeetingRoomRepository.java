package com.wiseaiassignment.domain.meetingroom.repository;

import com.wiseaiassignment.domain.meetingroom.model.MeetingRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MeetingRoomRepository extends JpaRepository<MeetingRoom, Long> {
	Optional<MeetingRoom> findByIdAndActive(Long id, boolean active);
}
