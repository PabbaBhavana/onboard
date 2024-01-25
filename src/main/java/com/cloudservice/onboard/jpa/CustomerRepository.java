package com.cloudservice.onboard.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cloudservice.onboard.entity.Customer;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
}
