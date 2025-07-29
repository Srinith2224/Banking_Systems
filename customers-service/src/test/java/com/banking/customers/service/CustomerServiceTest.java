package com.banking.customers.service;

import com.banking.customers.dto.CustomerDto;
import com.banking.customers.entity.Customer;
import com.banking.customers.exception.CustomerNotFoundException;
import com.banking.customers.exception.DuplicateEmailException;
import com.banking.customers.repository.CustomerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomerServiceTest {

    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private CustomerService customerService;

    private CustomerDto customerDto;
    private Customer customer;

    @BeforeEach
    void setUp() {
        customerDto = new CustomerDto();
        customerDto.setFirstName("Jordan");
        customerDto.setLastName("Lee");
        customerDto.setEmail("jordan.lee@bank.com");
        customerDto.setPhone("+31 690000000");
        customerDto.setAddress("10 Bank Avenue, Rotterdam");

        customer = new Customer();
        customer.setId(1L);
        customer.setFirstName("Jordan");
        customer.setLastName("Lee");
        customer.setEmail("jordan.lee@bank.com");
        customer.setPhone("+31 690000000");
        customer.setAddress("10 Bank Avenue, Rotterdam");
        customer.setCreatedAt(LocalDateTime.now());
    }

    @Test
    void createCustomer_Success() {
        when(customerRepository.existsByEmail(anyString())).thenReturn(false);
        when(customerRepository.save(any(Customer.class))).thenReturn(customer);

        CustomerDto result = customerService.createCustomer(customerDto);

        assertNotNull(result);
        assertEquals(customerDto.getEmail(), result.getEmail());
        assertEquals(customerDto.getFirstName(), result.getFirstName());
        verify(customerRepository).save(any(Customer.class));
    }

    @Test
    void createCustomer_DuplicateEmail() {
        when(customerRepository.existsByEmail(anyString())).thenReturn(true);

        assertThrows(DuplicateEmailException.class, () -> customerService.createCustomer(customerDto));
    }

    @Test
    void getCustomerById_Success() {
        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));

        CustomerDto result = customerService.getCustomerById(1L);

        assertNotNull(result);
        assertEquals(customer.getEmail(), result.getEmail());
    }

    @Test
    void getCustomerById_NotFound() {
        when(customerRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(CustomerNotFoundException.class, () -> customerService.getCustomerById(1L));
    }

    @Test
    void getAllCustomers_Success() {
        when(customerRepository.findAll()).thenReturn(Arrays.asList(customer));

        List<CustomerDto> results = customerService.getAllCustomers();

        assertNotNull(results);
        assertEquals(1, results.size());
    }

    @Test
    void deleteCustomer_Success() {
        when(customerRepository.existsById(1L)).thenReturn(true);
        doNothing().when(customerRepository).deleteById(1L);

        assertDoesNotThrow(() -> customerService.deleteCustomer(1L));
        verify(customerRepository).deleteById(1L);
    }

    @Test
    void deleteCustomer_NotFound() {
        when(customerRepository.existsById(1L)).thenReturn(false);

        assertThrows(CustomerNotFoundException.class, () -> customerService.deleteCustomer(1L));
    }
}
