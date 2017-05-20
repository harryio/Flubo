package com.harryio.flubo.activity;

import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.harryio.flubo.DataLayout;
import com.harryio.flubo.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CreateReminderActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.title)
    EditText title;
    @BindView(R.id.description)
    EditText description;
    @BindView(R.id.appBar)
    AppBarLayout appBar;
    @BindView(R.id.remindView)
    DataLayout remindView;
    @BindView(R.id.dateView)
    DataLayout dateView;
    @BindView(R.id.timeView)
    DataLayout timeView;
    @BindView(R.id.repeatView)
    DataLayout repeatView;
    @BindView(R.id.dateTimeView)
    LinearLayout dateTimeView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_reminder);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.remindView)
    public void onRemindViewClicked() {
    }

    @OnClick(R.id.dateView)
    public void onDateViewClicked() {
    }

    @OnClick(R.id.timeView)
    public void onTimeViewClicked() {
    }

    @OnClick(R.id.repeatView)
    public void onRepeatViewClicked() {
    }

    @OnClick(R.id.dateTimeView)
    public void onDateTimeViewClicked() {
    }

    @OnClick(R.id.fab)
    public void onFabClicked() {
    }
}
