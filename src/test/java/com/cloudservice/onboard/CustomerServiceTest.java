package com.cloudservice.onboard;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.junit.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.cloudservice.onboard.entity.Customer;
import com.cloudservice.onboard.entity.Subscription;
import com.cloudservice.onboard.entity.Subscription.Status;
import com.cloudservice.onboard.jpa.CustomerRepository;
import com.cloudservice.onboard.service.CustomerService;
import com.cloudservice.onboard.service.SubscriptionService;


class CustomerServiceTest {
	
	
	private CustomerService customerService;
	private CustomerRepository customerRepository;
	private SubscriptionService subscriptionService;
	
	@BeforeEach
	void init() {
	    customerRepository=mock(CustomerRepository.class);
	    customerService=new CustomerService(null);
	    customerService.setCustomerRepository(customerRepository);
	    subscriptionService=mock(SubscriptionService.class);
	    customerService.setSubscriptionService(subscriptionService);
	    
	}

	

	@Test
	void getCustomerByIdFound() {
		Long customerID=123L;
		Customer expectedCustomer=new Customer();
		expectedCustomer.setId(customerID);
		when(customerRepository.findById(customerID)).thenReturn(Optional.of(expectedCustomer));
		Customer actual=customerService.getCustomerById(customerID);
		assertEquals(expectedCustomer,actual);
		
		
	}
	
	@Test
	void getCustomerByIdNotFound() {
		try {
		Long customerID=123L;
		Customer expectedCustomer=new Customer();
		expectedCustomer.setId(customerID);
		when(customerRepository.findById(customerID)).thenReturn(Optional.empty());
		Customer actual=customerService.getCustomerById(customerID);
		assertNull(actual);
		}
		catch(Exception e) {
			System.out.println(e.getMessage());
		}
	}
	
	@Test
    void testUpdateCustomer_CustomerFound() {
        // Arrange
        Long customerId = 1L;
        Customer existingCustomer = new Customer();
        existingCustomer.setId(customerId);
        existingCustomer.setName("Old Name");
        existingCustomer.setEmail("old@example.com");

        Customer updatedCustomer = new Customer();
        updatedCustomer.setName("New Name");
        updatedCustomer.setEmail("new@example.com");

        Optional<Customer> optionalCustomer = Optional.of(existingCustomer);

        when(customerRepository.findById(customerId)).thenReturn(optionalCustomer);
        when(customerRepository.save(updatedCustomer)).thenAnswer(invocation -> invocation.getArgument(0));

        customerService.updateCustomer(customerId, updatedCustomer);
        
        assertEquals(updatedCustomer.getName(), existingCustomer.getName());
        assertEquals(updatedCustomer.getEmail(), existingCustomer.getEmail());
       
    }
	
	@Test
    void testUpdateCustomer_CustomerNotFound() {
		try {
        // Arrange
        Long customerId = 12L;
      
        Customer updatedCustomer = new Customer();
        updatedCustomer.setName("New Name");
        updatedCustomer.setEmail("new@example.com");
        Optional<Customer> optionalCustomer = Optional.empty();
        when(customerRepository.findById(customerId)).thenReturn(optionalCustomer);
        Customer result=customerService.updateCustomer(customerId, updatedCustomer);
        assertEquals(null,result);
		}
		catch(Exception e) {
			System.out.println(e.getMessage());
		}
       
    }
	
	@Test
    void testGetSubscriptions_CustomerFound() {
		
        Long customerId = 1L;
        Customer customer = new Customer();
        customer.setId(customerId);

        Subscription subscription1 = new Subscription();
        subscription1.setId(1L);
        subscription1.setCurrentStatus(Status.ACTIVE);
        subscription1.setCustomer(customer);

        Subscription subscription2 = new Subscription();
        subscription2.setId(2L);
        subscription2.setCurrentStatus(Status.ACTIVE);
        subscription2.setCustomer(customer);

        List<Subscription> allSubscriptions = List.of(subscription1, subscription2);

        Optional<Customer> optionalCustomer = Optional.of(customer);

        when(customerRepository.findById(customerId)).thenReturn(optionalCustomer);
        when(subscriptionService.getAllSubscriptions()).thenReturn(allSubscriptions);

        List<Subscription> result = customerService.getSubscriptions(customerId);

        // Assert
        assertEquals(2, result.size());
        assertEquals(subscription1, result.get(0));
        assertEquals(subscription2, result.get(1));
}
	
	@Test
    void testGetSubscriptions_CustomerFound_ReturnOnlyActive() {
		try {
		
        Long customerId = 1L;
        Customer customer = new Customer();
        customer.setId(customerId);

        Subscription subscription1 = new Subscription();
        subscription1.setId(1L);
        subscription1.setCurrentStatus(Status.ACTIVE);
        subscription1.setCustomer(customer);

        Subscription subscription2 = new Subscription();
        subscription2.setId(2L);
        subscription2.setCurrentStatus(Status.ACTIVE);
        subscription2.setCustomer(customer);
        
        Subscription subscription3 = new Subscription();
        subscription3.setId(3L);
        subscription3.setCurrentStatus(Status.CANCEL);
        subscription3.setCustomer(customer);
        
        Subscription subscription4 = new Subscription();
        subscription4.setId(4L);
        subscription4.setCurrentStatus(Status.CANCEL);
        subscription4.setCustomer(customer);

        List<Subscription> allSubscriptions = List.of(subscription1, subscription2,subscription3,subscription4);

        Optional<Customer> optionalCustomer = Optional.of(customer);

        when(customerRepository.findById(customerId)).thenReturn(optionalCustomer);
        when(subscriptionService.getAllSubscriptions()).thenReturn(allSubscriptions);
        List<Subscription> result = customerService.getSubscriptions(customerId);
        // Assert
        assertEquals(2, result.size());
        assertEquals(subscription1, result.get(0));
        assertEquals(subscription2, result.get(1));
}
	
	catch(Exception e) {
		System.out.println(e.getMessage());
	}

}
}
