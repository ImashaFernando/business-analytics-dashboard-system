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

    // show form
    @GetMapping
    public String showSalesPage(Model model){
        model.addAttribute("sale", new Sale());
        model.addAttribute("products", productRepo.findAll());
        return "sale-form";
    }

    // save sale
    @PostMapping("/save")
    public String saveSale(@ModelAttribute Sale sale){

        Product product = productRepo.findById(
                sale.getProduct().getId()
        ).orElse(null);

        if(product == null){
            return "redirect:/sales";
        }

        sale.setProduct(product);
        sale.setTotal(product.getPrice() * sale.getQuantity());
        sale.setDate(LocalDate.now());

        saleRepo.save(sale);

        return "redirect:/sales/list";
    }

    // list page
    @GetMapping("/list")
    public String viewSales(Model model){
        model.addAttribute("sales", saleRepo.findAll());
        return "sales-list";
    }
}