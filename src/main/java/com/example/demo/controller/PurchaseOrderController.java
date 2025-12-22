package com.example.demo.controller;

import com.example.demo.entity.PurchaseOrder;
import com.example.demo.service.PurchaseOrderService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/purchase-orders")
public class PurchaseOrderController {

    private final PurchaseOrderService service;

    public PurchaseOrderController(PurchaseOrderService service) {
        this.service = service;
    }

    @PostMapping
    public PurchaseOrder create(@RequestBody PurchaseOrder po) {
        return service.createPurchaseOrder(po);
    }

    @GetMapping("/supplier/{supplierId}")
    public List<PurchaseOrder> getBySupplier(@PathVariable Long supplierId) {
        return service.getPurchaseOrdersBySupplier(supplierId);
    }

    @GetMapping
    public List<PurchaseOrder> getAll() {
        return service.getAllPurchaseOrders();
    }
}
