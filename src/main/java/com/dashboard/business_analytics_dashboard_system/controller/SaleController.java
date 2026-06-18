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

        // ❗ CHECK NULL FIRST
        if (product == null || sale.getQuantity() == null || sale.getQuantity() <= 0) {
            return "redirect:/sales/add";
        }

        // ❗ CHECK STOCK
        if (product.getQuantity() < sale.getQuantity()) {
            System.out.println("Not enough stock!");
            return "redirect:/sales/add";
        }

        // ❗ REDUCE STOCK
        product.setQuantity(product.getQuantity() - sale.getQuantity());
        productRepo.save(product);

        // ❗ SAVE SALE
        sale.setProduct(product);
        sale.setTotal(product.getPrice() * sale.getQuantity());
        sale.setDate(java.time.LocalDate.now());

        saleRepo.save(sale);

        return "redirect:/sales/list";
    }

    // =========================
    // SALES LIST
    // =========================
    @GetMapping("/list")
    public String salesList(Model model) {
        model.addAttribute("sales", saleRepo.findAllByDeletedFalse());
        return "sales-list";
    }

    // =========================
    // DELETE SALE
    // =========================
    @GetMapping("/restore/{id}")
    public String restoreSale(@PathVariable Long id) {

        Sale sale = saleRepo.findById(id).orElse(null);

        if (sale != null) {

            // UNDO DELETE
            sale.setDeleted(false);
            saleRepo.save(sale);
        }

        return "redirect:/sales/trash";
    }
    @GetMapping("/delete-permanent/{id}")
    public String deletePermanent(@PathVariable Long id) {

        saleRepo.deleteById(id);

        return "redirect:/sales/trash";
    }
    // =========================
    // SAFETY REDIRECT (IMPORTANT FIX)
    // =========================
    @GetMapping
    public String redirectSales() {
        return "redirect:/sales/add";
    }
    @GetMapping("/delete/{id}")
    public String deleteSale(@PathVariable Long id) {

        Sale sale = saleRepo.findById(id).orElse(null);

        if (sale != null && sale.getProduct() != null) {

            Product product = sale.getProduct();

            // 🔥 RESTORE STOCK BACK
            product.setQuantity(product.getQuantity() + sale.getQuantity());
            productRepo.save(product);

            // 🔥 SOFT DELETE SALE
            sale.setDeleted(true);
            saleRepo.save(sale);
        }

        return "redirect:/sales/list";
    }
}