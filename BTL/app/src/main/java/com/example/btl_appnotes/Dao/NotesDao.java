package com.example.btl_appnotes.Dao;

import androidx.lifecycle.LiveData;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.btl_appnotes.Model.Notes;

import java.util.List;

@androidx.room.Dao
public interface NotesDao {

    @Query("SELECT * FROM DataNote_11")
    LiveData<List<Notes>> getallNotes();

    @Query("SELECT * FROM DataNote_11 ORDER BY notes_priorty DESC")
    LiveData<List<Notes>> highToLow();

    @Query("SELECT * FROM DataNote_11 ORDER BY notes_priorty ASC")
    LiveData<List<Notes>> lowToHigh();
    @Query("SELECT * FROM DataNote_11 ORDER BY notes_pin DESC")
    LiveData<List<Notes>> pinned();

    @Insert
    void insertNotes(Notes... notes);

    @Query("DELETE FROM DataNote_11 WHERE id=:id")
    void deleteNotes(int id);

    @Update
    void UpdateNotes(Notes notes);
}