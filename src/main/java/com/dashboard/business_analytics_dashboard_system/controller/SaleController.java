package com.dashboard.business_analytics_dashboard_system.controller;

import com.dashboard.business_analytics_dashboard_system.model.Product;
import com.dashboard.business_analytics_dashboard_system.model.Sale;
import com.dashboard.business_analytics_dashboard_system.repository.ProductRepository;
import com.dashboard.business_analytics_dashboard_system.repository.SaleRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@Controller
@RequestMapping("/sales")
public class SaleController {

    @Autowired
    private SaleRepository saleRepo;

    @Autowired
    private ProductRepository productRepo;

    // =========================
    // OPEN ADD SALE FORM
    // =========================
    @GetMapping("/add")
    public String showAddSale(Model model) {

        model.addAttribute("sale", new Sale());
        model.addAttribute("products", productRepo.findAll());

        return "add-sale";
    }

    // =========================
    // SAVE SALE
    // =========================
    @PostMapping("/save")
    public String saveSale(@ModelAttribute Sale sale) {

        Product product = productRepo.findById(
                sale.getProduct().getId()
        ).orElse(null);

        if (product == null || sale.getQuantity() == null || sale.getQuantity() <= 0) {
            return "redirect:/sales/list";
        }

        sale.setProduct(product);
        sale.setTotal(product.getPrice() * sale.getQuantity());
        sale.setDate(LocalDate.now());

        saleRepo.save(sale);

        return "redirect:/sales/list";
    }

    // =========================
    // SALES LIST
    // =========================
    @GetMapping("/list")
    public String salesList(Model model) {
        model.addAttribute("sales", saleRepo.findAll());
        return "sales-list";
    }

    // =========================
    // DELETE SALE
    // =========================
    @GetMapping("/delete/{id}")
    public String deleteSale(@PathVariable Long id) {
        saleRepo.deleteById(id);
        return "redirect:/sales/list";
    }

    // =========================
    // SAFETY REDIRECT (IMPORTANT FIX)
    // =========================
    @GetMapping
    public String redirectSales() {
        return "redirect:/sales/add";
    }
}