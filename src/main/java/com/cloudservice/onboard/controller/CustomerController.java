package com.cloudservice.onboard.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cloudservice.onboard.entity.Customer;
import com.cloudservice.onboard.entity.Subscription;
import com.cloudservice.onboard.service.CustomerService;

@RestController
@RequestMapping("/api/customers")
public class CustomerController {

	@Autowired
	private CustomerService customerService;

	@GetMapping
	public ResponseEntity<List<Customer>> getAllCustomers() {
		List<Customer> customers = customerService.getAllCustomers();
		return ResponseEntity.ok(customers);
	}

	@GetMapping("/{customerId}")
	public ResponseEntity<Customer> getCustomerById(@PathVariable Long customerId) {
		Customer customer = customerService.getCustomerById(customerId);
		if (customer != null) {
			return ResponseEntity.ok(customer);
		} else {
			return ResponseEntity.notFound().build();
		}

	}

	// return active subscriptions
	@GetMapping("/getSubscriptions/{customerId}")
	public ResponseEntity<List<Subscription>> getSubscriptions(@PathVariable Long customerId) {
		return ResponseEntity.ok(customerService.getSubscriptions(customerId));

	}

	@PostMapping("/addCustomer")
	public ResponseEntity<Customer> createCustomer(@RequestBody Customer newCustomer) {
		Customer createdCustomer = customerService.createCustomer(newCustomer);
		return ResponseEntity.ok(createdCustomer);
	}

	@PutMapping("/{customerId}")
	public ResponseEntity<Customer> updateCustomer(@PathVariable Long customerId,
			@RequestBody Customer updatedCustomer) {
		Customer customer = customerService.updateCustomer(customerId, updatedCustomer);
		if (customer != null) {
			return ResponseEntity.ok(customer);
		} else {
			return ResponseEntity.notFound().build();
		}
	}

	@DeleteMapping("/{customerId}")
	public ResponseEntity<Void> deleteCustomer(@PathVariable Long customerId) {
		if (customerService.deleteCustomer(customerId)) {
			return ResponseEntity.noContent().build();
		} else {
			return ResponseEntity.notFound().build();
		}
	}
}
