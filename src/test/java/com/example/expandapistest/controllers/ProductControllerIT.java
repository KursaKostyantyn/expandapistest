package com.example.expandapistest.controllers;

import com.example.expandapistest.utils.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@TestPropertySource("/application-test.properties")
@Sql(value = "/create-user-schema.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(value = "/create-user.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(value = "/drop-products-table.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
@SpringBootTest
@AutoConfigureMockMvc(printOnlyOnFailure = false)
class ProductControllerIT {

    private static final String USERNAME = "John";
    @Autowired
    MockMvc mockMvc;

    private String token;

    @BeforeEach
    void setUp() {
        token = JwtUtil.generateToken(USERNAME);
    }

    @Test
    void test_createTableIsAccessible_returnsForbidden() throws Exception {
        MockHttpServletRequestBuilder requestBuilder = post("/product/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                        "table" : "products",
                        "records" : [
                        {
                        "entryDate": "03-01-2023",
                        "itemCode": "11111",
                        "itemName": "Test Inventory 1",
                        "itemQuantity": "20",
                        "status": "Paid"
                        },
                        {
                        "entryDate": "03-01-2023",
                        "itemCode": "11111",
                        "itemName": "Test Inventory 2",
                        "itemQuantity": "20",
                        "status": "Paid"
                        }] }
                        """);
        //when
        mockMvc.perform(requestBuilder)
                //then
                .andDo(print())
                .andExpectAll(
                        status().isForbidden()
                );
    }


    @Test
    void test_createTable_returnsOk() throws Exception {
        //given
        MockHttpServletRequestBuilder requestBuilder = post("/product/add")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + token)
                .content("""
                        {
                        "table" : "products",
                        "records" : [
                        {
                        "entryDate": "03-01-2023",
                        "itemCode": "11111",
                        "itemName": "Test Inventory 1",
                        "itemQuantity": "20",
                        "status": "Paid"
                        },
                        {
                        "entryDate": "03-01-2023",
                        "itemCode": "11111",
                        "itemName": "Test Inventory 2",
                        "itemQuantity": "20",
                        "status": "Paid"
                        }] }
                        """);
        //when
        mockMvc.perform(requestBuilder)
                //then
                .andDo(print())
                .andExpectAll(
                        status().isOk()
                );
    }

    @Test
    @Sql(value = "/create-products-table.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = "/create-products.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void test_getAllProducts_returnsOkAndListProducts() throws Exception {
        //given
        MockHttpServletRequestBuilder requestBuilder = get("/product/all")
                .header("Authorization", "Bearer " + token);

        //when
        mockMvc.perform(requestBuilder)
                //then
                .andDo(print())
                .andExpectAll(
                        status().isOk(),
                        content().json("""
                                  {
                                                        "table" : "products",
                                                        "records" : [
                                                        {
                                                        "entryDate": "03-01-2023",
                                                        "itemCode": "11111",
                                                        "itemName": "Test Inventory 1",
                                                        "itemQuantity": "20",
                                                        "status": "Paid"
                                                        },
                                                        {
                                                        "entryDate": "03-01-2023",
                                                        "itemCode": "11111",
                                                        "itemName": "Test Inventory 2",
                                                        "itemQuantity": "20",
                                                        "status": "Paid"
                                                        }] }
                                """)
                );
    }

    @Test
    void test_getAllProductsIsAccessible_returnsForbidden() throws Exception {
        //given
        MockHttpServletRequestBuilder requestBuilder = get("/product/all");
        //when
        mockMvc.perform(requestBuilder)
                //then
                .andDo(print())
                .andExpect(status().isForbidden());
    }
}