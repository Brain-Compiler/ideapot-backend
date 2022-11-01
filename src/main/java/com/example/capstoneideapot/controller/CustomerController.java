package com.example.capstoneideapot.controller;

import com.example.capstoneideapot.entity.Customer;
import com.example.capstoneideapot.entity.dto.customer.AnswerDto;
import com.example.capstoneideapot.entity.dto.customer.CustomerDto;
import com.example.capstoneideapot.service.customer.CustomerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/service")
public class CustomerController {

    private final CustomerService customerService;

    // GET
    @GetMapping // admin // 모든 고객센터 문의를 가져옴
    public ResponseEntity<List<Customer>> allCustomerService() {
        return customerService.allCustomer();
    }

    @GetMapping("/{id}") // id를 통해 고객센터 문의를 가져옴
    public ResponseEntity<Customer> getCustomerById(@PathVariable Long id) {
        return customerService.getCustomerById(id);
    }

    // POST
    @PostMapping("/my-customer-service") // 자기의 고객센터 문의 내역만 가져옴 // admin
    public ResponseEntity<List<Customer>> getMyCustomerService(@RequestBody CustomerDto customerDto) {
        return customerService.getMyCustomerService(customerDto);
    }

    // PUT
    @PostMapping("/{id}") // 고객 문의 답변
    public ResponseEntity<Customer> answerCustomerService(@RequestBody AnswerDto answerDto) {
        return customerService.answerCustomerService(answerDto);
    }

    // DELETE

    // ELSE
    @GetMapping("/check-self-auth") // id를 통해 고객센터 문의를 가져올 때 본인이 맞는 지 확인
    public ResponseEntity<Boolean> checkSelfAuthentication(@RequestParam Long userId, @RequestParam Long writerId) {
        return customerService.checkSelfAuthentication(userId, writerId);
    }

}
