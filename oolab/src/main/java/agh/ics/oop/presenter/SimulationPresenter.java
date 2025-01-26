package agh.ics.oop.presenter;

import agh.ics.oop.Simulation;
import agh.ics.oop.model.*;
import agh.ics.oop.model.util.Boundary;
import agh.ics.oop.model.util.Statistics;
import agh.ics.oop.model.util.SubjectStatistics;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.HPos;
import javafx.scene.control.*;
import javafx.scene.layout.*;

public class SimulationPresenter implements MapChangeListener {
    private GrassField map;
    private Simulation simulation;
    private static final int GRIDPANEHEIGHT = 800;
    private static final int GRIDPANEWIDTH = 800;

    @FXML
    private GridPane mapGrid;
    @FXML
    private Label animalsLabel,grassLabel,freePositionLabel,energyLabel,lifetimeLabel,childrenLabel,genotypeLabel;
    @FXML
    private Label partAgeLabel, partEnergyLabel, grassConsumedLabel, partChildrenLabel, descendantsLabel, partGenotypeLabel, activeGeneLabel, dayOfDeathLabel;
    @FXML
    private VBox inspectionBox,statisticsBox;

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
        // clear grid
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

        // draw map with elements
        for (int x = map.getCurrentBounds().leftDownCorner().getX(); x <= map.getCurrentBounds().rightUpCorner().getX(); x++) {
            for (int y = map.getCurrentBounds().leftDownCorner().getY(); y <= map.getCurrentBounds().rightUpCorner().getY(); y++) {
                Vector2d pos = new Vector2d(x, y);
                Region cell = new Region();

                if (map.isOccupied(pos)) {
                    WorldElement element = map.objectAt(pos);
                    cell.setStyle(element.toRegionStyle());
                }
                else cell.setStyle("-fx-background-color: #219b40; " +
                        "-fx-background-size: cover; " +
                        "-fx-background-position: center;");

                cell.setOnMouseClicked(event -> {this.map.setSubjectAnimal(pos); updateSubjectStatistics();});

                mapGrid.add(cell, x - bounds.leftDownCorner().getX() + 1, bounds.rightUpCorner().getY() - y + 1);
                GridPane.setHalignment(mapGrid.getChildren().getLast(), HPos.CENTER);
                updateStatistics(this.map.getStatistics());
                updateSubjectStatistics();
            }
        }
    }

    private void updateStatistics(Statistics statistics){
        animalsLabel.setText(String.valueOf(statistics.animalsNumber()));
        grassLabel.setText(String.valueOf(statistics.grassNumber()));
        freePositionLabel.setText(String.valueOf(statistics.freePositionsNumber()));
        energyLabel.setText(String.format("%.2f",statistics.averageEnergy()));
        lifetimeLabel.setText(String.format("%.2f",statistics.averageLifetime()));
        childrenLabel.setText(String.format("%.2f",statistics.averageNumberOfChildren()));
        genotypeLabel.setText(statistics.mostPopularGenotype().toString());
    }

    private void updateSubjectStatistics() {
        Animal subjectAnimal = map.getSubjectAnimal();
        if (subjectAnimal != null) {
            SubjectStatistics subjectAnimalStats = subjectAnimal.getStatistics();
            partEnergyLabel.setText(String.valueOf(subjectAnimalStats.particularEnergy()));
            grassConsumedLabel.setText(String.valueOf(subjectAnimalStats.grassConsumed()));
            descendantsLabel.setText(String.valueOf(subjectAnimalStats.descendants()));
            partGenotypeLabel.setText(subjectAnimalStats.particularGenotype().getGenes().toString());
            activeGeneLabel.setText(String.valueOf(subjectAnimalStats.activeGene()));
            partAgeLabel.setText(String.valueOf(subjectAnimalStats.particularAge()));
            partChildrenLabel.setText(String.valueOf(subjectAnimalStats.particularChildren()));
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
}
