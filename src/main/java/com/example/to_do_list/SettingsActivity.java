package com.example.to_do_list;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.google.android.material.switchmaterial.SwitchMaterial;

public class SettingsActivity extends AppCompatActivity {

    private SharedPreferences sharedPrefs;
    private Switch switchReminder;
    private Switch switchTheme;
    private Spinner spinnerSort;

    private final String[] options = {"按时间", "按标题", "按优先级"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        sharedPrefs = getSharedPreferences("settings", MODE_PRIVATE);

        switchReminder = findViewById(R.id.switchReminder);
        switchTheme = findViewById(R.id.switchTheme);
        spinnerSort = findViewById(R.id.spinnerSortOrder);

        // 初始化状态
        switchReminder.setChecked(sharedPrefs.getBoolean("reminder", true));
        switchTheme.setChecked(sharedPrefs.getBoolean("dark_theme", false));

        // 初始化排序方式下拉菜单
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, options);
        spinnerSort.setAdapter(adapter);
        int sortIndex = sharedPrefs.getInt("sort_option", 0);  // 获取保存的排序选项索引
        spinnerSort.setSelection(sortIndex);  // 设置选中的项

        // 提醒开关监听
        switchReminder.setOnCheckedChangeListener((buttonView, isChecked) -> {
        sharedPrefs.edit().putBoolean("reminder", isChecked).apply();
    });

        // 主题切换监听
        switchTheme.setOnCheckedChangeListener((buttonView, isChecked) -> {
        sharedPrefs.edit().putBoolean("dark_theme", isChecked).apply();
        AppCompatDelegate.setDefaultNightMode(
            isChecked ? AppCompatDelegate.MODE_NIGHT_YES : AppCompatDelegate.MODE_NIGHT_NO
        );
    });

        // 排序选项监听
        spinnerSort.setOnItemSelectedListener(new android.widget.AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(android.widget.AdapterView<?> parent, android.view.View view, int position, long id) {
                sharedPrefs.edit().putInt("sort_option", position).apply();
                // TODO: 更新任务排序逻辑
            }

            @Override
            public void onNothingSelected(android.widget.AdapterView<?> parent) {
                // 可以不处理
            }
        });

    }

}
