package com.example.traveldiary;

import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.SVGPath;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.prefs.Preferences; //do zapamietania stanu mapy
import javafx.application.Platform;

public class MapController {

    //enum do statusy podrozy+kolory
    public enum TravelStatus {
        VISITED(Color.GREEN),
        NEXT_DESTINATION(Color.BLUE),
        PLANNED(Color.ORANGE),
        NONE(Color.LIGHTGRAY);

        private final Color color;
        TravelStatus(Color color) {
            this.color = color;
        }
        public Color getColor() {
            return color;
        }
    }

    @FXML
    public Group zoomGroup;

    @FXML
    public StackPane mapContainer;

    @FXML
    public ScrollPane mapScrollPane;

    private double zoom = 0.45;
    private static final double MAX_ZOOM = 1.5;
    private static final double MIN_ZOOM = 0.5;

    @FXML
    private Group worldMapGroup;

    //Map(ID kraju,status)
    private Map<String, TravelStatus> countryStates = new HashMap<>();

    private TravelStatus currentSelectionMode = TravelStatus.VISITED;

    private Preferences prefs = Preferences.userNodeForPackage(MapController.class);

    @FXML
    public void initialize() {

        StackPane.setAlignment(zoomGroup.getParent(), javafx.geometry.Pos.CENTER);
        //zoomGroup.setScaleShape(true);

        zoomFunction();

        mapScrollPane.setHvalue(0.5);
        mapScrollPane.setVvalue(0.5);
        loadSavedCountries();

        for (Node node : worldMapGroup.getChildren()) {
            if (node instanceof SVGPath country) {

                String countryId = country.getId();
                Platform.runLater(() -> {
                    //zapisany status z mapy+ kolor z Enuma
                    if (countryId != null) {
                        TravelStatus status = countryStates.getOrDefault(countryId, TravelStatus.NONE);
                        country.setFill(status.getColor());
                    } else {
                        country.setFill(TravelStatus.NONE.getColor());
                    }
                });
                /*
                country.setStroke(Color.BLACK);
                country.setStrokeWidth(0.5);
                */

                //init look
                country.setFill(Color.LIGHTGRAY);
                country.setStroke(Color.WHITE);
                country.setStrokeWidth(0.5);

                //hover look
                country.setOnMouseEntered(e -> {
                    //czy klucz występuje w mapie
                    if (countryId == null || !countryStates.containsKey(countryId)) {
                        country.setFill(Color.web("#666666")); //dark gray
                    }
                });
                country.setOnMouseExited(e -> {
                    //przywracanie koloru
                    TravelStatus status = countryStates.getOrDefault(countryId, TravelStatus.NONE);
                    country.setFill(status.getColor());
                });

                //klikniecie view
                country.setOnMouseClicked(e -> {
                    //country.setFill(Color.ORANGE);
                    //TODO TripDetailsController
                    if(countryId!=null) {
                        //spr aktualny status
                        TravelStatus currentState = countryStates.getOrDefault(countryId, TravelStatus.NONE);

                        //odznaczanie kraju
                        if (currentState == currentSelectionMode) {
                            countryStates.remove(countryId);
                            country.setFill(Color.web("#666666")); //hover
                        } else {
                            //przemalowanie kraju
                            countryStates.put(countryId, currentSelectionMode);
                            country.setFill(currentSelectionMode.getColor());
                        }
                    }
                    saveCountries();
                });
            }
        }
    }

    //metody do przyciskow
    @FXML
    public void setModeVisited() {this.currentSelectionMode = TravelStatus.VISITED;}
    @FXML
    public void setModeNextDestination() {this.currentSelectionMode = TravelStatus.NEXT_DESTINATION;}
    @FXML
    public void setModePlanned() {this.currentSelectionMode = TravelStatus.PLANNED;}


    private void zoomFunction() {
        mapContainer.setOnZoom(event -> {
            zoom *= event.getZoomFactor();

            if (zoom<MIN_ZOOM) zoom = MIN_ZOOM;
            if (zoom>MAX_ZOOM) zoom = MAX_ZOOM;

            zoomGroup.setScaleX(zoom);
            zoomGroup.setScaleY(zoom);

            event.consume();
        });
    }

    private void saveCountries() {
        //zapis w formacie klucz:wartosc, [PL:VISITED,DE:PLANNED]
        String mapAsString = countryStates.entrySet().stream()
                .map(entry -> entry.getKey() + ":" + entry.getValue().name())
                .collect(Collectors.joining(","));
        prefs.put("savedTravelStatesKey", mapAsString);
    }

    private void loadSavedCountries() {
        //czyta kraj:status po przecinku
        String savedData = prefs.get("savedTravelStatesKey", "");
        if (!savedData.isEmpty()) {
            String[] pairs = savedData.split(",");
            for (String pair : pairs) {
                String[] parts = pair.split(":");
                if (parts.length == 2) {
                    try {
                        String id = parts[0];
                        TravelStatus status = TravelStatus.valueOf(parts[1]);
                        countryStates.put(id, status);
                    } catch (IllegalArgumentException ignored) {
                    }
                }
            }
        }
    }
}