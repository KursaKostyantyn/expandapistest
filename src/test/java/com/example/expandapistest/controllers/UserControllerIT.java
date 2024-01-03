package com.example.expandapistest.controllers;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@TestPropertySource("/application-test.properties")
@Sql(value = "/drop-user-table.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(value = "/create-user-schema.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@SpringBootTest
@AutoConfigureMockMvc(printOnlyOnFailure = false)
class UserControllerIT {
    @Autowired
    private MockMvc mockMvc;

    @Test
    public void test_addUser_returnsCreated() throws Exception {
        //given
        MockHttpServletRequestBuilder requestBuilder = post("/user/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                        "username":"John",
                        "password": "qwerty"
                        }
                        """);
        //when
        mockMvc.perform(requestBuilder)
                //then
                .andDo(print())
                .andExpect(status().isCreated());
    }

    @Test
    @Sql(value = "/create-user.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    public void test_addExistingUser_returnsConflict() throws Exception {
        //given
        MockHttpServletRequestBuilder requestBuilder = post("/user/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                        "username":"John",
                        "password": "qwerty"
                        }
                        """);
        //when
        mockMvc.perform(requestBuilder)
                //then
                .andDo(print())
                .andExpectAll(
                        status().isConflict(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        content().json("""
                                {
                                "errorCode": 409,
                                "errorDescription": "User with username 'John' already exists"
                                }
                                """)
                )
        ;
    }

    @Test
    @Sql(value = "/create-user.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    public void test_authenticateUserValidData_returnsOk() throws Exception {
        //given
        MockHttpServletRequestBuilder requestBuilder = post("/user/authenticate")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                            "username": "John",
                            "password": "qwerty"
                        }
                        """);

        //when
        mockMvc.perform(requestBuilder)
                //then
                .andDo(print())
                .andExpectAll(
                        status().isOk(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        jsonPath("$.accessToken").exists(),
                        jsonPath("$.accessToken").isNotEmpty()
                );
    }

    @Test
    @Sql(value = "/create-user.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    public void test_authenticateUserInvalidUsername_returnsUnauthorized() throws Exception {
        //given
        MockHttpServletRequestBuilder requestBuilder = post("/user/authenticate")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                            "username": "Sem",
                            "password": "qwerty"
                        }
                        """);

        //when
        mockMvc.perform(requestBuilder)
                //then
                .andDo(print())
                .andExpectAll(
                        status().isUnauthorized(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        content().json("""
                                {
                                    "errorCode": 401,
                                    "errorDescription": "Дані користувача некоректні"
                                }
                                """)
                );
    }

    @Test
    @Sql(value = "/create-user.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    public void test_authenticateUserInvalidPassword_returnsUnauthorized() throws Exception {
        //given
        MockHttpServletRequestBuilder requestBuilder = post("/user/authenticate")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                            "username": "John",
                            "password": "12345"
                        }
                        """);

        //when
        mockMvc.perform(requestBuilder)
                //then
                .andDo(print())
                .andExpectAll(
                        status().isUnauthorized(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        content().json("""
                                {
                                    "errorCode": 401,
                                    "errorDescription": "Дані користувача некоректні"
                                }
                                """)
                );
    }

}