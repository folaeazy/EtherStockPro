package com.fola.EtherStockPro.services;


import com.fola.EtherStockPro.DTO.Response.ApiResponse;
import com.fola.EtherStockPro.DTO.SupplierDTO;
import com.fola.EtherStockPro.entity.Supplier;
import com.fola.EtherStockPro.exceptions.NotFoundException;
import com.fola.EtherStockPro.interfaces.SupplierService;
import com.fola.EtherStockPro.repository.SupplierRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class SupplierServiceImp implements SupplierService {
    private final SupplierRepository supplierRepository;
    private final ModelMapper modelMapper;



    @Override
    public ApiResponse<String> createSupplier(SupplierDTO supplierDTO) {

        Supplier newSupplier = modelMapper.map(supplierDTO, Supplier.class);

        supplierRepository.save(newSupplier);

        return ApiResponse.<String>builder()
                .status(HttpStatus.CREATED.value())
                .message("supplier created successfully")
                .build();
    }

    @Override
    public ApiResponse<List<SupplierDTO>> getAllSupplier() {
        List<Supplier> suppliers = supplierRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));
        List<SupplierDTO> supplierDTOS = modelMapper.map(suppliers, new TypeToken<List<SupplierDTO>>() {}.getType());

        return ApiResponse.<List<SupplierDTO>>builder()
                .status(HttpStatus.OK.value())
                .message("success")
                .supplierDTOS(supplierDTOS)
                .build();
    }

    @Override
    public ApiResponse<SupplierDTO> getSupplierById(Long id) {
        Supplier supplier = supplierRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Supplier not found"));

        SupplierDTO supplierDTO = modelMapper.map(supplier, SupplierDTO.class);

        return ApiResponse.<SupplierDTO>builder()
                .status(HttpStatus.OK.value())
                .message("success")
                .supplierDTO(supplierDTO)
                .build();
    }

    @Override
    public ApiResponse<SupplierDTO> updateSupplier(Long id, SupplierDTO supplierDTO) {
        Supplier existingSupplier = supplierRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("supplier not found") );

        existingSupplier.setName(supplierDTO.getName());
        if (supplierDTO.getAddress() !=null) existingSupplier.setAddress(supplierDTO.getAddress());
        if (supplierDTO.getContactInfo() != null) existingSupplier.setContactInfo(supplierDTO.getContactInfo());

        supplierRepository.save(existingSupplier);

        return ApiResponse.<SupplierDTO>builder()
                .status(HttpStatus.OK.value())
                .message("supplier updated successfully")
                .build();
    }

    @Override
    public ApiResponse<Void> deleteCSupplier(Long id) {
        supplierRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("supplier not found") );

        supplierRepository.deleteById(id);

        return ApiResponse.<Void>builder()
                .status(HttpStatus.OK.value())
                .message("supplier deleted successfully")
                .build();
    }
}
