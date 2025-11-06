package com.delivery_signal.eureka.client.hub.domain.vo;

import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Embeddable
@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PROTECTED, force = true)
public class Duration {

	private static final int MIN_MINUTES = 0;

	private int minutes;

	public Duration(int minutes) {
		validateDuration(minutes);
		this.minutes = minutes;
	}

	public static Duration of(int minutes) {
		return new Duration(minutes);
	}

	private void validateDuration(int minutes) {
		if (minutes < MIN_MINUTES) {
			throw new IllegalArgumentException("소요 시간은 음수일 수 없습니다.");
		}
	}
}
