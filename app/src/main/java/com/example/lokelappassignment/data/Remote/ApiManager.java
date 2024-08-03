package com.example.lokelappassignment.data.Remote;

import android.util.Log;
import android.widget.Toast;

import androidx.lifecycle.MutableLiveData;

import com.example.lokelappassignment.MainApplication;
import com.example.lokelappassignment.data.Local.model.JobData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiManager {
    private static final String TAG = ApiManager.class.getSimpleName();

    private static final String BASE_URL = "https://testapi.getlokalapp.com/";

    private static ApiManager instance;
    private ApiService apiService;
    private Retrofit retrofit;

    private Call<ResponseBody> call;

    private Call<ResponseBody> apiCall;

    private ApiManager() {
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .followRedirects(true)
                .followSslRedirects(true)
                .build();

        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        apiService = retrofit.create(ApiService.class);
    }

    public static ApiManager getInstance() {
        if (instance == null) {
            instance = new ApiManager();
        }
        return instance;
    }


    public void getJobs(MutableLiveData<List<JobData>> jobLiveList, int page){
        apiService.getJobs(page).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Log.d(TAG, "onResponse: success");
                    try {
                        JSONObject jsonObject = new JSONObject(response.body().string());
                        JSONArray jsonArray = jsonObject.getJSONArray("results");
                        Log.d(TAG, "onResponse: results");
                        List<JobData> jobDataList = new ArrayList<>();
                        for (int i = 0; i < jsonArray.length(); i++) {
                            if(i == 4){
                                continue;
                            }
                            JSONObject object = jsonArray.getJSONObject(i);

                            String companyName = object.getString("company_name");
                            String jobRole = object.getString("job_role");
                            Log.d(TAG, "onResponse: " + jobRole);
                            int id = object.getInt("id");
                            String title = object.getString("title");
                            String buttonText = object.getString("button_text");
                            String customLink = object.getString("custom_link");
                            boolean isBookmarked = object.getBoolean("is_bookmarked");
                            String jobHours = object.getString("job_hours");


                            JSONObject primaryDetailObject = object.getJSONObject("primary_details");
                            String place = primaryDetailObject.getString("Place");
                            String salary = primaryDetailObject.getString("Salary");
                            String jobType = primaryDetailObject.getString("Job_Type");
                            String qualification = primaryDetailObject.getString("Qualification");
                            String experience = primaryDetailObject.getString("Experience");

                            JSONArray jobTags = object.getJSONArray("job_tags");
                            JSONObject jobTagObject = jobTags.getJSONObject(0);
                            String vacancyValue = jobTagObject.getString("value");

                            JobData job = new JobData();
                            job.setId(id);
                            job.setCompanyName(companyName);
                            job.setJobRole(jobRole);
                            job.setTitle(title);
                            job.setButtonText(buttonText);
                            job.setTel(customLink);
                            job.setBookmarked(isBookmarked);
                            job.setJobHours(jobHours);
                            job.setLocation(place);
                            job.setSalary(salary);
                            job.setJobType(jobType);
                            job.setQualification(qualification);
                            job.setExperience(experience);
                            job.setVacancyValue(vacancyValue);

                            jobDataList.add(job);
                        }

                        jobLiveList.postValue(jobDataList);

                    }catch (JSONException e) {
                        throw new RuntimeException(e);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
                else{
                    String errorBody = null;

                    try {
                        if (response.errorBody() != null) {
                            errorBody = response.errorBody().string();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Log.d(TAG, "onResponse: jobList receiving failed - Error Body: " + errorBody);
                    Log.d(TAG, "onResponse: jobList " + response.code() + " msg " + response.message());

                    Toast.makeText(MainApplication.getInstance().getBaseContext(), "onResponse: jobList receiving failed - Error Body", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.wtf(TAG, "onFailure: ", t);
                Toast.makeText(MainApplication.getInstance().getBaseContext(), "onFailure", Toast.LENGTH_SHORT).show();
            }
        });
    }

}
