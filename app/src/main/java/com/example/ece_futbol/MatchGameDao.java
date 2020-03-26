package com.example.ece_futbol;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

@Dao
public interface MatchGameDao {
    @Query("SELECT * FROM matchGame WHERE uid = :matchUid")
    MatchGame findByUid(int matchUid);

    @Insert
    void insertAll(MatchGame... matches);
}
