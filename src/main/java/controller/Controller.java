package controller;

import javafx.animation.AnimationTimer;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.scene.transform.Scale;
import model.force.Force;
import model.object.*;
import model.surface.Surface;

import javax.naming.LimitExceededException;
import java.net.URL;
import java.util.ResourceBundle;


public class Controller implements Initializable{

    private static final double deltaT = 0.001;
    private static final int WINDOW_WIDTH = 1550;

    @FXML
    private Slider forceSlider;
    @FXML
    private TextField appliedForceTf;
    @FXML
    private Slider staticFrictionSlider;
    @FXML
    private TextField staticFrictionTf;
    @FXML
    private Slider kineticFrictionSlider;
    @FXML
    private TextField kineticFrictionTf;
    @FXML
    private CheckBox massCheckBox;
    @FXML
    private CheckBox forceCheckBox;
    @FXML
    private CheckBox forceValueCheckBox;
    @FXML
    private CheckBox resultantForceCheckBox;
    @FXML
    private CheckBox speedCheckBox;
    @FXML
    private CheckBox accelerationCheckBox;
    @FXML
    private Pane speedPane;
    @FXML
    private TextField speedTf;
    @FXML
    private Pane angularSpeedPane;
    @FXML
    private TextField angularSpeedTf;
    @FXML
    private Pane accelerationPane;
    @FXML
    private TextField accelerationTf;
    @FXML
    private Pane angularAccelerationPane;
    @FXML
    private TextField angularAccelerationTf;
    @FXML
    private TextField positionTf;
    @FXML
    private Pane resultantForcePane;
    @FXML
    private Text resultantForceText;
    @FXML
    private Pane appliedForcePane;
    @FXML
    private Text appliedForceText;
    @FXML
    private Pane frictionForcePane;
    @FXML
    private Text frictionText;
    @FXML
    private Label massLabel;
    @FXML
    private Text massText;
     
    @FXML 
    private ImageView resultantForceArrow;
    @FXML
    private ImageView appliedForceArrow;
    @FXML
    private ImageView frictionArrow;

    @FXML
    private Rectangle cubeObject;
    @FXML
    private Circle cylinderObject;
    @FXML
    private Line cylinderLine1;
    @FXML
    private Line cylinderLine2;
    @FXML
    private Rectangle cubeObjectOn;
    @FXML
    private Circle cylinderObjectOn;
    @FXML
    private Line cylinderLineOn1;
    @FXML
    private Line cylinderLineOn2;
    @FXML
    private ImageView surface1;
    @FXML
    private ImageView surface2;
    @FXML
    private ImageView sky1;
    @FXML
    private ImageView sky2;

    @FXML
    private Button resetButton;
    @FXML
    private Button continueButton;
    @FXML
    private Button pauseButton;

    @FXML
    private Label initLabel;
    @FXML
    private Rectangle initRectangle;
    @FXML
    private ImageView goku;


    private static final double MAXIMUM_SIDELENGTH = 200;
    private static final double MAXIMUM_RADIUS = 100;
    private double appliedValue;
    private double staticFriction;
    private double kineticFriction;
    private boolean isCube = false;
    private boolean isCylinder = false;
    private PhysicalObject object;
    private Surface surface = new Surface(0, 0);
    private Force appliedForce = new Force(0);
    private boolean paused=false;
    AnimationTimer timer;

    @FXML
    @Override
    public void initialize(URL arg0, ResourceBundle arg1){
        resetButton.getStyleClass().add("resetButton");
        speedPane.setVisible(false); accelerationPane.setVisible(false);
        angularAccelerationPane.setVisible(false); angularSpeedPane.setVisible(false);
        resultantForceArrow.setVisible(false); appliedForceArrow.setVisible(false); frictionArrow.setVisible(false);
        resultantForcePane.setVisible(false); appliedForcePane.setVisible(false); frictionForcePane.setVisible(false);
        cubeObjectOn.setVisible(false);
        cylinderObjectOn.setVisible(false);
        cylinderLineOn1.setVisible(false);
        cylinderLineOn2.setVisible(false);
        massLabel.setVisible(false);
        massText.setVisible(false);
        forceSlider.setDisable(true);
        surface1.setX(0); surface2.setX(0);
        sky1.setX(0); sky2.setX(0);
        initLabel.setVisible(false); initRectangle.setVisible(false); goku.setVisible(false);

        forceSlider.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number number, Number t1) {
                appliedValue = (double) forceSlider.getValue();
                appliedForceTf.setText(String.format("%.4f",appliedValue));
                appliedForce.setValue(appliedValue);
                checkBoxUpdate();
                timer.start();

            }
        });

        appliedForceTf.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {
                if (keyEvent.getCode() == KeyCode.ENTER) {
                    try {
                        appliedValue = Double.parseDouble(appliedForceTf.getText());
                        if(appliedValue<-500||appliedValue>500){
                            throw new Exception();
                        }
                        forceSlider.adjustValue(appliedValue);
                        timer.start();
                    } catch (NumberFormatException e) {
                        inputAlert();
                        appliedForceTf.setText(String.format("%.4f",forceSlider.getValue()));
                    }catch (Exception e){
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Limit Exceeded");
                        alert.setHeaderText("Force cannot exceed 500N or be less than -500N");
                        alert.showAndWait();
                        appliedForceTf.setText(String.format("%.4f",forceSlider.getValue()));
                    }
                }
            }
        });
        staticFrictionSlider.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number number, Number t1) {
                staticFriction = (double) staticFrictionSlider.getValue();
                if (staticFriction < kineticFriction) {
                    kineticFriction = staticFriction-0.0001;
                    kineticFrictionSlider.setValue(kineticFriction);
                    
                }
                staticFrictionTf.setText(String.format("%.4f",staticFriction));
                surface.setStaticFrictionCoefficient(staticFriction);
            }
        });
        staticFrictionTf.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {
                if (keyEvent.getCode() == KeyCode.ENTER) {
                    try {
                        staticFriction = Double.parseDouble(staticFrictionTf.getText());
                        if(staticFriction<0||staticFriction>1){
                            throw new Exception();
                        }
                        staticFrictionSlider.adjustValue(staticFriction);
                    } catch (NumberFormatException e) {
                        inputAlert();
                        staticFrictionTf.setText(String.format("%.4f",staticFrictionSlider.getValue()));
                    }catch (Exception e) {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Error");
                        alert.setHeaderText("Static friction coefficient cannot be less than 0 or greater than 1");
                        alert.showAndWait();
                        staticFrictionTf.setText(String.format("%.4f",staticFrictionSlider.getValue()));
                    }
                }
            }
        });
        kineticFrictionSlider.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number number, Number t1) {
                kineticFriction = (double) kineticFrictionSlider.getValue();
                if (kineticFriction > staticFriction) {
                    staticFriction = kineticFriction+0.0001;
                    staticFrictionSlider.setValue(staticFriction);
                }
                kineticFrictionTf.setText(String.format("%.4f",kineticFriction));
                surface.setKineticFrictionCoefficient(kineticFriction);
            }
        });
        kineticFrictionTf.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {
                if (keyEvent.getCode() == KeyCode.ENTER) {
                    try {
                        kineticFriction = Double.parseDouble(kineticFrictionTf.getText());
                        if(staticFriction<0||staticFriction>1){
                            throw new Exception();
                        }
                        kineticFrictionSlider.adjustValue(kineticFriction);
                    } catch (NumberFormatException e) {
                        inputAlert();
                        kineticFrictionTf.setText(String.format("%.4f",kineticFrictionSlider.getValue()));
                    }catch (Exception e){
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Error");
                        alert.setHeaderText("Kinetic friction coefficient cannot be less than 0 or greater than 1");
                        alert.showAndWait();
                        kineticFrictionTf.setText(String.format("%.4f",kineticFrictionSlider.getValue()));
                    }
                }
            }
        });

        timer = new AnimationTimer() {
            @Override
            public void handle(long l) {
                updateObject();
            }
        };
    }


    public void cubeDrag(MouseEvent event){
       cubeObject.setTranslateX(event.getX() + cubeObject.getTranslateX() );
       cubeObject.setTranslateY(event.getY() + cubeObject.getTranslateY() );
    }


    public void cubeRelease(MouseEvent event){
        forceSlider.setDisable(true);
        appliedForceTf.setEditable(false);
        cubeObject.setTranslateX(0);
        cubeObject.setTranslateY(0);
        if(cubeObject.getTranslateX() == cubeObjectOn.getTranslateX() && cubeObject.getTranslateY() == cubeObjectOn.getTranslateY()){
            resetButton();
            cubeObject.setVisible(false);
            cubeObjectOn.setVisible(true);
            initLabel.setVisible(true);
            initRectangle.setVisible(true);
            initLabel.setVisible(true);
            initRectangle.setVisible(true);
        }
        checkBoxUpdate();
    }

    //pop up dialog for inputting mass and side length of cube
    public void cubeInit(MouseEvent event){
        if(!isCube){ //if cube has not been chosen, initialize cube
            inputDialog("cube");
        }
        initLabel.setVisible(false);
        initRectangle.setVisible(false);
        checkBoxUpdate();
    }



    public void cylinderDrag(MouseEvent event){
        cylinderObject.setTranslateX(event.getX() + cylinderObject.getTranslateX() );
        cylinderObject.setTranslateY(event.getY() + cylinderObject.getTranslateY() );
        cylinderLine1.setTranslateX(event.getX() + cylinderObject.getTranslateX());
        cylinderLine1.setTranslateY(event.getY() + cylinderObject.getTranslateY());
        cylinderLine2.setTranslateX(event.getX() + cylinderObject.getTranslateX());
        cylinderLine2.setTranslateY(event.getY() + cylinderObject.getTranslateY());
    }

    public void cylinderRelease(MouseEvent event){
        forceSlider.setDisable(true);
        appliedForceTf.setEditable(false);
        cylinderObject.setTranslateX(0); cylinderObject.setTranslateY(0);
        cylinderLine1.setTranslateX(0); cylinderLine1.setTranslateY(0);
        cylinderLine2.setTranslateX(0); cylinderLine2.setTranslateY(0);
        if(cylinderObject.getTranslateX() == cylinderObjectOn.getTranslateX() && cylinderObject.getTranslateY() == cylinderObjectOn.getTranslateY()){
            resetButton();
            cylinderObject.setVisible(false);
            cylinderLine1.setVisible(false);
            cylinderLine2.setVisible(false);
            cylinderObjectOn.setVisible(true);
            cylinderLineOn1.setVisible(true);
            cylinderLineOn2.setVisible(true);
            initLabel.setVisible(true);
            initRectangle.setVisible(true);
            initLabel.setVisible(true);
            initRectangle.setVisible(true);
        }
        checkBoxUpdate();
    }

    //pop up dialog for inputting mass and radius of cylinder
    public void cylinderInit(MouseEvent event){
        if(!isCylinder){ //if cylinder has not been chosen, initialize cylinder
            inputDialog("cylinder");
        }
        initLabel.setVisible(false);
        initRectangle.setVisible(false);
        checkBoxUpdate();
    }



    public void inputDialog(String objectType){
        Dialog<String> dialog = new Dialog<>();
        dialog.setTitle("Detail of Object");
        TextField massTf = new TextField();
        Label massLabel = new Label("Mass(kg):");
        massTf.setPromptText("mass");
        TextField sideLengthTf = new TextField();
        Label sideLengthLabel = new Label("Side Length(m):");
        sideLengthTf.setPromptText("side length");
        TextField radiusTf = new TextField();
        Label radiusLabel = new Label("Radius(m):");
        radiusTf.setPromptText("radius");
        Label radiusThreshold = new Label("The radius cannot exceed 100m.");
        Label sideLengthThreshold = new Label("The sidelength cannot exceed 200m.");
        ButtonType confirmButton = new ButtonType("Confirm", ButtonBar.ButtonData.OK_DONE);
        BorderPane pane = new BorderPane();
        GridPane content = new GridPane();
        content.add(massLabel, 0, 0);
        content.add(massTf, 1, 0);
        if(objectType.equalsIgnoreCase("cube")){
            content.add(sideLengthLabel, 0, 1);
            content.add(sideLengthTf, 1, 1);
            content.add(sideLengthThreshold, 0, 2);
        }
        else if(objectType.equalsIgnoreCase("cylinder")){
            content.add(radiusLabel, 0, 1);
            content.add(radiusTf, 1, 1);
            content.add(radiusThreshold, 0, 2);
        }
        content.setHgap(10);
        content.setVgap(10);
        dialog.getDialogPane().getButtonTypes().add(confirmButton);
        dialog.getDialogPane().setContent(content);
        dialog.setResultConverter(dialogButton ->{
            if(dialogButton == confirmButton){
                try {
                    if(objectType.equalsIgnoreCase("cube")){
                        double mass = Double.parseDouble(massTf.getText());
                        double sideLength = Double.parseDouble(sideLengthTf.getText());
                        if(mass <= 0 || sideLength <= 0){
                            throw new Exception();
                        }
                        if(sideLength > MAXIMUM_SIDELENGTH){
                            throw new LimitExceededException();
                        }
                        object = new Cube(mass, sideLength, surface, null);
                        isCube = true;
                        isCylinder = false;
                        forceSlider.setDisable(false);
                        appliedForceTf.setEditable(true);

                    }
                    else if(objectType.equalsIgnoreCase("cylinder")){
                        double mass = Double.parseDouble(massTf.getText());
                        double radius = Double.parseDouble(radiusTf.getText());
                        if(mass <= 0 || radius <= 0){
                            throw new Exception();
                        }
                        else if(radius>MAXIMUM_RADIUS){
                            throw new LimitExceededException();
                        }
                        object = new Cylinder(mass, radius*2, surface, null);
                        isCylinder = true;
                        isCube = false;
                        forceSlider.setDisable(false);
                        appliedForceTf.setEditable(true);

                    }

                } catch (NumberFormatException e) {
                    inputAlert();
                    dialog.showAndWait();
                }catch(LimitExceededException e){
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Limit Exceeded");
                    if(objectType.equalsIgnoreCase("cube")) {
                        alert.setHeaderText("The side length of cube cannot exceed 200m.");
                    }
                    else if(objectType.equalsIgnoreCase("cylinder")){
                        alert.setHeaderText("The radius of cylinder cannot exceed 100m.");
                    }
                    alert.showAndWait();
                    dialog.showAndWait();
                }catch (Exception e){
                    inputAlert();
                    dialog.showAndWait();
                }

            }
            return null;
        });
        dialog.showAndWait();
    }

    public void checkBoxUpdate(){
        //mass checkBox
        if (massCheckBox.isSelected()&&(isCube||isCylinder)) {
            massLabel.setVisible(true);
            massText.setVisible(true);
            massText.setText(String.format("%.2fkg",object.getMass()));
        } else {
            massLabel.setVisible(false);
            massText.setVisible(false);
        }
        if(appliedForceTf.getText()!=null&&(isCube||isCylinder)){
            goku.setVisible(true);
        }
        else{
            goku.setVisible(false);
        }
        //Force Checkbox
        if (forceCheckBox.isSelected()&&(isCube||isCylinder)&&appliedForceTf.getText()!=null) {
            appliedForceArrow.setVisible(true);
            frictionArrow.setVisible(true);
        } else {
            appliedForceArrow.setVisible(false);
            frictionArrow.setVisible(false);
        }
        //Force Value Checkbox
        if (forceValueCheckBox.isSelected()&&(isCube||isCylinder)&&appliedForceTf.getText()!=null){
            appliedForcePane.setVisible(true);
            frictionForcePane.setVisible(true);
        } else {
            appliedForcePane.setVisible(false);
            frictionForcePane.setVisible(false);
        }
        //Resultant Force Checkbox
        if (resultantForceCheckBox.isSelected()&&(isCube||isCylinder)&&appliedForceTf.getText()!=null) {
            resultantForceArrow.setVisible(true);
            resultantForcePane.setVisible(true);
        } else {
            resultantForceArrow.setVisible(false);
            resultantForcePane.setVisible(false);
        }
        //Speed checkbox
        if (speedCheckBox.isSelected()&&(isCube||isCylinder)) {
            speedPane.setVisible(true);
            speedTf.setText(String.format("%.2f",object.getVelocity()));
            angularSpeedPane.setVisible(true);
        } else {
            speedPane.setVisible(false);
            angularSpeedPane.setVisible(false);
        }
        //Acceleration checkbox
        if (accelerationCheckBox.isSelected()&&(isCube||isCylinder)) {
            accelerationPane.setVisible(true);
            accelerationTf.setText(String.format("%.2f",object.getAcceleration()));
            angularAccelerationPane.setVisible(true);
        } else {
            accelerationPane.setVisible(false);
            angularAccelerationPane.setVisible(false);
        }
    }

    public void resetButton(){
        goku.setVisible(false);
        initLabel.setVisible(false);
        initRectangle.setVisible(false);
        cubeObject.setVisible(true);
        cubeObjectOn.setVisible(false);
        cylinderLine1.setVisible(true); cylinderLine2.setVisible(true);
        cylinderObject.setVisible(true);
        cylinderObjectOn.setVisible(false);
        cylinderLineOn1.setVisible(false); cylinderLineOn1.setRotate(0);
        cylinderLineOn2.setVisible(false); cylinderLineOn2.setRotate(0);
        isCube = false;
        isCylinder = false;
        object=null;
        forceSlider.setValue(0); kineticFrictionSlider.setValue(0); staticFrictionSlider.setValue(0);
        appliedForceTf.setText(null); staticFrictionTf.setText(null); kineticFrictionTf.setText(null);
        speedCheckBox.setSelected(false); accelerationCheckBox.setSelected(false); forceCheckBox.setSelected(false);
        forceValueCheckBox.setSelected(false); resultantForceCheckBox.setSelected(false);
        speedTf.setText(null); accelerationTf.setText(null); positionTf.setText(null);
        speedPane.setVisible(false); accelerationPane.setVisible(false);
        angularAccelerationTf.setText(null); angularSpeedTf.setText(null);
        angularAccelerationPane.setVisible(false); angularSpeedPane.setVisible(false);
        resultantForceArrow.setVisible(false);appliedForceArrow.setVisible(false); frictionArrow.setVisible(false);
        resultantForcePane.setVisible(false);appliedForcePane.setVisible(false); frictionForcePane.setVisible(false);
        massText.setVisible(false); massLabel.setVisible(false);
        forceSlider.setDisable(true);
        timer.stop();
        surface1.setX(0); surface2.setX(0);
        sky1.setX(0); sky2.setX(0);
    }

    public void continueButton(){
        if(paused&&object!=null){
            timer.start();
            paused = false;
        }

    }
    public void pauseButton(){
        timer.stop();
        paused = true;
    }

    public void updateObject(){
        object.setAppliedForce(appliedForce);
        object.setSurface(surface);
        double prevPosition = object.getPosition();
        double prevAngularPosition = object.getAngularPosition();
        object.update(deltaT);

        positionTf.setText(String.format("%.2f",object.getPosition()));
        speedTf.setText(String.format("%.2f",object.getVelocity()));
        accelerationTf.setText(String.format("%.2f",object.getAcceleration()));

        angularAccelerationTf.setText(String.format("%.2f",object.getAngularAcceleration()));
        angularSpeedTf.setText(String.format("%.2f",object.getAngularVelocity()));

        appliedForceText.setText(String.format("%.4fN",object.getAppliedForceValue()));
        frictionText.setText(String.format("%.4fN",object.getFrictionValue()));
        resultantForceText.setText(String.format("%.4fN",object.getResultantForceValue()));

        forceSlider.adjustValue(object.getAppliedForceValue());

        forceDisplay(object.getAppliedForceValue(), appliedForceArrow, appliedForcePane);
        forceDisplay(object.getFrictionValue(), frictionArrow, frictionForcePane);
        forceDisplay(object.getResultantForceValue(), resultantForceArrow, resultantForcePane);

        //create movement effect
        if (surface1.getX() - (object.getPosition() - prevPosition)*25 < -WINDOW_WIDTH) {
            surface1.setX(0);
            surface2.setX(0);
        } else if (surface1.getX() - (object.getPosition() - prevPosition)*25 > 0) {
            surface1.setX(-WINDOW_WIDTH);
            surface2.setX(-WINDOW_WIDTH);
        } else {
            surface1.setX(surface1.getX() - (object.getPosition() - prevPosition)*25);
            surface2.setX(surface2.getX() - (object.getPosition() - prevPosition)*25);
        }

        if(sky1.getX() - (object.getPosition() - prevPosition)*10 < -WINDOW_WIDTH){
            sky1.setX(0);
            sky2.setX(0);
        } else if(sky1.getX() - (object.getPosition() - prevPosition)*10 > 0){
            sky1.setX(-WINDOW_WIDTH);
            sky2.setX(-WINDOW_WIDTH);
        } else {
            sky1.setX(sky1.getX() - (object.getPosition() - prevPosition)*10);
            sky2.setX(sky2.getX() - (object.getPosition() - prevPosition)*10);
        }
        if(appliedForce.getValue()<0){
            goku.setScaleX(-1);
            goku.setTranslateX(cubeObject.getWidth()+goku.getBoundsInLocal().getWidth()-3);
        }
        else{
            goku.setScaleX(1);
            goku.setTranslateX(0);
        }
        //create rotation effect for cylinder object
        if(object instanceof  Cylinder){
            cylinderLineOn1.setRotate(cylinderLineOn1.getRotate()-(object.getAngularPosition()-prevAngularPosition)*180/Math.PI*25);
            cylinderLineOn2.setRotate(cylinderLineOn2.getRotate()-(object.getAngularPosition()-prevAngularPosition)*180/Math.PI*25);
        }

    }

    //scaling arrow according to force value & shifting the force pane
    public void forceDisplay(Double forceValue, ImageView arrow, Pane forcePane){
        double arrowWidth = arrow.getFitWidth();
        double scaleFactor = forceValue/arrowWidth/1.5;
        Scale scale = new Scale();
        scale.setX(scaleFactor);

        if(arrow == frictionArrow){
            scale.setY(0.5);
        }
        else{
            scale.setY(1);
        }
        //set pivot point to the initial root of the arrow
        scale.setPivotX(0); scale.setPivotY(0);
        arrow.getTransforms().setAll(scale);

        double arrowScaledWidth = arrow.getBoundsInParent().getWidth();
        double shiftFactor =0;
        if(forceValue>=0){
            shiftFactor = arrowScaledWidth - arrowWidth;
        }
        else{
            shiftFactor = -arrowScaledWidth-arrowWidth-1.2*forcePane.getWidth();
        }
        forcePane.setTranslateX(shiftFactor);

    }

    public void inputAlert(){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText("Invalid Input");
        alert.showAndWait();
    }

}
