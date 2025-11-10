package iuh.fit.se.nguyenphihung.controller;

import iuh.fit.se.nguyenphihung.entities.Customer;
import iuh.fit.se.nguyenphihung.entities.Order;
import iuh.fit.se.nguyenphihung.service.CustomerService;
import iuh.fit.se.nguyenphihung.service.OrderLineService;
import iuh.fit.se.nguyenphihung.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private OrderLineService orderLineService;

    @GetMapping
    public String listOrders(@RequestParam(required = false) String search, Model model, Authentication authentication) {
        List<Order> orders;

        if (authentication != null && authentication.getAuthorities().stream()
                .anyMatch(authority -> authority.getAuthority().equals("ROLE_ADMIN"))) {
            if (search != null && !search.isEmpty()) {
                orders = orderService.searchByCustomerName(search);
            } else {
                orders = orderService.findAll();
            }
        } else {
            String username = authentication.getName();
            Customer customer = customerService.findByUsername(username);
            orders = orderService.findByCustomerId(customer.getId());
        }

        model.addAttribute("orders", orders);
        model.addAttribute("search", search);
        return "order/list";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("order", new Order());
        model.addAttribute("customers", customerService.findAll());
        return "order/form";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Integer id, Model model) {
        Order order = orderService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid order Id:" + id));
        model.addAttribute("order", order);
        model.addAttribute("customers", customerService.findAll());
        return "order/form";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/save")
    public String saveOrder(@ModelAttribute Order order, @RequestParam Integer customerId) {
        Customer customer = customerService.findById(customerId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid customer Id:" + customerId));
        order.setCustomer(customer);
        orderService.save(order);
        return "redirect:/orders";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/delete/{id}")
    public String deleteOrder(@PathVariable Integer id) {
        orderService.deleteById(id);
        return "redirect:/orders";
    }

    @GetMapping("/{id}")
    public String viewOrderDetail(@PathVariable Integer id, Model model) {
        Order order = orderService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid order Id:" + id));
        model.addAttribute("order", order);
        model.addAttribute("orderLines", orderLineService.findByOrderId(id));
        return "order/detail";
    }
}

