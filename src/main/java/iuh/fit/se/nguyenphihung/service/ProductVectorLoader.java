package iuh.fit.se.nguyenphihung.service;


import iuh.fit.se.nguyenphihung.entities.Product;
import iuh.fit.se.nguyenphihung.repository.ProductRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class ProductVectorLoader {
    ProductRepository productRepository;
    VectorStore vectorStore;

    public void loadProductsToVectorDB() {
        if (vectorStore.similaritySearch("products").isEmpty()) {
            List<Product> products = productRepository.findAll();
            List<Document> documents = products.stream()
                    .map(p -> new Document("""
                            Tên sản phẩm: %s
                            Giá: %.0f₫
                            Tình trạng kho: %s
                            """.formatted(
                            p.getName(),
                            p.getPrice(),
                            p.getInStock() ? "Còn hàng" : "Hết hàng"
                    )))
                    .toList();
            vectorStore.add(documents);
        }
    }
}
