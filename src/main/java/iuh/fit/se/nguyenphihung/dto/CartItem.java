package iuh.fit.se.nguyenphihung.dto;

import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class CartItem implements Serializable {
    private Integer productId;
    private String productName;
    private BigDecimal price;
    private Integer quantity;
    
    public BigDecimal getSubTotal() {
        return price.multiply(BigDecimal.valueOf(quantity));
    }
}
