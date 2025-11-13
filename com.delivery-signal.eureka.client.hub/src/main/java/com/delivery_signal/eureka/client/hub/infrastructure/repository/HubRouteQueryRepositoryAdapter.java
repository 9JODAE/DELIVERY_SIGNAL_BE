package com.delivery_signal.eureka.client.hub.infrastructure.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import com.delivery_signal.eureka.client.hub.domain.entity.HubRoute;
import com.delivery_signal.eureka.client.hub.domain.entity.QHub;
import com.delivery_signal.eureka.client.hub.domain.entity.QHubRoute;
import com.delivery_signal.eureka.client.hub.domain.repository.HubRouteSearchRepository;
import com.delivery_signal.eureka.client.hub.domain.condition.HubRouteSearchCondition;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class HubRouteQueryRepositoryAdapter extends BaseQueryRepository implements HubRouteSearchRepository {

	private static final QHubRoute ROUTE = QHubRoute.hubRoute;
	private static final QHub DEPARTURE_HUB = new QHub("departureHub");
	private static final QHub ARRIVAL_HUB = new QHub("arrivalHub");
	private final JPAQueryFactory queryFactory;

	@Override
	public Page<HubRoute> searchHubRoutes(HubRouteSearchCondition condition) {
		Pageable pageable = createPageable(condition.page(), condition.size());
		BooleanBuilder conditions = buildSearchConditions(condition);
		OrderSpecifier<?> orderSpecifier = buildOrderSpecifier(
			condition.sortBy(),
			condition.direction(),
			ROUTE.createdAt,
			ROUTE.updatedAt
		);

		List<HubRoute> routes = fetchHubRoutes(conditions, orderSpecifier, pageable);
		long totalCount = countRoutes(conditions);

		return new PageImpl<>(routes, pageable, totalCount);
	}

	private List<HubRoute> fetchHubRoutes(BooleanBuilder conditions, OrderSpecifier<?> order, Pageable pageable) {
		return queryFactory
			.selectFrom(ROUTE)
			.join(ROUTE.departureHub, DEPARTURE_HUB).fetchJoin()
			.join(ROUTE.arrivalHub, ARRIVAL_HUB).fetchJoin()
			.where(conditions)
			.orderBy(order)
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize())
			.fetch();
	}

	private long countRoutes(BooleanBuilder conditions) {
		Long count = queryFactory
			.select(ROUTE.count())
			.from(ROUTE)
			.where(conditions)
			.fetchOne();

		return count != null ? count : 0L;
	}

	private BooleanBuilder buildSearchConditions(HubRouteSearchCondition condition) {
		BooleanBuilder builder = new BooleanBuilder();

		addDepartureHubNameCondition(builder, condition.departureHubName());
		addArrivalHubCondition(builder, condition.arrivalHubName());

		return builder;
	}

	private void addDepartureHubNameCondition(BooleanBuilder builder, String departureHubName) {
		if (StringUtils.hasText(departureHubName)) {
			builder.and(ROUTE.departureHub.name.contains(departureHubName));
		}
	}

	private void addArrivalHubCondition(BooleanBuilder builder, String address) {
		if (StringUtils.hasText(address)) {
			builder.and(ROUTE.arrivalHub.name.contains(address));
		}
	}
}
