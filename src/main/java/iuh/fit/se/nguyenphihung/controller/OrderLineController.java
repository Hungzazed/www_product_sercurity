package iuh.fit.se.nguyenphihung.controller;

import iuh.fit.se.nguyenphihung.entities.Order;
import iuh.fit.se.nguyenphihung.entities.OrderLine;
import iuh.fit.se.nguyenphihung.entities.Product;
import iuh.fit.se.nguyenphihung.service.OrderLineService;
import iuh.fit.se.nguyenphihung.service.OrderService;
import iuh.fit.se.nguyenphihung.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/orderlines")
public class OrderLineController {
    @Autowired
    private OrderLineService orderLineService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private ProductService productService;

    @GetMapping
    public String listOrderLines(Model model) {
        model.addAttribute("orderLines", orderLineService.findAll());
        return "orderline/list";
    }

    @GetMapping("/new")
    public String showCreateForm(@RequestParam(required = false) Integer orderId, Model model) {
        OrderLine orderLine = new OrderLine();
        if (orderId != null) {
            Order order = orderService.findById(orderId)
                    .orElseThrow(() -> new IllegalArgumentException("Invalid order Id:" + orderId));
            orderLine.setOrder(order);
        }
        model.addAttribute("orderLine", orderLine);
        model.addAttribute("orders", orderService.findAll());
        model.addAttribute("products", productService.findAll());
        return "orderline/form";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Integer id, Model model) {
        OrderLine orderLine = orderLineService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid orderLine Id:" + id));
        model.addAttribute("orderLine", orderLine);
        model.addAttribute("orders", orderService.findAll());
        model.addAttribute("products", productService.findAll());
        return "orderline/form";
    }

    @PostMapping("/save")
    public String saveOrderLine(@Valid @ModelAttribute OrderLine orderLine,
                                BindingResult result,
                                @RequestParam Integer orderId,
                                @RequestParam Integer productId,
                                Model model) {
        if (result.hasErrors()) {
            model.addAttribute("orders", orderService.findAll());
            model.addAttribute("products", productService.findAll());
            return "orderline/form";
        }
        
        Order order = orderService.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid order Id:" + orderId));
        Product product = productService.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid product Id:" + productId));

        orderLine.setOrder(order);
        orderLine.setProduct(product);
        orderLine.setPurchasePrice(product.getPrice());
        orderLineService.save(orderLine);

        return "redirect:/orders/" + orderId;
    }

    @GetMapping("/delete/{id}")
    public String deleteOrderLine(@PathVariable Integer id) {
        OrderLine orderLine = orderLineService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid orderLine Id:" + id));
        Integer orderId = orderLine.getOrder().getId();
        orderLineService.deleteById(id);
        return "redirect:/orders/" + orderId;
    }
}

