package com.example.btl_appnotes.ViewModel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.btl_appnotes.Model.Notes;
import com.example.btl_appnotes.Repository.NotesRepository;

import java.util.List;

public class NotesViewModel extends AndroidViewModel {
    public NotesRepository repository;
    public LiveData<List<Notes>> getAllNotes;
    public LiveData<List<Notes>> hightolow;
    public LiveData<List<Notes>> lowtohigh;
    public LiveData<List<Notes>> pinned;

    public NotesViewModel(Application application){
        super(application);

        repository = new NotesRepository(application);
        getAllNotes = repository.getallNotes;
        hightolow = repository.hightolow;
        lowtohigh = repository.lowtohigh;
        pinned = repository.pinned;
    }

    public void insertNotes(Notes notes){
        repository.insertNotes(notes);
    }
    public void deleteNotes(int id){
        repository.deleteNotes(id);
    }
    public void updateNotes(Notes notes){
        repository.updateNotes(notes);
    }
}