package com.example.psafe.ui.home;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.psafe.database.Repository;

public class HomeViewModel extends ViewModel {

    private final Repository repository;

    public Repository getRepository() {
        return repository;
    }


    public HomeViewModel() {
        repository = new Repository();

    }
}