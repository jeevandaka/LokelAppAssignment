package com.example.lokelappassignment.data;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.lokelappassignment.MainApplication;
import com.example.lokelappassignment.data.Local.model.JobData;
import com.example.lokelappassignment.data.Remote.ApiManager;

import java.util.List;

public class AppDataManager {

    private static final String TAG = AppDataManager.class.getSimpleName();

    private static AppDataManager instance;
    private ApiManager apiManager;
    private LiveData<List<JobData>> bookmarkedJobs;

    private AppDataManager(Context context) {
        apiManager = ApiManager.getInstance();
    }

    public static AppDataManager getInstance(Context context) {
        if (instance == null) {
            instance = new AppDataManager(context);
        }
        return instance;
    }


    public void getJobs(MutableLiveData<List<JobData>> jobLiveList, int page){
        apiManager.getJobs(jobLiveList,page);
    }

    public LiveData<List<JobData>> getBookmarkedJobs(){
        if(bookmarkedJobs == null){
            bookmarkedJobs = new MutableLiveData<>();
        }
        Log.d(TAG, "getBookMarks: livedata" + bookmarkedJobs);
        return bookmarkedJobs;
    }

    public void getBookMarks(){
        Log.d(TAG, "getBookMarks: ");
        Log.d(TAG, "getBookMarks: livedata" + bookmarkedJobs);
        bookmarkedJobs = MainApplication.getAppDatabase().jobDataDao().getAllJobData();
        Log.d(TAG, "getBookMarks: livedata" + bookmarkedJobs);
    }

}
