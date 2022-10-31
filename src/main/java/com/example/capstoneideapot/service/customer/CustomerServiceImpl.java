package com.example.capstoneideapot.service.customer;

import com.example.capstoneideapot.entity.Customer;
import com.example.capstoneideapot.entity.User;
import com.example.capstoneideapot.entity.dto.customer.AnswerDto;
import com.example.capstoneideapot.entity.dto.customer.CustomerDto;
import com.example.capstoneideapot.repository.CustomerRepository;
import com.example.capstoneideapot.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {

    private final UserRepository userRepository;

    private final CustomerRepository customerRepository;

    @Override
    public ResponseEntity<List<Customer>> allCustomer() {
        try {
            List<Customer> customerList = customerRepository.findAll();

            if (customerList.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            } else {
                return new ResponseEntity<>(customerList, HttpStatus.OK);
            }
        } catch (Exception exception) {
            log.info("error: {}", exception.getMessage());
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<Customer> getCustomerById(Long id) {
        Optional<Customer> customer = customerRepository.findById(id);

        if (customer.isPresent()) {
            return new ResponseEntity<>(customer.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @Override
    public ResponseEntity<List<Customer>> getMyCustomerService(CustomerDto customerDto) {
        Long userid = customerDto.getUserId();
        User user = userRepository.findById(userid).orElse(null);

        List<Customer> customerList = customerRepository.findAllByUser(user);

        if (!customerList.isEmpty()) {
            return new ResponseEntity<>(customerList, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @Override
    public ResponseEntity<Customer> answerCustomerService(AnswerDto answerDto) {
        try {
            Long customerId = answerDto.getId();
            Optional<Customer> customer = customerRepository.findById(customerId);

            if (customer.isPresent()) {
                Customer customerEntity = customer.get();
                String answer = answerDto.getAnswer();

                customerEntity.setAnswer(answer);
                customerRepository.save(customerEntity);

                return new ResponseEntity<>(customerEntity, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
            }
        } catch (Exception exception) {
            log.info("error: {}", exception.getMessage());
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<Boolean> checkSelfAuthentication(Long userId, Long writerId) {
        if (Objects.equals(userId, writerId)) {
            return new ResponseEntity<>(true, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(false, HttpStatus.OK);
        }
    }
}
