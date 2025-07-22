package com.example.customerservice.web;

import com.customer.service.customerservice.stub.CustomerServiceGrpc;
import com.customer.service.customerservice.stub.CustomerServiceOuterClass;
import com.example.customerservice.entities.Customer;
import com.example.customerservice.mappers.CustomerMapper;
import com.example.customerservice.repository.CustomerRepository;
import io.grpc.Status;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@GrpcService
public class CustomerGRPCService extends CustomerServiceGrpc.CustomerServiceImplBase{

    @Autowired
    CustomerRepository customerRepository;
    @Autowired
    CustomerMapper customerMapper;

    @Override
    public void
    getAllCustomers(CustomerServiceOuterClass.GetAllCustomersRequest request,
                    StreamObserver<CustomerServiceOuterClass.GetCustomersResponse>
                            responseObserver) {
        List<Customer> customers = customerRepository.findAll();
        List<CustomerServiceOuterClass.Customer> customersResponselist =
                customers.stream()
                        .map(customerMapper::fromCustomer).collect(Collectors.toList());
        CustomerServiceOuterClass.GetCustomersResponse customersResponse=
                CustomerServiceOuterClass.GetCustomersResponse.newBuilder()
                        .addAllCustomers(customersResponselist)
                        .build();
        responseObserver.onNext(customersResponse);
        responseObserver.onCompleted();
    }


    @Override
    public void saveCustomer(CustomerServiceOuterClass.SaveCustomerRequest request,
                             StreamObserver<CustomerServiceOuterClass.SaveCustomerResponse> responseObserver) {
        try {
            // Extract customer data from the request
            CustomerServiceOuterClass.CustomerRequest customerRequest = request.getCustomer();

            // Map the protobuf message to your domain model
            Customer mappedCustomer = customerMapper.fromGrpcCustomerRequest(customerRequest);

            // Assuming customerRepository has a method to save or update a customer
            Customer savedCustomer = customerRepository.save(mappedCustomer);

            // Map the saved customer back to protobuf message
            CustomerServiceOuterClass.Customer savedCustomerResponse = customerMapper.fromCustomer(savedCustomer);

            // Build the response message
            CustomerServiceOuterClass.SaveCustomerResponse saveCustomerResponse =
                    CustomerServiceOuterClass.SaveCustomerResponse.newBuilder()
                            .setCustomer(savedCustomerResponse)
                            .build();

            // Send the response to the client
            responseObserver.onNext(saveCustomerResponse);
            responseObserver.onCompleted();
        } catch (Exception e) {
            // Handle exceptions and send an error response
            responseObserver.onError(Status.INTERNAL
                    .withDescription("Error while processing the request.")
                    .asRuntimeException());
        }
    }


    @Override
    public void getCustomerById(CustomerServiceOuterClass.GetCustomerByIdRequest request,
                                StreamObserver<CustomerServiceOuterClass.GetCustomerByIdResponse> responseObserver) {
        try {
            // Assuming customerRepository has a method to find a customer by ID
            Optional<Customer> customerOptional = customerRepository.findById(request.getCustomerId());

            if (customerOptional.isPresent()) {
                // If customer is found, map it to protobuf message
                CustomerServiceOuterClass.Customer customerResponse =
                        customerMapper.fromCustomer(customerOptional.get());

                // Build the response message
                CustomerServiceOuterClass.GetCustomerByIdResponse getByIdResponse =
                        CustomerServiceOuterClass.GetCustomerByIdResponse.newBuilder()
                                .setCustomer(customerResponse)
                                .build();

                // Send the response to the client
                responseObserver.onNext(getByIdResponse);
                responseObserver.onCompleted();
            } else {
                // If customer is not found, send an error response
                responseObserver.onError(Status.NOT_FOUND
                        .withDescription("Customer not found for ID: " + request.getCustomerId())
                        .asRuntimeException());
            }
        } catch (Exception e) {
            // Handle exceptions and send an error response
            responseObserver.onError(Status.INTERNAL
                    .withDescription("Error while processing the request.")
                    .asRuntimeException());
        }
    }


}
