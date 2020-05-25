package com.liuchenxi.mygarden;

import android.os.Bundle;
import android.os.PersistableBundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.tabs.TabLayout;
import com.liuchenxi.foundation.base.BaseActivity;

public class MainActivity2 extends AppCompatActivity {
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        setContentView(R.layout.activity_main);
//        TabLayout mtab = findViewById(R.id.main_top_tab);
//        mtab.addTab(mtab.newTab().setText("ahahaha"));
    }
}
