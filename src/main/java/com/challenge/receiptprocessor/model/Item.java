package com.challenge.receiptprocessor.model;

import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Pattern;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Hidden
    private Long id;

    @NonNull
    @Pattern(regexp = "^[\\w\\s\\-]+$", message = "Invalid shortDescription")
    @Schema(description = "The Short Product Description for the item.", example = "Mountain Dew 12PK", required=true)
    public String shortDescription;

    @NonNull
    @Pattern(regexp = "^\\d+\\.\\d{2}$", message = "Invalid price value")
    @Schema(description = "The total price payed for this item.", example = "6.49", required = true)
    public String price;
}
