package agh.ics.oop.presenter;

import agh.ics.oop.*;
import agh.ics.oop.model.util.SimulationConfig;
import com.google.gson.GsonBuilder;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.io.*;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

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
    private TextField nameOfConfiguration;
    @FXML
    private ComboBox<String> configurations;

    private final static Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private final static Type TYPE = new TypeToken<Map<String, Map<String, Integer>>>(){}.getType();

    @FXML
    public void initialize() {
        getConfigurationsToComboBox();
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

    private void setConfig() {
        this.config = new SimulationConfig(basePopulation.getValue(),baseGrassNumber.getValue(),grassPerDay.getValue(),mapWidth.getValue(),mapHeight.getValue(),genotypeLength.getValue(),childCost.getValue(),minMutations.getValue(),maxMutations.getValue(),grassCalory.getValue(),baseEnergy.getValue(),readyToParent.getValue(),numberOfReservoirs.getValue(),oldNotGold.isSelected(),saveStats.isSelected());
    }

    private boolean verifyConfig(SimulationConfig config) {
        return (config.mapHeight() <= config.mapWidth() && config.childCost() < config.readyToParent() && config.minMutations() <= config.maxMutations() && config.maxMutations() <= config.genotypeLength());
    }

    private void setConfigToSpinners(Map<String, Integer> config) {
        mapWidth.getValueFactory().setValue(config.get("mapWidth"));
        mapHeight.getValueFactory().setValue(config.get("mapHeight"));
        baseGrassNumber.getValueFactory().setValue(config.get("baseGrassNumber"));
        grassPerDay.getValueFactory().setValue(config.get("grassPerDay"));
        basePopulation.getValueFactory().setValue(config.get("basePopulation"));
        baseEnergy.getValueFactory().setValue(config.get("baseEnergy"));
        readyToParent.getValueFactory().setValue(config.get("readyToParent"));
        childCost.getValueFactory().setValue(config.get("childCost"));
        minMutations.getValueFactory().setValue(config.get("minMutations"));
        maxMutations.getValueFactory().setValue(config.get("maxMutations"));
        genotypeLength.getValueFactory().setValue(config.get("genotypeLength"));
        numberOfReservoirs.getValueFactory().setValue(config.get("numberOfReservoirs"));
        grassCalory.getValueFactory().setValue(config.get("grassCalory"));
        oldNotGold.setSelected(config.get("oldNotGold") == 1);
    }

    public void onSimulationStartClicked() throws IOException {
        this.setConfig();
        errorMessage.setText("");
        if (verifyConfig(this.config)) {
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

    public void onClickSaveConfiguration() {
        String nameOfConfig = nameOfConfiguration.getText();

        errorMessage.setText("");
        if (nameOfConfig.isEmpty()) {
            errorMessage.setText("Name of configuration can't be empty");
            return;
        }

        setConfig();
        if (verifyConfig(this.config)) {
            Map<String, Map<String, Integer>> allConfigs;
            try (FileReader reader = new FileReader("configurations.json")) {
                allConfigs = GSON.fromJson(reader, TYPE);
                if (allConfigs == null) allConfigs = new HashMap<>();
            } catch (FileNotFoundException e) {
                allConfigs = new HashMap<>();
            } catch (IOException e) {
                System.out.println("An I/O error occurred: " + e.getMessage());
                return;
            }

            allConfigs.put(nameOfConfig, this.config.changeToJsonFormat());

            try (Writer writer = new FileWriter("configurations.json")) {
                GSON.toJson(allConfigs, writer);
            } catch (IOException e) {
                System.out.println("An I/O error occurred: " + e.getMessage());
                return;
            }
            getConfigurationsToComboBox();
        } else {
            errorMessage.setText("Invalid input data");
        }
    }

    private void getConfigurationsToComboBox() {
        Map<String, Map<String, Integer>> allConfigs;
        try (FileReader reader = new FileReader("configurations.json")) {
            allConfigs = GSON.fromJson(reader, TYPE);
            if (allConfigs == null) allConfigs = new HashMap<>();
        } catch (FileNotFoundException e) {
            allConfigs = new HashMap<>();
        } catch (IOException e) {
            System.out.println("An I/O error occurred: " + e.getMessage());
            return;
        }

        configurations.getItems().clear();
        for (String nameOfConfig : allConfigs.keySet()) {
            configurations.getItems().add(nameOfConfig);
        }

        Map<String, Map<String, Integer>> finalAllConfigs = allConfigs;
        configurations.setOnAction(event -> {
            String selectedItem = (String) configurations.getValue();
            System.out.println(selectedItem);
            if (selectedItem != null) {
                Map<String, Integer> selectedConfig = finalAllConfigs.get(selectedItem);
                setConfigToSpinners(selectedConfig);
            }
        });
    }
}
