package com.challenge.receiptprocessor.service;

import com.challenge.receiptprocessor.model.Item;
import com.challenge.receiptprocessor.model.Receipt;
import com.challenge.receiptprocessor.repository.ReceiptRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ReceiptServiceTest {

    @Mock
    private ReceiptRepository receiptRepository;

    @InjectMocks
    private ReceiptService receiptService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSaveReceipt() {

        Receipt receipt = Receipt.builder().retailer("RetailerName").purchaseDate( "2025-02-02").purchaseTime("12:00").total("10").items( List.of(Item.builder().price("10.0").shortDescription("item1").build())).build();
        Receipt savedReceipt = Receipt.builder().id(1L).retailer("RetailerName").purchaseDate( "2025-02-02").purchaseTime("12:00").total("10").items( List.of(Item.builder().price("10.0").shortDescription("item1").build())).build();
        

        when(receiptRepository.save(receipt)).thenReturn(savedReceipt);


        String receiptId = receiptService.saveReceipt(receipt);


        assertEquals("1", receiptId);
        verify(receiptRepository, times(1)).save(receipt); // Ensure save was called
    }

    @Test
    void testGetReceiptById_Found() {

        Receipt receipt = Receipt.builder().retailer("RetailerName").purchaseDate( "2025-02-02").purchaseTime("12:00").total("10").items( List.of(Item.builder().price("10.0").shortDescription("item1").build())).build();
        when(receiptRepository.findById(1L)).thenReturn(Optional.of(receipt));


        Optional<Receipt> result = receiptService.getReceiptById(1L);


        assertTrue(result.isPresent());
        assertEquals(receipt, result.get());
        verify(receiptRepository, times(1)).findById(1L); // Ensure findById was called
    }

    @Test
    void testGetReceiptById_NotFound() {

        when(receiptRepository.findById(1L)).thenReturn(Optional.empty());


        Optional<Receipt> result = receiptService.getReceiptById(1L);


        assertFalse(result.isPresent());
        verify(receiptRepository, times(1)).findById(1L); // Ensure findById was called
    }

    @Test
    void testCalculatePoints() {

        Item item1 = Item.builder().shortDescription("item 1").price("10.0").build();
        Item item2 = Item.builder().shortDescription("item 2").price("15.0").build();
        Receipt receipt = Receipt.builder().retailer("RetailerName").purchaseDate( "2025-02-02").purchaseTime("12:00").total("10").items( List.of(item2,item1)).build();

        int points = receiptService.calculatePoints(receipt);


        assertEquals(97, points);
    }

    @Test
    void testCalculatePoints_RoundDollarAmount() {

        Item item1 = Item.builder().shortDescription("item 1").price("10.0").build();
        Receipt receipt = Receipt.builder().retailer("RetailerName").purchaseDate( "2025-02-02").purchaseTime("12:00").total("10").items( List.of(item1)).build();

        receipt.setTotal("20.0");


        int points = receiptService.calculatePoints(receipt);


        assertEquals(39, points);
    }

    @Test
    void testCalculatePoints_MultipleOf0_25() {

        Item item1 = Item.builder().shortDescription("item 1").price("5.0").build();
        Receipt receipt = Receipt.builder().retailer("RetailerName").purchaseDate( "2025-02-02").purchaseTime("12:00").total("10").items( List.of(item1)).build();
        receipt.setTotal("5.25");
        int points = receiptService.calculatePoints(receipt);


        assertEquals(38, points);
    }

    @Test
    void testCalculatePoints_ItemDescriptionLength() {

        Item item1 = Item.builder().shortDescription("item 1").price("10.0").build();
        Receipt receipt = Receipt.builder().retailer("RetailerName").purchaseDate( "2025-02-02").purchaseTime("12:00").total("10").items( List.of(item1)).build();
        item1.setShortDescription("ShortDesc");
        receipt.setTotal("10.0");


        int points = receiptService.calculatePoints(receipt);


        assertEquals(39, points);
    }

    @Test
    void testCalculatePoints_PurchaseDateOdd() {

        Item item1 = Item.builder().shortDescription("item 1").price("10.0").build();
        Receipt receipt = Receipt.builder().retailer("RetailerName").purchaseDate( "2025-02-03").purchaseTime("12:00").total("10").items( List.of(item1)).build();
        receipt.setTotal("10.0");


        int points = receiptService.calculatePoints(receipt);


        assertEquals(45, points);
    }

    @Test
    void testCalculatePoints_PurchaseTimeBetween2and4() {

        Item item1 = Item.builder().shortDescription("item 1").price("10.0").build();
        Receipt receipt = Receipt.builder().retailer("RetailerName").purchaseDate("2025-02-03").purchaseTime("14:30").total("10").items(List.of(item1)).build();

        receipt.setTotal("10.0");


        int points = receiptService.calculatePoints(receipt);


        assertEquals(55, points);
    }
}
