package com.example.traveldiary;

import com.opencsv.CSVReader;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;

import java.io.InputStreamReader;
import java.util.Objects;

public class DiscoverController {

    @FXML private ListView<Destination> destinationsListView;
    @FXML private TextField searchField;

    @FXML private ToggleButton famousPlacesBtn;
    @FXML private ToggleButton destinationsBtn;

    @FXML private Label nameLabel;
    @FXML private Label countryLabel;
    @FXML private Label categoryLabel;
    @FXML private Label bestTimeLabel;
    @FXML private Label backgroundLabel;
    @FXML private Label significanceLabel;
    @FXML private Label statsLabel;

    private ObservableList<Destination> masterData = FXCollections.observableArrayList();
    private FilteredList<Destination> filteredData;

    @FXML
    public void initialize() {
        //grupa przyciskow
        ToggleGroup toggleGroup = new ToggleGroup();
        famousPlacesBtn.setToggleGroup(toggleGroup);
        destinationsBtn.setToggleGroup(toggleGroup);

        //klikniecie
        toggleGroup.selectedToggleProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal == null) {
                //prevents odkliknieciu obu przyciskow na raz
                oldVal.setSelected(true);
            } else {
                searchField.setText(""); //clear wyszukiwarke przy zmianie kategorii
                showDestinationDetails(null); //clear prawy panel

                if (newVal == famousPlacesBtn) {
                    loadFamousPlaces();
                } else {
                    loadTravelDestinations();
                }
            }
        });

        loadFamousPlaces();

        //przypiecie paska wyszukiwarki do aktualnej listy
        filteredData = new FilteredList<>(masterData, p -> true);
        destinationsListView.setItems(filteredData);

        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(dest -> {
                if (newValue == null || newValue.isEmpty()) return true;
                String lowerCaseFilter = newValue.toLowerCase();
                return dest.getName().toLowerCase().contains(lowerCaseFilter) ||
                        dest.getCountry().toLowerCase().contains(lowerCaseFilter);
            });
        });

        destinationsListView.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> showDestinationDetails(newValue)
        );
    }

    private void showDestinationDetails(Destination dest) {
        if (dest != null) {
            nameLabel.setText(dest.getName());
            countryLabel.setText("📍 " + dest.getCountry());
            categoryLabel.setText(dest.getCategory().isEmpty() ? "No data" : dest.getCategory());
            bestTimeLabel.setText(dest.getBestTime().isEmpty() ? "No data" : dest.getBestTime());
            backgroundLabel.setText(dest.getBackground());
            significanceLabel.setText(dest.getSignificance());
            statsLabel.setText(dest.getStats());
        } else {
            nameLabel.setText("Select a place from the list");
            countryLabel.setText("");
            categoryLabel.setText("");
            bestTimeLabel.setText("");
            backgroundLabel.setText("");
            significanceLabel.setText("");
            statsLabel.setText("");
        }
    }

    private void loadFamousPlaces() {
        masterData.clear();
        String csvPath = "/com/example/traveldiary/data/world_famous_places_2024.csv";

        try (CSVReader reader = new CSVReader(new InputStreamReader(
                Objects.requireNonNull(getClass().getResourceAsStream(csvPath))))) {

            String[] nextLine;
            boolean isHeader = true;

            while ((nextLine = reader.readNext()) != null) {
                if (isHeader) { isHeader = false; continue; }

                if (nextLine.length >= 13) {
                    String name = nextLine[0].trim();
                    String country = nextLine[1].trim();
                    String city = nextLine[2].trim();
                    String visitors = nextLine[3].trim();
                    String type = nextLine[4].trim();
                    String unesco = nextLine[5].trim();
                    String yearBuilt = nextLine[6].trim();
                    String fee = nextLine[7].trim();
                    String bestTime = nextLine[8].trim();
                    String region = nextLine[9].trim();
                    String famousFor = nextLine[12].trim();

                    //fix brakujacego p.n.e. w piramidach
                    if (name.equals("Great Pyramid of Giza") || name.equals("Acropolis")) {
                        if (!yearBuilt.contains("BC")) {
                            yearBuilt += " BC";
                        }
                    }

                    String location = city + ", " + country;
                    String backgroundInfo = "Region: " + region + "\n" +
                            "Year Built: " + yearBuilt + "\n" +
                            "UNESCO World Heritage: " + unesco + "\n" +
                            "Entry Fee: $" + fee;
                    String statsInfo = "Annual Visitors: " + visitors + " million";

                    masterData.add(new Destination(name, location, type, bestTime, backgroundInfo, famousFor, statsInfo));
                }
            }
        } catch (Exception e) {
            System.err.println("Blad wczytania pliku world_famous_places");
            e.printStackTrace();
        }
    }

    private void loadTravelDestinations() {
        masterData.clear();
        String csvPath = "/com/example/traveldiary/data/travel_destinations.csv";

        try (CSVReader reader = new CSVReader(new InputStreamReader(
                getClass().getResourceAsStream(csvPath)))) {

            String[] nextLine;
            boolean isHeader = true;

            while ((nextLine = reader.readNext()) != null) {
                if (isHeader) { isHeader = false; continue; }

                if (nextLine.length >= 4) {
                    String city = nextLine[0].trim();
                    String country = nextLine[1].trim();
                    String categories = nextLine[2].trim();
                    String bestTime = nextLine[3].trim();

                    String type = "Vacation Spot";
                    String backgroundInfo = "This location is highly recommended for: \n" + categories;
                    String significance = "Perfect destination for your next trip.";
                    String statsInfo = "Visitor statistics not tracked for this destination.";

                    masterData.add(new Destination(city, country, type, bestTime, backgroundInfo, significance, statsInfo));
                }
            }
        } catch (Exception e) {
            System.err.println("Blad wczytania pliku travel_destinations.csv");
            e.printStackTrace();
        }
    }
}