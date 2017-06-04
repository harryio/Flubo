package com.harryio.flubo.activity;

import android.app.LoaderManager;
import android.content.Loader;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.harryio.flubo.R;
import com.harryio.flubo.adapters.ReminderAdapter;
import com.harryio.flubo.data.ReminderLoader;
import com.harryio.flubo.model.Reminder;
import com.harryio.flubo.utils.Navigator;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class MainActivity extends BaseActivity implements ReminderAdapter.ClickListener,
        LoaderManager.LoaderCallbacks<List<Reminder>> {
    private static final String TAG = "MainActivity";

    private static final int LOADER_ID = 12;

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.appBarLayout)
    AppBarLayout appBarLayout;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    private Unbinder unbinder;
    private ReminderAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        unbinder = ButterKnife.bind(this);

        setUpRecyclerView();
        getLoaderManager().initLoader(LOADER_ID, null, this);
    }

    private void setUpRecyclerView() {
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new ReminderAdapter(this);
        adapter.setClickListener(this);

        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onListItemClicked(Reminder reminder) {
        Navigator.navigateToEditRemiderActivity(this, reminder.getId());
    }

    @Override
    public void onListCheckboxClicked(Reminder reminder, boolean isChecked) {

    }

    @OnClick(R.id.fab)
    public void onFabClicked() {
        Navigator.navigateToCreateReminderActivity(this);
    }

    @Override
    public Loader<List<Reminder>> onCreateLoader(int id, Bundle args) {
        return new ReminderLoader(this);
    }

    @Override
    public void onLoadFinished(Loader<List<Reminder>> loader, List<Reminder> data) {
        adapter.setReminders(data);
    }

    @Override
    public void onLoaderReset(Loader<List<Reminder>> loader) {
    }

    @Override
    protected void onDestroy() {
        unbinder.unbind();
        super.onDestroy();
    }
}
