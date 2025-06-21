package com.fola.EtherStockPro.interfaces;

import com.fola.EtherStockPro.DTO.Requests.TransactionRequestDTO;
import com.fola.EtherStockPro.DTO.Response.ApiResponse;
import com.fola.EtherStockPro.DTO.TransactionDTO;
import com.fola.EtherStockPro.enums.TransactionStatus;

import java.util.List;

public interface TransactionService {

    ApiResponse<String> restockInventory(TransactionRequestDTO transactionRequestDTO);

    ApiResponse<String> sell(TransactionRequestDTO transactionRequestDTO);

    ApiResponse<String> returnToSupplier(TransactionRequestDTO transactionRequestDTO);

    ApiResponse<List<TransactionDTO>> getAllTransactions(int page, int size, String searchText);

    ApiResponse<TransactionDTO> getTransactionById(Long id);

    ApiResponse<List<TransactionDTO>> getTransactionByMonthAndYear(int month, int year);

    ApiResponse<String> updateTransactionStatus(Long transactionId, TransactionStatus transactionStatus);
}
