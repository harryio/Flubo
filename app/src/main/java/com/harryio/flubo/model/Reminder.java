package com.harryio.flubo.model;

import java.util.UUID;

public class Reminder {
    private long id;
    private String title;
    private String description;
    private boolean completed;
    private long remindTime = -1L;
    private String repeatInterval;

    private Reminder(String title, String description, boolean completed, long remindTime,
                     String repeatInterval) {
        this.title = title;
        this.description = description;
        this.completed = completed;
        this.remindTime = remindTime;
        this.repeatInterval = repeatInterval;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public long getRemindTime() {
        return remindTime;
    }

    public void setRemindTime(long remindTime) {
        this.remindTime = remindTime;
    }

    public boolean isRemiderSet() {
        return remindTime != -1L;
    }

    public RepeatInterval getRepeatInterval() {
        return repeatInterval == null ? RepeatInterval.ONE_TIME : RepeatInterval.valueOf(repeatInterval);
    }

    public void setRepeatInterval(RepeatInterval repeatInterval) {
        this.repeatInterval = repeatInterval.name();
    }

    public static class Builder {
        private long id;
        private String title;
        private String description;
        private boolean completed;
        private long remindTime = -1L;
        private RepeatInterval repeatInterval;

        public Builder id(long id) {
            this.id = id;
            return this;
        }

        public Builder title(String title) {
            this.title = title;
            return this;
        }

        public Builder description(String description) {
            this.description = description;
            return this;
        }

        public Builder isCompleted(boolean completed) {
            this.completed = completed;
            return this;
        }

        public Builder remindAt(long remindTime) {
            this.remindTime = remindTime;
            return this;
        }

        public Builder withRepeatInterval(RepeatInterval repeatInterval) {
            this.repeatInterval = repeatInterval;
            return this;
        }

        public Reminder create() {
            return new Reminder(id, title, description, completed, remindTime, repeatInterval.name());
        }
    }
}
