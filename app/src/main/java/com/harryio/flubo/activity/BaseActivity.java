package com.harryio.flubo.activity;

import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;


public abstract class BaseActivity extends AppCompatActivity {
    public void showShortToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    public void showLongToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }
}
