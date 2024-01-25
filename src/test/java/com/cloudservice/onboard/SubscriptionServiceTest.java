package com.cloudservice.onboard;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.cloudservice.onboard.entity.ClientSubscription;
import com.cloudservice.onboard.entity.Customer;
import com.cloudservice.onboard.entity.Subscription;
import com.cloudservice.onboard.entity.Subscription.Plan;
import com.cloudservice.onboard.entity.Subscription.Status;
import com.cloudservice.onboard.jpa.CustomerRepository;
import com.cloudservice.onboard.jpa.SubscriptionRepository;
import com.cloudservice.onboard.service.CloudService;
import com.cloudservice.onboard.service.CustomerService;
import com.cloudservice.onboard.service.SubscriptionService;

class SubscriptionServiceTest {
	
	
	private SubscriptionRepository subscriptionRepository;
	private CloudService cloudService;
	private CustomerService customerService;
	private SubscriptionService subscriptionService;
	
	@BeforeEach
	void init() {
		subscriptionRepository=mock(SubscriptionRepository.class);
		cloudService=mock(CloudService.class);
	    customerService=mock(CustomerService.class);
	    subscriptionService=new SubscriptionService();
	    subscriptionService.setCloudService(cloudService);
	    subscriptionService.setCustomerService(customerService);
	    subscriptionService.setSubscriptionRepository(subscriptionRepository);
	    
	    
	}
	/**
	 * cancel subscription should put end date as today and make status from active to cancel 
	 */

	@Test
    void testCancelSubscription_SubscriptionFound() {
        // Arrange
        Long subscriptionId = 1L;
        Subscription subscription = new Subscription();
        subscription.setId(subscriptionId);
        subscription.setCurrentStatus(Status.ACTIVE);
        subscription.setEndDate(null);

        Optional<Subscription> optionalSubscription = Optional.of(subscription);

        when(subscriptionRepository.findById(subscriptionId)).thenReturn(optionalSubscription);
        when(subscriptionRepository.save(any(Subscription.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        Optional<Subscription> result = subscriptionService.cancelSubscription(subscriptionId);

        // Assert
        assertTrue(result.isPresent());

        Subscription canceledSubscription = result.get();
        assertEquals(subscriptionId, canceledSubscription.getId());
        assertEquals(LocalDate.now(), canceledSubscription.getEndDate());
        assertEquals(Status.CANCEL, canceledSubscription.getCurrentStatus());
}
	
	@Test
    void testCancelSubscription_SubscriptionNotFound() {
        // Arrange
        Long subscriptionId = 1L;
        Optional<Subscription> optionalSubscription = Optional.empty();

        when(subscriptionRepository.findById(subscriptionId)).thenReturn(optionalSubscription);

        // Act
        Optional<Subscription> result = subscriptionService.cancelSubscription(subscriptionId);

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }
	
	@Test
    void testChangePlan_SubscriptionFoundAndActive() {
        // Arrange
        Long subscriptionId = 1L;
        String newPlan = "PREMIUM";

        Subscription subscription = new Subscription();
        subscription.setId(subscriptionId);
        subscription.setCurrentStatus(Status.ACTIVE);
        subscription.setPlan(Plan.BASIC);

        Optional<Subscription> optionalSubscription = Optional.of(subscription);

        when(subscriptionRepository.findById(subscriptionId)).thenReturn(optionalSubscription);
        when(subscriptionRepository.save(any(Subscription.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        Optional<Subscription> result = subscriptionService.changePlan(subscriptionId, newPlan);

        // Assert
        assertNotNull(result);
        assertTrue(result.isPresent());

        Subscription changedSubscription = result.get();
        assertEquals(subscriptionId, changedSubscription.getId());
        assertEquals(Plan.PREMIUM, changedSubscription.getPlan());
        assertEquals(Status.ACTIVE, changedSubscription.getCurrentStatus());
        
    }
	
	@Test
    void testChangePlan_SubscriptionFoundButNotActive() {
        // Arrange
        Long subscriptionId = 1L;
        String newPlan = "PREMIUM";

        Subscription subscription = new Subscription();
        subscription.setId(subscriptionId);
        subscription.setCurrentStatus(Status.CANCEL);
        subscription.setPlan(Plan.BASIC);

        Optional<Subscription> optionalSubscription = Optional.of(subscription);

        when(subscriptionRepository.findById(subscriptionId)).thenReturn(optionalSubscription);

        // Act
        Optional<Subscription> result = subscriptionService.changePlan(subscriptionId, newPlan);

        // Assert
        assertNotNull(result);
        assertTrue(result.isPresent());

        Subscription unchangedSubscription = result.get();
        assertEquals(subscriptionId, unchangedSubscription.getId());
        assertEquals(Plan.BASIC, unchangedSubscription.getPlan()); // Plan should remain unchanged
        assertEquals(Status.CANCEL, unchangedSubscription.getCurrentStatus());

    }
	@Test
    void testPauseOrUnpausePlan_SubscriptionFoundAndActive_Pause() {
        // Arrange
        Long subscriptionId = 1L;
        Boolean pauseFlag = true;

        Subscription subscription = new Subscription();
        subscription.setId(subscriptionId);
        subscription.setCurrentStatus(Status.ACTIVE);

        Optional<Subscription> optionalSubscription = Optional.of(subscription);

        when(subscriptionRepository.findById(subscriptionId)).thenReturn(optionalSubscription);
        when(subscriptionRepository.save(any(Subscription.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        Optional<Subscription> result = subscriptionService.pauseOrUnpausePlan(subscriptionId, pauseFlag);

        // Assert
        assertNotNull(result);
        assertTrue(result.isPresent());

        Subscription pausedSubscription = result.get();
        assertEquals(subscriptionId, pausedSubscription.getId());
        assertEquals(Status.PAUSE, pausedSubscription.getCurrentStatus());

    }
	@Test
    void testPauseOrUnpausePlan_SubscriptionFoundAndActive_Unpause() {
        // Arrange
        Long subscriptionId = 1L;
        Boolean pauseFlag = false;

        Subscription subscription = new Subscription();
        subscription.setId(subscriptionId);
        subscription.setCurrentStatus(Status.PAUSE);

        Optional<Subscription> optionalSubscription = Optional.of(subscription);

        when(subscriptionRepository.findById(subscriptionId)).thenReturn(optionalSubscription);
        when(subscriptionRepository.save(any(Subscription.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        Optional<Subscription> result = subscriptionService.pauseOrUnpausePlan(subscriptionId, pauseFlag);

        // Assert
        assertNotNull(result);
        assertTrue(result.isPresent());

        Subscription unpausedSubscription = result.get();
        assertEquals(subscriptionId, unpausedSubscription.getId());
        assertEquals(Status.ACTIVE, unpausedSubscription.getCurrentStatus());

       
    }

    @Test
    void testPauseOrUnpausePlan_SubscriptionFoundButNotActive() {
        // Arrange
        Long subscriptionId = 1L;
        Boolean pauseFlag = true;

        Subscription subscription = new Subscription();
        subscription.setId(subscriptionId);
        subscription.setCurrentStatus(Status.CANCEL);

        Optional<Subscription> optionalSubscription = Optional.of(subscription);

        when(subscriptionRepository.findById(subscriptionId)).thenReturn(optionalSubscription);

        // Act
        Optional<Subscription> result = subscriptionService.pauseOrUnpausePlan(subscriptionId, pauseFlag);

        // Assert
        assertNotNull(result);
        assertTrue(result.isPresent());

        Subscription unchangedSubscription = result.get();
        assertEquals(subscriptionId, unchangedSubscription.getId());
        assertEquals(Status.CANCEL, unchangedSubscription.getCurrentStatus());

       
    }

    @Test
    void testPauseOrUnpausePlan_SubscriptionNotFound() {
        // Arrange
        Long subscriptionId = 1L;
        Boolean pauseFlag = true;

        Optional<Subscription> optionalSubscription = Optional.empty();

        when(subscriptionRepository.findById(subscriptionId)).thenReturn(optionalSubscription);

        // Act
        Optional<Subscription> result = subscriptionService.pauseOrUnpausePlan(subscriptionId, pauseFlag);

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());

       
    }
    
    @Test
    void testCreateSubscription_ActiveSubscriptionExists() {
        // Arrange
        Long customerID = 1L;
        Long serviceID = 1L;

        ClientSubscription newSubscription = new ClientSubscription(serviceID, serviceID, null, Plan.BASIC);
//        newSubscription.setCustomerID(customerID);
//        newSubscription.setServiceID(serviceID);
//        newSubscription.setPlan(Plan.BASIC);

        Customer existingCustomer = new Customer();
        existingCustomer.setId(customerID);

        com.cloudservice.onboard.entity.Service existingService = new com.cloudservice.onboard.entity.Service();
        existingService.setServiceID(serviceID);

        Subscription existingSubscription = new Subscription();
        existingSubscription.setId(1L);
        existingSubscription.setCurrentStatus(Status.ACTIVE);
        existingSubscription.setCustomer(existingCustomer);
        existingSubscription.setService(existingService);

        List<Subscription> existingSubscriptions = List.of(existingSubscription);

        when(subscriptionRepository.findAll()).thenReturn(existingSubscriptions);
        when(customerService.getCustomerById(customerID)).thenReturn(existingCustomer);
        when(cloudService.getServiceById(serviceID)).thenReturn(existingService);

        // Act
        Subscription result = subscriptionService.createSubscription(newSubscription);

        // Assert
        assertNotNull(result);
        assertEquals(existingSubscription, result);

    }
    
    @Test
    void testCreateSubscription_NoActiveSubscriptionExists() {
        // Arrange
        Long customerID = 1L;
        Long serviceID = 1L;

        ClientSubscription newSubscription = new ClientSubscription(serviceID, serviceID, null, Plan.BASIC);
//        newSubscription.setCustomerID(customerID);
//        newSubscription.setServiceID(serviceID);
//        newSubscription.setPlan(Plan.BASIC);

        Customer existingCustomer = new Customer();
        existingCustomer.setId(customerID);

        com.cloudservice.onboard.entity.Service existingService = new com.cloudservice.onboard.entity.Service();
        existingService.setServiceID(serviceID);

        List<Subscription> existingSubscriptions = new ArrayList<>();

        when(subscriptionRepository.findAll()).thenReturn(existingSubscriptions);
        when(customerService.getCustomerById(customerID)).thenReturn(existingCustomer);
        when(cloudService.getServiceById(serviceID)).thenReturn(existingService);
        when(subscriptionRepository.save(any(Subscription.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        Subscription result = subscriptionService.createSubscription(newSubscription);

        // Assert
        assertNotNull(result);
        assertEquals(Status.ACTIVE, result.getCurrentStatus());
        assertEquals(LocalDate.now(), result.getStartDate());
        assertEquals(existingCustomer, result.getCustomer());
        assertEquals(existingService, result.getService());
        assertEquals(newSubscription.getPlan(), result.getPlan());

    }

	

}
