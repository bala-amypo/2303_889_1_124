package com.example.demo.service;

import com.example.demo.entity.UserAccount;

public interface UserAccountService {
    UserAccount register(UserAccount userAccount);
    UserAccount findByEmailOrThrow(String email);
}
