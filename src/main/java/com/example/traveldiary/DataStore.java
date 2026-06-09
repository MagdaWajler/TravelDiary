package com.example.traveldiary;

import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DataStore {
    //na dokumentowanie
    public static final List<Trip> trips = new ArrayList<>();
    private static final String FILE_NAME = "trips.txt";

    //na planowanie
    public static final List<PlannedTrip> plannedTrips = new ArrayList<>();
    private static final String PLANNED_FILE_NAME = "planned_trips.txt";

    //na dokumentowanie
    public static void saveTrips() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME))) {
            for (Trip trip : trips) {
                String safeNotes = trip.getNotes().replace("\n", "<BR>");
                String imagesJoined = String.join(",", trip.getImagePaths());
                writer.write(trip.getTitle() + ";;;" + trip.getLocation() + ";;;" +
                        trip.getDate() + ";;;" + safeNotes + ";;;" + imagesJoined);
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("Blad zapisu!");
        }
    }

    public static void loadTrips() {
        trips.clear();
        File file = new File(FILE_NAME);
        if (!file.exists()) return;
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(";;;");
                if (parts.length >= 4) {
                    String title = parts[0];
                    String location = parts[1];
                    LocalDate date = LocalDate.parse(parts[2]);
                    String notes = parts[3].replace("<BR>", "\n");

                    List<String> imagePaths = new ArrayList<>();
                    if (parts.length == 5 && !parts[4].isEmpty()) {
                        imagePaths = new ArrayList<>(Arrays.asList(parts[4].split(",")));
                    }
                    trips.add(new Trip(title, location, date, notes, imagePaths));
                }
            }
        } catch (Exception e) {
            System.out.println("Blad wczytywania!");
        }
    }

    //na planowanie
    public static void savePlannedTrips() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(PLANNED_FILE_NAME))) {
            for (PlannedTrip plan : plannedTrips) {
                String safeGoals = plan.getGoals().replace("\n", "<BR>");
                writer.write(plan.getTitle() + ";;;" + plan.getLocation() + ";;;" +
                        plan.getStartDate() + ";;;" + plan.getDuration() + ";;;" +
                        plan.getBudget() + ";;;" + safeGoals);
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("Blad zapisu planow!");
        }
    }

    public static void loadPlannedTrips() {
        plannedTrips.clear();
        File file = new File(PLANNED_FILE_NAME);
        if (!file.exists()) return;
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(";;;");
                if (parts.length >= 6) {
                    String title = parts[0];
                    String location = parts[1];
                    LocalDate startDate = LocalDate.parse(parts[2]);
                    String duration = parts[3];
                    String budget = parts[4];
                    String goals = parts[5].replace("<BR>", "\n");

                    plannedTrips.add(new PlannedTrip(title, location, startDate, duration, budget, goals));
                }
            }
        } catch (Exception e) {
            System.out.println("Blad wczytywania planow!");
        }
    }
}