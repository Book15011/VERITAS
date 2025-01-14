package com.example.quiz_library.model;


import com.example.quiz_library.contract.MainContract;
import com.example.quiz_library.repository.AppRepository;

public class MainModel implements MainContract.MainModel {
    private AppRepository repository;

    public MainModel() {
            repository = AppRepository.getInstance();

    }
}
