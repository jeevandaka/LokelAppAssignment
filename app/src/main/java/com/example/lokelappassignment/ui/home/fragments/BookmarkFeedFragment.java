package com.example.lokelappassignment.ui.home.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.lokelappassignment.MainApplication;
import com.example.lokelappassignment.R;
import com.example.lokelappassignment.data.Local.model.JobData;
import com.example.lokelappassignment.databinding.FragmentBookmarkFeedBinding;
import com.example.lokelappassignment.databinding.FragmentJobsBinding;
import com.example.lokelappassignment.ui.home.HomeActivity;
import com.example.lokelappassignment.ui.home.adapters.BookmarkAdapter;
import com.example.lokelappassignment.ui.home.adapters.JobsAdapter;

import java.util.ArrayList;
import java.util.List;

public class BookmarkFeedFragment extends Fragment {
    private static final String TAG = BookmarkFeedFragment.class.getSimpleName();

    private BookmarkAdapter adapter;
    private FragmentBookmarkFeedBinding binding;
    List<JobData> bookmarkedDataList = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView: ");
        binding = FragmentBookmarkFeedBinding.inflate(inflater);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.d(TAG, "onViewCreated: list" + bookmarkedDataList);
        MainApplication.getAppDataManager().getBookMarks();

        MainApplication.getAppDataManager().getBookmarkedJobs().observe(getViewLifecycleOwner(), bookmark ->{
            Log.d(TAG, "onViewCreated: bookmark" + bookmark);
            binding.progressCircular.setVisibility(View.GONE);
            if(bookmark != null){
                Log.d(TAG, "onViewCreated: " + bookmark);
                bookmarkedDataList = bookmark;
                if(adapter == null){
                    adapter = new BookmarkAdapter(bookmark);
                    binding.recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
                    binding.recyclerView.setAdapter(adapter);

                    adapter.SetOnCallClicked( jobData ->{
                        ((HomeActivity) getActivity()).makePhoneCall(jobData.getTel());
                    });
                }
                else{
                    adapter.updateBookmarkList(bookmark);
                }
            }
            else{
                Log.d(TAG, "onViewCreated: bookmark is null");
            }
        });


        if(adapter != null){
            if(bookmarkedDataList != null){
                binding.recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
                binding.recyclerView.setAdapter(adapter);
                adapter.updateBookmarkList(bookmarkedDataList);
            }
        }

    }
}