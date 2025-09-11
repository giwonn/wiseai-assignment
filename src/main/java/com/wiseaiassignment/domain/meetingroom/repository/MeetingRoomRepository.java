package com.wiseaiassignment.domain.meetingroom.repository;

import com.wiseaiassignment.domain.meetingroom.model.MeetingRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MeetingRoomRepository extends JpaRepository<MeetingRoom, Long> {
}
