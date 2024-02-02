package com.example.pricetag.services.impl;

import com.example.pricetag.dto.CategoryDto;
import com.example.pricetag.dto.PaginationDto;
import com.example.pricetag.dto.ProductDto;
import com.example.pricetag.dto.SubCategoryDto;
import com.example.pricetag.entity.CartItem;
import com.example.pricetag.entity.Category;
import com.example.pricetag.entity.Product;
import com.example.pricetag.entity.SubCategory;
import com.example.pricetag.exceptions.ApplicationException;
import com.example.pricetag.repository.CartItemRepo;
import com.example.pricetag.repository.CategoryRepo;
import com.example.pricetag.repository.ProductRepo;
import com.example.pricetag.repository.SubCategoryRepo;
import com.example.pricetag.responses.CommonResponseDto;
import com.example.pricetag.services.CloudinaryService;
import com.example.pricetag.services.ProductService;
import com.example.pricetag.utils.ColorLogger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
    @Autowired
    private CloudinaryService cloudinaryService;
    @Autowired
    private CartItemRepo cartItemRepo;

    private static Product createNewProduct(ProductDto productDto, SubCategory filteredSubCategory, Category category) {
        Product newProduct = new Product();
        newProduct.setCategory(category);
        newProduct.setSubCategory(filteredSubCategory);
        newProduct.setName(productDto.getName());
        newProduct.setDescription(productDto.getDescription());
        newProduct.setActualPrice(productDto.getActualPrice());
        newProduct.setDiscountedPrice(productDto.getDiscountedPrice());
        if (productDto.getQuantity() > 0) {
            newProduct.setQuantity(productDto.getQuantity());
            newProduct.setIsInStock(true);
        }
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
                                        .quantity(newProduct.getQuantity())
                                        .images(newProduct.getImages())
                                        .isInStock(newProduct.getIsInStock())
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
    public CommonResponseDto getAllProducts(PaginationDto paginationDto) {
        try {
            if (paginationDto.getSortBy() == null) {
                paginationDto.setSortBy("createdAt");
            }
            if (paginationDto.getOrder() == null) {
                paginationDto.setOrder("desc");
            }
            Pageable pageable = PageRequest.of(paginationDto.getPage() - 1, paginationDto.getLimit(),
//                    Objects.equals(paginationDto.getOrder(), "asc")
                    paginationDto.getOrder().equals("asc") ? Sort.by(paginationDto.getSortBy()).ascending() : Sort.by(paginationDto.getSortBy()).descending());
            List<Product> products = productRepo.findAll(pageable).getContent();

            int productCount = productRepo.findAll().size();

            List<ProductDto> productDtoList = new ArrayList<>();
            products.forEach(product -> productDtoList.add(ProductDto.builder()
                    .productId(product.getId())
                    .name(product.getName())
                    .description(product.getDescription())
                    .actualPrice(product.getActualPrice())
                    .discountedPrice(product.getDiscountedPrice())
                    .category(CategoryDto
                            .builder()
                            .id(product.getCategory().getId())
                            .categoryName(product.getCategory().getCategoryName())
                            .createdAt(product.getCategory().getCreatedAt())
                            .updatedAt(product.getCategory().getUpdatedAt())
                            .build())
                    .subCategory(SubCategoryDto
                            .builder()
                            .id(product.getSubCategory().getId())
                            .subCategoryName(product.getSubCategory().getSubCategoryName())
                            .createdAt(product.getSubCategory().getCreatedAt())
                            .updatedAt(product.getSubCategory().getUpdatedAt())
                            .build())
                    .images(product.getImages())
                    .quantity(product.getQuantity())
                    .createdAt(product.getCreatedAt())
                    .updatedAt(product.getUpdatedAt())
                    .isInStock(product.getIsInStock())
                    .build()));

            return CommonResponseDto.builder()
                    .data(Map.of("results", productDtoList,
                            "pagination", Map.of("totalItems", productCount,
                                    "itemsPerPage", paginationDto.getLimit(),
                                    "totalPages", (int) Math.ceil((double) productCount / paginationDto.getLimit()),
                                    "currentPage", paginationDto.getPage(),
                                    "hasNext", paginationDto.getPage() < (int) Math.ceil((double) productCount / paginationDto.getLimit()),
                                    "hasPrevious", paginationDto.getPage() > 1
                            )))
                    .message("Product fetched successfully")
                    .success(true)
                    .build();
        } catch (Exception e) {
            ColorLogger.logError(e.toString());
            throw new ApplicationException("404", "Product not found", HttpStatus.NOT_FOUND);
        }
    }

    @Override
    public CommonResponseDto getProductsWithSubCategoryId(SubCategoryDto subCategoryDto, PaginationDto paginationDto)
            throws ApplicationException {

        Pageable pageable = PageRequest.of(paginationDto.getPage() - 1, paginationDto.getLimit());

        Optional<SubCategory> existingSubCategoryOptional = subCategoryRepo.findById(subCategoryDto.getId());


        if (existingSubCategoryOptional.isPresent()) {
//            SubCategory existingSubCategory = existingSubCategoryOptional.get();
            List<Product> product = productRepo.findAllBySubCategoryId(subCategoryDto.getId(), pageable);

            int productCount = productRepo.findAllBySubCategoryId(subCategoryDto.getId(), null).size();

//            product.forEach(d -> ColorLogger.logInfo("product :: " + d.getId()));

            return CommonResponseDto
                    .builder()
                    .message("Product fetch Successfully")
                    .data(Map.of("results", product,
                            "pagination", Map.of("totalItems", productCount,
                                    "itemsPerPage", paginationDto.getLimit(),
                                    "totalPages", (int) Math.ceil((double) productCount / paginationDto.getLimit()),
                                    "currentPage", paginationDto.getPage(),
                                    "hasNext", paginationDto.getPage() < (int) Math.ceil((double) productCount / paginationDto.getLimit()),
                                    "hasPrevious", paginationDto.getPage() > 1
                            )))
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
            product.getImages().forEach(image -> cloudinaryService.delete(image.getPublicId(),
                    "pricetag/" + product.getCategory().getCategoryName()
                            + "/"
                            + product.getSubCategory().getSubCategoryName()));

            CartItem existingCartItem = cartItemRepo.findByProductId(productId);
            if (existingCartItem != null) {
                cartItemRepo.delete(existingCartItem);
            }

            productRepo.delete(product);

            return CommonResponseDto.builder()
                    .message("Product has been deleted successfully")
                    .success(true)
                    .build();
        } else {
            throw new ApplicationException("404", "Product not found", HttpStatus.NOT_FOUND);
        }
    }


}
