package com.fola.EtherStockPro.DTO.Response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fola.EtherStockPro.DTO.*;
import com.fola.EtherStockPro.entity.Product;
import com.fola.EtherStockPro.enums.UserRoles;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse<T> {

    private int status;
    private Long id;
    private String message;

    //for login
    private String token;
    private UserRoles role;
    private  String expirationTime;

    //for pagination
    private Integer totalPages;
    private Long totalElement;


    //data output optional
    private UserDTO userDTO;
    private List<UserDTO> userDTOS;

    private CategoryDTO categoryDTO;
    private List<CategoryDTO> categoryDTOS;

    private SupplierDTO supplierDTO;
    private List<SupplierDTO> supplierDTOS;

    private Product product;
    private List<ProductDTO> productDTOS;

    private TransactionDTO transactionDTO;
    private List<TransactionDTO> transactionDTOS;

    private final LocalDateTime timestamp = LocalDateTime.now();

}
