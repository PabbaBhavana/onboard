package com.cloudservice.onboard.entity;

import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotBlank;

@Entity
public class Service {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long serviceID;
	@NotBlank(message = "Service Name should not be  blank")
	private String serviceName;
	private String description;
	@OneToMany(mappedBy = "service")
	private List<Subscription> subscriptions;

	public Long getServiceID() {
		return serviceID;
	}

	public void setServiceID(Long serviceID) {
		this.serviceID = serviceID;
	}

	public String getServiceName() {
		return serviceName;
	}

	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String desc) {
		this.description = desc;
	}

}
