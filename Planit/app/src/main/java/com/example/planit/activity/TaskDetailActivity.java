package com.example.planit.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.planit.R;
import com.example.planit.entity.SingletonEntity;
import com.example.planit.entity.subject.Subject;
import com.example.planit.entity.subject.SubjectManager;
import com.example.planit.entity.task.Task;
import com.example.planit.entity.task.TaskAdapter;
import com.example.planit.entity.task.TaskManager;
import com.example.planit.fragment.SubjectFragment;
import com.example.planit.fragment.TaskFragment;

public class TaskDetailActivity extends AppCompatActivity {

    private TaskManager taskManager;
    private SubjectManager subjectManager;
    private int taskId;
    private String taskName;
    private String taskDescription;
    private boolean taskCompleted;
    private int taskImportance;
    private String taskType;
    private int taskSubjectId;
    private String taskDueDate;
    private String taskDueTime;

    // Initialize SubjectManager with Singleton pattern
    private void initSubjectManager() {
        subjectManager = (SubjectManager) SingletonEntity.getInstance().get(SubjectFragment.SHARED_SUBJECT_LIST);
        if (subjectManager == null) {
            subjectManager = new SubjectManager(this);
            SingletonEntity.getInstance().put(SubjectFragment.SHARED_SUBJECT_LIST, subjectManager);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_detail);

        // Retrieve task details from the intent
        taskId = getIntent().getIntExtra("TASK_ID", -1);
        taskName = getIntent().getStringExtra("TASK_NAME");
        taskDescription = getIntent().getStringExtra("TASK_DESCRIPTION");
        taskCompleted = getIntent().getBooleanExtra("TASK_COMPLETED", false);
        taskImportance = getIntent().getIntExtra("TASK_IMPORTANCE", 0);
        taskType = getIntent().getStringExtra("TASK_TYPE");
        taskSubjectId = getIntent().getIntExtra("TASK_SUBJECT_ID", -1);
        taskDueDate = getIntent().getStringExtra("TASK_DUE_DATE");
        taskDueTime = getIntent().getStringExtra("TASK_DUE_TIME");

        taskManager = (TaskManager) SingletonEntity.getInstance().get(TaskFragment.SHARED_TASK_LIST);
        initSubjectManager();

        // Initialize views and display task details
        TextView taskNameView = findViewById(R.id.taskDetailName);
        TextView taskDescriptionView = findViewById(R.id.taskDetailDescription);
        TextView taskImportanceView = findViewById(R.id.taskDetailImportance);
        TextView taskTypeView = findViewById(R.id.taskDetailType);
        TextView taskSubjectView = findViewById(R.id.taskDetailSubject);
        TextView taskDueDateView = findViewById(R.id.taskDetailDueDate);
        TextView taskDueTimeView = findViewById(R.id.taskDetailDueTime);
        Switch taskCompletedSwitch = findViewById(R.id.taskDetailCompletedSwitch);
        Button deleteButton = findViewById(R.id.deleteTaskButton);
        Button editButton = findViewById(R.id.editTaskButton);

        // Set text for task details
        taskNameView.setText(taskName);
        taskDescriptionView.setText(taskDescription.length() > 0 ? taskDescription : "");

        // Set importance level from the resource array
        String[] importanceArray = getResources().getStringArray(R.array.importance_array);
        String importanceText = "";

        if (taskImportance >= 1 && taskImportance <= 3) {
            importanceText = importanceArray[taskImportance - 1];
        }
        taskImportanceView.setText(getString(R.string.task_priority) + " " + importanceText);

        taskTypeView.setText(getString(R.string.task_type) + " " + taskType);

        // Display subject details
        Subject subject = subjectManager.getById(taskSubjectId);
        if (subject != null) {
            taskSubjectView.setText(getString(R.string.task_subject) + " " + subject.getName());
        } else {
            taskSubjectView.setText(getString(R.string.no_subject));
        }

        // Display due date and time
        taskDueDateView.setText(getString(R.string.task_due_date) + " " + Task.formatDate(taskDueDate));
        taskDueTimeView.setText(taskDueTime.isEmpty() ? "" : getString(R.string.task_due_time) + " " + taskDueTime);

        taskCompletedSwitch.setChecked(taskCompleted);

        // Update task status when switch is toggled
        taskCompletedSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            taskManager.toggleCompleted(taskId);
            taskCompleted = isChecked;
            Toast.makeText(this, getString(R.string.toast_task_status_updated), Toast.LENGTH_SHORT).show();
        });

        // Handle task deletion
        deleteButton.setOnClickListener(v -> {
            if (taskId != -1) {
                taskManager.deleteTask(taskId);
                Toast.makeText(this, getString(R.string.toast_task_deleted), Toast.LENGTH_SHORT).show();
                finish();
            } else {
                new AlertDialog.Builder(this)
                        .setTitle(getString(R.string.dialog_error_title))
                        .setMessage(getString(R.string.dialog_task_delete_error))
                        .setPositiveButton(getString(R.string.dialog_positive_button), null)
                        .show();
            }
        });

        // Handle task editing
        editButton.setOnClickListener(v -> {
            if (taskId != -1) {
                Intent intent = new Intent(this, TaskEditActivity.class);
                intent.putExtra("TASK_ID", taskId);
                intent.putExtra("TASK_NAME", taskName);
                intent.putExtra("TASK_DESCRIPTION", taskDescription);
                intent.putExtra("TASK_COMPLETED", taskCompleted);
                intent.putExtra("TASK_IMPORTANCE", taskImportance);
                intent.putExtra("TASK_TYPE", taskType);
                intent.putExtra("TASK_SUBJECT_ID", taskSubjectId);
                intent.putExtra("TASK_DUE_DATE", taskDueDate);
                intent.putExtra("TASK_DUE_TIME", taskDueTime);
                startActivity(intent);
            } else {
                new AlertDialog.Builder(this)
                        .setTitle(getString(R.string.dialog_error_title))
                        .setMessage(getString(R.string.dialog_unexpected_error))
                        .setPositiveButton(getString(R.string.dialog_positive_button), null)
                        .show();
            }
        });
    }
}
