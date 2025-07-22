package com.example.customerservice.mappers;
import com.customer.service.customerservice.stub.CustomerServiceOuterClass;
import com.example.customerservice.entities.Customer;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component

@RequiredArgsConstructor
public class CustomerMapper {
    private final ModelMapper modelMapper;
    public CustomerServiceOuterClass.Customer fromCustomer(Customer customer){
        return this.modelMapper.map(customer,  CustomerServiceOuterClass.Customer.class);

    }
    public Customer fromGrpcCustomerRequest (CustomerServiceOuterClass.CustomerRequest customerRequest){
        return this.modelMapper.map(customerRequest, Customer.class);

    }

}
