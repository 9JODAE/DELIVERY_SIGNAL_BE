package com.delivery_signal.eureka.client.hub.domain.model;

import java.util.UUID;

import org.hibernate.annotations.SQLRestriction;
import org.hibernate.annotations.UuidGenerator;

import com.delivery_signal.eureka.client.hub.domain.vo.Distance;
import com.delivery_signal.eureka.client.hub.domain.vo.Duration;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "p_hub_routes")
@SQLRestriction("deleted_at IS NULL")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class HubRoute extends BaseEntity {

	@Id
	@UuidGenerator
	private UUID hubRouteId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "departure_hub_id", nullable = false)
	private Hub departureHub;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "arrival_hub_id", nullable = false)
	private Hub arrivalHub;

	@Embedded
	@AttributeOverride(name = "kilometers", column = @Column(name = "distance", nullable = false))
	private Distance distance;

	@Embedded
	@AttributeOverride(name = "minutes", column = @Column(name = "transit_time", nullable = false))
	private Duration transitTime;

	public static HubRoute create(Hub departureHub, Hub arrivalHub, Distance distance, Duration transitTime) {
		HubRoute hubRoute = new HubRoute();
		hubRoute.departureHub = departureHub;
		hubRoute.arrivalHub = arrivalHub;
		hubRoute.distance = distance;
		hubRoute.transitTime = transitTime;
		return hubRoute;
	}

}
