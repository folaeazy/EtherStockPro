package com.fola.EtherStockPro.services;

import com.fola.EtherStockPro.DTO.ProductDTO;
import com.fola.EtherStockPro.DTO.Response.ApiResponse;
import com.fola.EtherStockPro.entity.Category;
import com.fola.EtherStockPro.entity.Product;
import com.fola.EtherStockPro.exceptions.NotFoundException;
import com.fola.EtherStockPro.interfaces.ProductService;
import com.fola.EtherStockPro.repository.CategoryRepository;
import com.fola.EtherStockPro.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;
import java.util.UUID;


@Service
@RequiredArgsConstructor
@Slf4j
public class ProductServiceImp implements ProductService {

    private final ProductRepository productRepository;
    private  final ModelMapper modelMapper;
    private final CategoryRepository categoryRepository;

    // Directory to save image
    private static  final  String IMAGE_DIRECTORY = System.getProperty("user.dir") + "/product-image/";


    @Override
    public ApiResponse<ProductDTO> createProduct(ProductDTO productDTO, MultipartFile imageFile) {

        //Extract category
        Category category = categoryRepository.findById(productDTO.getCategoryId())
                .orElseThrow(() -> new NotFoundException("category not found"));

        // Map out product DTO to product entity

        Product newProduct =  Product.builder()
                .name(productDTO.getName())
                .sku(productDTO.getSku())
                .price(productDTO.getPrice())
                .category(category)
                .description(productDTO.getDescription())
                .stockQuantity(BigInteger.valueOf(productDTO.getStockQuantity()))
                .build();

        if(imageFile != null) {
            String imageUrl = saveImage(imageFile);
            newProduct.setImageUrl(imageUrl);
        }

        productRepository.save(newProduct);
        return ApiResponse.<ProductDTO>builder()
                .status(HttpStatus.CREATED.value())
                .message("product created successfully")
                .build();
    }

    @Override
    public ApiResponse<ProductDTO> updateProduct(ProductDTO productDTO, MultipartFile imageFile) {
        // check for existing product
        Product existingProduct = productRepository.findById(productDTO.getProductId())
                .orElseThrow(() -> new NotFoundException("Product not found"));

        // check if image is associated with the update
        if (imageFile != null && !imageFile.isEmpty()) {
            String imageUrl = saveImage(imageFile);
            existingProduct.setImageUrl(imageUrl);
        }

        // check if product category is part of update
        if (productDTO.getCategoryId() != null && productDTO.getCategoryId() > 0) {

            Category category = categoryRepository.findById(productDTO.getCategoryId())
                    .orElseThrow(() -> new NotFoundException("Category not found"));

            existingProduct.setCategory(category);
        }

        // check and update other fields
        if (productDTO.getName() != null  && !productDTO.getName().isBlank()) {
            existingProduct.setName(productDTO.getName());
        }

        if (productDTO.getSku() != null  && !productDTO.getSku().isBlank()) {
            existingProduct.setSku(productDTO.getSku());
        }

        if (productDTO.getDescription() != null  && !productDTO.getDescription().isBlank()) {
            existingProduct.setDescription(productDTO.getDescription());
        }

        if (productDTO.getPrice() != null  && productDTO.getPrice().compareTo(BigDecimal.ZERO) >= 0) {
            existingProduct.setPrice(productDTO.getPrice());
        }
        if (productDTO.getStockQuantity() != null  && productDTO.getStockQuantity() >= 0) {
            existingProduct.setStockQuantity(BigInteger.valueOf(productDTO.getStockQuantity()));
        }

        productRepository.save(existingProduct);

        return ApiResponse.<ProductDTO>builder()
                .status(HttpStatus.OK.value())
                .message("product updated successfully")
                .build();

    }

    @Override
    public ApiResponse<List<ProductDTO>> getAllProduct() {
        List<Product> products = productRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));
        List<ProductDTO> productDTOS = modelMapper.map(products, new TypeToken<List<ProductDTO>>() {}.getType());

        return ApiResponse.<List<ProductDTO>>builder()
                .status(HttpStatus.OK.value())
                .message("success")
                .productDTOS(productDTOS)
                .build();
    }

    @Override
    public ApiResponse<Product> getProductById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Product not found"));

        return ApiResponse.<Product>builder()
                .status(HttpStatus.OK.value())
                .message("success")
                .product(product)
                .build();
    }

    @Override
    public ApiResponse<Void> deleteProduct(Long id) {
        productRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Product not found"));

        productRepository.deleteById(id);

        return ApiResponse.<Void>builder()
                .status(HttpStatus.OK.value())
                .message("product deleted successfully")
                .build();
    }


    private String saveImage(MultipartFile imageFile) {

        //validate image check
        if (!imageFile.getContentType().startsWith("image/")) {
            throw new IllegalArgumentException("Only image files are allowed");
        }

        // create the directory  to store images if it  doesn't exist
        File directory = new File(IMAGE_DIRECTORY);

        if(!directory.exists()) {
            directory.mkdir();
            log.info("directory was created");
        }

        //generate unique file name for each image
        String uniqueFileName = UUID.randomUUID() + "_" + imageFile.getOriginalFilename();

        // Get the absolute path of the image
        String imagePath = IMAGE_DIRECTORY + uniqueFileName;

        try {
            File destinationFile = new File(imagePath);
            imageFile.transferTo(destinationFile);
        }catch (Exception e) {
            throw new IllegalArgumentException("error while saving image");
        }
        return imagePath;

    }
}
