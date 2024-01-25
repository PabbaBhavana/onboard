package com.cloudservice.onboard.entity;

import java.time.LocalDate;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;

@Entity
public class Customer {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long customerID;

	@NotBlank(message = "Name should not be  blank")
	private String name;

	@NotBlank(message = "email should not be  blank")
	@Email
	private String email;

	@NotBlank(message = "address should not be blank")
	private String address;

	@Column(name = "onboarding_date")
	@FutureOrPresent
	@NotBlank(message = "onboardingDate should not be  blank")
	private LocalDate onboardingDate;

	@OneToMany(mappedBy = "customer")
	private List<Subscription> subscriptions;

	public Long getId() {
		return customerID;
	}

	public void setId(Long id) {
		this.customerID = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public LocalDate getOnboardingDate() {
		return onboardingDate;
	}

	public void setOnboardingDate(LocalDate onboardingDate) {
		this.onboardingDate = onboardingDate;
	}

}
