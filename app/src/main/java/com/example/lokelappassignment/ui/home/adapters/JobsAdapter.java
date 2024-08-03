package com.example.lokelappassignment.ui.home.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lokelappassignment.MainApplication;
import com.example.lokelappassignment.R;
import com.example.lokelappassignment.data.Local.model.JobData;
import com.example.lokelappassignment.ui.home.HomeActivity;
import com.example.lokelappassignment.ui.home.fragments.JobsFeedFragment;
import com.google.android.material.button.MaterialButton;

import java.util.List;

public class JobsAdapter extends RecyclerView.Adapter<JobsAdapter.MyJobViewHolder> {
    private List<JobData> itemList;
    private OnCardClickListener onCardClickListener;
    private OnCallClickListener onCallClickListener;

    public JobsAdapter(List<JobData> itemList) {
        this.itemList = itemList;
    }

    @Override
    public MyJobViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.job_item, parent, false);
        return new MyJobViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyJobViewHolder holder, int position) {
        holder.bind();
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }


    public class MyJobViewHolder extends RecyclerView.ViewHolder {
        public TextView jobRole;
        public TextView title;
        public TextView salary;
        public TextView place;
        public TextView experience;
        public MaterialButton callHr;
        public ImageView bookmarkIcon;
        public CardView rootCardView;

        public MyJobViewHolder(View itemView) {
            super(itemView);
            jobRole = itemView.findViewById(R.id.companyNameTextView);
            title = itemView.findViewById(R.id.titleTextView);
            salary = itemView.findViewById(R.id.salaryTextView);
            place = itemView.findViewById(R.id.locationTextView);
            experience = itemView.findViewById(R.id.expTextView);
            callHr = itemView.findViewById(R.id.materialButton);
            bookmarkIcon = itemView.findViewById(R.id.bookmark_icon);
            rootCardView = itemView.findViewById(R.id.rootCard);

            setUpListener();
        }

        public void setUpListener(){
            rootCardView.setOnClickListener( v -> {
                onCardClickListener.onCardClicked(itemList.get(getAdapterPosition()));
            });

            callHr.setOnClickListener(v->{
                onCallClickListener.onCallClicked(itemList.get(getAdapterPosition()));
            });

            bookmarkIcon.setOnClickListener(v->{
                if(!itemList.get(getAdapterPosition()).isBookmarked()){
                    itemList.get(getAdapterPosition()).setBookmarked(true);
                    MainApplication.getAppDatabase().jobDataDao().insert(itemList.get(getAdapterPosition()));
                    bookmarkIcon.setImageResource(R.drawable.ic_filled_bookmark_24);
                }
                else{
                    itemList.get(getAdapterPosition()).setBookmarked(false);
                    MainApplication.getAppDatabase().jobDataDao().delete(itemList.get(getAdapterPosition()));
                    bookmarkIcon.setImageResource(R.drawable.ic_bookmark_border_24);
                }
            });
        }

        public void bind(){
            jobRole.setText(itemList.get(getAdapterPosition()).getJobRole());
            title.setText(itemList.get(getAdapterPosition()).getTitle());
            salary.setText(itemList.get(getAdapterPosition()).getSalary());
            place.setText(itemList.get(getAdapterPosition()).getLocation());
            experience.setText(itemList.get(getAdapterPosition()).getExperience());
            callHr.setText(itemList.get(getAdapterPosition()).getButtonText());
            if(MainApplication
                    .getAppDatabase()
                    .jobDataDao()
                    .isJobIdPresent(itemList.get(getAdapterPosition()).getId()) > 0
            )
            {
                bookmarkIcon.setImageResource(R.drawable.ic_filled_bookmark_24);
            }
            else{
                bookmarkIcon.setImageResource(R.drawable.ic_bookmark_border_24);
            }
        }
    }

    public void updateJobList(List<JobData> list){
        this.itemList.addAll(list);
        notifyDataSetChanged();
    }

    public void update(List<JobData> list){
        this.itemList = list;
        notifyDataSetChanged();
    }

    public interface OnCardClickListener{
        void onCardClicked(JobData jobData);
    }

    public interface OnCallClickListener{
        void onCallClicked(JobData jobData);
    }

    public void SetOnCardClickListener(OnCardClickListener onCardClickListener){
        this.onCardClickListener = onCardClickListener;
    }

    public void SetOnCallClicked(OnCallClickListener onCallClickListener){
        this.onCallClickListener = onCallClickListener;
    }
}