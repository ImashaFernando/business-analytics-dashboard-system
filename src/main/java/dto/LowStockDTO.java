package com.dashboard.business_analytics_dashboard_system.dto;

public class LowStockDTO {

    private String name;
    private int qty;
    private String status;

    public LowStockDTO(String name, int qty, String status) {
        this.name = name;
        this.qty = qty;
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public int getQty() {
        return qty;
    }

    public String getStatus() {
        return status;
    }

}

