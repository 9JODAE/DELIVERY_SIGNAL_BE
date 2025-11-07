package com.delivery_signal.eureka.client.hub.infrastructure.repository;

import com.delivery_signal.eureka.client.hub.domain.model.Hub;
import com.delivery_signal.eureka.client.hub.domain.model.QHub;
import com.delivery_signal.eureka.client.hub.domain.repository.HubQueryRepository;
import com.delivery_signal.eureka.client.hub.domain.mapper.HubSearchCondition;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class HubQueryRepositoryImpl extends BaseQueryRepository implements HubQueryRepository {

	private static final QHub HUB = QHub.hub;
	private final JPAQueryFactory queryFactory;

	@Override
	public Page<Hub> searchHubs(HubSearchCondition condition) {
		Pageable pageable = createPageable(condition.page(), condition.size());
		BooleanBuilder conditions = buildSearchConditions(condition);
		OrderSpecifier<?> orderSpecifier = buildOrderSpecifier(
			condition.sortBy(),
			condition.direction(),
			HUB.createdAt,
			HUB.updatedAt
		);

		List<Hub> hubs = fetchHubs(conditions, orderSpecifier, pageable);
		long totalCount = countHubs(conditions);

		return new PageImpl<>(hubs, pageable, totalCount);
	}

	private List<Hub> fetchHubs(BooleanBuilder conditions, OrderSpecifier<?> order, Pageable pageable) {
		return queryFactory
			.selectFrom(HUB)
			.where(conditions)
			.orderBy(order)
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize())
			.fetch();
	}

	private long countHubs(BooleanBuilder conditions) {
		Long count = queryFactory
			.select(HUB.count())
			.from(HUB)
			.where(conditions)
			.fetchOne();

		return count != null ? count : 0L;
	}

	private BooleanBuilder buildSearchConditions(HubSearchCondition condition) {
		BooleanBuilder builder = new BooleanBuilder();

		addNameCondition(builder, condition.name());
		addAddressCondition(builder, condition.address());

		return builder;
	}

	private void addNameCondition(BooleanBuilder builder, String name) {
		if (StringUtils.hasText(name)) {
			builder.and(HUB.name.contains(name));
		}
	}

	private void addAddressCondition(BooleanBuilder builder, String address) {
		if (StringUtils.hasText(address)) {
			builder.and(HUB.address.value.contains(address));
		}
	}
}