package com.example.psafe.ui.gallery;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.psafe.R;
import com.example.psafe.data.model.News;
import com.example.psafe.database.Repository;

import java.util.ArrayList;

public class GalleryViewModel extends ViewModel {

    private Repository repository;

    public GalleryViewModel() {
        repository = new Repository();

    }
    ArrayList<News> getAllNews(){
        return repository.getAllNewsArray();
    }
}