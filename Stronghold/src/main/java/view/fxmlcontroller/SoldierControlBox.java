package view.fxmlcontroller;

import controller.GameControl;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import model.Database;
import model.environment.buildings.Building;
import model.map.Block;
import model.units.Person;
import model.units.Soldier;
import model.units.enums.SoldierUnitState;
import model.units.workerunits.Engineer;
import view.GameViewController;
import view.enums.messages.GameMessages;

public class SoldierControlBox {
    public ImageView soldierImage;
    public Group states;
    public Text selectedBuilding;
    public Text selectedBlock;
    public Button attackBtn;
    public Button moveBtn;
    public Button attackBuildingBtn;
    public Pane pane;
    public Text name;
    public Button repairBtn;

    @FXML
    private void initialize() {
        pane.setBackground(new Background(new BackgroundFill(Color.DARKGOLDENROD,
                new CornerRadii(10), new Insets(0))));
        if (GameControl.getSelectedUnit() == null) {
            pane.getChildren().remove(repairBtn);
            return;
        }
        else if (GameControl.getSelectedUnit() instanceof Soldier) {
            Soldier soldier = (Soldier) GameControl.getSelectedUnit();
            soldierImage.setImage(new Image(soldier.getUnitName().getImagePath()));

            int i = 0;
            for (Node node : states.getChildren()) {
                ImageView state = (ImageView) node;
                state.setImage(new Image(SoldierUnitState.values()[i].getImagePath()));
                int finalI = i;
                state.setOnMouseClicked(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent mouseEvent) {
                        soldier.setSoldierUnitState(SoldierUnitState.values()[finalI]);
                    }
                });
                i++;
            }
            pane.getChildren().remove(repairBtn);
            setTexts();
        }
        else {
            Person person = GameControl.getSelectedUnit();
            soldierImage.setImage(new Image(person.getUnitName().getImagePath()));
            setTexts();
            pane.getChildren().removeAll(attackBtn, attackBuildingBtn);
            if (!(person instanceof Engineer)) {
                pane.getChildren().remove(repairBtn);
            }
        }
    }

    private void setTexts() {
        name.setText(GameControl.getSelectedUnit().getUnitName().getName());
        if (GameControl.getSelectedBuilding() != null) {
            Building building1 = GameControl.getSelectedBuilding();
            selectedBuilding.setText("Selected building: " + building1.toString());
        }

        if (!GameViewController.getMapPane().getSelectedTiles().isEmpty())
            selectedBlock.setText("Selected block: "+ GameViewController.getMapPane().getSelectedTiles().get(0).getBlock().toString());
        else if (Database.getCurrentMap() != null){
            selectedBlock.setText(Database.getCurrentMap().getBlockByXY(0,0).toString());
        }
    }

    public void attackBuilding(MouseEvent mouseEvent) {
        System.out.println(GameControl.attackSelectedBuilding().message());
    }

    public void move(MouseEvent mouseEvent) {
        Block block = GameViewController.getMapPane().getSelectedTiles().get(0).getBlock();
        System.out.println(GameControl.moveUnit(block.getX(), block.getY()).message());
    }

    public void attack(MouseEvent mouseEvent) {
        Block block = GameViewController.getMapPane().getSelectedTiles().get(0).getBlock();
        System.out.println(GameControl.attack(block.getX(), block.getY()).message());
    }

    public void repair(MouseEvent mouseEvent) {
        System.out.println(GameControl.repair());
    }
}
