package com.wiseaiassignment.domain.notification;

import com.wiseaiassignment.domain.notification.dto.MeetingNotificationData;

/**
 * 회의 관련 알림 서비스 인터페이스
 */
public interface MeetingNotificationService {

	/**
	 * 회의 예약 생성 시 참석자들에게 알림 발송
	 * @param notificationData 회의 정보 및 참석자 데이터
	 */
	void sendMeetingInvitation(MeetingNotificationData notificationData);

	/**
	 * 회의 예약 취소 시 참석자들에게 알림 발송
	 * @param notificationData 회의 정보 및 참석자 데이터
	 */
	void sendMeetingCancellation(MeetingNotificationData notificationData);

	/**
	 * 회의 예약 변경 시 참석자들에게 알림 발송
	 * @param notificationData 회의 정보 및 참석자 데이터
	 */
	void sendMeetingUpdate(MeetingNotificationData notificationData);

	/**
	 * 회의 시작 전 리마인더 알림 발송
	 * @param notificationData 회의 정보 및 참석자 데이터
	 */
	void sendMeetingReminder(MeetingNotificationData notificationData);
}