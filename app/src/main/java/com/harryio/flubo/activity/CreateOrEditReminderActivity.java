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

import com.harryio.flubo.R;
import com.harryio.flubo.customview.DataLayout;
import com.harryio.flubo.model.Reminder;
import com.harryio.flubo.model.RepeatInterval;
import com.harryio.flubo.utils.DateUtils;
import com.harryio.flubo.utils.PopupUtils;
import com.harryio.flubo.utils.Utils;

import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CreateOrEditReminderActivity extends BaseActivity implements
        DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener{
    private static final String TAG = "CreateReminderActivity";
    private static final int ANIM_DURATION = 165;

    private static final String ARG_REMINDER_ID = PACKAGE_NAME + "ARG_REMINDER_ID";

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
    private RepeatInterval repeatInterval = RepeatInterval.ONE_TIME;

    private String[] repeatIntervalStrings;

    private Reminder reminder;

    public static Intent getCallingIntent(Context context) {
        return new Intent(context, CreateOrEditReminderActivity.class);
    }

    public static Intent getCallingIntent(Context context, String reminderId) {
        Intent intent = new Intent(context, CreateOrEditReminderActivity.class);
        intent.putExtra(ARG_REMINDER_ID, reminderId);

        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_or_edit_reminder);
        ButterKnife.bind(this);

        repeatIntervalStrings = getResources().getStringArray(R.array.repeat_intervals);
        titleEdittext.requestFocus();
        setUpReminderIfPresent();
        setUpCalendar();
        setUpDateAndTimePickerDialogs();
        setUpReminderViews();
    }

    private void setUpReminderIfPresent() {
        Intent intent = getIntent();
        if (intent.hasExtra(ARG_REMINDER_ID)) {
            String reminderId = intent.getStringExtra(ARG_REMINDER_ID);
            //todo fetch reminder here
            titleEdittext.setText(reminder.getTitle());
            descriptionEdittext.setText(reminder.getDescription());
            shouldSetReminder = reminder.isRemiderSet();
            repeatInterval = reminder.getRepeatInterval();
        }
    }

    private void setUpCalendar() {
        calendar = Calendar.getInstance();
        if (reminder == null) {
            calendar.add(Calendar.HOUR, 1);
        } else {
            calendar.setTimeInMillis(reminder.getRemindTime());
        }

        if (shouldSetReminder) {
            setReminder();
        }
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
        long time = calendar.getTimeInMillis();
        dateView.setDataValue(DateUtils.getDateString(this, time));
        timeView.setDataValue(DateUtils.getTimeString(this, time));

        repeatView.setDataValue(repeatIntervalStrings[repeatInterval.ordinal()]);
    }

    @OnClick(R.id.remindView)
    public void onRemindViewClicked() {
        Utils.hideKeyboard(this, titleEdittext);
        Utils.hideKeyboard(this, descriptionEdittext);

        shouldSetReminder = !shouldSetReminder;
        if (shouldSetReminder) {
            setReminder();
        } else {
            removeReminder();
        }
    }

    private void setReminder() {
        remindView.setDataValue("ON");
        animateInDateTimeViews();
    }

    private void removeReminder() {
        remindView.setDataValue("OFF");
        animateOutDateTimeViews();
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
        PopupUtils.showRepeatIntervalDialog(this, repeatInterval.ordinal(), new PopupUtils.RepeatIntervalSelectedListener() {
            @Override
            public void onRepeatIntervalSelected(int which, String intervalString) {
                repeatInterval = RepeatInterval.values()[which];
                repeatView.setDataValue(intervalString);
            }
        });
    }

    @OnClick(R.id.fab)
    public void onFabClicked() {
        if (!isReminderDataValid()) return;

        Reminder reminder = new Reminder.Builder()
                .title(titleEdittext.getText().toString())
                .description(descriptionEdittext.getText().toString())
                .isCompleted(false)
                .remindAt(calendar.getTimeInMillis())
                .withRepeatInterval(repeatInterval)
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
