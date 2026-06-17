package com.dashboard.business_analytics_dashboard_system.controller;

import com.dashboard.business_analytics_dashboard_system.model.Sale;
import com.dashboard.business_analytics_dashboard_system.repository.SaleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class SaleTrashController {

    @Autowired
    private SaleRepository saleRepo;

    @GetMapping("/sales/trash")
    public String trash(Model model) {

        List<Sale> deletedSales = saleRepo.findAllByDeletedTrue();

        model.addAttribute("deletedSales", deletedSales);

        return "sales-trash";
    }
}