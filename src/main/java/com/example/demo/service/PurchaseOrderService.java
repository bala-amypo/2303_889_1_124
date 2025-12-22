package com.example.demo.service;

import com.example.demo.entity.PurchaseOrder;
import java.util.List;

public interface PurchaseOrderService {
    PurchaseOrder createPurchaseOrder(PurchaseOrder purchaseOrder);
    List<PurchaseOrder> getPurchaseOrdersBySupplier(Long supplierId);
    List<PurchaseOrder> getAllPurchaseOrders();
}
