package com.example.pricetag.features.product.service.impl;

import com.example.pricetag.dto.CommonResponseDto;
import com.example.pricetag.dto.PaginatedResponseDto;
import com.example.pricetag.dto.PaginationDto;
import com.example.pricetag.dto.SubCategoryDto;
import com.example.pricetag.entity.CartItem;
import com.example.pricetag.exceptions.ApplicationException;
import com.example.pricetag.features.category.dto.response.CategoryResponseDto;
import com.example.pricetag.features.category.entity.Category;
import com.example.pricetag.features.category.repository.CategoryRepo;
import com.example.pricetag.features.product.dto.ProductDto;
import com.example.pricetag.features.product.dto.response.ProductResponseDto;
import com.example.pricetag.features.product.entity.Product;
import com.example.pricetag.features.product.mapper.ProductMapper;
import com.example.pricetag.features.product.repository.ProductRepo;
import com.example.pricetag.features.product.service.ProductService;
import com.example.pricetag.features.subcategory.entity.SubCategory;
import com.example.pricetag.features.subcategory.repository.SubCategoryRepo;
import com.example.pricetag.repository.CartItemRepo;
import com.example.pricetag.utils.ColorLogger;
import com.example.pricetag.utils.PageableBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepo productRepo;
    private final CategoryRepo categoryRepo;
    private final SubCategoryRepo subCategoryRepo;
    private final CartItemRepo cartItemRepo;
    private final ProductMapper productMapper;

    private static Product createNewProduct(ProductDto productDto, SubCategory filteredSubCategory, Category category) {
        Product newProduct = new Product();
        newProduct.setCategory(category);
        newProduct.setSubCategory(filteredSubCategory);
        newProduct.setName(productDto.getName());
        newProduct.setDescription(productDto.getDescription());
        newProduct.setVariants(productDto.getVariants());

        return newProduct;
    }

    @Override
    public CommonResponseDto<List<ProductResponseDto>> getProductsWithSubCategoryId(SubCategoryDto subCategoryDto,
                                                                                    PaginationDto paginationDto) {
        Pageable pageable = PageableBuilder.buildPageable(paginationDto);
        if (subCategoryRepo.existsSubCategoryById(subCategoryDto.getId())) {
            Page<Product> existingProducts = productRepo.findAllBySubCategoryIdAndIsActiveTrue(subCategoryDto.getId(),
                                                                                               pageable);
            List<ProductResponseDto> productResponseDtoList = productMapper.mapProductListToProductResponseDtoList(
                    existingProducts.getContent());

            return CommonResponseDto
                    .<List<ProductResponseDto>>builder()
                    .message("Products fetch Successfully")
                    .data(productResponseDtoList)
                    .pagination(PaginatedResponseDto.from(existingProducts))
                    .success(true)
                    .status(HttpStatus.OK.value())
                    .build();
        } else {
            throw new ApplicationException("404", "Sub Category not found", HttpStatus.NOT_FOUND);
        }
    }

    @Override
    public CommonResponseDto createProduct(ProductDto productDto) {
        Optional<Category> categoryOptional = categoryRepo.findById(productDto
                                                                            .getCategory()
                                                                            .getId());

        if (categoryOptional.isPresent()) {
            Category category = categoryOptional.get();

            Optional<SubCategory> subCategoryOptional = category
                    .getSubCategories()
                    .stream()
                    .filter(subCategory -> subCategory
                            .getId()
                            .equals(productDto
                                            .getSubCategory()
                                            .getId()))
                    .findFirst();

            if (subCategoryOptional.isPresent()) {
                Product newProduct = createNewProduct(productDto, subCategoryOptional.get(), category);
                productRepo.save(newProduct);

                return CommonResponseDto
                        .builder()
                        .data(Map.of("results", ProductDto
                                .builder()
                                .productId(newProduct.getId())
                                .variants(newProduct.getVariants())
                                .description(newProduct.getDescription())
                                .name(newProduct.getName())
                                .category(CategoryResponseDto
                                                  .builder()
                                                  .id(newProduct
                                                              .getCategory()
                                                              .getId())
                                                  .name(newProduct
                                                                .getCategory()
                                                                .getCategoryName())
                                                  .build())
                                .subCategory(SubCategoryDto
                                                     .builder()
                                                     .id(newProduct
                                                                 .getSubCategory()
                                                                 .getId())
                                                     .subCategoryName(newProduct
                                                                              .getSubCategory()
                                                                              .getSubCategoryName())
                                                     .build())
//                                .images(newProduct.getImages())
                                .createdAt(newProduct.getCreatedAt())
                                .updatedAt(newProduct.getUpdatedAt())
                                .build()))
                        .message("Product has been created successfully")
                        .success(true)
                        .build();
            } else {
                throw new ApplicationException("404", "SubCategory is not present inside category",
                                               HttpStatus.NOT_FOUND);
            }
        } else {
            throw new ApplicationException("404", "Category not found", HttpStatus.NOT_FOUND);
        }
    }

    @Override
    public CommonResponseDto<List<ProductResponseDto>> getAllProducts(PaginationDto paginationDto) {
        Pageable pageable = PageableBuilder.buildPageable(paginationDto);

        Page<Product> existingProductList = productRepo.findAllByIsActiveTrue(pageable);

        List<ProductResponseDto> productResponseDtoList = productMapper.mapProductListToProductResponseDtoList(
                existingProductList.getContent());

        return CommonResponseDto
                .<List<ProductResponseDto>>builder()
                .message("Products fetched successfully")
                .data(productResponseDtoList)
                .pagination(PaginatedResponseDto.from(existingProductList))
                .success(true)
                .status(HttpStatus.OK.value())
                .build();

    }

    @Override
    public CommonResponseDto getSingleProduct(Long productId) {
        try {
            Product product = productRepo
                    .findById(productId)
                    .orElseThrow(() -> new ApplicationException("404", "Product not found", HttpStatus.NOT_FOUND));
            if (product != null) {
                ColorLogger.logInfo(product.toString());
                return CommonResponseDto
                        .builder()
                        .success(true)
                        .message("Product fetched successfully")
                        .data(Map.of("results", product))
                        .build();
            } else {
                return CommonResponseDto
                        .builder()
                        .success(false)
                        .message("Product not found")
                        .data(null)
                        .build();
            }
        } catch (Exception e) {
            ColorLogger.logError(e.toString());
            throw new ApplicationException("404", "Product not found", HttpStatus.NOT_FOUND);
        }
    }


    @Override
    public CommonResponseDto<List<ProductResponseDto>> getSearchProductsWithSubCategoryIdAndName(
            SubCategoryDto subCategoryDto, PaginationDto paginationDto, String name) throws ApplicationException {
        Pageable pageable = PageableBuilder.buildPageable(paginationDto);
        if (subCategoryRepo.existsSubCategoryById(subCategoryDto.getId())) {
            Page<Product> productsList = productRepo.findAllBySubCategoryIdAndNameContainingIgnoreCase(
                    subCategoryDto.getId(), pageable, name);

            List<ProductResponseDto> productResponseDtoList = productMapper.mapProductListToProductResponseDtoList(
                    productsList.getContent());
            
            return CommonResponseDto
                    .<List<ProductResponseDto>>builder()
                    .message("Search products fetch Successfully")
                    .data(productResponseDtoList)
                    .pagination(PaginatedResponseDto.from(productsList))
                    .status(HttpStatus.OK.value())
                    .success(true)
                    .build();
        } else {
            throw new ApplicationException("404", "Sub Category not found", HttpStatus.NOT_FOUND);
        }
    }

    @Override
    public CommonResponseDto deleteProductById(Long productId) {
        Optional<Product> productOptional = productRepo.findById(productId);
        if (productOptional.isPresent()) {
            Product product = productOptional.get();
//            product.getImages()
//                    .forEach(image -> cloudinaryService.deleteFile(image.getPublicId(), "pricetag/" + product.getCategory()
//                            .getCategoryName() + "/" + product.getSubCategory().getSubCategoryName()));

            CartItem existingCartItem = cartItemRepo.findByProductId(productId);
            if (existingCartItem != null) {
                cartItemRepo.delete(existingCartItem);
            }

            productRepo.delete(product);

            return CommonResponseDto
                    .builder()
                    .message("Product has been deleted successfully")
                    .success(true)
                    .build();
        } else {
            throw new ApplicationException("404", "Product not found", HttpStatus.NOT_FOUND);
        }
    }

    @Override
    public CommonResponseDto editProduct(ProductDto productDto) {
        Optional<Product> productOptional = productRepo.findById(productDto.getProductId());

        if (productOptional.isPresent()) {
            Product product = productOptional.get();
            product.setName(productDto.getName());
            product.setDescription(productDto.getDescription());
            if (productDto.getCategory() != null && productDto
                                                            .getCategory()
                                                            .getId() != null) {
                product.setCategory(categoryRepo
                                            .findById(productDto
                                                              .getCategory()
                                                              .getId())
                                            .orElseThrow(() -> new ApplicationException("404", "Category not found",
                                                                                        HttpStatus.NOT_FOUND)));
            }
            if (productDto.getSubCategory() != null && productDto
                                                               .getSubCategory()
                                                               .getId() != null) {
                product.setSubCategory(subCategoryRepo
                                               .findById(productDto
                                                                 .getSubCategory()
                                                                 .getId())
                                               .orElseThrow(
                                                       () -> new ApplicationException("404", "Sub Category not found",
                                                                                      HttpStatus.NOT_FOUND)));
            }

            try {
                productRepo.save(product);
                return CommonResponseDto
                        .builder()
                        .message("Product has been updated successfully")
                        .success(true)
                        .data(Map.of("results", product))
                        .build();
            } catch (DataAccessException e) {
                ColorLogger.logError("editProduct :: Database error :: " + e.getMessage());
                throw new ApplicationException("500", "Database error", HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } else {
            throw new ApplicationException("404", "Product not found", HttpStatus.NOT_FOUND);
        }
    }


}
