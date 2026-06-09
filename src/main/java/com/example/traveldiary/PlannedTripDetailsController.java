package com.example.traveldiary;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

public class PlannedTripDetailsController {
    @FXML private Label titleLabel;
    @FXML private Label locationLabel;
    @FXML private Label dateLabel;
    @FXML private Label durationLabel;
    @FXML private Label budgetLabel;
    @FXML private TextArea goalsArea;

    private PlannedTrip currentPlan;

    public void displayPlanDetails(PlannedTrip plan) {
        this.currentPlan = plan;
        titleLabel.setText(plan.getTitle());
        locationLabel.setText(plan.getLocation());
        dateLabel.setText(plan.getStartDate().toString());
        durationLabel.setText(plan.getDuration().isEmpty() ? "Not set" : plan.getDuration());
        budgetLabel.setText(plan.getBudget().isEmpty() ? "Not set" : plan.getBudget());
        goalsArea.setText(plan.getGoals());
    }

    @FXML
    protected void onDeleteClick() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete Plan");
        alert.setHeaderText("You are about to delete plan: " + titleLabel.getText());
        alert.setContentText("Are you sure?");
        alert.setGraphic(null);

        if (alert.showAndWait().get() == ButtonType.OK) {
            DataStore.plannedTrips.remove(currentPlan);
            DataStore.savePlannedTrips();
            onCloseClick();
        }
    }

    @FXML
    protected void onCloseClick() {
        ((Stage) titleLabel.getScene().getWindow()).close();
    }
}