package com.example.planit.entity.subject;

import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.planit.R;
import com.example.planit.activity.SubjectEditActivity;

import java.util.List;

public class SubjectAdapter extends RecyclerView.Adapter<SubjectAdapter.SubjectViewHolder> {

    private List<Subject> subjectList;

    // Constructor to initialize the list of subjects
    public SubjectAdapter(List<Subject> subjectList) {
        this.subjectList = subjectList;
    }

    // Create a new ViewHolder for each item in the RecyclerView
    @NonNull
    @Override
    public SubjectViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_subject, parent, false);
        return new SubjectViewHolder(view);
    }

    // Bind the data to the ViewHolder at the specified position
    @Override
    public void onBindViewHolder(@NonNull SubjectViewHolder holder, int position) {
        Subject subject = subjectList.get(position);
        holder.subjectName.setText(subject.getName());
        holder.subjectColor.setBackgroundColor(subject.getColor()); // Set the background color based on the subject's color

        // Set an OnClickListener to navigate to SubjectEditActivity when the item is clicked
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), SubjectEditActivity.class);
            // Pass the subject data to the edit activity
            intent.putExtra("SUBJECT_ID", subject.getId());
            intent.putExtra("SUBJECT_NAME", subject.getName());
            intent.putExtra("SUBJECT_COLOR", subject.getColor());
            v.getContext().startActivity(intent);
        });
    }

    // Return the total number of subjects in the list
    @Override
    public int getItemCount() {
        return subjectList.size();
    }

    // ViewHolder class to hold the views for each item in the RecyclerView
    public static class SubjectViewHolder extends RecyclerView.ViewHolder {
        TextView subjectName;
        View subjectColor;

        // Constructor to initialize the TextView and View for the subject's name and color
        public SubjectViewHolder(@NonNull View itemView) {
            super(itemView);
            subjectName = itemView.findViewById(R.id.subjectName);
            subjectColor = itemView.findViewById(R.id.subjectColor);
        }
    }
}
