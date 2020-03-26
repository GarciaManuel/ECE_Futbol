package com.example.ece_futbol;

import androidx.room.Dao;
import androidx.room.Index;
import androidx.room.Insert;
import androidx.room.Query;

@Dao
public interface TeamDao {
    @Query("SELECT * FROM team WHERE name LIKE :name")
    Team findByName(String name);

    @Insert
    void insertAll(Team... teams);
}
