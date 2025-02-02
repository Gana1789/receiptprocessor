package com.challenge.receiptprocessor.controller;

import com.challenge.receiptprocessor.model.Item;
import com.challenge.receiptprocessor.model.Receipt;
import com.challenge.receiptprocessor.service.ReceiptService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

class ProcessReceiptControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private ReceiptService receiptService;

    @InjectMocks
    private ProcessReceiptController processReceiptController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(processReceiptController).build();
    }

    @Test
    void testProcessReceipt_Success() throws Exception {

        Long receiptId = 1L;


        when(receiptService.saveReceipt(any(Receipt.class))).thenReturn(String.valueOf(receiptId));

        mockMvc.perform(post("/receipts/process")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"retailer\":\"RetailerName\",\"purchaseDate\":\"2025-02-02\",\"purchaseTime\":\"12:00\",\"items\":[{\"name\":\"item1\",\"price\":10.0}]}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(receiptId));
    }

    @Test
    void testProcessReceipt_InternalServerError() throws Exception {

        when(receiptService.saveReceipt(any(Receipt.class))).thenThrow(new RuntimeException("Error saving receipt"));


        mockMvc.perform(post("/receipts/process")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"retailer\":\"RetailerName\",\"purchaseDate\":\"2025-02-02\",\"purchaseTime\":\"12:00\",\"items\":[{\"name\":\"item1\",\"price\":10.0}]}"))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void testGetReceiptPoints_Success() throws Exception {

        Long receiptId = 1L;
        Receipt receiptRequest = Receipt.builder().retailer("RetailerName").purchaseDate( "2025-02-02").purchaseTime("12:00").total("10").items( List.of(Item.builder().price("10.0").shortDescription("item1").build())).build();
        when(receiptService.getReceiptById(anyLong())).thenReturn(Optional.of(receiptRequest));
        when(receiptService.calculatePoints(any(Receipt.class))).thenReturn(100); // Mock points calculation


        mockMvc.perform(get("/receipts/{id}/points", receiptId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.points").value(100));
    }

    @Test
    void testGetReceiptPoints_NotFound() throws Exception {

        Long receiptId = 1L;


        when(receiptService.getReceiptById(anyLong())).thenReturn(Optional.empty());


        mockMvc.perform(get("/receipts/{id}/points", receiptId))
                .andExpect(status().isNotFound()).andExpect(e-> Arrays.toString(e.getResponse().getContentAsByteArray()).equals("No receipt found for that ID."));

    }

    @Test
    void testGetReceiptPoints_InternalServerError() throws Exception {

        Long receiptId = 1L;
        when(receiptService.getReceiptById(anyLong())).thenThrow(new RuntimeException("Error fetching receipt"));


        mockMvc.perform(get("/receipts/{id}/points", receiptId))
                .andExpect(status().isInternalServerError());
    }
}
