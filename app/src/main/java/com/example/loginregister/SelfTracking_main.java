package com.example.loginregister;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.RadarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.RadarData;
import com.github.mikephil.charting.data.RadarDataSet;
import com.github.mikephil.charting.data.RadarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class SelfTracking_main extends AppCompatActivity {

    private TextView textView1, textView2;
    private FirebaseFirestore db;
    private Spinner chartTypeSpinner;
    private RadarChart radarChart;
    private BarChart barChart;
    private View addTaskButton;
    private RecyclerView taskRecyclerView;

    private final List<SelfTracking_task> tasks = new ArrayList<>();
    private Selftracking_A_task taskAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selftracking);

        db = FirebaseFirestore.getInstance();

        String email = "book@gmail.com"; // Replace with the actual email
        addRandomUsageData(email);
//        addRandomScoreData(email);

        // Initialize views
        chartTypeSpinner = findViewById(R.id.chartTypeSpinner);
        radarChart = findViewById(R.id.radarChart);
        barChart = findViewById(R.id.barChart);
        addTaskButton = findViewById(R.id.addTaskButton);
        taskRecyclerView = findViewById(R.id.taskRecyclerView);
        textView1 = findViewById(R.id.TextInItem1);
        textView2 = findViewById(R.id.TextInItem2);

        // Set up the spinner
        ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter.createFromResource(
                this, R.array.chart_types, android.R.layout.simple_spinner_item);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        chartTypeSpinner.setAdapter(spinnerAdapter);

        addTaskButton.setOnClickListener(v -> {
            showAddTaskDialog();
        });

        chartTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                String selectedType = parentView.getItemAtPosition(position).toString();
                updateChartVisibility(selectedType);
                updateTaskViews(selectedType);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // Do nothing
            }
        });

        retrieveAndDisplayUsageData();
        setupRadarChart();
        retrieveAndDisplayRadarData();

        // Initialize Task RecyclerView
        taskAdapter = new Selftracking_A_task(tasks);
        taskRecyclerView.setAdapter(taskAdapter);
        taskRecyclerView.setLayoutManager(new LinearLayoutManager(this));
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
            SelfTracking_task task = new SelfTracking_task(taskName, taskSubject);
            tasks.add(task);
            taskAdapter.notifyDataSetChanged();
        });
        builder.setNegativeButton("Cancel", null);

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void updateChartVisibility(String selectedType) {
        if (selectedType.equals(getString(R.string.usage))) {
            barChart.setVisibility(View.VISIBLE);
            radarChart.setVisibility(View.GONE);
            textView1.setVisibility(View.VISIBLE);
            textView2.setVisibility(View.GONE);
        } else if (selectedType.equals(getString(R.string.score))) {
            radarChart.setVisibility(View.VISIBLE);
            barChart.setVisibility(View.GONE);
            textView1.setVisibility(View.GONE);
            textView2.setVisibility(View.VISIBLE);
        } else if (selectedType.equals(getString(R.string.task_reminder))) {
            barChart.setVisibility(View.GONE);
            radarChart.setVisibility(View.GONE);
            textView1.setVisibility(View.GONE);
            textView2.setVisibility(View.GONE);
        }
    }

    private void updateTaskViews(String selectedType) {
        boolean showTasks = selectedType.equals(getString(R.string.task_reminder));
        int visibility = showTasks ? View.VISIBLE : View.GONE;

        addTaskButton.setVisibility(visibility);
        taskRecyclerView.setVisibility(visibility);
    }

    private void setupRadarChart() {
        RadarChart radarChart = findViewById(R.id.radarChart);

        XAxis xAxisRadar = radarChart.getXAxis();
        xAxisRadar.setValueFormatter(new IndexAxisValueFormatter(getSubjectLabels()));

        radarChart.getDescription().setEnabled(false);
        radarChart.getLegend().setEnabled(false);

        radarChart.invalidate();
    }

    private List<String> getSubjectLabels() {
        return Arrays.asList("Mathematics", "Geography", "Literature");
    }

    private void addRandomUsageData(String email) {
        Map<String, Object> usageHours = new HashMap<>();
        Random random = new Random();

        for (int dayIndex = 0; dayIndex < 7; dayIndex++) {
            String day = getDayFromIndex(dayIndex);
            long hours = random.nextInt(10) + 1;
            usageHours.put(day, hours);
        }

        db.collection("user_usage")
                .document(email)
                .set(usageHours)
                .addOnSuccessListener(aVoid -> {
                    // Data added successfully
                })
                .addOnFailureListener(e -> {
                    // Error adding data
                });
    }

    private void addRandomScoreData(String email) {
        Map<String, Object> subjectScores = new HashMap<>();
        Random random = new Random();

        List<String> subjects = getSubjectLabels();
        for (String subject : subjects) {
            float score = random.nextFloat() * 100;
            subjectScores.put(subject, score);
        }

        db.collection("user_score")
                .document(email)
                .set(subjectScores)
                .addOnSuccessListener(aVoid -> {
                    // Data added successfully
                })
                .addOnFailureListener(e -> {
                    // Error adding data
                });
    }

    private void retrieveAndDisplayUsageData() {
        db.collection("user_usage")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        List<BarEntry> entries = new ArrayList<>();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            String email = (String) document.getId();
                            for (int dayIndex = 0; dayIndex < 7; dayIndex++) {
                                String day = getDayFromIndex(dayIndex);
                                Long hours = (Long) document.get(day);
                                entries.add(new BarEntry(dayIndex, hours));
                            }
                        }

                        displayBarGraph(entries);
                    } else {
                        // Error getting documents
                    }
                });
    }

    private void retrieveAndDisplayRadarData() {
        db.collection("user_score")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        List<RadarEntry> entries = new ArrayList<>();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            String email = (String) document.getId();
                            for (String subject : getSubjectLabels()) {
                                Float score = document.getDouble(subject).floatValue();
                                entries.add(new RadarEntry(score));
                            }
                        }

                        displayRadarGraph(entries);
                    } else {
                        // Error getting documents
                    }
                });
    }

    private void displayBarGraph(List<BarEntry> entries) {
        BarChart barChart = findViewById(R.id.barChart);

        BarDataSet dataSet = new BarDataSet(entries, "App Usage Hours");
        dataSet.setValueTextSize(12f);
        dataSet.setValueTextColor(Color.BLACK);

        int[] pastelColors = {
                Color.rgb(111, 97, 192),
                Color.rgb(160, 132, 232),
                Color.rgb(139, 232, 229),
                Color.rgb(213, 255, 228)
        };
        dataSet.setColors(pastelColors);

        BarData barData = new BarData(dataSet);
        barChart.setData(barData);

        barChart.getDescription().setEnabled(false);
        barChart.getLegend().setEnabled(false);

        barChart.getAxisRight().setEnabled(false);
        barChart.getAxisLeft().setAxisMinimum(0f);

        XAxis xAxis = barChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setGranularity(1f);
        xAxis.setValueFormatter(new IndexAxisValueFormatter(getDaysOfWeek()));

        barChart.invalidate();
        barChart.animateY(1000);
    }

    private void displayRadarGraph(List<RadarEntry> entries) {
        RadarChart radarChart = findViewById(R.id.radarChart);

        RadarDataSet dataSet = new RadarDataSet(entries, "Subject Scores");
        dataSet.setColor(Color.BLUE);
        dataSet.setFillColor(Color.BLUE);
        dataSet.setDrawFilled(true);
        dataSet.setFillAlpha(180);
        dataSet.setLineWidth(2f);
        dataSet.setValueTextSize(12f);
        dataSet.setDrawHighlightCircleEnabled(true);
        dataSet.setDrawHighlightIndicators(false);

        int[] pastelColors = {
                Color.rgb(111, 97, 192),
                Color.rgb(160, 132, 232),
                Color.rgb(139, 232, 229),
                Color.rgb(213, 255, 228)
        };
        dataSet.setColors(pastelColors);

        RadarData radarData = new RadarData(dataSet);
        radarChart.setData(radarData);

        XAxis xAxisRadar = radarChart.getXAxis();
        xAxisRadar.setValueFormatter(new IndexAxisValueFormatter(getSubjectLabels()));

        radarChart.getDescription().setEnabled(false);
        radarChart.getLegend().setEnabled(false);

        radarChart.invalidate();
        barChart.animateY(1000);
    }

    private String getDayFromIndex(int index) {
        String[] days = {"Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"};
        return days[index];
    }

    private List<String> getDaysOfWeek() {
        return Arrays.asList("Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat");
    }
}