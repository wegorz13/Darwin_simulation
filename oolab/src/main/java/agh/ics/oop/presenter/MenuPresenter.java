package agh.ics.oop.presenter;

import agh.ics.oop.*;
import agh.ics.oop.model.*;
import agh.ics.oop.model.util.Boundary;
import agh.ics.oop.model.util.SimulationConfig;
import agh.ics.oop.model.util.Statistics;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.HPos;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import javax.swing.text.html.ImageView;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MenuPresenter {
    private SimulationConfig config;
    @FXML
    private VBox vBox;
    @FXML
    private Label errorMessage,animalsLabel,grassLabel,freePositionLabel,energyLabel,lifetimeLabel,childrenLabel,genotypeLabel;
    @FXML
    private VBox configBox,statisticsBox;
    @FXML
    private Spinner<Integer> mapWidth, mapHeight, baseGrassNumber, grassPerDay, basePopulation, baseEnergy, readyToParent, childCost, minMutations, maxMutations, genotypeLength, numberOfReservoirs, grassCalory;
    @FXML
    private CheckBox oldNotGold,saveStats;

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

    private void setConfig(){
        this.config = new SimulationConfig(basePopulation.getValue(),baseGrassNumber.getValue(),grassPerDay.getValue(),mapWidth.getValue(),mapHeight.getValue(),genotypeLength.getValue(),childCost.getValue(),minMutations.getValue(),maxMutations.getValue(),grassCalory.getValue(),baseEnergy.getValue(),readyToParent.getValue(),numberOfReservoirs.getValue(),oldNotGold.isSelected(),saveStats.isSelected());
    }

    private boolean verifyConfig(SimulationConfig config){
        return (config.mapHeight()<=config.mapWidth() && config.childCost()<config.readyToParent() && config.minMutations()<=config.maxMutations() && config.maxMutations()<=config.genotypeLength());
    }

    public void onSimulationStartClicked() throws IOException {
        this.setConfig();

        if (verifyConfig(this.config)){
            try {
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(getClass().getClassLoader().getResource("simulation.fxml"));
                BorderPane viewRoot = loader.load();
                SimulationPresenter presenter = loader.getController();
                Stage simulationStage = new Stage();
                simulationStage.setScene(new Scene(viewRoot));
                simulationStage.minWidthProperty().bind(viewRoot.minWidthProperty());
                simulationStage.minHeightProperty().bind(viewRoot.minHeightProperty());
                simulationStage.show();
                Simulation simulation = new Simulation(config);
                simulation.getMap().addListener(presenter);
                presenter.setSimulation(simulation);
                presenter.setMap(simulation.getMap());
                simulation.start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else {
            errorMessage.setText("Invalid input data");
        }
    }
}
