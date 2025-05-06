package com.example.to_do_list;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.DatePicker;

import com.google.android.material.button.MaterialButton;

import java.util.Calendar;

import androidx.appcompat.app.AppCompatActivity;

public class AddTaskActivity extends AppCompatActivity {

    private EditText editTextTitle, editTextDescription, editTextCategory;
    private TextView textViewSelectedTime;
    private String selectedTime = "未选择";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_add_task);

        // 初始化UI组件
        editTextTitle = findViewById(R.id.editTextTitle);
        editTextDescription = findViewById(R.id.editTextDescription);
        editTextCategory = findViewById(R.id.editTextCategory);
        textViewSelectedTime = findViewById(R.id.textViewSelectedTime);

        MaterialButton buttonPickDateTime = findViewById(R.id.buttonPickDateTime);
        Button buttonSaveTask = findViewById(R.id.buttonSaveTask);

        // 获取传递的数据
        Task task = (Task) getIntent().getSerializableExtra("task");
        String mode = getIntent().getStringExtra("mode");

        if ("edit".equals(mode) && task != null) {
            // 如果是编辑模式，填充任务数据
            editTextTitle.setText(task.getTitle());
            editTextDescription.setText(task.getDescription());
            editTextCategory.setText(task.getCategory());
            textViewSelectedTime.setText(task.getTime());
            selectedTime = task.getTime();  // 保存任务的时间
        }

        // 选择日期和时间
        buttonPickDateTime.setOnClickListener(v -> pickDateTime());

        // 保存任务按钮（在点击“保存”时判断是添加还是编辑，并把完整任务信息传回）
        buttonSaveTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = editTextTitle.getText().toString();
                String description = editTextDescription.getText().toString();
                String category = editTextCategory.getText().toString();
                String time = selectedTime.equals("未选择") ? textViewSelectedTime.getText().toString() : selectedTime;

                Task newTask = new Task(title, description, time, category);

                Intent result = new Intent();
                result.putExtra("task", newTask);

                String mode = getIntent().getStringExtra("mode");
                result.putExtra("mode", mode);
                if ("edit".equals(mode)) {
                    String originalTitle = getIntent().getSerializableExtra("task") != null
                            ? ((Task) getIntent().getSerializableExtra("task")).getTitle()
                            : "";
                    result.putExtra("originalTitle", originalTitle);
                }

                setResult(RESULT_OK, result);
                finish();
            }
        });

    }

    // 时间选择器
    private void pickDateTime() {
        final Calendar calendar = Calendar.getInstance();
        DatePickerDialog datePicker = new DatePickerDialog(this,
                (view, year, month, day) -> {
                    TimePickerDialog timePicker = new TimePickerDialog(AddTaskActivity.this,
                            (view1, hour, minute) -> {
                                selectedTime = year + "-" + (month + 1) + "-" + day + " " + hour + ":" + String.format("%02d", minute);
                                textViewSelectedTime.setText(selectedTime);
                            }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true);
                    timePicker.show();
                }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        datePicker.show();
    }
}
