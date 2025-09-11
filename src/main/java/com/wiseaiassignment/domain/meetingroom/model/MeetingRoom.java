package com.wiseaiassignment.domain.meetingroom.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.Instant;

@Entity
@Getter
public class MeetingRoom {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String name;

	private int capacity;

	@CreatedDate
	private Instant createdAt;

	@LastModifiedDate
	private Instant updatedAt;

	public static MeetingRoom create(String name, int capacity) {
		MeetingRoom meetingRoom = new MeetingRoom();
		meetingRoom.name = name;
		meetingRoom.capacity = capacity;
		return meetingRoom;
	}

}
