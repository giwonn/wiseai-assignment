package com.wiseaiassignment.domain.common.lib;

import java.time.LocalDateTime;

public class DateLib {
	private DateLib() {}

	public static LocalDateTime truncateToMinute(LocalDateTime time) {
		return time.withSecond(0).withNano(0);
	}

}
