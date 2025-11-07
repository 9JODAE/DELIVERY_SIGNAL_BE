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
public class Coordinate {

	private static final double MIN_LATITUDE = 33.0;
	private static final double MAX_LATITUDE = 39.0;
	private static final double MIN_LONGITUDE = 124.0;
	private static final double MAX_LONGITUDE = 132.0;

	private double latitude;
	private double longitude;

	private Coordinate(double latitude, double longitude) {
		validateLatitude(latitude);
		validateLongitude(longitude);
		this.latitude = latitude;
		this.longitude = longitude;
	}

	public static Coordinate of(double latitude, double longitude) {
		return new Coordinate(latitude, longitude);
	}

	private void validateLatitude(double latitude) {
		if (latitude < MIN_LATITUDE  || latitude > MAX_LATITUDE) {
			throw new IllegalArgumentException("위도는 " + MIN_LATITUDE + "에서 " + MAX_LATITUDE + " 사이여야 합니다.");
		}
	}

	private void validateLongitude(double longitude) {
		if (longitude < MIN_LONGITUDE || longitude > MAX_LONGITUDE) {
			throw new IllegalArgumentException("경도는 " + MIN_LONGITUDE + "에서 " + MAX_LONGITUDE + " 사이여야 합니다.");
		}
	}
}
