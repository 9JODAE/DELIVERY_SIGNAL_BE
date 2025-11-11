package com.delivery_signal.eureka.client.delivery.infrastructure.repository;

import com.delivery_signal.eureka.client.delivery.application.dto.DeliverySearchCondition;
import com.delivery_signal.eureka.client.delivery.common.UserRole;
import com.delivery_signal.eureka.client.delivery.domain.entity.Delivery;
import com.delivery_signal.eureka.client.delivery.domain.entity.QDelivery;
import com.delivery_signal.eureka.client.delivery.domain.repository.DeliveryQueryRepository;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class DeliveryQueryRepositoryImpl implements DeliveryQueryRepository {

    private final JPAQueryFactory queryFactory;
    private final QDelivery qDelivery = QDelivery.delivery;

    public DeliveryQueryRepositoryImpl(JPAQueryFactory queryFactory) {
        this.queryFactory = queryFactory;
    }

    /**
     * 배송 목록 검색 (페이징/정렬 포함)
     */
    @Override
    public Page<Delivery> searchDeliveries(Long currUserId, DeliverySearchCondition condition,
        Pageable pageable, UserRole role) {
        // 동적 조건 생성
        BooleanBuilder builder = createSearchConditions(condition);

        List<Delivery> deliveries = fetchDeliveries(builder, condition, pageable);
        Long total = countDeliveries(builder);
        if (total == null) total = 0L;

        return new PageImpl<>(deliveries, pageable, total);
    }

    /**
     * Pageable의 Sort 정보를 Querydsl의 OrderSpecifier로 변환
     */
    private List<OrderSpecifier> getOrderSpecifiers(Sort sort) {
        List<OrderSpecifier> orderSpecifiers = new ArrayList<>();

        // 정렬 순서 정의
        PathBuilder<Delivery> entityPath = new PathBuilder<>(Delivery.class, "delivery");
        sort.forEach(order -> {
            Order direction = order.isAscending() ? Order.ASC : Order.DESC;
            String property = order.getProperty();

            // 허용된 정렬 필드만 처리 (보안 및 성능)
            if (property.equals("createdAt") || property.equals("deliveryId") || property.equals("currStatus")) {
                orderSpecifiers.add(new OrderSpecifier(direction, entityPath.get(property)));
            } else {
                log.warn("허용되지 않은 정렬 필드: {}", property);
            }
        });

        // 기본 정렬: 정렬 조건이 없거나 유효하지 않으면 deliveryId 기준 최신순으로 정렬
        if (orderSpecifiers.isEmpty()) {
            orderSpecifiers.add(new OrderSpecifier(Order.DESC, qDelivery.deliveryId));
        }

        return orderSpecifiers;
    }

    // 검색 조건에 따른 BooleanExpression 생성
    private BooleanBuilder createSearchConditions(DeliverySearchCondition condition) {
        BooleanBuilder builder = new BooleanBuilder();

        // 허브 ID별 검색: fromHubId 또는 toHubId가 일치하는 경우
        Optional.ofNullable(condition.hubId())
            .ifPresent(id -> builder.and(qDelivery.departureHubId.eq(id)
                .or(qDelivery.destinationHubId.eq(id))));

        // 업체 ID
        Optional.ofNullable(condition.companyId())
            .ifPresent(id -> builder.and(qDelivery.companyId.eq(id)));

        // 배송 담당자 ID
        Optional.ofNullable(condition.deliveryManagerId())
            .ifPresent(id -> builder.and(qDelivery.deliveryManagerId.eq(id)));

        // 배송 상태별 검색
        Optional.ofNullable(condition.status())
            .ifPresent(status -> builder.and(qDelivery.currStatus.in(condition.status())));

        return builder;
    }

    private List<Delivery> fetchDeliveries(BooleanBuilder builder, DeliverySearchCondition condition,
        Pageable pageable) {
        // 데이터 조회 쿼리
        return queryFactory.selectFrom(qDelivery)
            .where(builder)
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .orderBy(getOrderSpecifiers(pageable.getSort()).toArray(OrderSpecifier[]::new))
            .fetch();
    }

    private Long countDeliveries(BooleanBuilder builder) {
        // 전체 카운트 쿼리
        Long count = queryFactory.select(qDelivery.count())
            .from(qDelivery)
            .where(builder)
            .fetchOne();
        if (count == null) count = 0L;
        return count;
    }
}
