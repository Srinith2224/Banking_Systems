package com.banking.customers.service;

import com.banking.customers.dto.CustomerDto;
import com.banking.customers.entity.Customer;
import com.banking.customers.exception.CustomerNotFoundException;
import com.banking.customers.exception.DuplicateEmailException;
import com.banking.customers.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CustomerService {
    
    private final CustomerRepository customerRepository;
    
    /**
     * Register a new customer
     * @param customerDto Customer details
     * @return Created customer
     */
    public CustomerDto createCustomer(CustomerDto customerDto) {
        if (customerRepository.existsByEmail(customerDto.getEmail())) {
            throw new DuplicateEmailException("Email already exists: " + customerDto.getEmail());
        }
        
        Customer customer = convertToEntity(customerDto);
        Customer savedCustomer = customerRepository.save(customer);
        return convertToDto(savedCustomer);
    }
    
    /**
     * Get all customers
     * @return List of all customers
     */
    public List<CustomerDto> getAllCustomers() {
        return customerRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
    
    /**
     * Get customer by ID
     * @param id Customer ID
     * @return Customer details
     */
    public CustomerDto getCustomerById(Long id) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new CustomerNotFoundException("Customer not found with id: " + id));
        return convertToDto(customer);
    }
    
    /**
     * Get customer by email
     * @param email Customer email
     * @return Customer details
     */
    public CustomerDto getCustomerByEmail(String email) {
        Customer customer = customerRepository.findByEmail(email)
                .orElseThrow(() -> new CustomerNotFoundException("Customer not found with email: " + email));
        return convertToDto(customer);
    }
    
    /**
     * Update customer details
     * @param id Customer ID
     * @param customerDto Updated customer details
     * @return Updated customer
     */
    public CustomerDto updateCustomer(Long id, CustomerDto customerDto) {
        Customer existingCustomer = customerRepository.findById(id)
                .orElseThrow(() -> new CustomerNotFoundException("Customer not found with id: " + id));
        
        // Check if email is being changed and if it already exists
        if (!existingCustomer.getEmail().equals(customerDto.getEmail()) &&
            customerRepository.existsByEmail(customerDto.getEmail())) {
            throw new DuplicateEmailException("Email already exists: " + customerDto.getEmail());
        }
        
        existingCustomer.setFirstName(customerDto.getFirstName());
        existingCustomer.setLastName(customerDto.getLastName());
        existingCustomer.setEmail(customerDto.getEmail());
        existingCustomer.setPhone(customerDto.getPhone());
        existingCustomer.setAddress(customerDto.getAddress());
        
        Customer updatedCustomer = customerRepository.save(existingCustomer);
        return convertToDto(updatedCustomer);
    }
    
    /**
     * Delete customer by ID
     * @param id Customer ID
     */
    public void deleteCustomer(Long id) {
        if (!customerRepository.existsById(id)) {
            throw new CustomerNotFoundException("Customer not found with id: " + id);
        }
        customerRepository.deleteById(id);
    }
    
    /**
     * Convert Customer entity to CustomerDto
     */
    private CustomerDto convertToDto(Customer customer) {
        CustomerDto dto = new CustomerDto();
        dto.setId(customer.getId());
        dto.setFirstName(customer.getFirstName());
        dto.setLastName(customer.getLastName());
        dto.setEmail(customer.getEmail());
        dto.setPhone(customer.getPhone());
        dto.setAddress(customer.getAddress());
        dto.setCreatedAt(customer.getCreatedAt().toString());
        return dto;
    }
    
    /**
     * Convert CustomerDto to Customer entity
     */
    private Customer convertToEntity(CustomerDto dto) {
        Customer customer = new Customer();
        customer.setFirstName(dto.getFirstName());
        customer.setLastName(dto.getLastName());
        customer.setEmail(dto.getEmail());
        customer.setPhone(dto.getPhone());
        customer.setAddress(dto.getAddress());
        return customer;
    }
}
