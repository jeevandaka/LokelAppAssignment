package com.example.lokelappassignment.ui.home.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.lokelappassignment.R;
import com.example.lokelappassignment.data.Local.model.JobData;
import com.example.lokelappassignment.data.Remote.ApiManager;
import com.example.lokelappassignment.databinding.FragmentJobsBinding;
import com.example.lokelappassignment.ui.home.HomeActivity;
import com.example.lokelappassignment.ui.home.adapters.JobsAdapter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class JobsFeedFragment extends Fragment {

    private static final String TAG = JobsFeedFragment.class.getSimpleName();

    private JobsViewModel viewModel;
    private JobsAdapter adapter;
    private FragmentJobsBinding binding;
    private int page = 1;
    private boolean isLoading = true;
    private List<JobData> joblist = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentJobsBinding.inflate(inflater);
        Log.d(TAG, "onCreateView: ");
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.progressCircular.setVisibility(View.VISIBLE);
        viewModel = new ViewModelProvider(this).get(JobsViewModel.class);
        Log.d(TAG, "onViewCreated: list" + joblist);
        viewModel.getJobs(page);

        viewModel.getObservableJobs().observe(getViewLifecycleOwner(), jobs -> {
            binding.progressCircular.setVisibility(View.GONE);
            binding.progressCircular2.setVisibility(View.GONE);
            Log.d(TAG, "onViewCreated: jobs" + jobs);
            if(jobs != null){
                joblist.addAll(jobs);
                isLoading = false;
                if(adapter == null){
                    adapter = new JobsAdapter(jobs);
                    binding.recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
                    binding.recyclerView.setAdapter(adapter);
                }
                else{
                    adapter.updateJobList(jobs);
                }

                binding.recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                    @Override
                    public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                        super.onScrolled(recyclerView, dx, dy);

                        if (!recyclerView.canScrollVertically(1) && !isLoading) {
                            page++;
                            binding.progressCircular2.setVisibility(View.VISIBLE);
                            isLoading = true;
                            viewModel.getJobs(page);
                        }
                    }
                });
            }
            else{
                isLoading = false;
            }

            if(adapter != null){
                adapter.SetOnCardClickListener(jobData -> {
                    JobDetailFragment newFragment = new JobDetailFragment();
                    Bundle args = new Bundle();
                    Log.d(TAG, "onViewCreated: " + jobData.getJobRole());
                    JobData obj = jobData;
                    args.putSerializable("job_obj", obj);

                    args.putString("role",jobData.getJobRole());
                    args.putString("title",jobData.getTitle());
                    args.putString("vacancy",jobData.getVacancyValue());
                    args.putString("location",jobData.getLocation());
                    args.putString("job_type",jobData.getJobType());
                    args.putString("salary",jobData.getSalary());
                    args.putString("experience",jobData.getExperience());
                    args.putString("education",jobData.getQualification());
                    args.putBoolean("isBookmarked",jobData.isBookmarked());

                    newFragment.setArguments(args);

                    newFragment.setOnNotify(() ->{
                        if(adapter != null){
                            adapter.notifyDataSetChanged();
                        }
                    });

                    requireActivity().getSupportFragmentManager().beginTransaction()
                            .add(R.id.flFragment, newFragment)
                            .addToBackStack(null)
                            .commit();
                });

                adapter.SetOnCallClicked( jobData ->{
                    ((HomeActivity) getActivity()).makePhoneCall(jobData.getTel());
                });
            }
        });

        Log.d(TAG, "onViewCreated: adapter" + adapter);
        if(adapter != null){
            if(joblist != null){
                Log.d(TAG, "onViewCreated: list updated");
                binding.progressCircular.setVisibility(View.GONE);
                binding.recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
                binding.recyclerView.setAdapter(adapter);
                Log.d(TAG, "onViewCreated at end job list: " + joblist);
                adapter.update(joblist);
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume: ");
    }
}