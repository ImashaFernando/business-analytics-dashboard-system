package com.dashboard.business_analytics_dashboard_system.repository;

import com.dashboard.business_analytics_dashboard_system.model.Sale;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SaleRepository extends JpaRepository<Sale, Long> {
}
