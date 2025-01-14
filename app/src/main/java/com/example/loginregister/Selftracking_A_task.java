package com.example.loginregister;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class Selftracking_A_task extends RecyclerView.Adapter<Selftracking_A_task.TaskViewHolder> {

    private List<SelfTracking_task> tasks;

    public Selftracking_A_task(List<SelfTracking_task> tasks) {
        this.tasks = tasks;
    }

    @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.task_reminder_item_task, parent, false);
        return new TaskViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskViewHolder holder, int position) {
        SelfTracking_task task = tasks.get(position);
        holder.bind(task);
    }

    @Override
    public int getItemCount() {
        return tasks.size();
    }

    class TaskViewHolder extends RecyclerView.ViewHolder {

        private final TextView taskNameTextView;
        private final TextView taskSubjectTextView;
        private final CheckBox taskCheckBox;
        private final TextView taskDetailsTextView;

        public TaskViewHolder(View itemView) {
            super(itemView);
            taskNameTextView = itemView.findViewById(R.id.taskNameTextView);
            taskSubjectTextView = itemView.findViewById(R.id.taskSubjectTextView);
            taskCheckBox = itemView.findViewById(R.id.taskCheckBox);
            taskDetailsTextView = itemView.findViewById(R.id.taskDetailsTextView);

            taskNameTextView.setOnClickListener(v -> {
                SelfTracking_task task = tasks.get(getAdapterPosition());
                task.setDetails(taskDetailsTextView.getText().toString());
                taskDetailsTextView.setVisibility(View.VISIBLE);
                taskDetailsTextView.setText(task.getDetails());
            });

            taskCheckBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
                SelfTracking_task task = tasks.get(getAdapterPosition());
                task.setDone(isChecked);
                if (isChecked) {
                    String warningMessage = task.getName() + " is done."; // Use getName() method
                    // Replace with your own Toast implementation
                    // Toast.makeText(itemView.getContext(), warningMessage, Toast.LENGTH_SHORT).show();
                    removeCompletedTask(task); // Remove task immediately
                }
            });
        }

        public void bind(SelfTracking_task task) {
            taskNameTextView.setText(task.getName()); // Use getName() method
            taskSubjectTextView.setText(task.getSubject()); // Use getSubject() method
            taskCheckBox.setChecked(task.isDone());

            if (task.isDone()) {
                itemView.setVisibility(View.GONE);
                taskDetailsTextView.setVisibility(View.GONE);
            } else {
                itemView.setVisibility(View.VISIBLE);
                taskDetailsTextView.setVisibility(View.GONE);
            }
        }

        private void removeCompletedTask(SelfTracking_task task) {
            tasks.remove(task);
            notifyDataSetChanged();
        }
    }

    private class SwipeToDeleteCallback extends ItemTouchHelper.SimpleCallback {

        public SwipeToDeleteCallback() {
            super(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT);
        }

        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            int position = viewHolder.getAdapterPosition();
            SelfTracking_task task = tasks.get(position);
            tasks.remove(task);
            notifyDataSetChanged();
        }
    }
}
