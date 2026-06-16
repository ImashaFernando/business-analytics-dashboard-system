package com.dashboard.business_analytics_dashboard_system.controller;

import com.dashboard.business_analytics_dashboard_system.model.Sale;
import com.dashboard.business_analytics_dashboard_system.repository.ProductRepository;
import com.dashboard.business_analytics_dashboard_system.repository.SaleRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import java.time.LocalDate;

import java.util.*;
import java.util.stream.Collectors;

@Controller
public class DashboardController {

    @Autowired
    private ProductRepository productRepo;

    @Autowired
    private SaleRepository saleRepo;

    @GetMapping("/dashboard")
    public String dashboard(Model model) {

        List<Sale> sales = saleRepo.findAll();   // ✅ ONLY ONCE

        // TOTAL COUNTS
        long totalProducts = productRepo.count();
        long totalSales = sales.size();

        // TOTAL REVENUE
        double totalRevenue = sales.stream()
                .mapToDouble(Sale::getTotal)
                .sum();

        model.addAttribute("totalProducts", totalProducts);
        model.addAttribute("totalSales", totalSales);
        model.addAttribute("totalRevenue", totalRevenue);

        // ⭐ TODAY SALES (NEW)
        double todaySales = sales.stream()
                .filter(s -> s.getDate() != null &&
                        s.getDate().equals(LocalDate.now()))
                .mapToDouble(Sale::getTotal)
                .sum();

        model.addAttribute("todaySales", todaySales);

        // GROUP SALES BY PRODUCT
        Map<String, Double> productSales = sales.stream()
                .filter(s -> s.getProduct() != null)
                .collect(Collectors.groupingBy(
                        s -> s.getProduct().getName(),
                        Collectors.summingDouble(Sale::getTotal)
                ));

        // CHART DATA
        List<String> labels = new ArrayList<>(productSales.keySet());
        List<Double> data = new ArrayList<>(productSales.values());

        model.addAttribute("salesLabels", labels);
        model.addAttribute("salesData", data);

        // BEST PRODUCT
        String bestProduct = productSales.entrySet()
                .stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse("-");

        model.addAttribute("bestProduct", bestProduct);

        return "dashboard";
    }
}