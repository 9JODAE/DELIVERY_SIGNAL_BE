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
public class Distance {

	private static final double MIN_DISTANCE_KM = 0.0;
	private static final double MAX_DISTANCE_KM = 10000.0;

	private double kilometers;

	public Distance(double kilometers) {
		validateDistance(kilometers);
		this.kilometers = kilometers;
	}

	public static Distance of(double kilometers) {
		return new Distance(kilometers);
	}

	private void validateDistance(double kilometers) {
		if (kilometers < MIN_DISTANCE_KM) {
			throw new IllegalArgumentException("거리는 음수일 수 없습니다.");
		}
		if (kilometers > MAX_DISTANCE_KM) {
			throw new IllegalArgumentException("거리는 " + MAX_DISTANCE_KM + "km를 초과할 수 없습니다.");
		}
	}

}
