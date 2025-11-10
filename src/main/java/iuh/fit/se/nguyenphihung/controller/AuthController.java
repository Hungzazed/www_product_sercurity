package iuh.fit.se.nguyenphihung.controller;

import iuh.fit.se.nguyenphihung.entities.Customer;
import iuh.fit.se.nguyenphihung.service.CustomerService;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class AuthController {

    @Autowired
    private CustomerService customerService;

    @GetMapping("/register")
    public String showRegisterForm(Model model) {
        model.addAttribute("customer", new Customer());
        return "register";
    }

    @PostMapping("/register")
    public String registerCustomer(
            @RequestParam @NotBlank(message = "Tên người dùng không được để trống") 
            @Size(min = 3, max = 50, message = "Tên người dùng phải có từ 3 đến 50 ký tự") String username,
            @RequestParam @NotBlank(message = "Mật khẩu không được để trống") 
            @Size(min = 6, message = "Mật khẩu phải có ít nhất 6 ký tự") String password,
            @RequestParam String confirmPassword,
            RedirectAttributes redirectAttributes,
            Model model) {

        try {
            // Validate username
            if (username == null || username.trim().isEmpty()) {
                model.addAttribute("errorUsername", "Tên người dùng không được để trống");
                model.addAttribute("username", username);
                return "register";
            }
            
            if (username.trim().length() < 3 || username.trim().length() > 50) {
                model.addAttribute("errorUsername", "Tên người dùng phải có từ 3 đến 50 ký tự");
                model.addAttribute("username", username);
                return "register";
            }

            // Validate password
            if (password == null || password.trim().isEmpty()) {
                model.addAttribute("errorPassword", "Mật khẩu không được để trống");
                model.addAttribute("username", username);
                return "register";
            }
            
            if (password.length() < 6) {
                model.addAttribute("errorPassword", "Mật khẩu phải có ít nhất 6 ký tự");
                model.addAttribute("username", username);
                return "register";
            }

            // Validate confirm password
            if (!password.equals(confirmPassword)) {
                model.addAttribute("errorConfirmPassword", "Mật khẩu xác nhận không khớp");
                model.addAttribute("username", username);
                return "register";
            }

            customerService.registerNewCustomer(username, password);
            redirectAttributes.addFlashAttribute("success", "Đăng ký thành công! Vui lòng đăng nhập.");
            return "redirect:/login";

        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("username", username);
            return "register";
        }
    }
}
