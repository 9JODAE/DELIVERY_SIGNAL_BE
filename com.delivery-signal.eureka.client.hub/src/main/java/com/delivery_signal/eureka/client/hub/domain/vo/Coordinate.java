package com.delivery_signal.eureka.client.hub.domain.vo;

import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Embeddable
@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Coordinate {

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
		if (latitude < 33.0  || latitude > 39.0) {
			throw new IllegalArgumentException("위도는 33.0에서 39.0 사이여야 합니다.");
		}
	}

	private void validateLongitude(double longitude) {
		if (longitude < 124.0 || longitude > 132.0) {
			throw new IllegalArgumentException("경도는 124.0에서 132.0 사이여야 합니다.");
		}
	}
}
