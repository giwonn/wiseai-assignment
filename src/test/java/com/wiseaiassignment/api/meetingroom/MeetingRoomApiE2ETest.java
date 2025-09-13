package com.wiseaiassignment.api.meetingroom;

import com.wiseaiassignment.api.ApiCustomResponse;
import com.wiseaiassignment.api.meetingroom.dto.MeetingRoomResponse;
import com.wiseaiassignment.domain.meetingroom.model.MeetingRoom;
import com.wiseaiassignment.domain.meetingroom.repository.MeetingRoomRepository;
import com.wiseaiassignment.helper.util.DatabaseCleanUp;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class MeetingRoomApiE2ETest {

	private static final String ENDPOINT = "/meeting-rooms";

	@Autowired
	TestRestTemplate testRestTemplate;

	@Autowired
	MeetingRoomRepository meetingRoomRepository;

	@Autowired
	DatabaseCleanUp databaseCleanUp;

	@AfterEach
	void tearDown() {
		databaseCleanUp.truncateAllTables();
	}

	@DisplayName("GET /meeting-rooms - 회의실 목록 조회")
	@Nested
	class GetMeetingRooms {

		@Test
		void 생성되어_있는_회의실_목록을_조회한다() {
			// given
			MeetingRoom meetingRoom = meetingRoomRepository.save(MeetingRoom.create("회의실1", 5));
			HttpEntity request = new HttpEntity<>(null);

			// when
			ParameterizedTypeReference<ApiCustomResponse<MeetingRoomResponse[]>> responseType =
					new ParameterizedTypeReference<>() {};
			ResponseEntity<ApiCustomResponse<MeetingRoomResponse[]>> response =
					testRestTemplate.exchange(ENDPOINT, HttpMethod.GET, request, responseType);

			// then
			assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
			assertThat(response.getBody()).isNotNull();
			assertThat(response.getBody().data()).hasSize(1);
			assertThat(response.getBody().data()[0].id()).isEqualTo(meetingRoom.getId());
			assertThat(response.getBody().data()[0].name()).isEqualTo(meetingRoom.getName());
			assertThat(response.getBody().data()[0].capacity()).isEqualTo(meetingRoom.getCapacity());
		}

		@Test
		void 생성되어_있는_회의실이_없으면_빈_목록을_조회한다() {
			// given
			HttpEntity request = new HttpEntity<>(null);

			// when
			ParameterizedTypeReference<ApiCustomResponse<MeetingRoomResponse[]>> responseType =
					new ParameterizedTypeReference<>() {};
			ResponseEntity<ApiCustomResponse<MeetingRoomResponse[]>> response =
					testRestTemplate.exchange(ENDPOINT, HttpMethod.GET, request, responseType);

			// then
			assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
			assertThat(response.getBody()).isNotNull();
			assertThat(response.getBody().data()).isEmpty();
		}
	}

}
