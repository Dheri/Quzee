/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package quzeeclient;

import java.util.ArrayList;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import utils.AlertBox;
import utils.Question;

/**
 *
 * Created on : 27-Jun-2017, 3:05:19 AM
 *
 * @author Parteek Dheri
 */
public class QuzeeClient extends Application {

    boolean configured = false;
    private ArrayList<Question> quesPaper;
    GamePlay gp;

    public void setQuesPaper(ArrayList<Question> quesPaper) {
        this.quesPaper = quesPaper;
    }

    public ArrayList<Question> getQuesPaper() {

        return this.quesPaper;
    }

    @Override
    public void start(Stage primaryStage) {

        Stage window = primaryStage;

        //
        BorderPane root = new BorderPane();

        // for top
        StackPane sp = new StackPane();
        Label welcome = new Label("Welcome");
        sp.getChildren().add(welcome);
        root.setTop(sp);

        // for center
        StackPane spC = new StackPane();
//        Image img = new Image("file:image.jpg");
//        ImageView imgV = new ImageView(img);
        Label hi = new Label("You need to configure before you start the test.\n"
                + "After the connection is established, you are good to go.");
        spC.getChildren().add(hi);
        root.setCenter(spC);

        //   for bottom
        GridPane gpBTM = new GridPane();
        gpBTM.setAlignment(Pos.CENTER);
        gpBTM.setPadding(new Insets(10, 10, 40, 10));
        gpBTM.setVgap(10);
        gpBTM.setHgap(12);

        Button configure = new Button("Configure");
        configure.setOnAction(e -> {

            ConfigureWindow cw = new ConfigureWindow();
            setQuesPaper(cw.display());
        });

        gpBTM.add(configure, 0, 0);

        Button startGame = new Button("Start Game");

        startGame.setOnAction(e -> {
            if (quesPaper != null) {
                gp = new GamePlay(quesPaper);
                gp.setGameplay();
            }else{
                AlertBox.display("Error", "You cant start without configuring");
            }
        });

        gpBTM.add(startGame, 0, 1);
        configure.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        startGame.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        GridPane.setFillWidth(configure, true);
        GridPane.setFillHeight(configure, true);
        GridPane.setFillWidth(startGame, true);
        GridPane.setFillHeight(startGame, true);

        root.setBottom(gpBTM);

        // settting up the stage
        String css = QuzeeClient.class
                .getResource("QuzeeClientStyle.css")
                .toExternalForm();

        Scene scene = new Scene(root, 600, 600);
        scene.getStylesheets().add(css);

        window.setTitle("Quzee Client");
        window.setScene(scene);
        window.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

}
