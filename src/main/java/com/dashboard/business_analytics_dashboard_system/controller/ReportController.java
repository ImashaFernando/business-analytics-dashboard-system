package com.dashboard.business_analytics_dashboard_system.controller;

import com.dashboard.business_analytics_dashboard_system.model.Sale;
import com.dashboard.business_analytics_dashboard_system.repository.SaleRepository;
import com.dashboard.business_analytics_dashboard_system.repository.ProductRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
public class ReportController {

    @Autowired
    private SaleRepository saleRepo;

    @Autowired
    private ProductRepository productRepo;

    @GetMapping("/report")
    public String report(Model model) {

        List<Sale> sales = saleRepo.findAll();

        double totalRevenue = sales.stream()
                .mapToDouble(Sale::getTotal)
                .sum();

        int totalSales = sales.size();

        int totalProducts = (int) productRepo.count();

        double maxSale = sales.stream()
                .mapToDouble(Sale::getTotal)
                .max()
                .orElse(0);

        double minSale = sales.stream()
                .mapToDouble(Sale::getTotal)
                .min()
                .orElse(0);

        String bestProduct = sales.stream()
                .collect(Collectors.groupingBy(
                        s -> s.getProduct().getName(),
                        Collectors.summingDouble(Sale::getTotal)
                ))
                .entrySet()
                .stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse("-");

        model.addAttribute("totalRevenue", totalRevenue);
        model.addAttribute("totalSales", totalSales);
        model.addAttribute("totalProducts", totalProducts);
        model.addAttribute("bestProduct", bestProduct);
        model.addAttribute("maxSale", maxSale);
        model.addAttribute("minSale", minSale);

        return "report";   // MUST match report.html
    }
}