package com.challenge.receiptprocessor.service;

import com.challenge.receiptprocessor.model.Item;
import com.challenge.receiptprocessor.model.Receipt;
import com.challenge.receiptprocessor.repository.ReceiptRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class ReceiptService {

    private final ReceiptRepository receiptRepository;

    public String saveReceipt(Receipt receipt) {
        log.info("Saving a new receipt into the DB");
        return String.valueOf(receiptRepository.save(receipt).getId());
    }

    public Optional<Receipt> getReceiptById(Long id) {
        log.info("Getting receipt date by an id from DB");
        return receiptRepository.findById(id);
    }
    public int calculatePoints(Receipt receipt){
        int points = 0;

        // 1. One point for every alphanumeric character in retailer name
        points += receipt.getRetailer().replaceAll("[^a-zA-Z0-9]", "").length();

        // 2. Convert total to BigDecimal
        BigDecimal total = new BigDecimal(receipt.getTotal());

        // 3. 50 points if the total is a round dollar amount
        if (total.scale() == 0) {
            points += 50;
        }

        // 4. 25 points if the total is a multiple of 0.25
        if (total.remainder(BigDecimal.valueOf(0.25)).compareTo(BigDecimal.ZERO) == 0) {
            points += 25;
        }

        // 5. 5 points for every two items
        points += (receipt.getItems().size() / 2) * 5;

        // 6. Points based on item description length
        for (Item item : receipt.getItems()) {
            String description = item.getShortDescription().trim();
            if (description.length() % 3 == 0) {
                BigDecimal itemPrice = new BigDecimal(item.getPrice());
                points += itemPrice.multiply(BigDecimal.valueOf(0.2)).setScale(0, RoundingMode.UP).intValue();
            }
        }


        // 7. 6 points if the day in the purchase date is odd
        LocalDate purchaseDate = LocalDate.parse(receipt.getPurchaseDate(), DateTimeFormatter.ISO_DATE);
        if (purchaseDate.getDayOfMonth() % 2 != 0) {
            points += 6;
        }

        // 8. 10 points if the purchase time is between 2:00 PM and 4:00 PM
        LocalTime purchaseTime = LocalTime.parse(receipt.getPurchaseTime(), DateTimeFormatter.ofPattern("HH:mm"));
        if (!purchaseTime.isBefore(LocalTime.of(14, 0)) && purchaseTime.isBefore(LocalTime.of(16, 0))) {
            points += 10;
        }

        return points;
    }
}
