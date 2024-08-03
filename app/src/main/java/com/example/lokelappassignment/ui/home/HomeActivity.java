package com.example.lokelappassignment.ui.home;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.lokelappassignment.R;
import com.example.lokelappassignment.databinding.ActivityHomeBinding;
import com.example.lokelappassignment.ui.home.fragments.BookmarkFeedFragment;
import com.example.lokelappassignment.ui.home.fragments.JobsFeedFragment;
import com.google.android.material.navigation.NavigationBarView;

public class HomeActivity extends AppCompatActivity {

    private static final int REQUEST_CALL_PERMISSION = 1;
    private String phoneNumber;

    ActivityHomeBinding binding;
    private JobsFeedFragment jobsFeedFragment;
    private BookmarkFeedFragment bookmarkFeedFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        jobsFeedFragment = new JobsFeedFragment();
        bookmarkFeedFragment = new BookmarkFeedFragment();
        loadFragment(jobsFeedFragment);

        Log.d("HomeActivity", "onCreate: ");
        binding.bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();

                if (id == R.id.jobs) {
                    // Handle jobs item
                    loadFragment(jobsFeedFragment);
                    return true;
                } else if (id == R.id.bookmarks) {
                    // Handle other item
                    loadFragment(bookmarkFeedFragment);
                    return true;
                } else {
                    return false;
                }
            }
        });
    }
    private void loadFragment(Fragment fragment) {

        if (fragment != null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.flFragment, fragment)
                    .commit();
        }
    }

    public void makePhoneCall(String phoneNumber) {
        this.phoneNumber = phoneNumber;
        if (ContextCompat.checkSelfPermission(HomeActivity.this,
                Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(HomeActivity.this,
                    new String[]{Manifest.permission.CALL_PHONE}, REQUEST_CALL_PERMISSION);
        } else {
            String dial = phoneNumber;
            startActivity(new Intent(Intent.ACTION_CALL, Uri.parse(dial)));
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CALL_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                makePhoneCall(phoneNumber);
            } else {
                // Permission denied, handle appropriately
            }
        }
    }
}