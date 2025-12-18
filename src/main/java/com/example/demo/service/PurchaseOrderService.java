package com.example.demo.service;

import com.example.demo.entity.PurchaseOrder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PurchaseOrderService {

    public PurchaseOrder create(PurchaseOrder p) {
        return p;
    }

    public List<PurchaseOrder> getAll() {
        return List.of();
    }
}
