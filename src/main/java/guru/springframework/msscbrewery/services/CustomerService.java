package guru.springframework.msscbrewery.services;

import guru.springframework.msscbrewery.web.model.CustomerDto;

import java.util.UUID;

public interface CustomerService {

    public CustomerDto getCustomerById(UUID id);

    void updateCustomer(UUID customerId, CustomerDto customerDto);

    CustomerDto createCustomer(CustomerDto customerDto);
}
