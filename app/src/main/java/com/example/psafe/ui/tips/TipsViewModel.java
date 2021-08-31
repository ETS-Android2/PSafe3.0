package com.example.psafe.ui.tips;

import androidx.lifecycle.ViewModel;

import com.example.psafe.database.Repository;

public class TipsViewModel extends ViewModel {
    // TODO: Implement the ViewModel
    private Repository repository;

    public Repository getRepository() {
        return repository;
    }


    public TipsViewModel() {
        repository = new Repository();

    }
}