package com.example.traveldiary;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.FileChooser;
import java.io.File;
import java.io.FileOutputStream;
import javafx.scene.control.Alert;

public class TripsController {

    @FXML private FlowPane tripsGrid;
    @FXML private Button addTripButton;
    @FXML private ToggleButton pastTripsBtn;
    @FXML private ToggleButton futurePlansBtn;

    //zmienna pamietajaca ktora zakladka jest otwarta
    private boolean isShowingPastTrips = true;

    @FXML
    public void initialize() {
        ToggleGroup toggleGroup = new ToggleGroup();
        pastTripsBtn.setToggleGroup(toggleGroup);
        futurePlansBtn.setToggleGroup(toggleGroup);

        toggleGroup.selectedToggleProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal == null) {
                oldVal.setSelected(true);
            } else {
                isShowingPastTrips = (newVal == pastTripsBtn);
                updateButtonStyles();
                refreshGrid();
            }
        });

        //ladowanie obu list z pliku przy starcie ekranu
        DataStore.loadTrips();
        DataStore.loadPlannedTrips();

        //wyglad przyciskow od razu po wlaczeniu okna
        updateButtonStyles();
        refreshGrid();
    }

    private void refreshGrid() {
        tripsGrid.getChildren().clear();
        tripsGrid.getChildren().add(addTripButton);

        if (isShowingPastTrips) {
            //dokumentowanie
            for (Trip trip : DataStore.trips) {
                VBox tile = createTile(trip.getTitle(), trip.getLocation());
                tile.setOnMouseClicked(event -> showTripsInfo(trip));
                tripsGrid.getChildren().add(tile);
            }
        } else {
            //planowanie
            for (PlannedTrip plan : DataStore.plannedTrips) {
                VBox tile = createTile(plan.getTitle(), plan.getLocation());
                tile.setOnMouseClicked(event -> showPlannedTripInfo(plan));
                tripsGrid.getChildren().add(tile);
            }
        }
    }

    //funkcja do robienia kafelka
    private VBox createTile(String titleText, String locationText) {
        VBox tile = new VBox();
        tile.setPrefSize(150, 150);
        tile.setSpacing(10);
        tile.setStyle("-fx-background-color: white; -fx-background-radius: 15; -fx-padding: 15; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.1), 5, 0, 0, 5); -fx-cursor: hand;");

        Label titleLabel = new Label(titleText);
        titleLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: #005380; -fx-font-size: 17px; -fx-font-family: 'Arial';");
        titleLabel.setWrapText(true);

        Label locationLabel = new Label(locationText);
        locationLabel.setStyle("-fx-font-size: 15px; -fx-text-fill: gray; -fx-font-family: 'Arial';");

        tile.getChildren().addAll(titleLabel, locationLabel);
        return tile;
    }

    private void showTripsInfo(Trip trip) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("trip-details-view.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            TripDetailsController controller = fxmlLoader.getController();
            controller.displayTripDetails(trip);

            Stage stage = new Stage();
            stage.setTitle("Details: " + trip.getTitle());
            stage.setScene(scene);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();
            refreshGrid();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showPlannedTripInfo(PlannedTrip plan) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("planned-trip-details-view.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            PlannedTripDetailsController controller = fxmlLoader.getController();
            controller.displayPlanDetails(plan);

            Stage stage = new Stage();
            stage.setTitle("Plan Details: " + plan.getTitle());
            stage.setScene(scene);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();
            refreshGrid();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    protected void onAddTripButtonClick() {
        try {
            // Wybor odpowiedniego pliku fxml w zaleznosci od aktywnej zakladki
            String fxmlFile = isShowingPastTrips ? "add-trip-view.fxml" : "add-planned-trip-view.fxml";
            String title = isShowingPastTrips ? "Add Past Trip" : "Plan New Trip";

            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource(fxmlFile));
            Scene scene = new Scene(fxmlLoader.load());
            Stage stage = new Stage();
            stage.setTitle(title);
            stage.setScene(scene);

            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();
            refreshGrid();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    //wyglad przyciskow zmieniajacych planowanie/dokumentowanie wycieczki
    private void updateButtonStyles() {
        String selectedStyle = "-fx-background-color: white; -fx-text-fill: #005380; -fx-font-weight: bold; -fx-font-size: 14px; -fx-background-radius: 25; -fx-padding: 8 25; -fx-cursor: hand; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.1), 3, 0, 0, 1);";
        String unselectedStyle = "-fx-background-color: transparent; -fx-text-fill: #555555; -fx-font-weight: bold; -fx-font-size: 14px; -fx-background-radius: 25; -fx-padding: 8 25; -fx-cursor: hand;";

        if (isShowingPastTrips) {
            pastTripsBtn.setStyle(selectedStyle);
            futurePlansBtn.setStyle(unselectedStyle);
        } else {
            pastTripsBtn.setStyle(unselectedStyle);
            futurePlansBtn.setStyle(selectedStyle);
        }
    }

    @FXML
    protected void onExportMindMapClick() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save Mind Map");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PNG Image", "*.png"));

        File file = fileChooser.showSaveDialog(pastTripsBtn.getScene().getWindow());

        if (file != null) {
            generateMindMap(file);
        }
    }

    private void generateMindMap(File file) {
        StringBuilder sb = new StringBuilder();

        //skladnia dla biblioteki PlantUML
        sb.append("@startwbs\n");

        //styl mapy mysli
        sb.append("<style>\n");
        sb.append("wbsDiagram {\n");
        sb.append("  Node {\n");
        sb.append("    Padding 15\n");
        sb.append("    RoundCorner 15\n");
        sb.append("    FontName Arial\n");
        sb.append("  }\n");
        sb.append("  :depth(0) {\n"); //lvl 0: My Travel Diary
        sb.append("    BackgroundColor #2c3e50\n");
        sb.append("    FontColor white\n");
        sb.append("  }\n");
        sb.append("  :depth(1) {\n"); //lvl 1: Documented Trips/Future Plans
        sb.append("    BackgroundColor #3498db\n");
        sb.append("    FontColor white\n");
        sb.append("    LineColor #2980b9\n");
        sb.append("  }\n");
        sb.append("  :depth(2) {\n"); //lvl 2: nazwy wycieczek
        sb.append("    BackgroundColor #1abc9c\n");
        sb.append("    FontColor white\n");
        sb.append("    LineColor #16a085\n");
        sb.append("  }\n");
        sb.append("  :depth(3) {\n"); //lvl 3: Boxy z detalami
        sb.append("    BackgroundColor #ffffff\n");
        sb.append("    FontColor #2c3e50\n");
        sb.append("    LineColor #bdc3c7\n");
        sb.append("  }\n");
        sb.append("}\n");
        sb.append("</style>\n");

        //korzen mapy
        sb.append("* My Travel Diary\n");

        //udokumentowane podroze
        if (!DataStore.trips.isEmpty()) {
            sb.append("** Documented Trips\n");
            for (Trip trip : DataStore.trips) {
                sb.append("*** ").append(trip.getTitle()).append("\n");
                //szczegoly
                StringBuilder details = new StringBuilder();
                details.append("Location: ").append(trip.getLocation()).append("\\n");
                details.append("Date: ").append(trip.getDate().toString()).append("\\n");

                if (trip.getNotes() != null && !trip.getNotes().isEmpty()) {
                    //czhyszczenie notatek, usuwanie enterow
                    String cleanNotes = formatBulletPoints(trip.getNotes());
                    //zawijanie tesktu co 60 znakow
                    String wrappedNotes = wrapText(cleanNotes, 60);
                    details.append("Notes: ").append(wrappedNotes).append("\\n");
                }

                if (trip.getImagePaths() != null && !trip.getImagePaths().isEmpty()) {
                    details.append("Photos attached: ").append(trip.getImagePaths().size()).append("\\n");
                }

                sb.append("**** ").append(details.toString()).append("\n");
            }
        }

        //planowane wycieczki
        if (!DataStore.plannedTrips.isEmpty()) {
            sb.append("** Future Plans\n");
            for (PlannedTrip plan : DataStore.plannedTrips) {
                sb.append("*** ").append(plan.getTitle()).append("\n");

                StringBuilder details = new StringBuilder();
                details.append("Location: ").append(plan.getLocation()).append("\\n");
                details.append("Date: ").append(plan.getStartDate().toString()).append("\\n");

                if (plan.getDuration() != null && !plan.getDuration().isEmpty()) {
                    details.append("Duration: ").append(plan.getDuration()).append("\\n");
                }

                if (plan.getBudget() != null && !plan.getBudget().isEmpty()) {
                    details.append("Budget: ").append(plan.getBudget()).append("\\n");
                }

                if (plan.getGoals() != null && !plan.getGoals().isEmpty()) {
                    String cleanGoals = formatBulletPoints(plan.getGoals());
                    String wrappedGoals = wrapText(cleanGoals, 60);
                    details.append("Goals: ").append(wrappedGoals).append("\\n");
                }

                sb.append("**** ").append(details.toString()).append("\n");
            }
        }

        sb.append("@endwbs\n");

        try {
            //przekazywanie wygenerowanego tekst do PlantUML
            net.sourceforge.plantuml.SourceStringReader reader = new net.sourceforge.plantuml.SourceStringReader(sb.toString());

            //zapis wygenerowanego obrazu do pliku png
            try (FileOutputStream fos = new FileOutputStream(file)) {
                reader.outputImage(fos);
            }

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Success");
            alert.setHeaderText("Diagram generated successfully.");
            alert.setContentText("Your diagram has been successfully saved to:\n" + file.getAbsolutePath());
            alert.showAndWait();

        } catch (Exception e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Failed to generate the diagram.");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }

    //pomocnicza do zawijania dlugiego tekstu
    protected String wrapText(String text, int maxLineLength) {
        if (text == null || text.isEmpty()) return "";
        StringBuilder result = new StringBuilder();
        String[] words = text.split(" ");
        int currentLength = 0;

        for (String word : words) {
            if (currentLength + word.length() > maxLineLength) {
                result.append("\\n"); // znak nowej linii dla PlantUML
                currentLength = 0;
            }
            result.append(word).append(" ");
            currentLength += word.length() + 1;
        }
        return result.toString().trim();
    }

    //pomocnicza do zamiany listy z '-' na przecinki
    protected String formatBulletPoints(String text) {
        if (text == null || text.isEmpty()) return "";

        String[] lines = text.split("\n");
        java.util.List<String> parts = new java.util.ArrayList<>();

        for (String line : lines) {
            String trimmed = line.trim();
            if (trimmed.startsWith("-")) {
                trimmed = trimmed.substring(1).trim();
            }
            if (!trimmed.isEmpty()) {
                parts.add(trimmed);
            }
        }
        return String.join(", ", parts);
    }
}