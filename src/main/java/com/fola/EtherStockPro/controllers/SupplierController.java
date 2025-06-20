package com.fola.EtherStockPro.controllers;



import com.fola.EtherStockPro.DTO.Response.ApiResponse;

import com.fola.EtherStockPro.DTO.SupplierDTO;
import com.fola.EtherStockPro.interfaces.SupplierService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/suppliers")
@RequiredArgsConstructor
public class SupplierController {

    private final SupplierService supplierService;

    @PostMapping("/create")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<ApiResponse<String>> createSupplier(@RequestBody @Valid SupplierDTO supplierDTO) {
        return ResponseEntity.ok(supplierService.createSupplier(supplierDTO));

    }

    @GetMapping("/all")
    public ResponseEntity<ApiResponse<List<SupplierDTO>>> getAllSuppier() {
        return ResponseEntity.ok(supplierService.getAllSupplier());

    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<SupplierDTO>>  getSupplierById(@PathVariable Long id) {
        return ResponseEntity.ok(supplierService.getSupplierById(id));

    }


    @PutMapping("/update/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<ApiResponse<SupplierDTO>> updateSupplier(@PathVariable Long id , @RequestBody @Valid SupplierDTO supplierDTO){
        return ResponseEntity.ok(supplierService.updateSupplier(id, supplierDTO));
    }


    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<ApiResponse<Void>>  deleteSupplier(@PathVariable Long id) {
        return ResponseEntity.ok(supplierService.deleteCSupplier(id));

    }
}
