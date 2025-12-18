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

    @PutMapping("/{id}")
    public PurchaseOrder update(@PathVariable Long id, @RequestBody PurchaseOrder po) {
        return service.updatePurchaseOrder(id, po);
    }

    @GetMapping("/{id}")
    public PurchaseOrder get(@PathVariable Long id) {
        return service.getPurchaseOrderById(id);
    }

    @GetMapping
    public List<PurchaseOrder> getAll() {
        return service.getAllPurchaseOrders();
    }

    @GetMapping("/supplier/{supplierId}")
    public List<PurchaseOrder> getBySupplier(@PathVariable Long supplierId) {
        return service.getPurchaseOrdersBySupplier(supplierId);
    }
}
