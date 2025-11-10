package iuh.fit.se.nguyenphihung.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "order_lines")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = {"order", "product"})
public class OrderLine {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotNull(message = "OrderLine phải thuộc về một đơn hàng")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @NotNull(message = "OrderLine phải có sản phẩm")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @NotNull(message = "Số lượng không được để trống")
    @Min(value = 1, message = "Số lượng phải lớn hơn hoặc bằng 1")
    @Max(value = 10000, message = "Số lượng không được vượt quá 10000")
    @Column(nullable = false)
    private Integer amount;

    @NotNull(message = "Giá mua không được để trống")
    @DecimalMin(value = "0.00", inclusive = false, message = "Giá mua phải lớn hơn 0")
    @Digits(integer = 8, fraction = 2, message = "Giá mua không hợp lệ (tối đa 8 chữ số, 2 chữ thập phân)")
    @Column(name = "purchase_price", nullable = false, precision = 10, scale = 2)
    private BigDecimal purchasePrice;

    public BigDecimal getSubTotal() {
        return purchasePrice.multiply(BigDecimal.valueOf(amount));
    }
}

