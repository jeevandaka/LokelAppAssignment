package com.example.lokelappassignment.data.Local.db;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.lokelappassignment.data.Local.model.JobData;

import java.util.List;

@Dao
public interface JobDataDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(JobData jobData);

    @Delete
    void delete(JobData jobData);

    @Query("SELECT * FROM job_data")
    LiveData<List<JobData>> getAllJobData();

    @Query("SELECT COUNT(*) FROM job_data WHERE id = :jobId")
    int isJobIdPresent(int jobId);


}
