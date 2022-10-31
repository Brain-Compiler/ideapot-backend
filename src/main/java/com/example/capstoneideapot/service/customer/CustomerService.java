package com.example.capstoneideapot.service.customer;

import com.example.capstoneideapot.entity.Customer;
import com.example.capstoneideapot.entity.dto.customer.AnswerDto;
import com.example.capstoneideapot.entity.dto.customer.CustomerDto;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface CustomerService {

    // GET
    ResponseEntity<List<Customer>> allCustomer();

    ResponseEntity<Customer> getCustomerById(Long id);

    // POST
    ResponseEntity<List<Customer>> getMyCustomerService(CustomerDto customerDto);

    // PUT
    ResponseEntity<Customer> answerCustomerService(AnswerDto answerDto);

    // DELETE

    // ELSE
    ResponseEntity<Boolean> checkSelfAuthentication(Long userId, Long writerId);

}
