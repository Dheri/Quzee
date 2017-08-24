/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

/**
 *
 * Created on : 28-Jun-2017, 4:13:51 PM
 *
 * @author Parteek Dheri
 */
import javafx.stage.*;
import javafx.scene.*;
import javafx.scene.layout.*;
import javafx.scene.control.*;
import javafx.geometry.*;
import javafx.scene.image.Image;

public class AlertBox {

    public static void display(String title, String message) {
        Stage window = new Stage();
        StackPane sp = new StackPane();

        //Block events to other windows
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle(title);
        window.setMinWidth(250);

        Label label = new Label();
        label.setText(message);
        Button closeButton = new Button("Close this window");
        closeButton.setOnAction(e -> window.close());

        GridPane layout = new GridPane();

        layout.add(label, 0, 0);
        layout.add(closeButton, 0, 1);

        layout.setAlignment(Pos.BASELINE_CENTER);
        layout.setPadding(new Insets(10, 10, 40, 10));
        layout.setVgap(10);
        layout.setHgap(12);

        layout.setStyle("-fx-font-size:1.3em;"
                + "-fx-background-color: rgba(95, 158, 160, 0.4);"
        );

        Image bgIm = new Image("images/swirl_pattern.png");
        BackgroundImage bgi = new BackgroundImage(bgIm, null, null, null, null);
        Background bg = new Background(bgi);
        sp.setBackground(bg);

        sp.getChildren().add(layout);
        //Display window and wait for it to be closed before returning
        Scene scene = new Scene(sp);
        window.setScene(scene);
        window.showAndWait();
    }
}
