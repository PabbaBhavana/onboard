package com.cloudservice.onboard.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cloudservice.onboard.entity.ClientSubscription;
import com.cloudservice.onboard.entity.Customer;
import com.cloudservice.onboard.entity.Subscription;
import com.cloudservice.onboard.entity.Subscription.Plan;
import com.cloudservice.onboard.entity.Subscription.Status;
import com.cloudservice.onboard.jpa.SubscriptionRepository;

@Service
public class SubscriptionService {
	@Autowired
	private SubscriptionRepository subscriptionRepository;
	public SubscriptionRepository getSubscriptionRepository() {
		return subscriptionRepository;
	}

	public void setSubscriptionRepository(SubscriptionRepository subscriptionRepository) {
		this.subscriptionRepository = subscriptionRepository;
	}

	public CloudService getCloudService() {
		return cloudService;
	}

	public void setCloudService(CloudService cloudService) {
		this.cloudService = cloudService;
	}

	public CustomerService getCustomerService() {
		return customerService;
	}

	public void setCustomerService(CustomerService customerService) {
		this.customerService = customerService;
	}

	@Autowired
	private CloudService cloudService;

	@Autowired
	private CustomerService customerService;

	public List<Subscription> getAllSubscriptions() {
		return subscriptionRepository.findAll();
	}

	// Get a subscription by ID
	public Optional<Subscription> getSubscriptionById(Long subscriptionId) {
		return subscriptionRepository.findById(subscriptionId);
	}

	// Cancel subscription
	public Optional<Subscription> cancelSubscription(Long subscriptionId) {
		Optional<Subscription> optinalSubs = subscriptionRepository.findById(subscriptionId);
		if (optinalSubs.isPresent()) {
			Subscription subs = optinalSubs.get();
			// set end date as today
			subs.setEndDate(LocalDate.now());
			// set status as cancel
			subs.setCurrentStatus(Status.CANCEL);
			subscriptionRepository.save(subs);
		}
		return optinalSubs;
	}

	// change plan
	public Optional<Subscription> changePlan(Long subscriptionId, String plan) {
		Optional<Subscription> optinalSubs = subscriptionRepository.findById(subscriptionId);
		if (optinalSubs.isPresent()) {
			// change the plan only if its active
			Subscription subs = optinalSubs.get();
			if (subs.getCurrentStatus() == Status.ACTIVE) {
				subs.setPlan(Plan.valueOf(plan));
				subscriptionRepository.save(subs);

			}

		}
		return optinalSubs;
	}

	// pause plan
	public Optional<Subscription> pauseOrUnpausePlan(Long subscriptionId, Boolean flag) {
		Optional<Subscription> optinalSubs = subscriptionRepository.findById(subscriptionId);
		if (optinalSubs.isPresent()) {
			// pause the plan only if its active
			Subscription subs = optinalSubs.get();
			if (subs.getCurrentStatus() == Status.ACTIVE && flag) {
				subs.setCurrentStatus(Status.PAUSE);
			}
			if (!flag) {
				subs.setCurrentStatus(Status.ACTIVE);
			}
			subscriptionRepository.save(subs);

		}
		return optinalSubs;

	}

	public Subscription createSubscription(ClientSubscription newSubscription) {
		// check if an active subscription for the same serviceID already exists
		// if exists return the same
		// else create a new subscription and set status to active
		Long customerID = newSubscription.getCustomerID();
		Long serviceID = newSubscription.getServiceID();

		Optional<Subscription> result = subscriptionRepository.findAll().stream()
				.filter(subscription -> Status.ACTIVE.equals(subscription.getCurrentStatus()))
				.filter(subscription -> subscription.getCustomer().getId().equals(customerID))
				.filter(subscription -> subscription.getService().getServiceID().equals(serviceID)).findFirst();

		if (result.isPresent()) {
			Subscription foundSubscription = result.get();
			return foundSubscription;

		} else {
//			create new subscription
			Subscription subs = new Subscription();
			// set status as active
			subs.setCurrentStatus(Status.ACTIVE);
			// set start date
			subs.setStartDate(LocalDate.now());
			Customer customer = customerService.getCustomerById(customerID);
			// get customer object and service object and save

			com.cloudservice.onboard.entity.Service service = (com.cloudservice.onboard.entity.Service) cloudService
					.getServiceById(serviceID);
			subs.setCustomer(customer);
			subs.setService(service);
			// set plan
			subs.setPlan(newSubscription.getPlan());

			return subscriptionRepository.save(subs);
		}

	}

	public Optional<Subscription> updateSubscription(Long subscriptionId, Subscription updatedSubscription) {
		// Check if the subscription exists before updating
		if (subscriptionRepository.existsById(subscriptionId)) {
			updatedSubscription.setId(subscriptionId);
			return Optional.of(subscriptionRepository.save(updatedSubscription));
		} else {
			return Optional.empty(); // Subscription not found
		}

	}

}