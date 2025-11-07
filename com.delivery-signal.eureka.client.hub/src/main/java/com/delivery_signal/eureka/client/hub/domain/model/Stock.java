package com.delivery_signal.eureka.client.hub.domain.model;

import java.util.UUID;

import org.hibernate.annotations.SQLRestriction;

import com.delivery_signal.eureka.client.hub.domain.vo.ProductId;

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
@Table(name = "p_stocks")
@SQLRestriction("deleted_at IS NULL")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Stock extends BaseEntity {

	@Id
	private UUID stockId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "hub_id", nullable = false)
	private Hub hub;

	@Embedded
	@AttributeOverride(name = "productId", column = @Column(name = "product_id", nullable = false))
	private ProductId productId;

	@Column(nullable = false)
	private int quantity;

	public static Stock create(Hub hub, ProductId productId, int quantity) {
		Stock stock = new Stock();
		stock.stockId = UUID.randomUUID();
		stock.hub = hub;
		stock.productId = productId;
		stock.quantity = quantity;
		return stock;
	}
}
