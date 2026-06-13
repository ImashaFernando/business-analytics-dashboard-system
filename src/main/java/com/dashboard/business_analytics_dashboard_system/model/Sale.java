package com.dashboard.business_analytics_dashboard_system.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
public class Sale {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    private Integer quantity;

    private Double total;

    private LocalDate date;

    // getters and setters

    public Long getId() { return id; }

    public void setId(Long id) { this.id = id; }

    public Product getProduct() { return product; }

    public void setProduct(Product product) { this.product = product; }

    public Integer getQuantity() { return quantity; }

    public void setQuantity(Integer quantity) { this.quantity = quantity; }

    public Double getTotal() { return total; }

    public void setTotal(Double total) { this.total = total; }

    public LocalDate getDate() { return date; }

    public void setDate(LocalDate date) { this.date = date; }
}