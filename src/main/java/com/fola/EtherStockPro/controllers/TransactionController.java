package com.fola.EtherStockPro.controllers;


import com.fola.EtherStockPro.DTO.Requests.TransactionRequestDTO;
import com.fola.EtherStockPro.DTO.Response.ApiResponse;
import com.fola.EtherStockPro.DTO.TransactionDTO;
import com.fola.EtherStockPro.enums.TransactionStatus;
import com.fola.EtherStockPro.interfaces.TransactionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/transactions")
public class TransactionController {

    private final TransactionService transactionService;

    @PostMapping("/purchase")
    public ResponseEntity<ApiResponse<String>> restockInventory(@RequestBody @Valid TransactionRequestDTO transactionRequestDTO) {
        return ResponseEntity.ok(transactionService.restockInventory(transactionRequestDTO));

    }

    @PostMapping("/sell")
    public ResponseEntity<ApiResponse<String>> sell(@RequestBody @Valid TransactionRequestDTO transactionRequestDTO) {
        return ResponseEntity.ok(transactionService.sell(transactionRequestDTO));

    }

    @PostMapping("/return")
    public ResponseEntity<ApiResponse<String>> returnToSupplier(@RequestBody @Valid TransactionRequestDTO transactionRequestDTO) {
        return ResponseEntity.ok(transactionService.returnToSupplier(transactionRequestDTO));
    }

    @GetMapping("/all")
    public ResponseEntity<ApiResponse<List<TransactionDTO>>> getAllTransactions(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "1000") int size,
            @RequestParam(required = false) String searchText) {

        return ResponseEntity.ok(transactionService.getAllTransactions(page, size, searchText));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<TransactionDTO>> getTransactionById(@PathVariable Long id) {
        return ResponseEntity.ok(transactionService.getTransactionById(id));
    }

    @GetMapping("/by-month-year")
    public ResponseEntity<ApiResponse<List<TransactionDTO>>> getTransactionByMonthAndYear(
            @RequestParam int month, @RequestParam int year){

        return ResponseEntity.ok(transactionService.getTransactionByMonthAndYear(month, year));
    }

    @PutMapping("/update/{transactionId}")
    public ResponseEntity<ApiResponse<String>> updateTransactionStatus(
            @PathVariable Long transactionId,
            @RequestParam @Valid TransactionStatus transactionStatus) {

        return ResponseEntity.ok(transactionService.updateTransactionStatus(transactionId, transactionStatus));
    }
}
