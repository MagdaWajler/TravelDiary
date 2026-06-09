package com.example.traveldiary;

import java.time.LocalDate;

public class PlannedTrip {
    private String title;
    private String location;
    private LocalDate startDate;
    private String duration;
    private String budget;
    private String goals;

    public PlannedTrip(String title, String location, LocalDate startDate, String duration, String budget, String goals) {
        this.title = title;
        this.location = location;
        this.startDate = startDate;
        this.duration = duration;
        this.budget = budget;
        this.goals = goals;
    }

    //gettery
    public String getTitle() { return title; }
    public String getLocation() { return location; }
    public LocalDate getStartDate() { return startDate; }
    public String getDuration() { return duration; }
    public String getBudget() { return budget; }
    public String getGoals() { return goals; }

    //settery
    public void setTitle(String title) { this.title = title; }
    public void setLocation(String location) { this.location = location; }
    public void setStartDate(LocalDate startDate) { this.startDate = startDate; }
    public void setDuration(String duration) { this.duration = duration; }
    public void setBudget(String budget) { this.budget = budget; }
    public void setGoals(String goals) { this.goals = goals; }
}