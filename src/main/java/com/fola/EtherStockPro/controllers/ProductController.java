package com.fola.EtherStockPro.controllers;

import com.fola.EtherStockPro.DTO.ProductDTO;
import com.fola.EtherStockPro.DTO.Response.ApiResponse;
import com.fola.EtherStockPro.entity.Product;
import com.fola.EtherStockPro.interfaces.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/products")
public class ProductController {

    private  final ProductService productService;

    @PostMapping("/create")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<ApiResponse<ProductDTO>> createProduct(ProductDTO productDTO, MultipartFile imageFile){

    }

    @PatchMapping("/update")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<ApiResponse<ProductDTO>> updateProduct(ProductDTO productDTO, MultipartFile imageFile){

    }

    @GetMapping("/all")
    public ResponseEntity< ApiResponse<List<ProductDTO>>> getAllProduct(){

    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Product>> getProductById(Long id){

    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> deleteProduct(Long id) {

    }
}
