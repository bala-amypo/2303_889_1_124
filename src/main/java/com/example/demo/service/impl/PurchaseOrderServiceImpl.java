package com.example.demo.service.impl;

import com.example.demo.entity.PurchaseOrder;
import com.example.demo.entity.Supplier;
import com.example.demo.repository.PurchaseOrderRepository;
import com.example.demo.service.PurchaseOrderService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PurchaseOrderServiceImpl implements PurchaseOrderService {

    private final PurchaseOrderRepository repository;

    public PurchaseOrderServiceImpl(PurchaseOrderRepository repository) {
        this.repository = repository;
    }

    @Override
    public PurchaseOrder create(PurchaseOrder order) {

        Supplier supplier = order.getSupplier();

        if (supplier == null || !supplier.getActive()) {
            throw new RuntimeException("Supplier is inactive or missing");
        }

        return repository.save(order);
    }

    @Override
    public List<PurchaseOrder> getOrdersByCategory(Long categoryId) {
        return repository.findByCategoryId(categoryId);
    }
}
