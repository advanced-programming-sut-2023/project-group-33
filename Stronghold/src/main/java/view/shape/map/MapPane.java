package view.shape.map;

import javafx.animation.FadeTransition;
import javafx.animation.ScaleTransition;
import javafx.beans.value.ChangeListener;
import javafx.event.EventHandler;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.util.Duration;
import model.map.Map;
import model.map.Texture;

import java.util.ArrayList;

public class MapPane extends Pane {
    private static double WIDTH;
    private static double HEIGHT;
    Map map;
    Group allTiles;
    Group allPersons;
    Text detailsText = new Text();
    private double scale = 1;
    private final double MAX_SCALE = 1.5;
    private final double MIN_SCALE = 0.5;
    private ArrayList<MapTile> selected;
    private Rectangle selectRect;


    public MapPane(Map map) {
        this.map = map;
        WIDTH = map.getWidth();
        HEIGHT = map.getHeight();
        int tilesCount = (int) (WIDTH * HEIGHT);
        allTiles = new Group();
        selected = new ArrayList<>();

        for (int i = 0; i < tilesCount; i++) {
            int x = (int) (i / WIDTH);
            int y = (int) (i % WIDTH);

            MapTile tile = tileInit(x, y);
            setTileHover(tile);
            setTileDetailShow(tile);

            allTiles.getChildren().add(tile);
        }
        this.getChildren().add(allTiles);

        setSelectRect();
        setDragMove();
    }

    public void setTexture(Texture texture) {
        for (MapTile tile : selected) {
            map.setTexture(tile.getBlock().getX(), tile.getBlock().getY(), texture);
            tile.setFill(new ImagePattern(new Image(tile.getBlock().getTexture().getImagePath())));
        }
    }

//    public void setTex

    private void setSelectRect() {
        selectRect = new Rectangle(0.01, 0.01);
        selectRect.setFill(Color.LIGHTBLUE);
        selectRect.setStroke(Color.DEEPSKYBLUE);
        selectRect.setOpacity(0.15);
    }

    private void resetSelection() {
        setSelectRect();
        handleSelection();
    }

    private void setDragMove() {
        MapPane mapPane = this;
        final Delta dragDelta = new Delta();
        mapPane.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                dragDelta.x = mapPane.getLayoutX() - mouseEvent.getSceneX();
                dragDelta.y = mapPane.getLayoutY() - mouseEvent.getSceneY();
                if (mouseEvent.isPrimaryButtonDown()) {
                    mapPane.setCursor(Cursor.MOVE);
                }
                if (mouseEvent.isControlDown() && mouseEvent.isPrimaryButtonDown()) {
                    mapPane.getChildren().add(selectRect);
                    selectRect.setX(mouseEvent.getX());
                    selectRect.setY(mouseEvent.getY());
                    mapPane.setCursor(Cursor.NONE);
                }
            }
        });
        mapPane.setOnMouseReleased(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if (mouseEvent.isControlDown()) {
                    mapPane.getChildren().remove(selectRect);
                    handleSelection();
                }
                mapPane.setCursor(Cursor.HAND);
            }
        });
        mapPane.setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if (mouseEvent.isControlDown() && mouseEvent.isPrimaryButtonDown()) {
                    selectRect.setWidth((mouseEvent.getSceneX() + dragDelta.x - mapPane.getLayoutX()) / scale);
                    selectRect.setHeight((mouseEvent.getSceneY() + dragDelta.y - mapPane.getLayoutY()) / scale);
                }
                else if (mouseEvent.isPrimaryButtonDown()) {
                    mapPane.setLayoutX(mouseEvent.getSceneX() + dragDelta.x);
                    mapPane.setLayoutY(mouseEvent.getSceneY() + dragDelta.y);
                }
            }
        });
        mapPane.setOnMouseEntered(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                mapPane.setCursor(Cursor.HAND);
            }
        });

        mapPane.setOnScroll(new EventHandler<ScrollEvent>() {
            @Override
            public void handle(ScrollEvent scrollEvent) {
                ScaleTransition scaleTransition = new ScaleTransition(Duration.millis(100));
                scaleTransition.setNode(mapPane);
                if (scrollEvent.getDeltaY() < 0)
                    scale -= 0.05;
                else
                    scale += 0.05;
                if (scale <= MIN_SCALE) {
                    scale = MIN_SCALE;
                } else if (scale >= MAX_SCALE) {
                    scale = MAX_SCALE;
                }

                scaleTransition.setToX(scale);
                scaleTransition.setToY(scale);
                scaleTransition.play();
            }
        });
    }

    private void showDetailsText(MapTile tile) {
        StringBuilder output = new StringBuilder(tile.getBlock().showDetails());
        if (tile.isSick()) {
            output.append("\nSick :(");
        }
        detailsText.setTextAlignment(TextAlignment.CENTER);
        detailsText.setText(output.toString());
        detailsText.setLayoutX(tile.getLayoutX() + 25);
        detailsText.setLayoutY(tile.getLayoutY() - 18);

        this.getChildren().add(detailsText);
    }

    private void showSelectedBlocksDetails() {
        StringBuilder output = new StringBuilder();

        if (selected.isEmpty())
            return;

        detailsText.setTextAlignment(TextAlignment.CENTER);

        for (MapTile tile : selected) {
            output.append( tile.getBlock().showDetails());
            if (tile.isSick()) {
                output.append("\nSick :(");
            }
            output.append("\n------------\n");
        }
        detailsText.setText(output.toString());
        detailsText.setLayoutX(selected.get(0).getLayoutX() + 25);
        detailsText.setLayoutY(selected.get(0).getLayoutY() - 18);
        this.getChildren().add(detailsText);
    }

    private void hideDetailsText(MapTile tile) {
        this.getChildren().remove(detailsText);
    }

    private MapTile tileInit(int x, int y) {
        MapTile tile = new MapTile();

        tile.setBlock(map.getBlockByXY(x, y));
        tile.setLayoutX(MapTile.TILE_WIDTH / 2 * (x - y));
        tile.setLayoutY(MapTile.TILE_WIDTH / 4 * (x + y));
        tile.setFill(new ImagePattern(new Image(tile.getBlock().getTexture().getImagePath())));
        map.getBlockByXY(x, y).setTile(tile);

        return tile;
    }

    private void setTileHover(MapTile tile) {
        tile.hoverProperty().addListener((ChangeListener<Boolean>) (observable, oldValue, newValue) -> {
            if (newValue) {
                FadeTransition ft = new FadeTransition(Duration.millis(100), tile);
                ft.setFromValue(1);
                ft.setFromValue(0.8);
                ft.play();
            } else {
                FadeTransition ft = new FadeTransition(Duration.millis(100), tile);
                ft.setFromValue(0.8);
                ft.setFromValue(1);
                ft.play();
            }
        });
    }

    private void setTileDetailShow(MapTile tile) {
        tile.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                {
                    if (mouseEvent.isControlDown() && mouseEvent.isSecondaryButtonDown()) {
                        showSelectedBlocksDetails();
                    } else if (mouseEvent.isSecondaryButtonDown()) {
                        resetSelection();
                        showDetailsText(tile);
                    } else if (mouseEvent.isPrimaryButtonDown() ) {
                        selected = new ArrayList<>();
                        selected.add(tile);
                    }
                }
            }
        });

        tile.setOnMouseReleased(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                hideDetailsText(tile);
            }
        });
    }

    private void handleSelection() {
        for (Node node : allTiles.getChildren()) {
            MapTile tile = (MapTile) node;
            if (selectRect.getBoundsInParent().intersects(tile.getBoundsInParent())) {
                select(tile);
                if (!this.selected.contains(tile))
                    this.selected.add(tile);
            } else {
                tile.setStroke(Color.TRANSPARENT);
                this.selected.remove(tile);
            }
        }
    }

    private void select(MapTile tile) {
        tile.setStroke(Color.WHITE);
        tile.setStrokeWidth(0.6);
    }

    public Group getAllTiles() {
        return allTiles;
    }

    public ArrayList<MapTile> getSelectedTiles() {
        return selected;
    }

    public double getScale() {
        return scale;
    }

    public void reset() {
        scale = 1;
        ScaleTransition scaleTransition = new ScaleTransition(Duration.millis(100));
        scaleTransition.setNode(this);
        scaleTransition.setToX(scale);
        scaleTransition.setToY(scale);
        scaleTransition.play();
        this.setLayoutX(500);
        this.setLayoutY(-500);
    }
}


class Delta {
    double x, y;
}
