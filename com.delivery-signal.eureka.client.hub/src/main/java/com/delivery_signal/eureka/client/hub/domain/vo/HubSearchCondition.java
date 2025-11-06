package com.delivery_signal.eureka.client.hub.domain.vo;

import org.springframework.data.domain.Sort;

public record HubSearchCondition(
	String name,
	String address,
	Integer page,
	Integer size,
	String sortBy,
	String direction
) {}
