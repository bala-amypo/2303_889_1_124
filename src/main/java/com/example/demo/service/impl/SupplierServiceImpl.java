package com.example.demo.service.impl;

import com.example.demo.entity.DiversityClassification;
import com.example.demo.entity.Supplier;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.repository.DiversityClassificationRepository;
import com.example.demo.repository.SupplierRepository;
import com.example.demo.service.SupplierService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class SupplierServiceImpl implements SupplierService {

    private final SupplierRepository supplierRepository;
    private final DiversityClassificationRepository classificationRepository;

    public SupplierServiceImpl(SupplierRepository supplierRepository,
                               DiversityClassificationRepository classificationRepository) {
        this.supplierRepository = supplierRepository;
        this.classificationRepository = classificationRepository;
    }

    @Override
    public Supplier createSupplier(Supplier supplier) {

        Set<DiversityClassification> managedClassifications = new HashSet<>();

        for (DiversityClassification dc : supplier.getDiversityClassifications()) {

            if (dc.getId() == null) {
                throw new RuntimeException("Classification ID must not be null");
            }

            DiversityClassification existing =
                    classificationRepository.findById(dc.getId())
                            .orElseThrow(() ->
                                    new ResourceNotFoundException(
                                            "DiversityClassification not found with id: " + dc.getId()
                                    )
                            );

            managedClassifications.add(existing);
        }

        supplier.setDiversityClassifications(managedClassifications);
        supplier.setUpdatedAt(LocalDateTime.now());

        return supplierRepository.save(supplier);
    }

    @Override
    public Supplier getSupplierById(Long id) {
        return supplierRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Supplier not found with id: " + id)
                );
    }

    @Override
    public List<Supplier> getAllSuppliers() {
        return supplierRepository.findAll();
    }

    @Override
    public void deactivateSupplier(Long id) {
        Supplier supplier = getSupplierById(id);
        supplier.setIsActive(false);
        supplierRepository.save(supplier);
    }
}
