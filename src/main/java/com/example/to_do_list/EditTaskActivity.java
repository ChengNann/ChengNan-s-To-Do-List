package com.example.to_do_list;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class EditTaskActivity extends AppCompatActivity {

    private EditText editTitle, editDesc, editTime, editCategory;
    private Button buttonSave;
    private Task originalTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_task);

        editTitle = findViewById(R.id.editTitle);
        editDesc = findViewById(R.id.editDesc);
        editTime = findViewById(R.id.editTime);
        editCategory = findViewById(R.id.editCategory);
        buttonSave = findViewById(R.id.buttonSave);

        originalTask = (Task) getIntent().getSerializableExtra("task");
        if (originalTask != null) {
            editTitle.setText(originalTask.getTitle());
            editDesc.setText(originalTask.getDescription());
            editTime.setText(originalTask.getTime());
            editCategory.setText(originalTask.getCategory());
        }

        buttonSave.setOnClickListener(v -> {
            String newTitle = editTitle.getText().toString();
            String newDesc = editDesc.getText().toString();
            String newTime = editTime.getText().toString();
            String newCategory = editCategory.getText().toString();

            Task updatedTask = new Task(newTitle, newDesc, newTime, newCategory);
            Intent resultIntent = new Intent();
            resultIntent.putExtra("updatedTask", updatedTask);
            resultIntent.putExtra("originalTitle", originalTask.getTitle()); // 用于识别任务
            setResult(RESULT_OK, resultIntent);
            finish();
        });
    }
}
