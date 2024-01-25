package com.cloudservice.onboard.entity;

import java.time.LocalDate;

import com.cloudservice.onboard.entity.Subscription.Plan;

public class ClientSubscription {

	private Long customerID;
	private Long serviceID;
	private LocalDate startDate;

	private Plan plan;

	// Constructor
	public ClientSubscription(Long customerID, Long serviceID, LocalDate startDate, Plan plan) {
		this.customerID = customerID;
		this.serviceID = serviceID;
		this.startDate = startDate;
		this.plan = plan;
	}

	// Getters
	public Long getCustomerID() {
		return customerID;
	}

	public Long getServiceID() {
		return serviceID;
	}

	public LocalDate getStartDate() {
		return startDate;
	}

	public Plan getPlan() {
		return this.plan;
	}

	// Setters
	public void setCustomerID(Long customerID) {
		this.customerID = customerID;
	}

	public void setServiceID(Long serviceID) {
		this.serviceID = serviceID;
	}

	public void setStartDate(LocalDate startDate) {
		this.startDate = startDate;
	}

	public void setPlan(Plan plan) {
		this.plan = plan;
	}
}