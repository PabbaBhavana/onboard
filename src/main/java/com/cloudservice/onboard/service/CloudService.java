package com.cloudservice.onboard.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cloudservice.onboard.jpa.ServiceRepository;

@Service
public class CloudService {

	@Autowired
	private ServiceRepository serviceRepository;

	// Get all services
	public List<com.cloudservice.onboard.entity.Service> getAllServices() {
		return serviceRepository.findAll();
	}

	// Get a service by ID
	public com.cloudservice.onboard.entity.Service getServiceById(Long serviceId) {
		Optional<com.cloudservice.onboard.entity.Service> optionalService = serviceRepository.findById(serviceId);
		return optionalService.orElse(null);
	}

	// Create a new service
	public com.cloudservice.onboard.entity.Service createService(com.cloudservice.onboard.entity.Service newService) {
		return (com.cloudservice.onboard.entity.Service) serviceRepository.save(newService);
	}

	// Update an existing service
	public Service updateService(Long serviceId, com.cloudservice.onboard.entity.Service updatedService) {
		Optional<com.cloudservice.onboard.entity.Service> optionalService = serviceRepository.findById(serviceId);

		if (optionalService.isPresent()) {
			com.cloudservice.onboard.entity.Service existingService = optionalService.get();
			// Update existing service properties with the ones from updatedService
			existingService.setServiceName(((com.cloudservice.onboard.entity.Service) updatedService).getServiceName());
			existingService.setDescription(((com.cloudservice.onboard.entity.Service) updatedService).getDescription());
			// Update other properties as needed

			// Save the updated service
			return (Service) serviceRepository.save(existingService);
		} else {
			return null; // Service not found
		}
	}

	// Delete a service by ID
	public boolean deleteService(Long serviceId) {
		if (serviceRepository.existsById(serviceId)) {
			serviceRepository.deleteById(serviceId);
			return true; // Deletion successful
		} else {
			return false; // Service not found
		}
	}
}
