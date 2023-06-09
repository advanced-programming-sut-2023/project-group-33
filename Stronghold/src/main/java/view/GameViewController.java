package view;

import controller.GameControl;
import javafx.animation.Animation.Status;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TabPane;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Screen;
import model.App;
import model.Database;
import model.Game;
import model.environment.buildings.Building;
import model.environment.buildings.ResourceExtractorBuilding;
import model.environment.buildings.UnitMakerBuilding;
import model.map.Block;
import model.map.Texture;
import model.units.Person;
import utility.DataManager;
import view.animation.MoveAnimation;
import view.fxmlcontroller.BuildingScroll;
import view.fxmlcontroller.CreateUnitBox;
import view.fxmlcontroller.DropUnitBox;
import view.fxmlcontroller.SoldierControlBox;
import view.shape.BuildingNode;
import view.shape.Fire;
import view.shape.PersonNode;
import view.shape.government.ControlPanel;
import view.shape.map.MapPane;
import view.shape.map.MapTile;
import view.shape.map.MiniMap;

import java.io.IOException;
import java.util.Objects;
import java.util.Queue;

// TODO: 6/29/2023 add current government. 
public class GameViewController {
    private static Scene scene;
    private static Pane mainPane;
    private static MapPane mapPane;
    private static MiniMap miniMap;
    private static ScrollPane mapTextureOptions;
    private static TabPane buildingScroll;
    private static ControlPanel controlPanel;
    private static Pane personControl;
    private static ScrollPane dropUnitBox;
    private static Pane createUnitBox;
    private static Pane extractorRateSetBox;
    private static Group allPersons;
    private static Group allBuildings;

    public static void setGameViewController() throws IOException {

        GameViewController.mainPane = new Pane();
        mainPane.setBackground(new Background(new BackgroundFill(Color.BLANCHEDALMOND, new CornerRadii(0), new Insets(0))));
        mapPane = new MapPane(Database.getCurrentGame().getMap());
        controlPanel = new ControlPanel();
        miniMap = controlPanel.getMiniMap();

        mapTextureOptions = FXMLLoader.load(Objects.requireNonNull(MapPane.class.getResource(DataManager.CHANGE_TEXTURE_BOX)));
        buildingScroll = FXMLLoader.load(Objects.requireNonNull(BuildingScroll.class.getResource(DataManager.BUILDING_SCROLL_BOX)));
        dropUnitBox = FXMLLoader.load(Objects.requireNonNull(DropUnitBox.class.getResource("/fxml/drop-unit-box.fxml")));
        allPersons = new Group();
        allBuildings = new Group();

        scene = new Scene(mainPane);

        mainPane.getChildren().add(mapPane);
        addNode(controlPanel.gethBox(),0, Screen.getPrimary().getBounds().getHeight() - controlPanel.gethBox().getMaxHeight());
        setKeys();
        for (Building building : Database.getCurrentGame().getAllBuildings()) {
            addBuilding(building);
        }

        mapPane.getChildren().addAll(allBuildings, allPersons);
    }

    public static void updateControlPanel() {
        controlPanel.update();
    }

    public static Scene getScene() {
        return scene;
    }

    private static void setKeys() {

        scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {

                if (keyEvent.getCode().equals(KeyCode.T)) {
                    addNode(mapTextureOptions, App.centerX - mapTextureOptions.getPrefWidth() / 2,
                            App.centerY - mapTextureOptions.getPrefHeight() / 2 - 100);
                } else if (keyEvent.getCode().equals(KeyCode.U)) {
                    addNode(dropUnitBox, App.centerX - dropUnitBox.getPrefWidth() / 2,
                            App.centerY - dropUnitBox.getPrefHeight() / 2 - 100);
                } else if (keyEvent.getCode().equals(KeyCode.R)) {
                    mapPane.reset();
                } else if (keyEvent.getCode().equals(KeyCode.V)) {
                    if (Database.getCopyBuilding() != null) {
                        Block block = mapPane.getSelectedTiles().get(0).getBlock();
                        GameControl.dropBuilding(block.getX(), block.getY(), Database.getCopyBuilding());
                    }
                } else if (keyEvent.getCode().equals(KeyCode.N)) {
                    GameControl.nextTurn();
                } else if (keyEvent.getCode().equals(KeyCode.M)) {
                    removeNode(personControl);
                    try {
                        personControl = FXMLLoader.load(SoldierControlBox.class.getResource(DataManager.SOLDIER_CONTROL_BOX));
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }

                    addNode(personControl, App.centerX - personControl.getPrefWidth() / 2,
                            App.centerY - personControl.getPrefHeight() / 2);
                } else if (keyEvent.getCode().equals(KeyCode.B)) {
                    removeNode(createUnitBox);
                    removeNode(extractorRateSetBox);
                    if (GameControl.getSelectedBuilding() instanceof UnitMakerBuilding) {
                        try {
                            createUnitBox = FXMLLoader.load(CreateUnitBox.class.getResource("/fxml/unit-maker-building-control-box.fxml"));
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                        addNode(createUnitBox, App.centerX - createUnitBox.getPrefWidth() / 2,
                                App.centerY - createUnitBox.getPrefHeight() / 2);
                    }
                    if (GameControl.getSelectedBuilding() instanceof ResourceExtractorBuilding) {
                        try {
                            extractorRateSetBox = FXMLLoader.load(CreateUnitBox.class.getResource("/fxml/extractor-building-control-box.fxml"));
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                        addNode(extractorRateSetBox, App.centerX - extractorRateSetBox.getPrefWidth() / 2,
                                App.centerY - extractorRateSetBox.getPrefHeight() / 2);
                    }
                } else {
                    mainPane.getChildren().remove(mapTextureOptions);
                    mainPane.getChildren().remove(personControl);
                    mainPane.getChildren().remove(dropUnitBox);
                    mainPane.getChildren().remove(createUnitBox);
                    mainPane.getChildren().remove(extractorRateSetBox);
                }
            }
        });
    }

    public static void addNode(Node node, double x, double y) {
        if (!mainPane.getChildren().contains(node)) {
            mainPane.getChildren().add(node);
            node.setLayoutX(x);
            node.setLayoutY(y);
        }
    }

    public static void removeNode(Node node) {
        mainPane.getChildren().remove(node);
    }

    public static void addUnit(Person person) {
        PersonNode pn = new PersonNode(person);
        pn.setLayoutX(getLayoutXForPerson(person.getBlock().getTile()));
        pn.setLayoutY(getLayoutYForPerson(person.getBlock().getTile()));
        allPersons.getChildren().add(pn);
    }

    public static double getLayoutXForPerson(MapTile tile) {
        return tile.getLayoutX() + MapTile.TILE_WIDTH * 0.1 + tile.getBlock().getUnits().size() * 3;
    }

    public static double getLayoutYForPerson(MapTile tile) {
        return tile.getLayoutY() +
                MapTile.TILE_HEIGHT * 0.1 + tile.getBlock().getUnits().size() * 1.5;
    }

    public static PersonNode getPersonNodeByPerson(Person person) {
        for (Node child : allPersons.getChildren()) {
            if (!(child instanceof PersonNode))
                return null;
            PersonNode pn = (PersonNode) child;
            if (pn.getPerson().equals(person))
                return pn;
        }
        return null;
    }

    public static BuildingNode getBuildingNodeByBuilding(Building building) {
        // TODO: 6/29/2023 check null
        for (Node child : allBuildings.getChildren()) {
            if (!(child instanceof BuildingNode))
                return null;
            BuildingNode bn = (BuildingNode) child;
            if (bn.getBuilding().equals(building))
                return bn;
        }
        return null;
    }

    public static void moveUnit(Person person, Queue<Block> movedQueue) {
        if (movedQueue.isEmpty())
            return;
        MapTile toTile = movedQueue.poll().getTile();
        PersonNode pn = getPersonNodeByPerson(person);
        if (pn == null)
            return;

        MoveAnimation ma = new MoveAnimation(pn, toTile);
        ma.play();
        person.setMoving(true);
        ma.statusProperty().addListener(new ChangeListener<Status>() {

            @Override
            public void changed(ObservableValue<? extends Status> observableValue,
                                Status oldValue, Status newValue) {
                if (newValue == Status.STOPPED) {
                    if (movedQueue.isEmpty()) {
                        person.setMoving(false);
                        return;
                    }
                    ma.changeDestination(movedQueue.poll().getTile());
                    ma.play();
                }
            }
        });
    }

    public static void selectUnit() {

    }

    public static void addBuilding(Building building) {
        BuildingNode bn = new BuildingNode(building);
        bn.setLayoutX(building.getBlock().getTile().getLayoutX());
        bn.setLayoutY(building.getBlock().getTile().getLayoutY() - bn.getHeight() / 2);
        allBuildings.getChildren().add(bn);
    }

    public static void removeBuilding(Building building) {
        allBuildings.getChildren().remove(getBuildingNodeByBuilding(building));
    }

    public static void removePerson(Person person) {
        allPersons.getChildren().remove(getPersonNodeByPerson(person));
    }

    public static void setFire(Building building) {
        BuildingNode bn = getBuildingNodeByBuilding(building);

        Fire fire = new Fire();
        fire.setLayoutX(bn.getLayoutX() - MapTile.TILE_WIDTH / 4);
        fire.setLayoutY(bn.getLayoutY() - MapTile.TILE_HEIGHT);

        allBuildings.getChildren().add(fire);
    }

    public static void setTexture(Texture texture) {
        mapPane.setTexture(texture);
        miniMap.setTexture(texture, mapPane.getSelectedTiles());
    }

    public static void makeItSick(MapTile tile) {
        tile.makeItSick();
        miniMap.makeItSick(tile);
    }

    public static void cureIt(MapTile tile) {
        miniMap.cureIt(tile);
    }

    public static MapPane getMapPane() {
        return mapPane;
    }
}

