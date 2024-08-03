package com.example.lokelappassignment.data.Remote;

import com.example.lokelappassignment.data.Local.model.JobData;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiService {
    @GET("common/jobs")
    Call<ResponseBody> getJobs(@Query("page") int page);
}
