package com.delivery_signal.eureka.client.hub.infrastructure.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import com.delivery_signal.eureka.client.hub.domain.condition.StockSearchCondition;
import com.delivery_signal.eureka.client.hub.domain.entity.QHub;
import com.delivery_signal.eureka.client.hub.domain.entity.QStock;
import com.delivery_signal.eureka.client.hub.domain.entity.Stock;
import com.delivery_signal.eureka.client.hub.domain.repository.StockSearchRepository;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class StockQueryRepositoryAdapter extends BaseQueryRepository implements StockSearchRepository {

	private static final QStock STOCK = QStock.stock;
	private static final QHub HUB = QHub.hub;
	private final JPAQueryFactory queryFactory;

	@Override
	public Page<Stock> searchStocks(StockSearchCondition condition) {
		Pageable pageable = createPageable(condition.page(), condition.size());
		BooleanBuilder conditions = buildSearchConditions(condition);
		OrderSpecifier<?> orderSpecifier = buildOrderSpecifier(
			condition.sortBy(),
			condition.direction(),
			STOCK.createdAt,
			STOCK.updatedAt
		);

		List<Stock> stocks = fetchStocks(conditions, orderSpecifier, pageable);
		long totalCount = countStocks(conditions);

		return new PageImpl<>(stocks, pageable, totalCount);
	}

	private List<Stock> fetchStocks(BooleanBuilder conditions, OrderSpecifier<?> order, Pageable pageable) {
		return queryFactory
			.selectFrom(STOCK)
			.join(STOCK.hub, HUB).fetchJoin()
			.where(conditions)
			.orderBy(order)
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize())
			.fetch();
	}

	private long countStocks(BooleanBuilder conditions) {
		Long count = queryFactory
			.select(STOCK.count())
			.from(STOCK)
			.where(conditions)
			.fetchOne();

		return count != null ? count : 0L;
	}

	private BooleanBuilder buildSearchConditions(StockSearchCondition condition) {
		BooleanBuilder builder = new BooleanBuilder();

		addProductIdsCondition(builder, condition.productIds());
		addHubIdCondition(builder, condition.hubId());

		return builder;
	}

	private void addProductIdsCondition(BooleanBuilder builder, List<UUID> productIds) {
		if (productIds != null && !productIds.isEmpty()) {
			builder.and(STOCK.productId.value.in(productIds));
		}
	}

	private void addHubIdCondition(BooleanBuilder builder, UUID hubId) {
		if (hubId != null) {
			builder.and(HUB.hubId.eq(hubId));
		}
	}


}
