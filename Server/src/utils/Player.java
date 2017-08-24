/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import java.io.Serializable;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.layout.GridPane;

/**
 *
 * Created on : 29-Jun-2017, 4:32:35 AM
 *
 * @author Parteek Dheri
 */
public class Player implements Serializable {

    private int id;
    private String name;
    public String ipAddress;
    public boolean submitted;
    private int score;
    private int maxScore;

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public int getScore() {
        return score;
    }

    public int getMaxScore() {
        return maxScore;
    }

    public Player(String name) {
        this.name = name;

    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public void setMaxScore(int maxScore) {
        this.maxScore = maxScore;
    }

    @Override
    public String toString() {
        return "Player{" + "id=" + id + ", name=" + name
                + ", ipAddress=" + ipAddress
                + ", submitted=" + submitted
                + ", score=" + score
                + ", maxScore=" + maxScore + '}';
    }

    public GridPane toPane() {
        GridPane gp = new GridPane();
//        Label l = new Label(this.toString());

       Label nameL = new Label("Name : " + name);
        String[] address = ipAddress.split("/");
        Label ipAddressL = new Label("IP Address : " + address[1]);
        Label scoreL;
        if (!submitted) {
            gp.setStyle("-fx-background-color:#ffabac;");
            scoreL = new Label("Test not Submitted ");
        } else {
            gp.setStyle("-fx-background-color:#82FF82;");
            scoreL = new Label("Score =  " + score + " out of " + maxScore);
        }
        Separator separator = new Separator();

        gp.add(nameL, 1, 1);
        gp.add(ipAddressL, 2, 2);
        gp.add(scoreL, 2, 3);
        gp.add(separator, 1, 4, 2, 1);

        gp.setVgap(12);

        return gp;
    }

}
