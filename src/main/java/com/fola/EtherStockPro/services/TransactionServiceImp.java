package com.fola.EtherStockPro.services;

import com.fola.EtherStockPro.DTO.Requests.TransactionRequestDTO;
import com.fola.EtherStockPro.DTO.Response.ApiResponse;
import com.fola.EtherStockPro.DTO.TransactionDTO;
import com.fola.EtherStockPro.entity.Product;
import com.fola.EtherStockPro.entity.Supplier;
import com.fola.EtherStockPro.entity.Transaction;
import com.fola.EtherStockPro.entity.User;
import com.fola.EtherStockPro.enums.TransactionStatus;
import com.fola.EtherStockPro.enums.TransactionType;
import com.fola.EtherStockPro.exceptions.NotFoundException;
import com.fola.EtherStockPro.exceptions.ValueRequiredException;
import com.fola.EtherStockPro.interfaces.TransactionService;
import com.fola.EtherStockPro.interfaces.UserService;
import com.fola.EtherStockPro.repository.ProductRepository;
import com.fola.EtherStockPro.repository.SupplierRepository;
import com.fola.EtherStockPro.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class TransactionServiceImp implements TransactionService {

    private final TransactionRepository transactionRepository;
    private final ModelMapper modelMapper;
    private final SupplierRepository supplierRepository;
    private final UserService userService;
    private final ProductRepository productRepository;

    @Override
    public ApiResponse<String> restockInventory(TransactionRequestDTO transactionRequestDTO) {
        Long productId = transactionRequestDTO.getProductId();
        Long supplierId = transactionRequestDTO.getSupplierId();
        Integer quantity = transactionRequestDTO.getQuantity();

        if (supplierId == null) throw new ValueRequiredException("supplier id is required");

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new NotFoundException("product not found"));

        Supplier supplier = supplierRepository.findById(supplierId)
                .orElseThrow(() -> new NotFoundException("supplier not found"));

        User user = userService.getCurrentLoggedInUser();

        //Update stock quantity and re-save
        product.setStockQuantity(product.getStockQuantity() + quantity);
        productRepository.save(product);

        // create a transaction
        Transaction transaction = Transaction.builder()
                .transactionType(TransactionType.PURCHASE)
                .transactionStatus(TransactionStatus.COMPLETED)
                .product(product)
                .user(user)
                .supplier(supplier)
                .description(transactionRequestDTO.getDescription())
                .totalProducts(quantity)
                .totalPrice(product.getPrice().multiply(BigDecimal.valueOf(quantity)))
                .build();

        transactionRepository.save(transaction);

        return ApiResponse.<String>builder()
                .status(HttpStatus.OK.value())
                .message("Transaction restock created")
                .build();
    }

    @Override
    public ApiResponse<String> sell(TransactionRequestDTO transactionRequestDTO) {
        Long productId = transactionRequestDTO.getProductId();
        Long supplierId = transactionRequestDTO.getSupplierId();
        Integer quantity = transactionRequestDTO.getQuantity();

        if (supplierId == null) throw new ValueRequiredException("supplier id is required");

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new NotFoundException("product not found"));

        Supplier supplier = supplierRepository.findById(supplierId)
                .orElseThrow(() -> new NotFoundException("supplier not found"));

        User user = userService.getCurrentLoggedInUser();

        //Update stock quantity and re-save
        product.setStockQuantity(product.getStockQuantity() - quantity);
        productRepository.save(product);

        // create a transaction
        Transaction transaction = Transaction.builder()
                .transactionType(TransactionType.SALE)
                .transactionStatus(TransactionStatus.COMPLETED)
                .product(product)
                .user(user)
                .description(transactionRequestDTO.getDescription())
                .totalProducts(quantity)
                .totalPrice(product.getPrice().multiply(BigDecimal.valueOf(quantity)))
                .build();
        transactionRepository.save(transaction);

        return ApiResponse.<String>builder()
                .status(HttpStatus.OK.value())
                .message("Transaction sold successful")
                .build();
    }

    @Override
    public ApiResponse<String> returnToSupplier(TransactionRequestDTO transactionRequestDTO) {
        Long productId = transactionRequestDTO.getProductId();
        Long supplierId = transactionRequestDTO.getSupplierId();
        Integer quantity = transactionRequestDTO.getQuantity();

        if (supplierId == null) throw new ValueRequiredException("supplier id is required");

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new NotFoundException("product not found"));

        Supplier supplier = supplierRepository.findById(supplierId)
                .orElseThrow(() -> new NotFoundException("supplier not found"));

        User user = userService.getCurrentLoggedInUser();

        //Update stock quantity and re-save
        product.setStockQuantity(product.getStockQuantity() - quantity);
        productRepository.save(product);

        // create a transaction
        Transaction transaction = Transaction.builder()
                .transactionType(TransactionType.RETURN_TO_SUPPLIER)
                .transactionStatus(TransactionStatus.PROCESSING)
                .product(product)
                .user(user)
                .supplier(supplier)
                .description(transactionRequestDTO.getDescription())
                .totalProducts(quantity)
                .totalPrice(BigDecimal.ZERO)
                .build();
        transactionRepository.save(transaction);

        return ApiResponse.<String>builder()
                .status(HttpStatus.OK.value())
                .message("Transaction return successfully initialized")
                .build();
    }

    @Override
    public ApiResponse<List<TransactionDTO>> getAllTransactions(int page, int size, String searchText) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "id"));
        Page<Transaction> transactionPage = transactionRepository.searchTransactions(searchText, pageable);

        List<TransactionDTO> transactionDTOS = modelMapper
                .map(transactionPage.getContent(), new TypeToken<List<TransactionDTO>>() {}.getType());

        transactionDTOS.forEach(transactionDTO -> {
            transactionDTO.setSupplier(null);
            transactionDTO.setUser(null);
            transactionDTO.setProduct(null);
        });

        return ApiResponse.<List<TransactionDTO>>builder()
                .status(HttpStatus.OK.value())
                .message("success")
                .transactionDTOS(transactionDTOS)
                .build();
    }


    @Override
    public ApiResponse<TransactionDTO> getTransactionById(Long id) {
        Transaction transaction = transactionRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("transaction not found"));

        TransactionDTO transactionDTO = modelMapper.map(transaction, TransactionDTO.class);

        transactionDTO.getUser().setTransactions(null); // removing user transaction in list

        return  ApiResponse.<TransactionDTO>builder()
                .status(HttpStatus.OK.value())
                .message("success")
                .transactionDTO(transactionDTO)
                .build();
    }

    @Override
    public ApiResponse<List<TransactionDTO>> getTransactionByMonthAndYear(int month, int year) {
        List<Transaction> transactions = transactionRepository.findTransactionByMonthAndYear(month, year);

        List<TransactionDTO> transactionDTOS = modelMapper
                .map(transactions, new TypeToken<List<TransactionDTO>>() {}.getType());

        transactionDTOS.forEach(transactionDTO -> {
            transactionDTO.setSupplier(null);
            transactionDTO.setUser(null);
            transactionDTO.setProduct(null);
        });

        return ApiResponse.<List<TransactionDTO>>builder()
                .status(HttpStatus.OK.value())
                .message("success")
                .transactionDTOS(transactionDTOS)
                .build();
    }

    @Override
    public ApiResponse<String> updateTransactionStatus(Long transactionId, TransactionStatus transactionStatus) {
        Transaction existingTransaction = transactionRepository.findById(transactionId)
                .orElseThrow(() -> new NotFoundException("transaction not found"));


        existingTransaction.setTransactionStatus(transactionStatus);
        existingTransaction.setUpdatedAt(LocalDateTime.now());

        transactionRepository.save(existingTransaction);

        return  ApiResponse.<String>builder()
                .status(HttpStatus.OK.value())
                .message("Transaction status updated")
                .build();
    }
}
