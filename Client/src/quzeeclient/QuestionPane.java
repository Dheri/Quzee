/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package quzeeclient;

import utils.Question;
import java.util.ArrayList;
import java.util.Arrays;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;

/**
 *
 * Created on : 27-Jun-2017, 6:01:56 PM
 *
 * @author Parteek Dheri
 */
public final class QuestionPane extends GridPane {

    private final ArrayList<Question> quesList;
    int correctAnswers = 0;
    int maxScore;

    /**
     *
     * @param quesList All questions to be added in pane
     */
    public QuestionPane(ArrayList<Question> quesList) {
        this.quesList = quesList;
        setQPane();
    }

    /**
     *
     * @return number of questions in the pane
     */
    public int getNumQues() {
        return quesList.size();
    }

    public int checkAnswers() {
        GridPane qGrid;
        GridPane options;
        correctAnswers = 0;
        for (int i = 0; i < quesList.size(); i++) {
            boolean[] recivedAnswers = new boolean[4];
            System.out.print(1 + i + " >--> ");
            Node response = (Node) this.getChildren().get(i);
            qGrid = (GridPane) response;
            options = (GridPane) qGrid.getChildren().get(2); //this contains 4 options checkboxes

            recivedAnswers[0] = ((CheckBox) (options.getChildren().get(0))).isSelected();
            recivedAnswers[1] = ((CheckBox) (options.getChildren().get(1))).isSelected();
            recivedAnswers[2] = ((CheckBox) (options.getChildren().get(2))).isSelected();
            recivedAnswers[3] = ((CheckBox) (options.getChildren().get(3))).isSelected();

            for (int j = 0; j < options.getChildren().size(); j++) { //checkbox.setStyle("-fx-opacity: 1");
                ((CheckBox) (options.getChildren().get(j))).setDisable(true);
                ((CheckBox) (options.getChildren().get(j))).setStyle(
                         "-fx-opacity: 0.7;"
                );
                if (quesList.get(i).getRightAnswer()[j]) {
//                    ((CheckBox) (options.getChildren().get(j))).setDisable(true);
                    ((CheckBox) (options.getChildren().get(j))).setStyle(
                            "-fx-border-color: green; "
                            + "-fx-border-radius: 5;"
                            + "-fx-border-width: 2;"
                            + "-fx-border-style: dotted;"
                            + "-fx-opacity: 1;"
                            +"-fx-font-size: 1.05em"
                            +"-fx-font-size: 1.05em"
                    );
                }
            }

            if (Arrays.equals(recivedAnswers, quesList.get(i).getRightAnswer())) {
                correctAnswers++;
                System.out.println("Right" + i + Arrays.toString(recivedAnswers));
//                GridPane gp = options.getChildren().get(2).add(new Label("correct "),0,0); // cast to gp
//               GridPane gp;
//            qGrid.add(new Label("correct "), 4, 4);
                qGrid.add(new ImageView(new Image("images/tick.png")), 2, 1);

            } else {
                qGrid.add(new ImageView(new Image("images/cancel.png")), 2, 1);
            }

        }
        maxScore = quesList.size();
        GamePlay.score.setText("Your Score is " + correctAnswers + " out of " + quesList.size());
        return correctAnswers;
    }

    /**
     * Makes formatted Pane which contain all questions.
     */
    public void setQPane() {

        for (int i = 0; i < quesList.size(); i++) {
            Question ques = quesList.get(i);
            GridPane root = new GridPane();

            Label qLBL = new Label(ques.getQues()[0]);
            Label num = new Label(i + 1 + " -> ");
            root.add(num, 0, 0);

            root.add(qLBL, 1, 0);

            GridPane gpBTM = new GridPane();
            CheckBox opt1 = new CheckBox(ques.getQues()[1]);
            opt1.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
            gpBTM.add(opt1, 0, 0);
            CheckBox opt2 = new CheckBox(ques.getQues()[2]);
            opt2.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
            gpBTM.add(opt2, 1, 0);
            CheckBox opt3 = new CheckBox(ques.getQues()[3]);
            opt3.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
            gpBTM.add(opt3, 0, 1);
            CheckBox opt4 = new CheckBox(ques.getQues()[4]);
            opt4.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
            gpBTM.add(opt4, 1, 1);

            root.add(gpBTM, 1, 1);
            gpBTM.setAlignment(Pos.TOP_LEFT);
            gpBTM.setPadding(new Insets(2, 20, 10, 4));
            gpBTM.setVgap(8);
            gpBTM.setHgap(16);

            this.add(root, 0, i);

            if (i % 2 == 0) {
                root.setStyle("-fx-background-color:#99d5cf;"
                //                        + "-fx-font-size:1.4em;"
                );
            } else {
                root.setStyle("-fx-background-color:#fffeb3;"
                //                        + "-fx-font-size:1.4em;"
                );

            }

        }
    }

}
