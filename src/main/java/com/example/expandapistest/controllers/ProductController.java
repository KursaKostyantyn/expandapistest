package com.example.expandapistest.controllers;

import com.example.expandapistest.models.dto.DynamicTable;
import com.example.expandapistest.services.impl.ProductServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/product")
public class ProductController {

    private final ProductServiceImpl productServiceImpl;

    @PostMapping("/add")
    public void createTable(@RequestBody DynamicTable dynamicTable) {
        productServiceImpl.createTableJdbc(dynamicTable);
    }

    @GetMapping("/all")
    public ResponseEntity<DynamicTable> getAllProducts() {
        return productServiceImpl.getAllProducts();
    }

}
