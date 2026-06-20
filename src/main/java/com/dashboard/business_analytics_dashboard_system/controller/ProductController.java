package com.dashboard.business_analytics_dashboard_system.controller;

import com.dashboard.business_analytics_dashboard_system.model.Product;
import com.dashboard.business_analytics_dashboard_system.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/products")
public class ProductController {

    @Autowired
    private ProductRepository productRepo;

    @GetMapping
    public String list(Model model){
        model.addAttribute("products", productRepo.findAll());
        return "products";
    }

    @GetMapping("/add")
    public String addForm(Model model){
        model.addAttribute("product", new Product());
        return "product-form";
    }

    @PostMapping("/save")
    public String save(@ModelAttribute Product product){
        productRepo.save(product);
        return "redirect:/products";
    }

    @GetMapping("/edit/{id}")
    public String editProduct(@PathVariable Long id, Model model){
        Product product = productRepo.findById(id).orElse(null);
        model.addAttribute("product", product);
        return "product-form";
    }

    // ✅ DELETE (IMPORTANT FIX)
    @GetMapping("/delete/{id}")
    public String deleteProduct(@PathVariable Long id){
        productRepo.deleteById(id);
        return "redirect:/products";
    }
}