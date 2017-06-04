package com.harryio.flubo.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.harryio.flubo.R;
import com.harryio.flubo.model.Reminder;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;

public class ReminderAdapter extends RecyclerView.Adapter<ReminderAdapter.ReminderViewHolder> {
    private Context context;
    private List<Reminder> reminders = new ArrayList<>();
    private ClickListener clickListener;

    public ReminderAdapter(Context context) {
        this.context = context;
    }

    static class ReminderViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.title)
        TextView title;
        @BindView(R.id.description)
        TextView description;
        @BindView(R.id.checkbox)
        CheckBox checkBox;

        private ReminderAdapter adapter;

        ReminderViewHolder(View itemView, ReminderAdapter adapter) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            this.adapter = adapter;
        }

        @OnClick(R.id.item_root_view)
        void onItemClick() {
            adapter.onItemClicked(getAdapterPosition());
        }

        @OnCheckedChanged(R.id.checkbox)
        void onCheckChanged(boolean isChecked) {
            adapter.onCheckboxClicked(getAdapterPosition(), isChecked);
        }
    }

    @Override
    public ReminderViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.layout_list_item, parent, false);
        return new ReminderViewHolder(view, this);
    }

    @Override
    public void onBindViewHolder(ReminderViewHolder holder, int position) {
        Reminder reminder = reminders.get(position);

        holder.title.setText(reminder.getTitle());
        holder.description.setText(reminder.getDescription());
        holder.checkBox.setChecked(reminder.isCompleted());
    }

    public void setReminders(List<Reminder> reminders) {
        this.reminders.clear();
        this.reminders.addAll(reminders);
        notifyDataSetChanged();
    }

    private void onItemClicked(int position) {
        clickListener.onListItemClicked(reminders.get(position));
    }

    private void onCheckboxClicked(int position, boolean isChecked) {
        clickListener.onListCheckboxClicked(reminders.get(position), isChecked);
    }

    public Reminder getReminderAt(int position) {
        return reminders.get(position);
    }

    public List<Reminder> getReminders() {
        return new ArrayList<>(reminders);
    }

    @Override
    public int getItemCount() {
        return reminders.size();
    }

    public void setClickListener(ClickListener clickListener) {
        this.clickListener = clickListener;
    }

    public interface ClickListener {
        void onListItemClicked(Reminder reminder);

        void onListCheckboxClicked(Reminder reminder, boolean isChecked);
    }
}