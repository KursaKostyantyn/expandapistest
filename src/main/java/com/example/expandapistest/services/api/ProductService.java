package com.example.expandapistest.services.api;

import com.example.expandapistest.models.dto.DynamicTable;
import org.springframework.http.ResponseEntity;

public interface ProductService {
    public void createTableJdbc(DynamicTable dynamicTable);
    public ResponseEntity<DynamicTable> getAllProducts();
}
