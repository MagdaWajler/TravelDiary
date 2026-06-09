package com.example.traveldiary;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import java.io.File;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class AddTripController {
    @FXML public TextField titleField;
    @FXML public TextField locationField;
    @FXML public DatePicker datePicker;
    @FXML public TextArea notesArea;

    private Trip tripToEdit = null;

    @FXML
    public void initialize() {
        datePicker.getEditor().setEditable(false);
        datePicker.getEditor().setOnMouseClicked(e -> datePicker.show());
    }

    private List<String> selectedImagePaths = new ArrayList<>();

    @FXML
    public void onSelectImagesClick() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Images", "*.png", "*.jpg", "*.jpeg")
        );

        List<File> files = fileChooser.showOpenMultipleDialog(titleField.getScene().getWindow());

        if (files != null) {
            for (File file : files) {
                selectedImagePaths.add(file.toURI().toString());
            }
        }
    }

    //wczytanie do edytora
    public void loadTripToEdit(Trip trip) {
        this.tripToEdit = trip; //przypisanie currentTrip

        titleField.setText(trip.getTitle());
        locationField.setText(trip.getLocation());
        datePicker.setValue(trip.getDate());
        notesArea.setText(trip.getNotes());

        this.selectedImagePaths = new ArrayList<>(trip.getImagePaths());
    }

    @FXML
    public void onSaveButtonClick() {

        String title = titleField.getText();
        String location = locationField.getText();
        LocalDate date = datePicker.getValue();
        String notes = notesArea.getText();

        StringBuilder errorMessage = new StringBuilder();
        if (title == null || title.trim().isEmpty()) {
            errorMessage.append("- Missing Title\n");
        }
        if (location == null || location.trim().isEmpty()) {
            errorMessage.append("- Missing Location\n");
        }
        if (date == null) {
            errorMessage.append("- Missing Date\n");
        }

        if (errorMessage.length() > 0) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Information Required");
            alert.setHeaderText("Please complete the following fields:");
            alert.setContentText(errorMessage.toString());
            alert.showAndWait();
            return;
        }

        if (tripToEdit != null) { //edycja
            tripToEdit.setTitle(title);
            tripToEdit.setLocation(location);
            tripToEdit.setDate(date);
            tripToEdit.setNotes(notes);
            tripToEdit.setImagePaths(selectedImagePaths);
        } else { //dodanie nowej
            Trip newTrip = new Trip(title, location, date, notes, selectedImagePaths);
            DataStore.trips.add(newTrip); //dodanie do bazy tripow
        }

        //zapis do pliku
        DataStore.saveTrips();

        Stage stage = (Stage) titleField.getScene().getWindow();
        stage.close();
    }
}