package com.example.traveldiary;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Trip {

    private String title;
    private String location;
    private LocalDate date;
    private String notes;
    private List<String> imagePaths;

    public Trip(String title, String location, LocalDate startDate, String notes, List<String> imagePaths) {
        this.title = title;
        this.location = location;
        this.date = startDate;
        this.notes = notes;
        this.imagePaths = imagePaths;
    }

    public List<String> getImagePaths() {
        return imagePaths;
    }

    public String getTitle() {
        return title;
    }

    public String getLocation() {
        return location;
    }

    public LocalDate getDate() {
        return date;
    }

    public String getNotes() {
        return notes;
    }

    public void setTitle(String title) { this.title = title; }
    public void setLocation(String location) { this.location = location; }
    public void setDate(LocalDate date) { this.date = date; }
    public void setNotes(String notes) { this.notes = notes; }
    public void setImagePaths(List<String> imagePaths) {
        this.imagePaths = new ArrayList<>(imagePaths);
    }
}
