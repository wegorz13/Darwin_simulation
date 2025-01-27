package agh.ics.oop.presenter;

import agh.ics.oop.Simulation;
import agh.ics.oop.model.*;
import agh.ics.oop.model.MapChangeListener;
import agh.ics.oop.model.WorldElement;
import agh.ics.oop.model.WorldMap;
import agh.ics.oop.model.records.Boundary;
import agh.ics.oop.model.records.Statistics;
import agh.ics.oop.model.records.SubjectStatistics;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.layout.*;

public class SimulationPresenter implements MapChangeListener {
    private GrassField map;
    private Simulation simulation;
    private static final int GRIDPANEHEIGHT = 800;
    private static final int GRIDPANEWIDTH = 800;
    private static final int MAX_CHART_DATA_SIZE=100;
    private boolean showPreferredPositions = false;
    private boolean showGenotypeCarriers = false;
    private final XYChart.Series<Number, Number> animalSeries = new XYChart.Series<>();
    private final XYChart.Series<Number, Number> grassSeries = new XYChart.Series<>();

    @FXML
    private GridPane mapGrid;
    @FXML
    private Label animalsLabel,grassLabel,freePositionLabel,energyLabel,lifetimeLabel,childrenLabel,genotypeLabel;
    @FXML
    private Label subjectAgeLabel, subjectEnergyLabel, grassConsumedLabel, subjectChildrenLabel, descendantsLabel, subjectGenotypeLabel, activeGeneLabel, dayOfDeathLabel;
    @FXML
    private LineChart<Number, Number> animalPopulationChart;
    @FXML
    private LineChart<Number, Number> grassPopulationChart;

    public void initialize(){
        animalSeries.setName("Animals");
        grassSeries.setName("Grass");
        animalPopulationChart.setCreateSymbols(false);
        grassPopulationChart.setCreateSymbols(false);
        animalPopulationChart.getData().add(animalSeries);
        grassPopulationChart.getData().add(grassSeries);
    }

    public void setMap(GrassField map) {
        this.map = map;
    }

    public void setSimulation(Simulation simulation) {
        this.simulation = simulation;
    }
    
    private void clearGrid() {
        mapGrid.getChildren().retainAll(mapGrid.getChildren().getFirst()); // hack to retain visible grid lines
        mapGrid.getColumnConstraints().clear();
        mapGrid.getRowConstraints().clear();
    }

    private void drawMap() {
        clearGrid();

        // update current bounds
        Boundary bounds = map.getCurrentBounds();
        int mapWidth = bounds.rightUpCorner().getX() - bounds.leftDownCorner().getX() + 1;
        int mapHeight = bounds.rightUpCorner().getY() - bounds.leftDownCorner().getY() + 1;
        int cellWidth = GRIDPANEWIDTH / mapWidth;
        int cellHeight = GRIDPANEHEIGHT / mapHeight;

        // draw labels
        mapGrid.getColumnConstraints().add(new ColumnConstraints(cellWidth));
        mapGrid.getRowConstraints().add(new RowConstraints(cellHeight));
        Label label = new Label("y/x");
        mapGrid.add(label, 0, 0);
        GridPane.setHalignment(label, HPos.CENTER);

        for(int i = 0; i < mapWidth; i++){
            Label cell = new Label(Integer.toString(i + bounds.leftDownCorner().getX()));
            GridPane.setHalignment(cell, HPos.CENTER);
            mapGrid.getColumnConstraints().add(new ColumnConstraints(cellWidth));
            mapGrid.add(cell, i+1, 0);
        }

        for(int i = 0; i < mapHeight; i++){
            Label cell = new Label(Integer.toString(bounds.rightUpCorner().getY()- i));
            GridPane.setHalignment(cell, HPos.CENTER);
            mapGrid.getRowConstraints().add(new RowConstraints(cellHeight));
            mapGrid.add(cell, 0, i+1);
        }

        Statistics currentStatistics = this.map.getStatistics();

        // draw map with elements
        for (int x = map.getCurrentBounds().leftDownCorner().getX(); x <= map.getCurrentBounds().rightUpCorner().getX(); x++) {
            for (int y = map.getCurrentBounds().leftDownCorner().getY(); y <= map.getCurrentBounds().rightUpCorner().getY(); y++) {
                String backgroundColor = (showPreferredPositions && (int)Math.ceil(0.4*simulation.getConfig().mapHeight())<=y && y<=(int)Math.floor(0.6 * simulation.getConfig().mapHeight()))?  "#006400" : "#219b40";

                Vector2d pos = new Vector2d(x, y);
                StackPane cellPane = new StackPane();
                Region cell = new Region();

                if (map.isOccupied(pos)) {
                    WorldElement element = map.objectAt(pos);

                    if (element instanceof Animal animal){
                        ProgressBar energyBar = new ProgressBar();
                        energyBar.setProgress((double) animal.getEnergy() / 20); // Assuming 100 is max energy
                        energyBar.setPrefWidth(cellWidth); // Match cell width
                        energyBar.setMaxHeight((double) cellHeight / 5); // Thin progress bar

                        if (showGenotypeCarriers && animal.getGenotype().equals(currentStatistics.mostPopularGenotype())){
                            cell.setStyle(animal.toRegionStyle("#DC143C"));
                        }
                        else cell.setStyle(animal.toRegionStyle(backgroundColor));

                        cellPane.getChildren().addAll(cell, energyBar);
                        StackPane.setAlignment(energyBar, Pos.BOTTOM_CENTER);
                    }
                    else if (element instanceof Water){
                        cell.setStyle(element.toRegionStyle("#0000ff"));
                        cellPane.getChildren().add(cell);
                    }
                    else{
                        cell.setStyle(element.toRegionStyle(backgroundColor));
                        cellPane.getChildren().add(cell);
                    }
                }
                else {
                    cell.setStyle("-fx-background-color: " + backgroundColor + ";"  +
                        "-fx-background-size: cover; " +
                        "-fx-background-position: center;");
                    cellPane.getChildren().add(cell);}

                cellPane.setOnMouseClicked(event -> {
                    this.map.setSubjectAnimal(pos);
                    updateSubjectStatistics();
                });

                mapGrid.add(cellPane, x - bounds.leftDownCorner().getX() + 1, bounds.rightUpCorner().getY() - y + 1);
                GridPane.setHalignment(mapGrid.getChildren().getLast(), HPos.CENTER);
            }
        }
        updateStatistics(currentStatistics);
        updateSubjectStatistics();
    }

    private void updateStatistics(Statistics statistics){
        animalsLabel.setText(String.valueOf(statistics.animalsNumber()));
        grassLabel.setText(String.valueOf(statistics.grassNumber()));
        freePositionLabel.setText(String.valueOf(statistics.freePositionsNumber()));
        energyLabel.setText(String.format("%.2f",statistics.averageEnergy()));
        lifetimeLabel.setText(String.format("%.2f",statistics.averageLifetime()));
        childrenLabel.setText(String.format("%.2f",statistics.averageNumberOfChildren()));
        genotypeLabel.setText(statistics.mostPopularGenotype().toString());

        int currentDay = map.getDayOfSimulation();

        animalSeries.getData().add(new XYChart.Data<>(currentDay, statistics.animalsNumber()));
        grassSeries.getData().add(new XYChart.Data<>(currentDay, statistics.grassNumber()));

        if (animalSeries.getData().size() > MAX_CHART_DATA_SIZE) {
            animalSeries.getData().removeFirst();
        }
        if (grassSeries.getData().size() > MAX_CHART_DATA_SIZE) {
            grassSeries.getData().removeFirst();
        }
        NumberAxis animalXAxis = (NumberAxis) animalPopulationChart.getXAxis();
        animalXAxis.setAutoRanging(false);
        animalXAxis.setLowerBound(Math.max(0, currentDay - MAX_CHART_DATA_SIZE));
        animalXAxis.setUpperBound(currentDay);

        NumberAxis grassXAxis = (NumberAxis) grassPopulationChart.getXAxis();
        grassXAxis.setAutoRanging(false);
        grassXAxis.setLowerBound(Math.max(0, currentDay - MAX_CHART_DATA_SIZE));
        grassXAxis.setUpperBound(currentDay);
    }

    private void updateSubjectStatistics() {
        Animal subjectAnimal = map.getSubjectAnimal();
        if (subjectAnimal != null) {
            SubjectStatistics subjectAnimalStats = subjectAnimal.getStatistics();
            subjectEnergyLabel.setText(String.valueOf(subjectAnimalStats.subjectEnergy()));
            grassConsumedLabel.setText(String.valueOf(subjectAnimalStats.grassConsumed()));
            descendantsLabel.setText(String.valueOf(subjectAnimalStats.descendants()));
            subjectGenotypeLabel.setText(subjectAnimalStats.particularGenotype().toString());
            activeGeneLabel.setText(String.valueOf(subjectAnimalStats.activeGene()));
            subjectAgeLabel.setText(String.valueOf(subjectAnimalStats.subjectAge()));
            subjectChildrenLabel.setText(String.valueOf(subjectAnimalStats.subjectChildren()));
            if (subjectAnimalStats.dayOfDeath() > 0){
                dayOfDeathLabel.setText(String.valueOf(subjectAnimalStats.dayOfDeath()));
            }
        }
    }

    @Override
    public void mapChanged(WorldMap worldMap, String message) {
        Platform.runLater(this::drawMap);
    }

    public void onClickPause() {
        simulation.onClickPause();
    }

    public void onClickResume() {
        simulation.onClickResume();
    }

    public void onClickShowPreferred(){
        this.showPreferredPositions=!showPreferredPositions;
    }

    public void onClickShowGenotype() {
        this.showGenotypeCarriers=!showGenotypeCarriers;
    }
}
