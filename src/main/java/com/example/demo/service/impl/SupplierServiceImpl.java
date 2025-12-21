package com.example.demo.service.impl;

import com.example.demo.entity.Supplier;
import com.example.demo.repository.SupplierRepository;
import com.example.demo.service.SupplierService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SupplierServiceImpl implements SupplierService {

    private final SupplierRepository repository;

    public SupplierServiceImpl(SupplierRepository repository) {
        this.repository = repository;
    }

    @Override
    public Supplier createSupplier(Supplier supplier) {
        supplier.setActive(true);
        return repository.save(supplier);
    }

    @Override
    public List<Supplier> getAllSuppliers() {
        return repository.findAll();
    }

    @Override
    public Supplier getSupplier(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Supplier not found"));
    }

    @Override
    public void deactivateSupplier(Long id) {
        Supplier supplier = getSupplier(id);
        supplier.setActive(false);
        repository.save(supplier);
    }
}
