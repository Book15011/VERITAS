package com.example.loginregister;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class Selftracking_taskren extends AppCompatActivity {

    private final List<Task> tasks = new ArrayList<>();
    private TaskAdapter taskAdapter;

    @SuppressLint("NotifyDataSetChanged")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_taskreminder);

        RecyclerView taskRecyclerView = findViewById(R.id.taskRecyclerView);
        Button addTaskButton = findViewById(R.id.addTaskButton);

        taskAdapter = new TaskAdapter();
        taskAdapter.setTasks(tasks);
        taskRecyclerView.setAdapter(taskAdapter);
        taskRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new SwipeToDeleteCallback());
        itemTouchHelper.attachToRecyclerView(taskRecyclerView);

        addTaskButton.setOnClickListener(v -> {
            showAddTaskDialog();
        });
    }

    private void showAddTaskDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Add New Task");

        View dialogView = getLayoutInflater().inflate(R.layout.task_remainder_dialog_add_task, null);
        EditText taskNameEditText = dialogView.findViewById(R.id.taskNameEditText);
        EditText taskSubjectEditText = dialogView.findViewById(R.id.taskSubjectEditText);

        builder.setView(dialogView);
        builder.setPositiveButton("Done", (dialog, which) -> {
            String taskName = taskNameEditText.getText().toString();
            String taskSubject = taskSubjectEditText.getText().toString();
            Task task = new Task(taskName, taskSubject);
            tasks.add(task);
            taskAdapter.notifyDataSetChanged();
        });
        builder.setNegativeButton("Cancel", null);

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public class Task {
        private final String name;
        private final String subject;
        private String details;
        private boolean isDone;

        public Task(String name, String subject) {
            this.name = name;
            this.subject = subject;
            this.details = "";
            this.isDone = false;
        }

        public String getDetails() {
            return details;
        }

        public void setDetails(String details) {
            this.details = details;
        }

        public boolean isDone() {
            return isDone;
        }

        public void setDone(boolean done) {
            isDone = done;
        }
    }

    public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskViewHolder> {
        private List<Task> tasks;

        public void setTasks(List<Task> tasks) {
            this.tasks = tasks;
            notifyDataSetChanged();
        }

        @NonNull
        @Override
        public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.task_reminder_item_task, parent, false);
            return new TaskViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull TaskViewHolder holder, int position) {
            Task task = tasks.get(position);
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
                    Task task = tasks.get(getAdapterPosition());
                    task.setDetails(taskDetailsTextView.getText().toString());
                    taskDetailsTextView.setVisibility(View.VISIBLE);
                    taskDetailsTextView.setText(task.getDetails());
                });

                taskCheckBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
                    Task task = tasks.get(getAdapterPosition());
                    task.setDone(isChecked);
                    if (isChecked) {
                        String warningMessage = task.name + " is done.";
                        Toast.makeText(itemView.getContext(), warningMessage, Toast.LENGTH_SHORT).show();
                        removeCompletedTask(task); // Remove task immediately
                    }
                });
            }

            @SuppressLint("NotifyDataSetChanged")
            public void bind(Task task) {
                taskNameTextView.setText(task.name);
                taskSubjectTextView.setText(task.subject);
                taskCheckBox.setChecked(task.isDone());

                if (task.isDone()) {
                    itemView.setVisibility(View.GONE);
                    taskDetailsTextView.setVisibility(View.GONE);
                } else {
                    itemView.setVisibility(View.VISIBLE);
                    taskDetailsTextView.setVisibility(View.GONE);
                }
            }

            private void removeCompletedTask(Task task) {
                tasks.remove(task);
                taskAdapter.notifyDataSetChanged();
            }
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
            Task task = tasks.get(position);
            tasks.remove(task);
            taskAdapter.notifyDataSetChanged();
        }
    }
}
