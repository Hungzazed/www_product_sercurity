package iuh.fit.se.nguyenphihung.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(name = "orders")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = {"customer", "orderLines"})
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotNull(message = "Ngày đặt hàng không được để trống")
    @PastOrPresent(message = "Ngày đặt hàng không thể là ngày tương lai")
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(nullable = false)
    private Calendar date;

    @NotNull(message = "Đơn hàng phải có khách hàng")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<OrderLine> orderLines = new LinkedHashSet<>();

    public BigDecimal getTotalAmount() {
        return orderLines.stream()
                .map(ol -> ol.getPurchasePrice().multiply(BigDecimal.valueOf(ol.getAmount())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}

