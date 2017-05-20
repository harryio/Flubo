package com.harryio.flubo.activity;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TimePicker;

import com.harryio.flubo.DataLayout;
import com.harryio.flubo.R;
import com.harryio.flubo.model.Reminder;
import com.harryio.flubo.utils.DateUtils;

import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CreateReminderActivity extends BaseActivity implements
        DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener{
    private static final String TAG = "CreateReminderActivity";
    private static final int ANIM_DURATION = 200;

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.title)
    EditText titleEdittext;
    @BindView(R.id.description)
    EditText descriptionEdittext;
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

    private boolean shouldSetReminder;
    private Calendar calendar;
    private DatePickerDialog datePickerDialog;
    private TimePickerDialog timePickerDialog;

    public static Intent getCallingIntent(Context context) {
        return new Intent(context, CreateReminderActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_reminder);
        ButterKnife.bind(this);

        setUpCalendar();
        setUpDateAndTimePickerDialogs();
        setUpReminderViews();
    }

    private void setUpCalendar() {
        calendar = Calendar.getInstance();
        calendar.add(Calendar.HOUR, 1);
    }

    private void setUpDateAndTimePickerDialogs() {
        datePickerDialog = new DatePickerDialog(this, this,
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH));

        timePickerDialog = new TimePickerDialog(this, this,
                calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE),
                DateFormat.is24HourFormat(this));
    }

    private void setUpReminderViews() {
        dateView.setDataValue("Today");
        timeView.setDataValue(DateFormat.getTimeFormat(this).format(calendar.getTime()));
    }

    @OnClick(R.id.remindView)
    public void onRemindViewClicked() {
        shouldSetReminder = !shouldSetReminder;
        if (shouldSetReminder) {
            animateInDateTimeViews();
        } else {
            animateOutDateTimeViews();
        }
    }

    @OnClick(R.id.dateView)
    public void onDateViewClicked() {
        datePickerDialog.show();
    }

    @OnClick(R.id.timeView)
    public void onTimeViewClicked() {
        timePickerDialog.show();
    }

    @OnClick(R.id.repeatView)
    public void onRepeatViewClicked() {
    }

    @OnClick(R.id.fab)
    public void onFabClicked() {
        if (!isReminderDataValid()) return;

        Reminder reminder = new Reminder.Builder()
                .title(titleEdittext.getText().toString())
                .description(descriptionEdittext.getText().toString())
                .isCompleted(false)
                .remindAt(calendar.getTimeInMillis())
                .create();
    }

    private boolean isReminderDataValid() {
        String title = titleEdittext.getText().toString();
        if (TextUtils.isEmpty(title)) {
            showShortToast(getString(R.string.create_reminder_empty_title_error_message));
            return false;
        }

        if (DateUtils.isTimeInPast(calendar.getTimeInMillis())) {
            showShortToast(getString(R.string.create_reminder_invalid_reminder_time_message));
            return false;
        }

        return true;
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

        if (DateUtils.isToday(calendar.getTimeInMillis())) {
            dateView.setDataValue("Today");
        } else {
            dateView.setDataValue(DateFormat.getDateFormat(this).format(calendar.getTime()));
        }
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
        calendar.set(Calendar.MINUTE, minute);

        timeView.setDataValue(DateFormat.getTimeFormat(this).format(calendar.getTime()));
    }

    private void animateInDateTimeViews() {
        int childCount = dateTimeView.getChildCount();
        for (int i = 0; i < childCount; i++) {
            final View childView = dateTimeView.getChildAt(i);
            ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(childView, "alpha", 0, 1);
            objectAnimator.setDuration(ANIM_DURATION);
            objectAnimator.setStartDelay(i * ANIM_DURATION);
            objectAnimator.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {
                    childView.setAlpha(0);
                    childView.setVisibility(View.VISIBLE);
                }

                @Override
                public void onAnimationEnd(Animator animation) {

                }

                @Override
                public void onAnimationCancel(Animator animation) {

                }

                @Override
                public void onAnimationRepeat(Animator animation) {

                }
            });
            objectAnimator.start();
        }
    }

    private void animateOutDateTimeViews() {
        int childCount = dateTimeView.getChildCount();
        if (childCount == 0) return;

        for (int i = childCount - 1; i >= 0; --i) {
            final View childView = dateTimeView.getChildAt(i);
            ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(childView, "alpha", 1, 0);
            objectAnimator.setDuration(ANIM_DURATION);
            objectAnimator.setStartDelay(childCount * ANIM_DURATION - i * ANIM_DURATION);
            objectAnimator.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {
                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    childView.setVisibility(View.GONE);
                }

                @Override
                public void onAnimationCancel(Animator animation) {

                }

                @Override
                public void onAnimationRepeat(Animator animation) {

                }
            });
            objectAnimator.start();
        }
    }
}
