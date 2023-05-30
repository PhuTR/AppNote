package com.example.btl_appnotes.Repository;

import android.app.Application;

import androidx.lifecycle.LiveData;


import com.example.btl_appnotes.Dao.NotesDao;
import com.example.btl_appnotes.Database.NotesDatabase;
import com.example.btl_appnotes.Model.Notes;

import java.util.List;

public class NotesRepository {

    public NotesDao notesDao;
    public LiveData<List<Notes>> getallNotes;
    public LiveData<List<Notes>> hightolow;
    public LiveData<List<Notes>> lowtohigh;
    public LiveData<List<Notes>> pinned;

    public NotesRepository(Application application){
        NotesDatabase database = NotesDatabase.getDatabaseInstance(application);
        notesDao = database.notesDao();
        getallNotes = notesDao.getallNotes();
        hightolow = notesDao.highToLow();
        lowtohigh = notesDao.lowToHigh();
        pinned = notesDao.pinned();

    }

    public void insertNotes(Notes notes){
        notesDao.insertNotes(notes);
    }

    public void deleteNotes(int id){
        notesDao.deleteNotes(id);
    }

    public void updateNotes(Notes notes){
        notesDao.UpdateNotes(notes);
    }
}