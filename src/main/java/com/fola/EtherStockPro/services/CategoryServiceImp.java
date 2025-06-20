package com.fola.EtherStockPro.services;

import com.fola.EtherStockPro.DTO.CategoryDTO;
import com.fola.EtherStockPro.DTO.Response.ApiResponse;
import com.fola.EtherStockPro.entity.Category;
import com.fola.EtherStockPro.exceptions.NotFoundException;
import com.fola.EtherStockPro.interfaces.CategoryService;
import com.fola.EtherStockPro.repository.CategoryRepository;
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
public class CategoryServiceImp implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final ModelMapper modelMapper;


    @Override
    public ApiResponse<String> createCategory(CategoryDTO categoryDTO) {
        Category newCategory = modelMapper.map(categoryDTO, Category.class);

        categoryRepository.save(newCategory);

        return ApiResponse.<String>builder()
                .status(HttpStatus.CREATED.value())
                .message("category created successfully")
                .build();
    }

    @Override
    public ApiResponse<List<CategoryDTO>> getAllCategory() {

        List<Category> categories = categoryRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));
        List<CategoryDTO> categoryDTOS = modelMapper.map(categories, new TypeToken<List<CategoryDTO>>() {}.getType());

        return ApiResponse.<List<CategoryDTO>>builder()
                .status(HttpStatus.OK.value())
                .message("success")
                .categoryDTOS(categoryDTOS)
                .build();
    }

    @Override
    public ApiResponse<CategoryDTO> getCategoryById(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Category not found"));

        CategoryDTO categoryDTO = modelMapper.map(category, CategoryDTO.class);

        return ApiResponse.<CategoryDTO>builder()
                .status(HttpStatus.OK.value())
                .message("success")
                .categoryDTO(categoryDTO)
                .build();

    }

    @Override
    public ApiResponse<CategoryDTO> updateCategory(Long id, CategoryDTO categoryDTO) {
        Category existingCategory = categoryRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("category not found") );

        existingCategory.setName(categoryDTO.getName());
        categoryRepository.save(existingCategory);

        return ApiResponse.<CategoryDTO>builder()
                .status(HttpStatus.OK.value())
                .message("category updated successfully")
                .build();
    }

    @Override
    public ApiResponse<Void> deleteCategory(Long id) {
        categoryRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("category not found") );

        categoryRepository.deleteById(id);

        return ApiResponse.<Void>builder()
                .status(HttpStatus.OK.value())
                .message("category deleted successfully")
                .build();

    }
}
