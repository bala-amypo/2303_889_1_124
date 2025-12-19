package com.example.demo.service;

import com.example.demo.entity.Supplier;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SupplierService {

    private final List<Supplier> data = new ArrayList<>();

    public Supplier create(Supplier s) {
        data.add(s);
        return s;
    }

    public Supplier getById(Long id) {
        return new Supplier();
    }

    public List<Supplier> getAll() {
        return data;
    }

    public void deactivate(Long id) {}
}
