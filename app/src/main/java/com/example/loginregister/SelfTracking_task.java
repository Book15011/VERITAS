package com.example.loginregister;

public class SelfTracking_task {
    private final String name;
    private final String subject;
    private String details;
    private boolean isDone;

    public SelfTracking_task(String name, String subject) {
        this.name = name;
        this.subject = subject;
        this.details = "";
        this.isDone = false;
    }

    public String getName() {
        return name;
    }

    public String getSubject() {
        return subject;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public boolean isDone() {
        return isDone;
    }

    public void setDone(boolean done) {
        isDone = done;
    }

    // ... (other methods)
}
