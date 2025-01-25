package agh.ics.oop.presenter;

import agh.ics.oop.Simulation;
import agh.ics.oop.SimulationEngine;
import agh.ics.oop.model.*;
import agh.ics.oop.model.util.Boundary;
import agh.ics.oop.model.util.SimulationConfig;
import agh.ics.oop.model.util.Statistics;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.HPos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import java.util.ArrayList;
import java.util.List;

public class SimulationPresenter implements MapChangeListener {
    private GrassField map;
    private SimulationConfig config;
    private Statistics statistics;
    private static final int GRIDPANEHEIGHT = 800;
    private static final int GRIDPANEWIDTH = 800;

    @FXML
    private GridPane mapGrid;
    @FXML
    private Label errorMessage,animalsLabel,grassLabel,freePositionLabel,energyLabel,lifetimeLabel,childrenLabel,genotypeLabel;
    @FXML
    private VBox configBox,statisticsBox;
    @FXML
    private Spinner<Integer> mapWidth, mapHeight, baseGrassNumber, grassPerDay, basePopulation, baseEnergy, readyToParent, childCost, minMutations, maxMutations, genotypeLength, numberOfReservoirs, grassCalory;
    @FXML
    private CheckBox oldNotGold;
    @FXML
    private Label dayOfDeathLabel,partAgeLabel,partEnergyLabel,grassConsumedLabel,descendantsLabel,partGenotypeLabel,activeGeneLabel,partChildrenLabel;

    @FXML
    public void initialize() {
        // Configure spinners with default values and ranges
        mapWidth.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(5, 1000, 12));
        mapHeight.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(5, 1000, 12));
        baseGrassNumber.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 100, 20));
        grassPerDay.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 50, 10));
        basePopulation.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 500, 15));
        baseEnergy.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 1000, 15));
        readyToParent.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 500, 10));
        childCost.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 500, 5));
        minMutations.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 10, 0));
        maxMutations.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 10, 3));
        genotypeLength.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 20, 5));
        numberOfReservoirs.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 5, 1));
        grassCalory.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 20, 10));
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

                cell.setOnMouseClicked(event -> {this.map.setSubjectAnimal(pos);});

                mapGrid.add(cell, x - bounds.leftDownCorner().getX() + 1, bounds.rightUpCorner().getY() - y + 1);
                GridPane.setHalignment(mapGrid.getChildren().getLast(), HPos.CENTER);
                updateStatistics(this.statistics);
            }
        }
     }

    private void setConfig(){
         this.config = new SimulationConfig(basePopulation.getValue(),baseGrassNumber.getValue(),grassPerDay.getValue(),mapWidth.getValue(),mapHeight.getValue(),genotypeLength.getValue(),childCost.getValue(),minMutations.getValue(),maxMutations.getValue(),grassCalory.getValue(),baseEnergy.getValue(),readyToParent.getValue(),numberOfReservoirs.getValue(),oldNotGold.isSelected());
     }

     private boolean verifyConfig(SimulationConfig config){
        return (config.mapHeight()<=config.mapWidth() && config.childCost()<config.readyToParent() && config.minMutations()<=config.maxMutations() && config.maxMutations()<=config.genotypeLength());
     }

    public void onSimulationStartClicked() {
        this.setConfig();

        if (verifyConfig(this.config)){
            configBox.setVisible(false);
            configBox.setManaged(false);
            statisticsBox.setVisible(true);
            statisticsBox.setManaged(true);

            List<MoveDirection> directions = new ArrayList<>();
            List<Vector2d> positions = new ArrayList<>();
            GrassField map = new GrassField(this.config);
            map.addListener(this);
            Simulation simulation = new Simulation(positions, directions, map);
            SimulationEngine engine = new SimulationEngine(List.of(simulation));
            new Thread(engine::runSync).start();
        }
        else {
            errorMessage.setText("Invalid input data");
        }
    }

    private void updateStatistics(Statistics statistics){
        animalsLabel.setText(String.valueOf(statistics.animalsNumber()));
        grassLabel.setText(String.valueOf(statistics.grassNumber()));
        freePositionLabel.setText(String.valueOf(statistics.freePositionsNumber()));
        energyLabel.setText(String.format("%.2f",statistics.averageEnergy()));
        lifetimeLabel.setText(String.format("%.2f",statistics.averageLifetime()));
        childrenLabel.setText(String.format("%.2f",statistics.averageNumberOfChildren()));
        genotypeLabel.setText(String.valueOf(statistics.mostPopularGenotype().getGenes()));

        if (statistics.subjectStatistics()!=null){
            partEnergyLabel.setText(String.valueOf(statistics.subjectStatistics().particularEnergy()));
            grassConsumedLabel.setText(String.valueOf(statistics.subjectStatistics().grassConsumed()));
            descendantsLabel.setText(String.valueOf(statistics.subjectStatistics().descendants()));
            partGenotypeLabel.setText(String.valueOf(statistics.subjectStatistics().particularGenotype().getGenes()));
            activeGeneLabel.setText(String.valueOf(statistics.subjectStatistics().activeGene()));
            partAgeLabel.setText(String.valueOf(statistics.subjectStatistics().particularAge()));
            partChildrenLabel.setText(String.valueOf(statistics.subjectStatistics().particularChildren()));
            if (statistics.subjectStatistics().dayOfDeath()>0){
                dayOfDeathLabel.setText(String.valueOf(statistics.subjectStatistics().dayOfDeath()));
            }
        }
    }

    @Override
    public void mapChanged(GrassField worldMap, String message) {
        this.map=worldMap;
        this.statistics=worldMap.getStatistics();
        Platform.runLater(this::drawMap);
    }
}
