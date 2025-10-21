package iuh.fit.se.nguyenphihung.config;

import iuh.fit.se.nguyenphihung.entities.*;
import iuh.fit.se.nguyenphihung.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderLineRepository orderLineRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Override
    public void run(String... args) {
        if (categoryRepository.count() > 0) {
            return;
        }

        // Tạo danh mục
        Category electronics = new Category();
        electronics.setName("Điện tử");
        categoryRepository.save(electronics);

        Category fashion = new Category();
        fashion.setName("Thời trang");
        categoryRepository.save(fashion);

        Category books = new Category();
        books.setName("Sách");
        categoryRepository.save(books);

        // Tạo sản phẩm
        Product phone = new Product();
        phone.setName("iPhone 15 Pro Max");
        phone.setPrice(new BigDecimal("29990000"));
        phone.setInStock(true);
        phone.setCategory(electronics);
        productRepository.save(phone);

        Product laptop = new Product();
        laptop.setName("MacBook Pro M3");
        laptop.setPrice(new BigDecimal("45990000"));
        laptop.setInStock(true);
        laptop.setCategory(electronics);
        productRepository.save(laptop);

        Product shirt = new Product();
        shirt.setName("Áo sơ mi nam");
        shirt.setPrice(new BigDecimal("299000"));
        shirt.setInStock(true);
        shirt.setCategory(fashion);
        productRepository.save(shirt);

        Product book1 = new Product();
        book1.setName("Đắc nhân tâm");
        book1.setPrice(new BigDecimal("89000"));
        book1.setInStock(true);
        book1.setCategory(books);
        productRepository.save(book1);

        // Tạo comment cho sản phẩm
        Comment comment1 = new Comment();
        comment1.setText("Sản phẩm rất tốt, chất lượng cao!");
        comment1.setProduct(phone);
        commentRepository.save(comment1);

        Comment comment2 = new Comment();
        comment2.setText("Giao hàng nhanh, đóng gói cẩn thận.");
        comment2.setProduct(phone);
        commentRepository.save(comment2);

        // Tạo khách hàng
        Customer customer1 = new Customer();
        customer1.setName("Nguyễn Văn A");
        customer1.setPassword("123");
        customer1.setRoles(List.of(ROLE.ROLE_CUSTOMER));
        Calendar cal1 = Calendar.getInstance();
        cal1.set(2023, Calendar.JANUARY, 15);
        customer1.setCustomerSince(cal1);
        customerRepository.save(customer1);

        Customer customer2 = new Customer();
        customer2.setName("Trần Thị B");
        customer2.setPassword("123");
        customer2.setRoles(List.of(ROLE.ROLE_ADMIN));
        Calendar cal2 = Calendar.getInstance();
        cal2.set(2023, Calendar.MARCH, 20);
        customer2.setCustomerSince(cal2);
        customerRepository.save(customer2);

        Customer customer3 = new Customer();
        customer3.setName("Lê Văn C");
        customer3.setPassword("123");
        customer3.setRoles(List.of(ROLE.ROLE_CUSTOMER));
        Calendar cal3 = Calendar.getInstance();
        cal3.set(2024, Calendar.JUNE, 10);
        customer3.setCustomerSince(cal3);
        customerRepository.save(customer3);

        // Tạo hóa đơn
        Order order1 = new Order();
        order1.setCustomer(customer1);
        Calendar orderDate1 = Calendar.getInstance();
        orderDate1.set(2024, Calendar.OCTOBER, 1);
        order1.setDate(orderDate1);
        orderRepository.save(order1);

        // Tạo chi tiết hóa đơn 1
        OrderLine orderLine1 = new OrderLine();
        orderLine1.setOrder(order1);
        orderLine1.setProduct(phone);
        orderLine1.setAmount(1);
        orderLine1.setPurchasePrice(phone.getPrice());
        orderLineRepository.save(orderLine1);

        OrderLine orderLine2 = new OrderLine();
        orderLine2.setOrder(order1);
        orderLine2.setProduct(book1);
        orderLine2.setAmount(2);
        orderLine2.setPurchasePrice(book1.getPrice());
        orderLineRepository.save(orderLine2);

        // Tạo hóa đơn 2
        Order order2 = new Order();
        order2.setCustomer(customer2);
        Calendar orderDate2 = Calendar.getInstance();
        orderDate2.set(2024, Calendar.OCTOBER, 5);
        order2.setDate(orderDate2);
        orderRepository.save(order2);

        OrderLine orderLine3 = new OrderLine();
        orderLine3.setOrder(order2);
        orderLine3.setProduct(laptop);
        orderLine3.setAmount(1);
        orderLine3.setPurchasePrice(laptop.getPrice());
        orderLineRepository.save(orderLine3);

        OrderLine orderLine4 = new OrderLine();
        orderLine4.setOrder(order2);
        orderLine4.setProduct(shirt);
        orderLine4.setAmount(3);
        orderLine4.setPurchasePrice(shirt.getPrice());
        orderLineRepository.save(orderLine4);

        System.out.println("✓ Dữ liệu mẫu đã được tạo thành công!");
    }
}
