package com.harryio.flubo.model;

public class Reminder {
    private String title;
    private String description;
    private boolean completed;
    private long remindTime = -1L;

    private Reminder(String title, String description, boolean completed, long remindTime) {
        this.title = title;
        this.description = description;
        this.completed = completed;
        this.remindTime = remindTime;
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

    public static class Builder {
        private String title;
        private String description;
        private boolean completed;
        private long remindTime = -1L;

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

        public Reminder create() {
            return new Reminder(title, description, completed, remindTime);
        }
    }
}
