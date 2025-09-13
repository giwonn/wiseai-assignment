package com.wiseaiassignment.domain.common;

import com.wiseaiassignment.domain.common.lib.DateLib;
import org.junit.jupiter.api.Test;


import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class DateLibTest {

	@Test
	void DateTimeLib를_입력받아_분단위까지만_남긴다() {
		// given
		LocalDateTime target = LocalDateTime.of(2025,1,1,13,30,45,123456789);
		LocalDateTime expect = LocalDateTime.of(2025,1,1,13,30, 0, 0);

		// when
		LocalDateTime sut = DateLib.truncateToMinute(target);

		// when & then
		assertThat(sut).isEqualTo(expect);
	}
}
