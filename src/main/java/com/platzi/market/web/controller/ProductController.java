package com.platzi.market.web.controller;

import com.platzi.market.domain.Product;
import com.platzi.market.domain.service.ProductService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    @GetMapping("")
    @ApiOperation("Get All Supermarket Products")
    @ApiResponse(code = 200, message = "Ok")
    public ResponseEntity getAll() {
        return new ResponseEntity(productService.getAll(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @ApiOperation("Search a product by id")
    @ApiResponses({
            @ApiResponse(code = 200, message = "ok"),
            @ApiResponse(code = 404, message = "product not found")
    })
    public ResponseEntity getProduct(@ApiParam(value = "the id of the product", required = true, example = "7") @PathVariable("id") long productId) {
        return productService.getProduct(productId)
                .map(product -> new ResponseEntity(product, HttpStatus.OK))
                .orElse(new ResponseEntity(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/category/{id}")
    public ResponseEntity getByCategory(@PathVariable("id") long categoryId) {

        final Optional<List<Product>> products = productService.getByCategory(categoryId);
        if (products.isPresent() && !products.get().isEmpty()) {
            return new ResponseEntity(products.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("")
    public ResponseEntity save(@RequestBody Product product) {
        return new ResponseEntity(productService.save(product), HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity delete(@PathVariable("id") long productId) {
        if (productService.delete(productId)) {
            return new ResponseEntity(HttpStatus.OK);
        } else {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
    }

}
