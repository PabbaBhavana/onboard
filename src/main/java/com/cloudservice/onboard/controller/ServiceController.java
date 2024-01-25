package com.cloudservice.onboard.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cloudservice.onboard.entity.Service;
import com.cloudservice.onboard.service.CloudService;

@RestController
@RequestMapping("/api/service")
public class ServiceController {

	@Autowired
	private CloudService cloudService;

	// Get all services
	@GetMapping
	public ResponseEntity<List<Service>> getAllServices() {
		List<Service> services = cloudService.getAllServices();
		return new ResponseEntity<>(services, HttpStatus.OK);
	}

	// Get a service by ID
	@GetMapping("/{serviceId}")
	public ResponseEntity<Service> getServiceById(@PathVariable Long serviceId) {
		Service service = cloudService.getServiceById(serviceId);
		if (service != null) {
			return new ResponseEntity<>(service, HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

	// Create a new service
	@PostMapping("/addService")
	public ResponseEntity<Service> createService(@RequestBody Service newService) {
		Service createdService = (Service) cloudService.createService(newService);
		return new ResponseEntity<>(createdService, HttpStatus.CREATED);
	}

	// Update an existing service
	@PutMapping("/{serviceId}")
	public ResponseEntity<Service> updateService(@PathVariable Long serviceId, @RequestBody Service updatedService) {
		Service updated = (Service) cloudService.updateService(serviceId, updatedService);
		if (updated != null) {
			return new ResponseEntity<>(updated, HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

	// Delete a service by ID
	@DeleteMapping("/{serviceId}")
	public ResponseEntity<Void> deleteService(@PathVariable Long serviceId) {
		boolean deleted = cloudService.deleteService(serviceId);
		if (deleted) {
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}
}
