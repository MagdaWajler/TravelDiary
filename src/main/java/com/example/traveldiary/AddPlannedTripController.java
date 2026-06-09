package com.example.traveldiary;

import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.time.LocalDate;

public class AddPlannedTripController {

    @FXML private TextField titleField;
    @FXML private TextField locationField;
    @FXML private DatePicker datePicker;
    @FXML private TextField durationField;
    @FXML private TextField budgetField;
    @FXML private TextArea goalsArea;

    @FXML
    protected void onSavePlanClick() {
        String title = titleField.getText();
        String location = locationField.getText();
        LocalDate date = datePicker.getValue();
        String duration = durationField.getText();
        String budget = budgetField.getText();
        String goals = goalsArea.getText();

        if (title.isEmpty() || date == null) {
            System.out.println("Tytul i data sa wymagane!");
            return;
        }

        PlannedTrip newPlan = new PlannedTrip(title, location, date, duration, budget, goals);
        DataStore.plannedTrips.add(newPlan);
        DataStore.savePlannedTrips();

        //zamykanie okna po zapisaniu
        Stage stage = (Stage) titleField.getScene().getWindow();
        stage.close();
    }
}