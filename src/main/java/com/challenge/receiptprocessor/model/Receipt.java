package com.challenge.receiptprocessor.model;

import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.List;


@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Receipt {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Hidden
    private Long id;

    @NonNull
    @Pattern(regexp = "^[\\w\\s\\-&]+$", message = "Invalid retailer format")
    @Schema(description = "The name of the retailer or store the receipt is from.", example = "M&M Corner Market", required = true)
    public String retailer;
    @NonNull
    @Schema(description = "The date of the purchase printed on the receipt.", example = "2022-01-01", required=true)
    public String purchaseDate;

    @NonNull
    @Schema(description = "The time of the purchase printed on the receipt. 24-hour time expected.", example = "13:01", required=true)
    public String purchaseTime;

    @NonNull
    @Size(min = 1, message = "Items list must contain at least one item")
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    public List<Item> items;

    @NonNull
    @Pattern(regexp = "^\\d+\\.\\d{2}$", message = "Invalid total value")
    @Schema(description = "The total amount paid on the receipt.", example = "6.49", required=true)
    public String total;

}
