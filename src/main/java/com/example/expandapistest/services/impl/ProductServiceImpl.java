package com.example.expandapistest.services.impl;

import com.example.expandapistest.exception.NotFoundException;
import com.example.expandapistest.exception.TableExistsException;
import com.example.expandapistest.models.dto.DynamicTable;
import com.example.expandapistest.services.api.ProductService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.BadSqlGrammarException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ProductServiceImpl implements ProductService {

    private JdbcTemplate jdbcTemplate;

    @Override
    public void createTableJdbc(DynamicTable dynamicTable) {
        String tableName = dynamicTable.getTable();
        Map<String, String> column = dynamicTable.getRecords().get(0);
        StringBuilder createTableQuery = new StringBuilder("CREATE TABLE " + tableName + " (id INT AUTO_INCREMENT PRIMARY KEY, ");
        for (Map.Entry<String, String> entry : column.entrySet()) {
            createTableQuery
                    .append(entry.getKey())
                    .append(" VARCHAR(255), ");
        }
        createTableQuery.delete(createTableQuery.length() - 2, createTableQuery.length());
        createTableQuery.append(")");
        try {
            jdbcTemplate.execute(createTableQuery.toString());
            dynamicTable.getRecords().forEach(map -> insertDataIntoTable(tableName, map));
        } catch (BadSqlGrammarException e) {
            throw new TableExistsException(String.format("Table already exists tableName: '%s'", tableName));
        }

    }

    private void insertDataIntoTable(String tableName, Map<String, String> column) {
        StringBuilder insertDataQuery = new StringBuilder("INSERT INTO " + tableName + " (");
        for (Map.Entry<String, String> entry : column.entrySet()) {
            insertDataQuery.append(entry.getKey()).append(", ");
        }
        insertDataQuery.delete(insertDataQuery.length() - 2, insertDataQuery.length());
        insertDataQuery.append(") VALUES (?, ?, ?, ?, ?)");
        jdbcTemplate.update(insertDataQuery.toString(), column.values().toArray());
    }

    @Override
    public ResponseEntity<DynamicTable> getAllProducts() {
        String tableName = "products";
        try {
            String getDataQuery = "SELECT * FROM " + tableName.toUpperCase();
            List<Map<String, Object>> rows = jdbcTemplate.queryForList(getDataQuery);
            List<Map<String, String>> records = rows.stream()
                    .map(row -> row.entrySet().stream()
                            .collect(Collectors.toMap(
                                    Map.Entry::getKey,
                                    entry -> entry.getValue() != null ? entry.getValue().toString() : ""))).toList();
            DynamicTable dynamicTable = new DynamicTable(tableName, records);
            return ResponseEntity.ok(dynamicTable);
        } catch (BadSqlGrammarException e) {
            throw new NotFoundException(String.format("Table '%s' doesn't exist", tableName));
        }
    }
}
