package com.harryio.flubo.activity;

import android.app.LoaderManager;
import android.content.Loader;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.harryio.flubo.R;
import com.harryio.flubo.adapters.ReminderAdapter;
import com.harryio.flubo.data.ReminderDAO;
import com.harryio.flubo.data.ReminderLoader;
import com.harryio.flubo.model.Reminder;
import com.harryio.flubo.service.AlarmHelperService;
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

    @BindView(R.id.rootView)
    View rootView;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.appBarLayout)
    AppBarLayout appBarLayout;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    private Unbinder unbinder;
    private ReminderAdapter adapter;
    private ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper
            .SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
        @Override
        public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder,
                              RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
            final Reminder reminder = adapter.getReminderAt(viewHolder.getAdapterPosition());
            AlarmHelperService.startActionDeleteAlarm(MainActivity.this, reminder.getId());
            ReminderDAO.delete(MainActivity.this, reminder.getId());
            Snackbar snackbar = Snackbar.make(rootView, "Reminder Deleted", Snackbar.LENGTH_LONG)
                    .setAction("UNDO", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            ReminderDAO.insert(MainActivity.this, reminder);
                            AlarmHelperService.startActionCreateAlarm(MainActivity.this,
                                    reminder.getId());
                        }
                    });
            snackbar.setActionTextColor(Color.RED);
            View snackbarView = snackbar.getView();
            ((TextView) snackbarView.findViewById(android.support.design.R.id.snackbar_text))
                    .setTextColor(Color.YELLOW);
            snackbar.show();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        unbinder = ButterKnife.bind(this);

        setUpToolbar();
        setUpRecyclerView();
        getLoaderManager().initLoader(LOADER_ID, null, this);
    }

    private void setUpToolbar() {
        toolbar.inflateMenu(R.menu.menu_main_activity);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.item_delete_all:
                        final List<Reminder> reminders = adapter.getReminders();
                        AlarmHelperService.startActionDeleteAllAlarms(MainActivity.this);
                        ReminderDAO.deleteAll(MainActivity.this);
                        Snackbar snackbar = Snackbar.make(rootView, "All reminders deleted", Snackbar.LENGTH_LONG)
                                .setAction("UNDO", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        ReminderDAO.insert(MainActivity.this, reminders);
                                        AlarmHelperService.startActionCreateAllAlarms(MainActivity.this);
                                    }
                                });
                        snackbar.setActionTextColor(Color.RED);
                        View snackbarView = snackbar.getView();
                        ((TextView) snackbarView.findViewById(android.support.design.R.id.snackbar_text))
                                .setTextColor(Color.YELLOW);
                        snackbar.show();
                        return true;

                    default: return false;
                }
            }
        });
    }

    private void setUpRecyclerView() {
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);

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
