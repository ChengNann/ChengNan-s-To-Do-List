package com.example.to_do_list;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class AboutActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about);  // 设置为你的 about.xml 布局文件

        // 获取返回按钮
        Button buttonBack = findViewById(R.id.buttonBack);

        // 设置返回按钮的点击事件
        buttonBack.setOnClickListener(v -> {
            // 返回到主界面
            finish();
        });
    }
}
