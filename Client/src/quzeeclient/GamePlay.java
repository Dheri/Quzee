/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package quzeeclient;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import static quzeeclient.ConfigureWindow.you;
import utils.AlertBox;
import utils.Player;
import utils.Question;

/**
 *
 * Created on : 27-Jun-2017, 4:08:31 PM
 *
 * @author Parteek Dheri
 */
public class GamePlay {

    /**
     *
     */
    ArrayList<Question> quesPaper;

    static Label score;
    static int correctAnswers;
    private boolean canSubmit = true;

    /**
     *
     * @param quesPaper
     */
    public GamePlay(ArrayList<Question> quesPaper) {
        this.quesPaper = quesPaper;
    }

    public void setGameplay() {
        BorderPane root = new BorderPane();

        root.setCenter(setQuestionPaper(this.quesPaper));

        GridPane gpBTM = new GridPane();
        Button submitBTN = new Button("Submit Answers");
        submitBTN.setOnAction(e -> {
            onSubmit(root);
        });

        gpBTM.setAlignment(Pos.CENTER);
        gpBTM.setPadding(new Insets(10, 10, 7, 10));
        gpBTM.add(submitBTN, 1, 0);

        score = new Label("Your Score is : ");
//        score.setText("");
        gpBTM.add(score, 1, 2);
        root.setBottom(gpBTM);
        root.setStyle("-fx-font-size:1.35em");
        Stage window = new Stage();

        // set priotity to this window.
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle("The Quiz");

        Scene scene = new Scene(root, 700, 600);
        window.setScene(scene);
        window.showAndWait();// wait till window is closed to return to caller

    }

    public void onSubmit(BorderPane root) {
        if (canSubmit) {
            System.out.println(root.getChildren());
            try {
                ScrollPane spN = (ScrollPane) root.getChildren().get(0);// this caused error once
                QuestionPane qpvar = (QuestionPane) spN.getContent();
                correctAnswers = qpvar.checkAnswers();
                ConfigureWindow.you.setScore(correctAnswers);
                ConfigureWindow.you.setMaxScore(qpvar.maxScore);
                ConfigureWindow.you.submitted = true;
                uploadScore();
                canSubmit = false;// 
                System.out.println(correctAnswers);
            } catch (java.lang.IndexOutOfBoundsException ert) {
                System.out.println(" around Line 90 GamePlay" + ert);
            }

        }

    }

    private void uploadScore() {
        try {
            Socket socket = new Socket(ConfigureWindow.IPaddress, ConfigureWindow.port+1);

            // info about you that will be sent to the server

                //write player info oon socket
                ObjectOutputStream sktOutput = new ObjectOutputStream(socket.getOutputStream());
                sktOutput.writeObject(ConfigureWindow.you);

        } catch (IOException e) {
            AlertBox.display("Error", "something bad happened while Submitting Scores");
            System.out.println("IOException" + e);
        } catch (Exception ex) {
            AlertBox.display("Error", "something bad happened while Submitting Scores" + ex);
        }
    }

    public static ScrollPane setQuestionPaper(ArrayList<Question> quesPaper) {
        ScrollPane sp = new ScrollPane();
        QuestionPane quesPane = new QuestionPane(quesPaper);
        sp.setContent(quesPane);
        //*********************************************************************
        return sp;
    }
}
