package com.example.to_do_list;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.WindowCompat;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    // UI组件
    private LinearLayout taskContainer;
    private Toolbar toolbar;
    private List<Task> taskList = new ArrayList<>();
    List<Task> completedTaskList = new ArrayList<>();


    // 用于启动添加任务活动并获取结果
    private final ActivityResultLauncher<Intent> taskLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    String mode = result.getData().getStringExtra("mode");
                    Task task = (Task) result.getData().getSerializableExtra("task");
                    String originalTitle = result.getData().getStringExtra("originalTitle");

                    //判断是添加任务还是编辑任务
                    if ("add".equals(mode)) {
                        taskList.add(task);
                    } else if ("edit".equals(mode)) {
                        for (int i = 0; i < taskList.size(); i++) {
                            if (taskList.get(i).getTitle().equals(originalTitle)) {
                                taskList.set(i, task);
                                break;
                            }
                        }
                    }

                    refreshTaskViews();
                }
            }
    );


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 设置为全屏模式，隐藏状态栏
        getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
        );

        setContentView(R.layout.activity_main);

        // 初始化 taskContainer，确保变量名与 XML 中的 ID 一致
        taskContainer = findViewById(R.id.container);

        // 添加一些示例任务
        taskList.add(new Task("Buy Groceries", "Buy fruits and vegetables", "2025-04-25", "Shopping"));
        taskList.add(new Task("Finish Homework", "Complete math homework", "2025-04-26", "Study"));

        // 刷新任务视图
        if (taskContainer != null) {
            refreshTaskViews();
        } else {
            Log.e("MainActivity", "taskContainer is null!");
        }

        // 设置 Toolbar
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // 设置标题颜色为白色
        toolbar.setTitleTextColor(Color.WHITE);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);  // 加载菜单
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        // 点击添加任务按钮
        if (id == R.id.menu_add) {
            // 启动AddTaskActivity来添加任务
            Intent intent = new Intent(this, AddTaskActivity.class);
            intent.putExtra("mode", "add");
            taskLauncher.launch(intent);
            return true;

            // 点击设置按钮
        } else if (id == R.id.menu_settings) {
            // 跳转到设置页面（假设是SettingsActivity）
            startActivity(new Intent(this, SettingsActivity.class));
            return true;

            // 点击关于按钮
        } else if (id == R.id.menu_about) {
            // 跳转到 AboutActivity 页面
            startActivity(new Intent(this, AboutActivity.class));
            return true;
        }else if (id == R.id.menu_CompletedTasks) {
        Intent intent = new Intent(this, CompletedTasksActivity.class);
        intent.putExtra("completedTasks", new ArrayList<>(completedTaskList));
        startActivity(intent);
        return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences prefs = getSharedPreferences("settings", MODE_PRIVATE);
        int sortOption = prefs.getInt("sort_option", 0);
        sortTasks(sortOption);
        refreshTaskViews(); // 加上刷新UI
    }


    private void sortTasks(int option) {
        switch (option) {
            case 0: // 按时间
                Collections.sort(taskList, Comparator.comparing(Task::getTime));
                break;
            case 1: // 按标题
                Collections.sort(taskList, Comparator.comparing(Task::getTitle));
                break;
            case 2: // 按优先级
                Collections.sort(taskList, Comparator.comparingInt(Task::getPriority));
                break;
        }
    }


    private void refreshTaskViews() {
        if (taskContainer != null) {
            taskContainer.removeAllViews(); // 清空原有任务视图
            for (Task task : taskList) {
                View taskView = getLayoutInflater().inflate(R.layout.item_task, null);

                // 设置文本内容
                ((TextView) taskView.findViewById(R.id.taskTitle)).setText(task.getTitle());
                ((TextView) taskView.findViewById(R.id.taskDesc)).setText(task.getDescription());
                ((TextView) taskView.findViewById(R.id.taskTime)).setText(task.getTime());
                ((TextView) taskView.findViewById(R.id.taskCategory)).setText("分类：" + task.getCategory());

                // 绑定按钮逻辑
                taskView.findViewById(R.id.buttonDetails).setOnClickListener(v -> {
                    // 显示详情
                    showTaskDetails(task);
                });

                //编辑任务
                taskView.findViewById(R.id.buttonEdit).setOnClickListener(v -> {
                    Intent intent = new Intent(this, AddTaskActivity.class);
                    intent.putExtra("mode", "edit");
                    intent.putExtra("task", task);
                    intent.putExtra("originalTitle", task.getTitle());
                    taskLauncher.launch(intent);
                });

                //删除任务
                taskView.findViewById(R.id.buttonDelete).setOnClickListener(v -> {
                    // 删除任务并刷新视图
                    taskList.remove(task);
                    refreshTaskViews();
                });

                //完成任务
                CheckBox checkbox = taskView.findViewById(R.id.checkboxComplete);
                checkbox.setChecked(false); // 默认未完成
                checkbox.setOnCheckedChangeListener((buttonView, isChecked) -> {
                    if (isChecked) {
                        // 将任务移至已完成列表并刷新
                        taskList.remove(task);
                        completedTaskList.add(task);
                        refreshTaskViews();
                    }
                });

                taskContainer.addView(taskView); // 添加视图到容器中
            }
        } else {
            Log.e("MainActivity", "LinearLayout is null!");
        }
    }

    private void showTaskDetails(Task task) {
        String message = "标题: " + task.getTitle() + "\n"
                + "描述: " + task.getDescription() + "\n"
                + "时间: " + task.getTime() + "\n"
                + "分类: " + task.getCategory();

        new android.app.AlertDialog.Builder(this)
                .setTitle("任务详情")
                .setMessage(message)
                .setPositiveButton("确定", null)
                .show();
    }


}
