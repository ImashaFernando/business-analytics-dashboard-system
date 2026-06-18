package com.dashboard.business_analytics_dashboard_system.controller;

import com.dashboard.business_analytics_dashboard_system.model.Product;
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

        // =========================
        // ACTIVE SALES ONLY (SOFT DELETE)
        // =========================
        List<Sale> sales = saleRepo.findAllByDeletedFalse();

        // =========================
        // TOTALS
        // =========================
        long totalProducts = productRepo.count();
        long totalSales = sales.size();

        double totalRevenue = sales.stream()
                .mapToDouble(Sale::getTotal)
                .sum();

        model.addAttribute("totalProducts", totalProducts);
        model.addAttribute("totalSales", totalSales);
        model.addAttribute("totalRevenue", totalRevenue);

        // =========================
        // TODAY SALES
        // =========================
        double todaySales = sales.stream()
                .filter(s -> s.getDate() != null &&
                        s.getDate().equals(LocalDate.now()))
                .mapToDouble(Sale::getTotal)
                .sum();

        model.addAttribute("todaySales", todaySales);

        // =========================
        // LOW STOCK (SORTED + STATUS)
        // =========================
        List<Map<String, Object>> lowStockProducts = productRepo.findAll()
                .stream()
                .filter(p -> p.getQuantity() != null)
                .filter(p -> p.getQuantity() < 5)
                .sorted(Comparator.comparing(Product::getQuantity))
                .map(p -> {
                    Map<String, Object> map = new HashMap<>();
                    map.put("name", p.getName() != null ? p.getName() : "Unknown");
                    map.put("qty", p.getQuantity());

                    String status;
                    if (p.getQuantity() <= 2) {
                        status = "CRITICAL";
                    } else {
                        status = "WARNING";
                    }

                    map.put("status", status);
                    return map;
                })
                .toList();
;

        model.addAttribute("lowStockProducts", lowStockProducts);

        // =========================
        // SALES GROUPING (CHART)
        // =========================
        Map<String, Double> productSales = sales.stream()
                .filter(s -> s.getProduct() != null)
                .collect(Collectors.groupingBy(
                        s -> s.getProduct().getName(),
                        Collectors.summingDouble(Sale::getTotal)
                ));

        model.addAttribute("salesLabels", new ArrayList<>(productSales.keySet()));
        model.addAttribute("salesData", new ArrayList<>(productSales.values()));

        // =========================
        // BEST PRODUCT
        // =========================
        String bestProduct = productSales.entrySet()
                .stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse("-");

        model.addAttribute("bestProduct", bestProduct);

        return "dashboard";
    }
}