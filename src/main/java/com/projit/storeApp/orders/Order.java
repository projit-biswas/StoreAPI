package com.projit.storeApp.orders;

import com.projit.storeApp.carts.Cart;
import com.projit.storeApp.payments.PaymentStatus;
import com.projit.storeApp.users.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "orders")
public class Order {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
	@JoinColumn(name = "customer_id")
	private User customer;

	@Column(name = "status")
	@Enumerated(EnumType.STRING)
	private PaymentStatus status;

	@Column(name = "created_at", insertable = false, updatable = false)
	private LocalDateTime createdAt;

	@Column(name = "total_price")
	private BigDecimal totalPrice;

	@OneToMany(mappedBy = "order", cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
	private Set<OrderItem> items = new LinkedHashSet<>();

	public static Order createOrder(Cart cart, User customer) {
		var order = new Order();
		order.setCustomer(customer);
		order.setStatus(PaymentStatus.PENDING);
		order.setTotalPrice(cart.getTotalPrice());

		cart.getItems().forEach(item -> {
			var orderItem = new OrderItem(order, item.getProduct(), item.getQuantity());
			order.items.add(orderItem);
		});
		return order;
	}

	public boolean isPlacedByCustomer(User customer) {
		return this.customer.equals(customer);
	}

}
