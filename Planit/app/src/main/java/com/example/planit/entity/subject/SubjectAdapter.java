package com.example.planit.entity.subject;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.planit.R;
//import com.example.planit.activity.SubjectEditActivity;

import java.util.List;

public class SubjectAdapter extends RecyclerView.Adapter<SubjectAdapter.SubjectViewHolder> {

    private List<Subject> subjectList;

    public SubjectAdapter(List<Subject> subjectList) {
        this.subjectList = subjectList;
    }

    @NonNull
    @Override
    public SubjectViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_subject, parent, false);
        return new SubjectViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SubjectViewHolder holder, int position) {
        Subject subject = subjectList.get(position);
        holder.subjectName.setText(subject.getName());
        holder.subjectColor.setBackgroundColor(subject.getColor());

//        holder.itemView.setOnClickListener(v -> {
//            Intent intent = new Intent(v.getContext(), SubjectEditActivity.class);
//            intent.putExtra("SUBJECT_ID", subject.getId());
//            intent.putExtra("SUBJECT_NAME", subject.getName());
//            intent.putExtra("SUBJECT_COLOR", subject.getColor());
//            v.getContext().startActivity(intent);
//        });
    }

    @Override
    public int getItemCount() {
        return subjectList.size();
    }

    public static class SubjectViewHolder extends RecyclerView.ViewHolder {
        TextView subjectName;
        View subjectColor;

        public SubjectViewHolder(@NonNull View itemView) {
            super(itemView);
            subjectName = itemView.findViewById(R.id.subjectName);
            subjectColor = itemView.findViewById(R.id.subjectColor);
        }
    }
}