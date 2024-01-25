package com.cloudservice.onboard.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cloudservice.onboard.entity.Service;

@Repository
public interface ServiceRepository extends JpaRepository<Service, Long> {
	// You can add custom query methods if needed
}
