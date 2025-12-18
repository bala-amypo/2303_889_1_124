package com.example.demo.service;

import com.example.demo.entity.Supplier;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SupplierService {

    private final List<Supplier> data = new ArrayList<>();

    public Supplier createSupplier(Supplier s) {
        data.add(s);
        return s;
    }

    public Supplier updateSupplier(Long id, Supplier s) {
        return s;
    }

    public Supplier getSupplierById(Long id) {
        return new Supplier();
    }

    public List<Supplier> getAllSuppliers() {
        return data;
    }

    public void deactivateSupplier(Long id) {}
}
