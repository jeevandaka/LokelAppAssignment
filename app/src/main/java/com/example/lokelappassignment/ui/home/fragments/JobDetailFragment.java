package com.example.lokelappassignment.ui.home.fragments;

import android.app.Notification;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.lokelappassignment.MainApplication;
import com.example.lokelappassignment.R;
import com.example.lokelappassignment.data.Local.model.JobData;
import com.example.lokelappassignment.data.Remote.ApiManager;
import com.example.lokelappassignment.databinding.FragmentJobDetailBinding;
import com.example.lokelappassignment.databinding.FragmentJobsBinding;
import com.example.lokelappassignment.ui.home.HomeActivity;

public class JobDetailFragment extends Fragment {
    private static final String TAG = JobDetailFragment.class.getSimpleName();
    OnNotify onNotify;
    private FragmentJobDetailBinding binding;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentJobDetailBinding.inflate(inflater);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Bundle b = getArguments();
        Log.d(TAG, "onViewCreated: " + b);
        if(b != null){
            Log.d(TAG, "onViewCreated: " + b);
            String role = b.getString("role");
            String title = b.getString("title");
            String vacancy = b.getString("vacancy");
            String location = b.getString("location");
            String jobType = b.getString("job_type");
            String salary = b.getString("salary");
            String qualification = b.getString("education");
            String exp = b.getString("experience");
            boolean isBookmarked = b.getBoolean("isBookmarked");
            JobData jobData = (JobData) b.getSerializable("job_obj");

            binding.companyName.setText(jobData.getCompanyName());
            binding.roleNameTextView.setText(role);
            binding.titleTextView.setText(title);
            binding.vacancyTextView.setText(vacancy);
            binding.locationTextView.setText(location);
            binding.jobTypeTextView.setText(jobType);
            binding.genderTextView.setText("male");
            binding.salaryTextView.setText(salary);
            binding.expTextView.setText(exp);
            binding.educationTextView.setText(qualification);


            if(jobData != null && MainApplication
                    .getAppDatabase()
                    .jobDataDao()
                    .isJobIdPresent(jobData.getId()) > 0){
                binding.bookmarkIcon.setImageResource(R.drawable.ic_filled_bookmark_24);
            }
            else{
                binding.bookmarkIcon.setImageResource(R.drawable.ic_bookmark_border_24);
            }


            binding.bookmarkIcon.setOnClickListener(v -> {

                if(!jobData.isBookmarked()){
                    jobData.setBookmarked(true);
                    MainApplication.getAppDatabase().jobDataDao().insert(jobData);
                    binding.bookmarkIcon.setImageResource(R.drawable.ic_filled_bookmark_24);
                }
                else{
                    jobData.setBookmarked(false);
                    MainApplication.getAppDatabase().jobDataDao().delete(jobData);
                    binding.bookmarkIcon.setImageResource(R.drawable.ic_bookmark_border_24);
                }

                onNotify.onClick();
            });

            binding.materialButton.setOnClickListener(v->{
                ((HomeActivity) getActivity()).makePhoneCall(jobData.getTel());
            });

        }
    }

    interface OnNotify {
        void onClick();
    }

    void setOnNotify(OnNotify onNotify){
        this.onNotify = onNotify;
    }
}