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
@RequestMapping("/api/transactions/admin")
@PreAuthorize("hasAuthority('ADMIN')")
public class AdminTransactionController {

    private final TransactionService transactionService;

    @PostMapping("/deposit")
    public ResponseEntity<ApiResponse<TransactionDTO>> deposit(@Valid @RequestBody TransactionRequest request) {
        return ResponseEntity.ok(transactionService.deposit(request));
    }
}
