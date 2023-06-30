package view.shape.profile;

import javafx.geometry.Pos;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import model.Database;
import model.User;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class RankScroll {
    private ScrollPane scrollPane;
    private HBox hBox;

    public RankScroll() {

        hBox = new HBox();
        hBox.setAlignment(Pos.CENTER);
        ArrayList<User> users = Database.getUsers();
        Collections.sort(users, new Comparator<User>() {
            @Override
            public int compare(User o1, User o2) {
                if (o1.getScore() == o2.getScore()){
                    return o1.getHighScore() - o2.getHighScore();
                }
                return o1.getScore() - o2.getScore();
            }
        });

        for (User user : users) {
            hBox.getChildren().add(new RankNode(user).gethBox());
        }

        scrollPane = new ScrollPane();
        scrollPane.setPrefSize(400,600);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setPannable(true);
        scrollPane.setContent(hBox);
    }

    public ScrollPane getScrollPane() {
        return scrollPane;
    }


}
