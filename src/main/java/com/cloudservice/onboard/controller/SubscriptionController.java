package com.cloudservice.onboard.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cloudservice.onboard.entity.ClientSubscription;
import com.cloudservice.onboard.entity.Subscription;
import com.cloudservice.onboard.service.SubscriptionService;

@RestController
@RequestMapping("/api/subscription")
public class SubscriptionController {

	@Autowired
	private SubscriptionService subscriptionService;

	@GetMapping
	public ResponseEntity<List<Subscription>> getAllSubscriptions() {
		List<Subscription> subscriptions = subscriptionService.getAllSubscriptions();
		return ResponseEntity.ok(subscriptions);
	}

	@GetMapping("/{id}")
	public ResponseEntity<Subscription> getSubscriptionById(@PathVariable Long id) {
		Optional<Subscription> subscription = subscriptionService.getSubscriptionById(id);
		return subscription.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
	}

	@PostMapping("/add")
	public ResponseEntity<Subscription> createSubscription(@RequestBody ClientSubscription newSubscription) {
		Subscription createdSubscription = subscriptionService.createSubscription(newSubscription);
		return ResponseEntity.ok(createdSubscription);
	}

	@PutMapping("/cancel/{id}")
//	canceling the subscription means to add end date as today 
	// set status as cancel
	public Optional<Subscription> cancelSubscription(@PathVariable Long id) {
		return this.subscriptionService.cancelSubscription(id);
	}

	@PutMapping("/changeplan")
	public Optional<Subscription> changePlan(@RequestParam String plan, @RequestParam Long id) {
		return this.subscriptionService.changePlan(id, plan);
	}

	@PutMapping("/pause/{id}")
	public Optional<Subscription> pauseSubscription(@PathVariable Long id) {
		return this.subscriptionService.pauseOrUnpausePlan(id, true);
	}

	@PutMapping("/resume/{id}")
	public Optional<Subscription> resumeSubscription(@PathVariable Long id) {
		return this.subscriptionService.pauseOrUnpausePlan(id, false);
	}

	@PutMapping("/{id}")
	public ResponseEntity<Subscription> updateSubscription(@PathVariable Long id,
			@RequestBody Subscription updatedSubscription) {
		Optional<Subscription> result = subscriptionService.updateSubscription(id, updatedSubscription);
		return result.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
	}

}
