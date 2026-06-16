package com.example.transactionservice.service.impl;

import com.example.transactionservice.dto.AccountDTO;
import com.example.transactionservice.dto.ApiResponse;
import com.example.transactionservice.dto.TransactionDTO;
import com.example.transactionservice.dto.TransactionRequest;
import com.example.transactionservice.entity.Transaction;
import com.example.transactionservice.enums.*;
import com.example.transactionservice.exceptions.BadRequestException;
import com.example.transactionservice.exceptions.NotFoundException;
import com.example.transactionservice.feign.AccountFeignClient;
import com.example.transactionservice.kafka.dto.BalanceUpdateEvent;
import com.example.transactionservice.kafka.service.TransactionEventPublisher;
import com.example.transactionservice.repository.TransactionRepository;
import com.example.transactionservice.service.TransactionService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;
    private final AccountFeignClient accountFeignClient;
    private final ModelMapper modelMapper;
    private final TransactionEventPublisher transactionEventPublisher;

    @Override
    @Transactional
    public ApiResponse<TransactionDTO> deposit(TransactionRequest request) {
        fetchAndValidateAccount(request.getToAccountNumber());

        Transaction deposit = Transaction.builder()
                .reference("DEP" + UUID.randomUUID().toString().substring(0,8))
                .fromBankCode("DIGI")
                .currency(Currency.USD)
                .toAccountNumber(request.getToAccountNumber())
                .toBankCode("DIGI")
                .amount(request.getAmount())
                .transactionDirection(TransactionDirection.CREDIT)
                .channel(Channel.API)
                .description(request.getDescription())
                .transactionType(TransactionType.DEPOSIT)
                .transactionStatus(TransactionStatus.SUCCESS)
                .createdAt(LocalDateTime.now())
                .build();

        Transaction savedTransaction = transactionRepository.save(deposit);

        BalanceUpdateEvent balanceUpdateEvent = BalanceUpdateEvent.builder()
                .accountNumber(request.getToAccountNumber())
                .amount(request.getAmount())
                .currency(Currency.USD)
                .transactionDirection(TransactionDirection.CREDIT)
                .transactionStatus(TransactionStatus.SUCCESS)
                .reference(savedTransaction.getReference())
                .build();

        transactionEventPublisher.sendBalanceUpdate(balanceUpdateEvent);

        return new ApiResponse<>(201, "Deposit successfully", modelMapper.map(savedTransaction, TransactionDTO.class));
    }

    @Override
    public ApiResponse<TransactionDTO> transfer(TransactionRequest request) {
        return null;
    }

    @Override
    public ApiResponse<TransactionDTO> withdraw(TransactionRequest request) {
        return null;
    }

    @Override
    public ApiResponse<TransactionDTO> getTransactionByReference(String reference) {
        return null;
    }

    @Override
    public ApiResponse<List<TransactionDTO>> getAllTransactionHistoryFromAnAccountNumber(String accountNumber) {
        return null;
    }

    @Override
    public ApiResponse<List<TransactionDTO>> getTransactionHistory(String accountNumber, LocalDateTime start, LocalDateTime end) {
        return null;
    }

    @Override
    public ApiResponse<List<TransactionDTO>> getMyTransactionHistoryByDirection(TransactionRequest request) {
        return null;
    }



    private AccountDTO fetchAndValidateAccount(String accountNumber) {
        ApiResponse<AccountDTO> response = accountFeignClient.getAccountByNumber(accountNumber);

        if(response == null || response.data() == null) {
            throw new NotFoundException("Account " + accountNumber + " not found");
        }

        AccountDTO account = response.data();

        if(account.getAccountStatus().equals(AccountStatus.CLOSED)) {
            throw new BadRequestException("Transaction denied: Account status is CLOSED");
        }

        return account;
    }
}
