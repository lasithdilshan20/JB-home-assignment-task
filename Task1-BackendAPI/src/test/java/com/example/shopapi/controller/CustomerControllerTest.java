package com.example.shopapi.controller;

import com.example.shopapi.model.Customer;
import com.example.shopapi.service.CustomerService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CustomerController.class)
public class CustomerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CustomerService customerService;

    @Autowired
    private ObjectMapper objectMapper;

    private Customer customer1;
    private Customer customer2;
    private List<Customer> customers;

    @BeforeEach
    void setUp() {
        customer1 = new Customer(1L, "John", "Doe", "john.doe@example.com");
        customer2 = new Customer(2L, "Jane", "Smith", "jane.smith@example.com");
        customers = Arrays.asList(customer1, customer2);
    }

    @Test
    void getAllCustomers_ShouldReturnAllCustomers() throws Exception {
        when(customerService.getAllCustomers()).thenReturn(customers);

        mockMvc.perform(get("/api/customers"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].name", is("John")))
                .andExpect(jsonPath("$[0].surname", is("Doe")))
                .andExpect(jsonPath("$[0].email", is("john.doe@example.com")))
                .andExpect(jsonPath("$[1].id", is(2)))
                .andExpect(jsonPath("$[1].name", is("Jane")))
                .andExpect(jsonPath("$[1].surname", is("Smith")))
                .andExpect(jsonPath("$[1].email", is("jane.smith@example.com")));

        verify(customerService, times(1)).getAllCustomers();
    }

    @Test
    void getCustomerById_WithValidId_ShouldReturnCustomer() throws Exception {
        when(customerService.getCustomerById(1L)).thenReturn(customer1);

        mockMvc.perform(get("/api/customers/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("John")))
                .andExpect(jsonPath("$.surname", is("Doe")))
                .andExpect(jsonPath("$.email", is("john.doe@example.com")));

        verify(customerService, times(1)).getCustomerById(1L);
    }

    @Test
    void createCustomer_WithValidData_ShouldReturnCreatedCustomer() throws Exception {
        Customer newCustomer = new Customer(null, "Bob", "Johnson", "bob.johnson@example.com");
        Customer savedCustomer = new Customer(3L, "Bob", "Johnson", "bob.johnson@example.com");

        when(customerService.createCustomer(any(Customer.class))).thenReturn(savedCustomer);

        mockMvc.perform(post("/api/customers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newCustomer)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(3)))
                .andExpect(jsonPath("$.name", is("Bob")))
                .andExpect(jsonPath("$.surname", is("Johnson")))
                .andExpect(jsonPath("$.email", is("bob.johnson@example.com")));

        verify(customerService, times(1)).createCustomer(any(Customer.class));
    }

    @Test
    void updateCustomer_WithValidData_ShouldReturnUpdatedCustomer() throws Exception {
        Customer updatedCustomer = new Customer(1L, "John", "Updated", "john.updated@example.com");

        when(customerService.updateCustomer(eq(1L), any(Customer.class))).thenReturn(updatedCustomer);

        mockMvc.perform(put("/api/customers/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedCustomer)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("John")))
                .andExpect(jsonPath("$.surname", is("Updated")))
                .andExpect(jsonPath("$.email", is("john.updated@example.com")));

        verify(customerService, times(1)).updateCustomer(eq(1L), any(Customer.class));
    }

    @Test
    void deleteCustomer_WithValidId_ShouldReturnNoContent() throws Exception {
        doNothing().when(customerService).deleteCustomer(1L);

        mockMvc.perform(delete("/api/customers/1"))
                .andExpect(status().isNoContent());

        verify(customerService, times(1)).deleteCustomer(1L);
    }

    @Test
    void createCustomer_WithInvalidData_ShouldReturnBadRequest() throws Exception {
        Customer invalidCustomer = new Customer(null, "", "", "invalid-email");

        mockMvc.perform(post("/api/customers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidCustomer)))
                .andExpect(status().isBadRequest());

        verify(customerService, never()).createCustomer(any(Customer.class));
    }
}