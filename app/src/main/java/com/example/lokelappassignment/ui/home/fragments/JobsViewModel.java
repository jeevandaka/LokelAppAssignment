package com.example.lokelappassignment.ui.home.fragments;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.lokelappassignment.MainApplication;
import com.example.lokelappassignment.data.Local.model.JobData;

import java.util.List;

public class JobsViewModel extends ViewModel {
    private static final String TAG = JobsViewModel.class.getSimpleName();
    private final MutableLiveData<List<JobData>> jobs = new MutableLiveData<>();;

    public LiveData<List<JobData>> getObservableJobs() {
        return jobs;
    }

    public void getJobs(int page){
        MainApplication.getAppDataManager().getJobs(jobs,page);
    }

}
