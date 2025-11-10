package iuh.fit.se.nguyenphihung.controller;

import iuh.fit.se.nguyenphihung.entities.Category;
import iuh.fit.se.nguyenphihung.entities.Product;
import iuh.fit.se.nguyenphihung.service.CategoryService;
import iuh.fit.se.nguyenphihung.service.CommentService;
import iuh.fit.se.nguyenphihung.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/products")
public class ProductController {
    @Autowired
    private ProductService productService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private CommentService commentService;

    @GetMapping
    public String listProducts(@RequestParam(required = false) String search,
                               @RequestParam(required = false) Integer categoryId,
                               Model model) {
        List<Product> products;
        if (search != null && !search.isEmpty()) {
            products = productService.searchByName(search);
        } else if (categoryId != null) {
            products = productService.findByCategory(categoryId);
        } else {
            products = productService.findAll();
        }
        model.addAttribute("products", products);
        model.addAttribute("categories", categoryService.findAll());
        model.addAttribute("search", search);
        model.addAttribute("selectedCategoryId", categoryId);
        return "product/list";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("product", new Product());
        model.addAttribute("categories", categoryService.findAll());
        return "product/form";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Integer id, Model model) {
        Product product = productService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid product Id:" + id));
        model.addAttribute("product", product);
        model.addAttribute("categories", categoryService.findAll());
        return "product/form";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/save")
    public String saveProduct(@Valid @ModelAttribute Product product,
                              BindingResult result,
                              @RequestParam(required = false) Integer categoryId,
                              Model model) {
        if (result.hasErrors()) {
            model.addAttribute("categories", categoryService.findAll());
            return "product/form";
        }
        Category category = categoryService.findById(categoryId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid category Id:" + categoryId));
        product.setCategory(category);
        productService.save(product);
        return "redirect:/products";
    }


    @GetMapping("/delete/{id}")
    public String deleteProduct(@PathVariable Integer id) {
        productService.deleteById(id);
        return "redirect:/products";
    }

    @GetMapping("/detail/{id}")
    public String viewProductDetail(@PathVariable Integer id, Model model) {
        Product product = productService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid product Id:" + id));
        model.addAttribute("product", product);
        model.addAttribute("comments", commentService.findByProductId(id));
        return "product/detail";
    }

    @PostMapping("/detail/{id}/comment")
    public String addComment(@PathVariable Integer id,
                            @RequestParam String commentText,
                            org.springframework.web.servlet.mvc.support.RedirectAttributes redirectAttributes) {
        try {
            if (commentText == null || commentText.trim().isEmpty()) {
                redirectAttributes.addFlashAttribute("error", "Comment cannot be empty");
                return "redirect:/products/detail/" + id;
            }

            Product product = productService.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("Invalid product Id:" + id));

            iuh.fit.se.nguyenphihung.entities.Comment comment = new iuh.fit.se.nguyenphihung.entities.Comment();
            comment.setText(commentText);
            comment.setProduct(product);

            commentService.save(comment);
            redirectAttributes.addFlashAttribute("success", "Comment added successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to add comment: " + e.getMessage());
        }

        return "redirect:/products/detail/" + id;
    }
}
