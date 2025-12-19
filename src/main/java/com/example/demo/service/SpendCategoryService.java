package com.example.demo.service;

import com.example.demo.entity.SpendCategory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SpendCategoryService {

    private final List<SpendCategory> data = new ArrayList<>();

    public SpendCategory create(SpendCategory s) {
        data.add(s);
        return s;
    }

    public List<SpendCategory> getAll() {
        return data;
    }
}
