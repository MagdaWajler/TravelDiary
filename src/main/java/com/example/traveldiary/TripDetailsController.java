package com.example.traveldiary;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.util.List;

public class TripDetailsController {
    @FXML private Label dateLabel;
    @FXML private Label titleLabel, locationLabel;
    @FXML private TextArea notesArea;
    @FXML private HBox imageGallery;

    private Trip currentTrip;

    //wyswietlenie na ekranie podgladu
    public void displayTripDetails(Trip trip) {
        this.currentTrip = trip;
        titleLabel.setText(trip.getTitle());
        locationLabel.setText(trip.getLocation());
        dateLabel.setText(trip.getDate().toString());
        notesArea.setText(trip.getNotes());
        imageGallery.getChildren().clear();

        for (int i = 0; i < trip.getImagePaths().size(); i++) {
            String path = trip.getImagePaths().get(i);
            int currentIndex = i;

            //photo gallery preview
            ImageView iv = new ImageView(new Image(path));
            iv.setFitHeight(130);
            iv.setPreserveRatio(true);

            iv.setCursor(javafx.scene.Cursor.HAND);
            iv.setOnMouseClicked(event -> showGallery(trip.getImagePaths(), currentIndex));
            imageGallery.getChildren().add(iv);

        }
    }

    private int galleryIndex;

    private void showGallery(List<String> paths, int currentIndex) {

        galleryIndex = currentIndex;
        Stage stage = new Stage();

        ImageView imageView = new ImageView();
        imageView.setPreserveRatio(true);
        imageView.setSmooth(true);

        StackPane root = new StackPane(imageView);
        root.setStyle("-fx-background-color: black;");
        Scene scene = new Scene(root, 1000, 700);

        imageView.fitWidthProperty().bind(root.widthProperty());
        imageView.fitHeightProperty().bind(root.heightProperty());

        Runnable updateImage = () -> {
            imageView.setImage(new Image(paths.get(galleryIndex)));
            stage.setTitle("Photo " + (galleryIndex + 1) + " out of " + paths.size());
        };

        updateImage.run();

        //przwijanie zdjec
        scene.setOnKeyPressed(e -> {
            switch (e.getCode()) {
                case RIGHT -> {
                    galleryIndex = (galleryIndex + 1) % paths.size();
                    updateImage.run();
                }
                case LEFT -> {
                    galleryIndex = (galleryIndex - 1 + paths.size()) % paths.size();
                    updateImage.run();
                }
                case ESCAPE -> stage.close();
            }
        });

        stage.setScene(scene);
        stage.setMinWidth(400);
        stage.setMinHeight(300);
        stage.show();
    }

    @FXML
    protected void onDeleteClick() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete Trip");
        alert.setHeaderText("You are about to delete: " + titleLabel.getText());
        alert.setContentText("Are you sure? This action cannot be undone.");
        alert.setGraphic(null);

        if (alert.showAndWait().get() == ButtonType.OK) {
            DataStore.trips.remove(currentTrip);
            DataStore.saveTrips();
            onCloseClick();
        }
    }

    @FXML protected void onCloseClick() { ((Stage) titleLabel.getScene().getWindow()).close(); }

    @FXML
    public void onEditClick(ActionEvent actionEvent) {
        try {
            //okno i kontroler dodawania tripa
            FXMLLoader loader = new FXMLLoader(getClass().getResource("add-trip-view.fxml"));
            javafx.scene.Parent root = loader.load();
            AddTripController controller = loader.getController();

            //przygotowanie do aktualizacji danych tripa
            controller.loadTripToEdit(currentTrip);

            Stage stage = new Stage();
            stage.setTitle("Edit Trip");
            stage.setScene(new Scene(root));
            stage.initModality(javafx.stage.Modality.APPLICATION_MODAL);

            stage.showAndWait();

            //reload szczegolow tripa
            displayTripDetails(currentTrip);

        } catch (java.io.IOException e) {
            e.printStackTrace();
        }

    }
}