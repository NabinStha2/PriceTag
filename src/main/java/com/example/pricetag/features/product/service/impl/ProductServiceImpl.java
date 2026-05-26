package com.example.pricetag.features.product.service.impl;

import com.example.pricetag.dto.CommonResponseDto;
import com.example.pricetag.dto.PaginatedResponseDto;
import com.example.pricetag.dto.PaginationDto;
import com.example.pricetag.dto.SubCategoryDto;
import com.example.pricetag.entity.CartItem;
import com.example.pricetag.exceptions.ApplicationException;
import com.example.pricetag.features.category.entity.Category;
import com.example.pricetag.features.category.repository.CategoryRepo;
import com.example.pricetag.features.product.dto.request.CreateProductRequestDto;
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
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Locale;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepo productRepo;
    private final CategoryRepo categoryRepo;
    private final SubCategoryRepo subCategoryRepo;
    private final CartItemRepo cartItemRepo;
    private final ProductMapper productMapper;

    private static Product createNewProduct(CreateProductRequestDto createProductRequestDto,
                                            SubCategory filteredSubCategory, Category category, String slug) {
        Product newProduct = new Product();
        newProduct.setCategory(category);
        newProduct.setSubCategory(filteredSubCategory);
        newProduct.setName(createProductRequestDto.getName());
        newProduct.setSlug(slug);
        newProduct.setDescription(createProductRequestDto.getDescription());
        newProduct.setShortDescription(createProductRequestDto.getShortDescription());
        newProduct.setBrand(createProductRequestDto.getBrand());
        newProduct.setBasePrice(createProductRequestDto.getBasePrice());
        newProduct.setDiscountedPrice(createProductRequestDto.getDiscountedPrice());

//        if (productDto.getVariants() != null) {
//            productDto
//                    .getVariants()
//                    .forEach(variant -> variant.setProduct(newProduct));
//            newProduct.setVariants(productDto.getVariants());
//        }

        return newProduct;
    }

    private static String createSlug(String value) {
        return value
                .toLowerCase(Locale.ROOT)
                .trim()
                .replaceAll("[^a-z0-9]+", "-")
                .replaceAll("(^-|-$)", "");
    }

    private static void validateCreateProduct(CreateProductRequestDto createProductRequestDto) {
        if (createProductRequestDto.getName() == null || createProductRequestDto
                .getName()
                .isBlank()) {
            throw new ApplicationException("400", "Product name is required", HttpStatus.BAD_REQUEST);
        }

        if (createProductRequestDto.getDescription() == null || createProductRequestDto
                .getDescription()
                .isBlank()) {
            throw new ApplicationException("400", "Product description is required", HttpStatus.BAD_REQUEST);
        }

        if (createProductRequestDto.getBasePrice() == null) {
            throw new ApplicationException("400", "Product base price is required", HttpStatus.BAD_REQUEST);
        }
    }

    private String buildUniqueSlug(String name) {
        String baseSlug = createSlug(name);
        if (baseSlug.isBlank()) {
            baseSlug = "product";
        }

        String slug = baseSlug;
        int counter = 1;

        while (productRepo.existsBySlug(slug)) {
            slug = baseSlug + "-" + counter;
            counter++;
        }

        return slug;
    }

    private String resolveSlug(CreateProductRequestDto createProductRequestDto) {
        if (createProductRequestDto.getSlug() == null || createProductRequestDto
                .getSlug()
                .isBlank()) {
            return buildUniqueSlug(createProductRequestDto.getName());
        }

        String slug = createSlug(createProductRequestDto.getSlug());
        if (slug.isBlank()) {
            throw new ApplicationException("400", "Product slug is invalid", HttpStatus.BAD_REQUEST);
        }

        if (productRepo.existsBySlug(slug)) {
            throw new ApplicationException("409", "Product slug already exists", HttpStatus.CONFLICT);
        }

        return slug;
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
    @Transactional
    public CommonResponseDto<ProductResponseDto> createProduct(CreateProductRequestDto createProductRequestDto) {
        Long categoryId = createProductRequestDto
                .getCategory()
                .getId();
        Long subCategoryId = createProductRequestDto
                .getSubCategory()
                .getId();

        validateCreateProduct(createProductRequestDto);

        Category existingCategory = categoryRepo
                .findById(categoryId)
                .orElseThrow(() -> new ApplicationException("404", "Category not found", HttpStatus.NOT_FOUND));

        SubCategory existingSubCategory = subCategoryRepo
                .findByIdAndCategoryId(subCategoryId, categoryId)
                .orElseThrow(() -> new ApplicationException("404", "Sub Category not found in the specified category",
                                                            HttpStatus.NOT_FOUND));

        String slug = resolveSlug(createProductRequestDto);
        Product newProduct = createNewProduct(createProductRequestDto, existingSubCategory, existingCategory, slug);
        Product savedProduct = productRepo.save(newProduct);
        ProductResponseDto productResponseDto = productMapper.mapProductToProductResponseDto(savedProduct);

        return CommonResponseDto
                .<ProductResponseDto>builder()
                .data(productResponseDto)
                .message("Product has been created successfully")
                .status(HttpStatus.CREATED.value())
                .success(true)
                .build();
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
    public CommonResponseDto<Void> deleteProductById(Long productId) {
        Product existingProduct = productRepo
                .findByIdAndIsActiveTrue(productId)
                .orElseThrow(() -> new ApplicationException("404", "Product not found", HttpStatus.NOT_FOUND));
//            product.getImages()
//                    .forEach(image -> cloudinaryService.deleteFile(image.getPublicId(), "pricetag/" + product.getCategory()
//                            .getCategoryName() + "/" + product.getSubCategory().getSubCategoryName()));

        CartItem existingCartItem = cartItemRepo.findByProductId(productId);
        if (existingCartItem != null) {
            cartItemRepo.delete(existingCartItem);
        }

//        productRepo.delete(existingProduct);

        existingProduct.setIsActive(false);
        existingProduct.setIsDeleted(true);
        productRepo.save(existingProduct);

        return CommonResponseDto
                .<Void>builder()
                .message("Product has been deleted successfully")
                .status(HttpStatus.OK.value())
                .success(true)
                .build();
    }

    @Override
    @Transactional
    public CommonResponseDto<ProductResponseDto> editProduct(CreateProductRequestDto createProductRequestDto) {
        Product existingProduct = productRepo
                .findByIdAndIsActiveTrue(createProductRequestDto.getProductId())
                .orElseThrow(() -> new ApplicationException("404", "Product not found", HttpStatus.NOT_FOUND));

        existingProduct.setName(createProductRequestDto.getName());
        existingProduct.setDescription(createProductRequestDto.getDescription());

        if (createProductRequestDto.getCategory() != null && createProductRequestDto
                                                                     .getCategory()
                                                                     .getId() != null) {
            existingProduct.setCategory(categoryRepo
                                                .findById(createProductRequestDto
                                                                  .getCategory()
                                                                  .getId())
                                                .orElseThrow(() -> new ApplicationException("404", "Category not found",
                                                                                            HttpStatus.NOT_FOUND)));
        }
        if (createProductRequestDto.getSubCategory() != null && createProductRequestDto
                                                                        .getSubCategory()
                                                                        .getId() != null) {
            existingProduct.setSubCategory(subCategoryRepo
                                                   .findById(createProductRequestDto
                                                                     .getSubCategory()
                                                                     .getId())
                                                   .orElseThrow(() -> new ApplicationException("404",
                                                                                               "Sub Category not found",
                                                                                               HttpStatus.NOT_FOUND)));
        }

        try {
            productRepo.save(existingProduct);
            return CommonResponseDto
                    .<ProductResponseDto>builder()
                    .message("Product has been updated successfully")
                    .success(true)
                    .status(HttpStatus.OK.value())
                    .build();
        } catch (DataAccessException e) {
            ColorLogger.logError("editProduct :: Database error :: " + e.getMessage());
            throw new ApplicationException("500", "Database error", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


}
