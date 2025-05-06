package com.example.to_do_list;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

public class CompletedTasksActivity extends AppCompatActivity {

    private LinearLayout completedContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_completed_tasks);

        completedContainer = findViewById(R.id.completedContainer);

        List<Task> completedTasks = (List<Task>) getIntent().getSerializableExtra("completedTasks");

        for (Task task : completedTasks) {
            View view = getLayoutInflater().inflate(R.layout.item_task, null);
            ((TextView) view.findViewById(R.id.taskTitle)).setText(task.getTitle());
            ((TextView) view.findViewById(R.id.taskDesc)).setText(task.getDescription());
            ((TextView) view.findViewById(R.id.taskTime)).setText(task.getTime());
            ((TextView) view.findViewById(R.id.taskCategory)).setText("分类：" + task.getCategory());

            // 隐藏不需要的按钮
            view.findViewById(R.id.buttonEdit).setVisibility(View.GONE);
            view.findViewById(R.id.buttonDelete).setVisibility(View.GONE);
            view.findViewById(R.id.buttonDetails).setVisibility(View.GONE);
            view.findViewById(R.id.checkboxComplete).setVisibility(View.GONE);

            completedContainer.addView(view);
        }
    }
}

