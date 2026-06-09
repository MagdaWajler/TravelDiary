package com.example.traveldiary;

public class Destination {
    private String name;                //City lub Place Name
    private String country;             //Country
    private String category;            //Category (np. beach, historical)
    private String bestTime;            //Best_Time_to_Travel
    private String background;          //Historical Background
    private String significance;        //Cultural Significance/Description
    private String stats;               //Visitor Statistics

    public Destination(String name, String country, String category, String bestTime, String background, String significance, String stats) {
        this.name = name;
        this.country = country;
        this.category = category;
        this.bestTime = bestTime;
        this.background = background;
        this.significance = significance;
        this.stats = stats;
    }

    //gettery
    public String getName() { return name; }
    public String getCountry() { return country; }
    public String getCategory() { return category; }
    public String getBestTime() { return bestTime; }
    public String getBackground() { return background; }
    public String getSignificance() { return significance; }
    public String getStats() { return stats; }

    @Override
    public String toString() {
        return name + ", " + country; // To będzie widoczne na liście po lewej
    }
}