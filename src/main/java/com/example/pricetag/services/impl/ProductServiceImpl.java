package com.example.pricetag.services.impl;

import com.example.pricetag.dto.CategoryDto;
import com.example.pricetag.dto.ProductDto;
import com.example.pricetag.dto.SubCategoryDto;
import com.example.pricetag.entity.Category;
import com.example.pricetag.entity.Product;
import com.example.pricetag.entity.SubCategory;
import com.example.pricetag.exceptions.ApplicationException;
import com.example.pricetag.repository.CategoryRepo;
import com.example.pricetag.repository.ProductRepo;
import com.example.pricetag.repository.SubCategoryRepo;
import com.example.pricetag.responses.CommonResponseDto;
import com.example.pricetag.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepo productRepo;
    @Autowired
    private CategoryRepo categoryRepo;
    @Autowired
    private SubCategoryRepo subCategoryRepo;

    private static Product createNewProduct(ProductDto productDto, SubCategory filteredSubCategory, Category category) {
        Product newProduct = new Product();
        newProduct.setCategory(category);
        newProduct.setSubCategory(filteredSubCategory);
        newProduct.setName(productDto.getName());
        newProduct.setDescription(productDto.getDescription());
        newProduct.setActualPrice(productDto.getActualPrice());
        newProduct.setDiscountedPrice(productDto.getDiscountedPrice());
        return newProduct;
    }

    @Override
    public CommonResponseDto createProduct(ProductDto productDto) {
        Optional<Category> categoryOptional = categoryRepo.findById(productDto.getCategory().getId());

        if (categoryOptional.isPresent()) {
            Category category = categoryOptional.get();

            Optional<SubCategory> subCategoryOptional = category.getSubCategories().stream()
                    .filter(subCategory -> subCategory.getId().equals(productDto.getSubCategory().getId()))
                    .findFirst();

            if (subCategoryOptional.isPresent()) {
                Product newProduct = createNewProduct(productDto, subCategoryOptional.get(), category);
                productRepo.save(newProduct);

                return CommonResponseDto.builder()
                        .data(Map.of("results",
                                ProductDto
                                        .builder()
                                        .productId(newProduct.getId())
                                        .actualPrice(newProduct.getActualPrice())
                                        .discountedPrice(newProduct.getDiscountedPrice())
                                        .description(newProduct.getDescription())
                                        .name(newProduct.getName())
                                        .category(CategoryDto
                                                .builder()
                                                .id(newProduct.getCategory().getId())
                                                .categoryName(newProduct.getCategory().getCategoryName())
                                                .build())
                                        .subCategory(SubCategoryDto
                                                .builder()
                                                .id(newProduct.getSubCategory().getId())
                                                .subCategoryName(newProduct.getSubCategory().getSubCategoryName())
                                                .build())
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
    public CommonResponseDto getAllProducts() {
        List<Product> products = productRepo.findAll();
        List<ProductDto> productDtoList = new ArrayList<>();
        products.forEach(product -> {
            productDtoList.add(ProductDto.builder()
                    .productId(product.getId())
                    .name(product.getName())
                    .description(product.getDescription())
                    .actualPrice(product.getActualPrice())
                    .discountedPrice(product.getDiscountedPrice())
                    .category(CategoryDto
                            .builder()
                            .id(product.getCategory().getId())
                            .categoryName(product.getCategory().getCategoryName())
                            .build())
                    .subCategory(SubCategoryDto
                            .builder()
                            .id(product.getSubCategory().getId())
                            .subCategoryName(product.getSubCategory().getSubCategoryName())
                            .build())
                    .build());
        });
        return CommonResponseDto.builder()
                .data(Map.of("results", productDtoList))
                .message("Product fetched successfully")
                .success(true)
                .build();
    }

    @Override
    public CommonResponseDto deleteProductById(Long productId) {
        Optional<Product> productOptional = productRepo.findById(productId);
        if (productOptional.isPresent()) {
            Product product = productOptional.get();
            productRepo.delete(product);

            return CommonResponseDto.builder()
                    .data(product)
                    .message("Product has been deleted successfully")
                    .success(true)
                    .build();
        } else {
            throw new ApplicationException("404", "Product not found", HttpStatus.NOT_FOUND);
        }
    }

    

}
