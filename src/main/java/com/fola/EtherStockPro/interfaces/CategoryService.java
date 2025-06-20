package com.fola.EtherStockPro.interfaces;

import com.fola.EtherStockPro.DTO.CategoryDTO;
import com.fola.EtherStockPro.DTO.Response.ApiResponse;

import java.util.List;

public interface CategoryService {

    ApiResponse<String> createCategory(CategoryDTO categoryDTO);

    ApiResponse<List<CategoryDTO>> getAllCategory();

    ApiResponse<CategoryDTO>  getCategoryById(Long id);

    ApiResponse<CategoryDTO> updateCategory(Long id ,CategoryDTO categoryDTO);

    ApiResponse<Void>  deleteCategory(Long id);

}
