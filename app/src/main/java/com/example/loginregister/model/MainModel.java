package com.example.loginregister.model;


import com.example.loginregister.contract.MainContract;
import com.example.loginregister.repository.AppRepository;

public class MainModel implements MainContract.MainModel {
    private AppRepository repository;

    public MainModel() {
            repository = AppRepository.getInstance();

    }
}
