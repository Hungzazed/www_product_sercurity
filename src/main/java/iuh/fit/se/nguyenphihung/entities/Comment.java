package iuh.fit.se.nguyenphihung.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

@Entity
@Table(name = "comments")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = "product")
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank(message = "Nội dung bình luận không được để trống")
    @Size(min = 1, max = 1000, message = "Nội dung bình luận phải có từ 1 đến 1000 ký tự")
    @Column(nullable = false, length = 1000)
    private String text;

    @NotNull(message = "Bình luận phải thuộc về một sản phẩm")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;
}

