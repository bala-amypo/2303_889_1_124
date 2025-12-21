package com.example.demo.service;

import com.example.demo.entity.UserAccount;

public interface UserAccountService {

    UserAccount register(UserAccount user);

    UserAccount getByEmail(String email);
}
