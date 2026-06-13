package com.dashboard.business_analytics_dashboard_system.repository;

import com.dashboard.business_analytics_dashboard_system.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
}