package com.fola.EtherStockPro.interfaces;

import com.fola.EtherStockPro.DTO.Response.ApiResponse;
import com.fola.EtherStockPro.DTO.SupplierDTO;

import java.util.List;

public interface SupplierService {

    ApiResponse<String> createSupplier(SupplierDTO supplierDTO);

    ApiResponse<List<SupplierDTO>> getAllSupplier();

    ApiResponse<SupplierDTO>  getSupplierById(Long id);

    ApiResponse<SupplierDTO> updateSupplier(Long id , SupplierDTO supplierDTO);

    ApiResponse<Void>  deleteCSupplier(Long id);


}
