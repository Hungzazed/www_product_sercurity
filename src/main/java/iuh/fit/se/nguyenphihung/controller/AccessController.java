package iuh.fit.se.nguyenphihung.controller;

import iuh.fit.se.nguyenphihung.entities.Customer;
import iuh.fit.se.nguyenphihung.entities.ROLE;
import iuh.fit.se.nguyenphihung.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Calendar;
import java.util.List;

@Controller
public class AccessController {
    @Autowired
    private CustomerService customerService;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @GetMapping("/register")
    public String showRegisterForm(Model model) {
        model.addAttribute("customer", new Customer());
        return "register";
    }

    @PostMapping("/register")
    public String registerCustomer(@ModelAttribute Customer customer,
                                   @RequestParam String confirmPassword,
                                   Model model) {
        if (customerService.findByUsername(customer.getName()) != null) {
            model.addAttribute("error", "Tên đăng nhập đã tồn tại!");
            model.addAttribute("customer", customer);
            return "register";
        }
        if (!customer.getPassword().equals(confirmPassword)) {
            model.addAttribute("error", "Mật khẩu xác nhận không khớp!");
            model.addAttribute("customer", customer);
            return "register";
        }
        if (customer.getPassword().length() < 6) {
            model.addAttribute("error", "Mật khẩu phải có ít nhất 6 ký tự!");
            model.addAttribute("customer", customer);
            return "register";
        }
        customer.setPassword(passwordEncoder.encode(customer.getPassword()));
        customer.setRoles(List.of(ROLE.ROLE_CUSTOMER));
        customer.setCustomerSince(Calendar.getInstance());
        customerService.save(customer);
        return "redirect:/login?registered=true";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }
}

