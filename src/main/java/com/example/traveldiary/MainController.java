package com.example.traveldiary;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.layout.StackPane;

import java.io.IOException;
import java.util.Objects;

public class MainController {

    public StackPane rootPane;
    @FXML
    private StackPane contentArea;

    @FXML
    public void initialize() {
        loadView("dashboard-view.fxml");
    }

    @FXML
    private void showDashboard() {
        loadView("dashboard-view.fxml");
    }

    @FXML
    private void showTrips() {
        loadView("trips-view.fxml");
    }

    @FXML
    private void showSettings() {
        loadView("settings-view.fxml");
    }

    @FXML
    public void showMap() {
        loadView("map-view.fxml");
    }

    private void loadView(String fxmlFile) {
        try {
            Node view = FXMLLoader.load(Objects.requireNonNull(getClass().getResource(fxmlFile)));
            contentArea.getChildren().setAll(view);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @FXML
    protected void showDiscoverView() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("discover-view.fxml"));
            Node view = fxmlLoader.load();

            contentArea.getChildren().setAll(view);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}