package com.fola.EtherStockPro.interfaces;

import com.fola.EtherStockPro.DTO.ProductDTO;
import com.fola.EtherStockPro.DTO.Response.ApiResponse;
import com.fola.EtherStockPro.entity.Product;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ProductService {

    ApiResponse<ProductDTO> createProduct(ProductDTO productDTO, MultipartFile imageFile);

    ApiResponse<ProductDTO> updateProduct(ProductDTO productDTO, MultipartFile imageFile);

    ApiResponse<List<ProductDTO>> getAllProduct();

    ApiResponse<Product> getProductById(Long id);

    ApiResponse<Void> deleteProduct(Long id);

}
