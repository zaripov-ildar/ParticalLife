package ru.starstreet.particallife;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.shape.Circle;
import ru.starstreet.particallife.model.*;

import java.net.URL;
import java.util.*;
import java.util.function.Consumer;

public class Controller implements Initializable {
    public TextField velocityField;
    public TextField minDistanceField;
    public TextField maxDistanceField;
    @FXML
    private TextField particlesAmountField;
    @FXML
    private Button runStopButton;
    @FXML
    private MenuButton positionsMenu;
    @FXML
    private AnchorPane rightPane;
    @FXML
    private Pane canvas;
    @FXML
    private VBox typePropertiesList;
    @FXML
    private ColorPicker colorPicker;
    @FXML
    private MenuButton typeMenu;
    @FXML
    private TextField typeAmountField;
    private PetriDish petri;
    private int selectedType;
    private boolean isRunning;
    private List<Circle> circles;
    private double width;
    private double height;
    private double scale;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        selectedType = 0;
        petri = new PetriDish();
        initPositionMenu();
        initListeners();
        initField();
        initTypesProperties();

    }

    private void initTypesProperties() {
        typeAmountField.setText(String.valueOf(petri.getTypesAmount()));
        typePropertiesList.getChildren().clear();
        for (int i = 0; i < petri.getTypesAmount(); i++) {
            Map<Integer, Double> attraction = petri.getType(selectedType).getAttraction();
            TextField tf = new TextField(String.valueOf(attraction.get(i)));
            final int finalI = i;
            addTextFieldFocusListener(tf, x -> attraction.put(finalI, x));
            HBox pane = new HBox(new Label(String.valueOf(i)), tf);
            typePropertiesList.getChildren().add(pane);
        }
    }

    private void initPositionMenu() {
        for (Sower value : Sower.values()) {
            MenuItem item = new MenuItem(value.name());
            positionsMenu.getItems().add(item);
        }
        positionsMenu.setText(petri.getSower().name());
    }


    private void initField() {
        int size = petri.getParticles().size();
        updateScale();
        petri.setScale(scale);
        circles = new ArrayList<>(size);
        canvas.getChildren().clear();
        for (int i = 0; i < size; i++) {
            double x = petri.getX(i);
            double y = petri.getY(i);
            Circle c = new Circle(x, y, 5);
            c.setFill(petri.getParticles().get(i).getType().getColor());
            circles.add(c);
        }
        Group group = new Group();
        group.getChildren().addAll(circles);
        canvas.setStyle("-fx-background-color: black");
        canvas.getChildren().add(group);

        velocityField.setText(String.valueOf(petri.getVELOCITY()));
        minDistanceField.setText(String.valueOf(petri.getDISTANCE_MIN()));
        maxDistanceField.setText(String.valueOf(petri.getDISTANCE_MAX()));
    }



    private void initListeners() {
        runStopButton.setOnAction(actionEvent -> {
            isRunning = !isRunning;
            runStopButton.setText(isRunning ? "Stop" : "Run!");
            startLife();
        });
        rightPane.widthProperty().addListener((obs, prevVal, newVal) -> {
            width = (double) newVal;
            updateScale();
            canvas.setPrefWidth(scale);
            petri.setScale(scale);
        });
        rightPane.heightProperty().addListener((obs, prevVal, newVal) -> {
            height = (double) newVal;
            updateScale();
            canvas.setPrefWidth(scale);
            petri.setScale(scale);
        });
        addTextFieldFocusListener(velocityField, x -> petri.setVELOCITY(x));
        addTextFieldFocusListener(minDistanceField, x -> petri.setDISTANCE_MIN(x));
        addTextFieldFocusListener(maxDistanceField, x -> petri.setDISTANCE_MAX(x));


        colorPicker.setOnAction(actionEvent -> petri.getType(selectedType).setColor(colorPicker.getValue()));

    }

    private void addTextFieldFocusListener(TextField tf, Consumer<Double> consumer) {
        tf.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal) {
                try {
                    double t = Double.parseDouble(tf.getText());
                    consumer.accept(t);
                } catch (NumberFormatException ignore) {
                }
            }
        });
    }

    private void updateScale() {
        width = rightPane.getWidth();
        height = rightPane.getHeight();
        scale = Math.min(width, height);
    }

    public void setTypesAmount() {
        selectedType = 0;
        petri.setTypesAmount(getNewAmount());
        typeAmountField.setText(String.valueOf(petri.getTypesAmount()));
        updateProperties();
    }

    private void updateProperties() {
        typePropertiesList.getChildren().clear();
        typeMenu.getItems().clear();
        int typesAmount = petri.getTypesAmount();
        for (int i = 0; i < typesAmount; i++) {
            addTypeToTypeMenu(i);
            addTypeToTypeProperties(i);
        }
        typeMenu.setText("Type " + selectedType);
        colorPicker.setValue(petri.getColor(0));
    }

    private void addTypeToTypeProperties(int i) {
        TextField tf = new TextField("0");
        addTextFieldFocusListener(tf, x -> petri.getType(selectedType).getAttraction().put(i, Double.parseDouble(tf.getText())));
        HBox pane = new HBox(new Label(String.valueOf(i)), tf);
        typePropertiesList.getChildren().add(pane);
    }


    private void addTypeToTypeMenu(int i) {
        MenuItem item = new MenuItem("Type " + i);
        item.setOnAction(actionEvent -> {
            typeMenu.setText(item.getText());
            selectedType = i;
            colorPicker.setValue(petri.getColor(i));
            fillTypePropertiesList(petri.getType(i));
        });
        typeMenu.getItems().add(item);
    }

    private void fillTypePropertiesList(ParticleType type) {
        for (int i = 0; i < typePropertiesList.getChildren().size(); i++) {
            HBox box = (HBox) typePropertiesList.getChildren().get(i);
            TextField field = (TextField) box.getChildren().get(1);
            field.setText(String.valueOf(type.getAttraction().get(i)));
        }
        colorPicker.setValue(type.getColor());
    }


    private int getNewAmount() {
        int newAmount;
        try {
            newAmount = Integer.parseInt(typeAmountField.getText());
        } catch (NumberFormatException ignore) {
            newAmount = 0;
        }
        return newAmount;
    }

    private void startLife() {
        Thread t = new Thread(() -> {
            while (isRunning) {
                petri.calculateNext();
                Platform.runLater(() -> {
                    for (int i = 0; i < petri.getParticles().size(); i++) {
                        double x = petri.getX(i);
                        double y = petri.getY(i);
                        circles.get(i).setCenterX(x);
                        circles.get(i).setCenterY(y);
                        circles.get(i).fillProperty().set(petri.getParticleColor(i));
                    }
                });
            }
        });
        t.setDaemon(true);
        t.start();
    }

    public void setParticlesAmount(ActionEvent actionEvent) {

            petri.setParticlesAmount(Integer.parseInt(particlesAmountField.getText()));
            particlesAmountField.setText(String.valueOf(petri.getParticlesAmount()));
            initField();

        System.out.println(circles.size() + "\t" + petri.getParticlesAmount());


    }
}