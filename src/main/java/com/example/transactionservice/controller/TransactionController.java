package com.example.transactionservice.controller;

import com.example.transactionservice.dto.ApiResponse;
import com.example.transactionservice.dto.TransactionDTO;
import com.example.transactionservice.dto.TransactionRequest;
import com.example.transactionservice.service.TransactionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/transactions")
public class TransactionController {

    private final TransactionService transactionService;

    @PostMapping("/transfer")
    public ResponseEntity<ApiResponse<TransactionDTO>> transfer(@Valid @RequestBody TransactionRequest request) {
        return ResponseEntity.ok(transactionService.transfer(request));
    }

    @PostMapping("/withdraw")
    public ResponseEntity<ApiResponse<TransactionDTO>> withdraw(@Valid @RequestBody TransactionRequest request) {
        return ResponseEntity.ok(transactionService.withdraw(request));
    }
}
