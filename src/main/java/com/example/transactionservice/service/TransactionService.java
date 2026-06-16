package com.example.transactionservice.service;

import com.example.transactionservice.dto.ApiResponse;
import com.example.transactionservice.dto.TransactionDTO;
import com.example.transactionservice.dto.TransactionRequest;

import java.time.LocalDateTime;
import java.util.List;

public interface TransactionService {

    ApiResponse<TransactionDTO> deposit(TransactionRequest request);
    ApiResponse<TransactionDTO> transfer(TransactionRequest request);
    ApiResponse<TransactionDTO> withdraw(TransactionRequest request);
    ApiResponse<TransactionDTO> getTransactionByReference(String reference);
    ApiResponse<List<TransactionDTO>> getAllTransactionHistoryFromAnAccountNumber(String accountNumber);
    ApiResponse<List<TransactionDTO>> getTransactionHistory(String accountNumber, LocalDateTime start, LocalDateTime end);
    ApiResponse<List<TransactionDTO>> getMyTransactionHistoryByDirection(TransactionRequest request);
}
