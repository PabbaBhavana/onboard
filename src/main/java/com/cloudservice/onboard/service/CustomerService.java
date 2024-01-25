package com.cloudservice.onboard.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import com.cloudservice.onboard.entity.Customer;
import com.cloudservice.onboard.entity.Subscription;
import com.cloudservice.onboard.entity.Subscription.Status;
import com.cloudservice.onboard.jpa.CustomerRepository;

@Service
public class CustomerService {

	@Autowired
	private CustomerRepository customerRepository;

	public CustomerRepository getCustomerRepository() {
		return customerRepository;
	}

	public void setCustomerRepository(CustomerRepository customerRepository) {
		this.customerRepository = customerRepository;
	}

	private SubscriptionService subscriptionService;

	public SubscriptionService getSubscriptionService() {
		return subscriptionService;
	}

	public void setSubscriptionService(SubscriptionService subscriptionService) {
		this.subscriptionService = subscriptionService;
	}

	@Autowired
	public CustomerService(@Lazy SubscriptionService subscriptionService) {
		this.subscriptionService = subscriptionService;
	}

	// Get all customers
	public List<Customer> getAllCustomers() {
		return customerRepository.findAll();
	}

	// Get a customer by ID
	public Customer getCustomerById(Long customerId) {
		Optional<Customer> optionalCustomer = customerRepository.findById(customerId);
		return optionalCustomer.orElse(null);
	}

	// Create a new customer
	public Customer createCustomer(Customer newCustomer) {
		return customerRepository.save(newCustomer);
	}

	// Update an existing customer
	public Customer updateCustomer(Long customerId, Customer updatedCustomer) {
		Optional<Customer> optionalCustomer = customerRepository.findById(customerId);

		if (optionalCustomer.isPresent()) {
			Customer existingCustomer = optionalCustomer.get();
			// Update existing customer properties with the ones from updatedCustomer
			existingCustomer.setName(updatedCustomer.getName());
			existingCustomer.setEmail(updatedCustomer.getEmail());
			// Update other properties as needed

			// Save the updated customer
			return customerRepository.save(existingCustomer);
		} else {
			return null; // Customer not found
		}
	}

	// Delete a customer by ID
	public boolean deleteCustomer(Long customerId) {
		if (customerRepository.existsById(customerId)) {
			customerRepository.deleteById(customerId);
			return true; // Deletion successful
		} else {
			return false; // Customer not found
		}
	}

	public List<Subscription> getSubscriptions(Long customerId) {
		Optional<Customer> optionalCustomer = customerRepository.findById(customerId);
		if (optionalCustomer.isPresent()) {
			Customer customer = optionalCustomer.get();
			List<Subscription> activeSubscriptionsForCustomer = subscriptionService.getAllSubscriptions().stream()
					.filter(subscription -> Status.ACTIVE.equals(subscription.getCurrentStatus()))
					.filter(subscription -> subscription.getCustomer().getId().equals(customer.getId()))
					.collect(Collectors.toList());
			return activeSubscriptionsForCustomer;
		}

		return new ArrayList<Subscription>();
	}
}
