package com.harryio.flubo.activity;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.harryio.flubo.DataLayout;
import com.harryio.flubo.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CreateReminderActivity extends BaseActivity {
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

    public static Intent getCallingIntent(Context context) {
        return new Intent(context, CreateReminderActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_reminder);
        ButterKnife.bind(this);
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
