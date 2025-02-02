package com.challenge.receiptprocessor.controller;

import com.challenge.receiptprocessor.model.Receipt;
import com.challenge.receiptprocessor.service.ReceiptService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/receipts")
public class ProcessReceiptController {
    private final ReceiptService receiptService;

    @PostMapping("/process")
    public ResponseEntity<Object> processReceipt(@Valid @RequestBody Receipt receiptRequest){
        try {
            log.info("Started processing retailer {} receiptRequest", receiptRequest.getRetailer());
            var id=receiptService.saveReceipt(receiptRequest);
            return ResponseEntity.ok().body(Map.of("id", id));
        }
        catch (Exception ex){
            log.error("Error processing receipt: ", ex);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{id}/points")
    public ResponseEntity<Object> getReceiptPoints(@PathVariable Long id) {
        try{
            log.info("Started calculating points for the receipt");
            Optional<Receipt> obtainedReceipt = receiptService.getReceiptById(id);

            if (obtainedReceipt.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No receipt found for that ID.");
            }

            Receipt receipt = obtainedReceipt.get();
            var points = receiptService.calculatePoints(receipt);
            return ResponseEntity.ok(Map.of("points", points));
        }
        catch (Exception ex){
            log.error("Error calculating points for a receipt with id {}: ", id, ex);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }
}
