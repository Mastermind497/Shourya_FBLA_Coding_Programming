package org.lpsstudents.shourya.backend.example.data.entity;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import org.lpsstudents.shourya.backend.example.data.OrderState;

public interface OrderSummary {
	Long getId();

	OrderState getState();

	Customer getCustomer();

	List<OrderItem> getItems();

	LocalDate getDueDate();

	LocalTime getDueTime();

	PickupLocation getPickupLocation();

	Integer getTotalPrice();
}
