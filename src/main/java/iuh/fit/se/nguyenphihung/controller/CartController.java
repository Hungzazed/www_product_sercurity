package iuh.fit.se.nguyenphihung.controller;

import iuh.fit.se.nguyenphihung.dto.CartItem;
import iuh.fit.se.nguyenphihung.entities.Customer;
import iuh.fit.se.nguyenphihung.entities.Order;
import iuh.fit.se.nguyenphihung.entities.OrderLine;
import iuh.fit.se.nguyenphihung.entities.Product;
import iuh.fit.se.nguyenphihung.service.CustomerService;
import iuh.fit.se.nguyenphihung.service.OrderService;
import iuh.fit.se.nguyenphihung.service.ProductService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedHashSet;
import java.util.List;

@Controller
@RequestMapping("/cart")
public class CartController {
    
    @Autowired
    private ProductService productService;
    
    @Autowired
    private OrderService orderService;
    
    @Autowired
    private CustomerService customerService;
    
    private static final String CART_SESSION_KEY = "SHOPPING_CART";

    private List<CartItem> getCartFromSession(HttpSession session) {
        List<CartItem> cart = (List<CartItem>) session.getAttribute(CART_SESSION_KEY);
        if (cart == null) {
            cart = new ArrayList<>();
            session.setAttribute(CART_SESSION_KEY, cart);
        }
        return cart;
    }

    @PreAuthorize("hasAnyRole('CUSTOMER', 'ADMIN')")
    @PostMapping("/add/{productId}")
    public String addToCart(@PathVariable Integer productId,
                          @RequestParam(defaultValue = "1") Integer quantity,
                          HttpSession session,
                          RedirectAttributes redirectAttributes) {
        
        Product product = productService.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("Sản phẩm không tồn tại: " + productId));
        
        if (!product.getInStock()) {
            redirectAttributes.addFlashAttribute("error", "Sản phẩm đã hết hàng!");
            return "redirect:/products/detail/" + productId;
        }
        
        List<CartItem> cart = getCartFromSession(session);

        boolean found = false;
        for (CartItem item : cart) {
            if (item.getProductId().equals(productId)) {
                item.setQuantity(item.getQuantity() + quantity);
                found = true;
                break;
            }
        }

        if (!found) {
            CartItem newItem = new CartItem(
                productId,
                product.getName(),
                product.getPrice(),
                quantity
            );
            cart.add(newItem);
        }
        
        session.setAttribute(CART_SESSION_KEY, cart);
        redirectAttributes.addFlashAttribute("success", "Đã thêm sản phẩm vào giỏ hàng!");
        
        return "redirect:/products";
    }
    
    // Xem giỏ hàng và thanh toán (chỉ CUSTOMER và ADMIN)
    @PreAuthorize("hasAnyRole('CUSTOMER', 'ADMIN')")
    @GetMapping
    public String viewCart(HttpSession session, Model model) {
        List<CartItem> cart = getCartFromSession(session);
        
        BigDecimal total = cart.stream()
                .map(CartItem::getSubTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        
        model.addAttribute("cartItems", cart);
        model.addAttribute("total", total);
        
        return "cart/view";
    }

    @PreAuthorize("hasAnyRole('CUSTOMER', 'ADMIN')")
    @PostMapping("/checkout")
    public String checkout(HttpSession session, 
                         Authentication authentication,
                         RedirectAttributes redirectAttributes) {
        
        List<CartItem> cart = getCartFromSession(session);
        
        if (cart == null || cart.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "Giỏ hàng trống!");
            return "redirect:/cart";
        }
        
        try {
            String username = authentication.getName();
            Customer customer = customerService.findByUsername(username);
            
            if (customer == null) {
                redirectAttributes.addFlashAttribute("error", "Không tìm thấy thông tin khách hàng!");
                return "redirect:/cart";
            }

            Order order = new Order();
            order.setDate(Calendar.getInstance());
            order.setCustomer(customer);
            order.setOrderLines(new LinkedHashSet<>());
            for (CartItem item : cart) {
                Product product = productService.findById(item.getProductId())
                        .orElseThrow(() -> new IllegalArgumentException("Sản phẩm không tồn tại: " + item.getProductId()));
                if (!product.getInStock()) {
                    redirectAttributes.addFlashAttribute("error", 
                        "Sản phẩm '" + product.getName() + "' đã hết hàng!");
                    return "redirect:/cart";
                }
                
                OrderLine orderLine = new OrderLine();
                orderLine.setOrder(order);
                orderLine.setProduct(product);
                orderLine.setAmount(item.getQuantity());
                orderLine.setPurchasePrice(item.getPrice());
                
                order.getOrderLines().add(orderLine);
            }

            orderService.save(order);
            session.removeAttribute(CART_SESSION_KEY);
            
            redirectAttributes.addFlashAttribute("success", 
                "Đặt hàng thành công! Mã đơn hàng: " + order.getId());
            
            return "redirect:/orders/" + order.getId();
            
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", 
                "Có lỗi xảy ra khi đặt hàng: " + e.getMessage());
            return "redirect:/cart";
        }
    }
}
