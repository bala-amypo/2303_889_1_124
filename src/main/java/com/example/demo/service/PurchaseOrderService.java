package com.example.demo.service;

import com.example.demo.entity.PurchaseOrder;

import java.util.List;

public interface PurchaseOrderService {

    PurchaseOrder create(PurchaseOrder order);

    List<PurchaseOrder> getOrdersByCategory(Long categoryId);
}
