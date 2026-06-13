package com.dashboard.business_analytics_dashboard_system.controller;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

import com.dashboard.business_analytics_dashboard_system.model.Sale;
import com.dashboard.business_analytics_dashboard_system.repository.ProductRepository;
import com.dashboard.business_analytics_dashboard_system.repository.SaleRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DashboardController {

    @Autowired
    private ProductRepository productRepo;

    @Autowired
    private SaleRepository saleRepo;

    @GetMapping("/dashboard")
    public String dashboard(Model model) {

        // TOTAL COUNTS
        long totalProducts = productRepo.count();
        long totalSales = saleRepo.count();

        model.addAttribute("totalProducts", totalProducts);
        model.addAttribute("totalSales", totalSales);

        // 💰 TOTAL REVENUE
        double totalRevenue = saleRepo.findAll()
                .stream()
                .mapToDouble(Sale::getTotal)
                .sum();

        model.addAttribute("totalRevenue", totalRevenue);

        // 🏆 BEST PRODUCT + 📊 GROUPED SALES DATA
        Map<String, Double> productSales = new HashMap<>();

        for (Sale s : saleRepo.findAll()) {

            String name = s.getProduct().getName();
            double total = s.getTotal();

            productSales.put(name,
                    productSales.getOrDefault(name, 0.0) + total);
        }

        // Convert Map → Lists for Chart
        List<String> labels = new ArrayList<>(productSales.keySet());
        List<Double> data = new ArrayList<>(productSales.values());

        model.addAttribute("salesLabels", labels);
        model.addAttribute("salesData", data);

        // 🏆 BEST SELLING PRODUCT
        String bestProduct = productSales.entrySet()
                .stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse("");

        model.addAttribute("bestProduct", bestProduct);

        return "dashboard";
    }
}