package com.wiseaiassignment.domain.notification;

import com.wiseaiassignment.domain.notification.dto.ReservationNotificationData;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {

	@Async
	public void sendEmail(ReservationNotificationData notificationData) {
		// 이메일 전송 로직 생략
	}
}
