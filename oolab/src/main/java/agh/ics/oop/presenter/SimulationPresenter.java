package agh.ics.oop.presenter;

import agh.ics.oop.OptionsParser;
import agh.ics.oop.Simulation;
import agh.ics.oop.SimulationEngine;
import agh.ics.oop.model.*;
import agh.ics.oop.model.util.Boundary;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.HPos;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;

import java.util.List;

import static java.lang.Math.round;

public class SimulationPresenter implements MapChangeListener {
    private WorldMap map;
    private static final int GRIDPANEHEIGHT = 400;
    private static final int GRIDPANEWIDTH = 400;
    @FXML
    private GridPane mapGrid;
    @FXML
    private TextField moveList;
    @FXML
    private Label moveDescription;
    
    public void setWorldMap(WorldMap map) {
        this.map = map;
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
                Label cell = new Label(" ");
                if (map.isOccupied(pos)) {
                    cell.setText(map.objectAt(pos).toString());
                }
                mapGrid.add(cell, x - bounds.leftDownCorner().getX() + 1, bounds.rightUpCorner().getY() - y + 1);
                GridPane.setHalignment(mapGrid.getChildren().getLast(), HPos.CENTER);
            }
        }
     }

    public void onSimulationStartClicked() {
        try {
            List<MoveDirection> directions = OptionsParser.parse(moveList.getText().split(" "));
            List<Vector2d> positions = List.of(new Vector2d(2,2), new Vector2d(3,4));
            GrassField map = new GrassField(10);
            map.addListener(this);
            Simulation simulation = new Simulation(positions, directions, map);
            SimulationEngine engine = new SimulationEngine(List.of(simulation));
            new Thread(engine::runAsync).start();
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void mapChanged(WorldMap worldMap, String message) {
        setWorldMap(worldMap);
        Platform.runLater(() -> {
            drawMap();
            moveDescription.setText(message);
        });
    }
}
