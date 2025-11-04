package com.delivery_signal.eureka.client.hub.application.dto;

import java.util.UUID;

import com.delivery_signal.eureka.client.hub.domain.model.Hub;

import lombok.Getter;

@Getter
public class HubDetailResponse {

	private UUID hubId;
	private String name;
	private String address;
	private double latitude;
	private double longitude;

	public static HubDetailResponse from(Hub hub) {
		HubDetailResponse response = new HubDetailResponse();
		response.hubId = hub.getHubId();
		response.name = hub.getName();
		response.address = hub.getAddress();
		response.latitude = hub.getLatitude();
		response.longitude = hub.getLongitude();
		return response;
	}
}
