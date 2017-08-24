/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package quzeeserver;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import static javafx.geometry.Orientation.VERTICAL;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import utils.*;

/**
 *
 * Created on : 9-Jul-2017, 6:27:41 PM
 *
 * @author Parteek Dheri
 */
public class Servering { //Servering.addClient

    static ArrayList<Player> clients;
    UploadThread uptd;
    ScoreListener sl;
    BorderPane root = new BorderPane();
    Button SurveyResults;
    Button close;
    InetAddress ip;
    GridPane top = new GridPane();

    public static void addNewClient(Player incoming) {
        for (int i = 0; i < clients.size(); i++) {
            if (clients.get(i).ipAddress.equals(incoming.ipAddress)) {
                clients.set(i, incoming);
                return;
            }
        }
        clients.add(incoming);
    }

    /**
     * used while submitting score to update client list
     *
     * @param incoming the player who is subbmiting score
     */
    public static void addClient(Player incoming) {
//        System.out.println("add client activated");
        boolean added = false;
        for (int i = 0; i < clients.size(); i++) {
            if (clients.get(i).ipAddress.equals(incoming.ipAddress)) {
                if (!added) {
                    clients.set(i, incoming);
                    added = true;
                } else {
                    clients.remove(i);
                }
            }
        }
    }

    Label serverAddress = new Label();
    Socket test = new Socket();

    //starting a new thread
    public void startUpload(int port, ArrayList<Question> questionPaper) {
        clients = new ArrayList();

        if (uptd == null) {
            uptd = new UploadThread(port, questionPaper);
            sl = new ScoreListener(port + 1);// self explanatory

            uptd.start();
            sl.start();
//                AlertBox.display("Congratulations", "Server started successfully");
        } else {
            AlertBox.display("Error", "Server is already running, \ncan not start a new server \nSorry!");
        }

        try {
            ip = InetAddress.getLocalHost();
            serverAddress.setText("Connect to -> " + ip);
        } catch (UnknownHostException ex) {
            System.out.println(ex);
        }
        root.setTop(serverAddress);

//        FlowPane clientsFP = new FlowPane(Orientation.VERTICAL, 5, 5);
        VBox clientsFP = new VBox();

        ScrollPane sp = new ScrollPane();
        sp.setContent(clientsFP);

        root.setCenter(sp);
        //*********************************************** 
        Button refreshB = new Button("Refresh");
        refreshB.setOnAction(e -> {
            // copies all clients to a collection colec and  add colec to FlowPane 

            ArrayList<GridPane> clientsAL = new ArrayList();
            for (Player pl : clients) {
                System.out.println(pl);
                clientsAL.add(pl.toPane()); // converts player to presentable pane 
            }
            Collection colec = clientsAL;
            try {
                clientsFP.getChildren().clear();//clear screen
                clientsFP.getChildren().addAll(colec);
            } catch (Exception exp) {
                System.out.println(e + " \n" + exp);
            }
        });
        GridPane gpBTM = new GridPane();
        gpBTM.add(refreshB, 0, 0);
        gpBTM.setAlignment(Pos.CENTER);
        gpBTM.setPadding(new Insets(12, 5, 5, 5));

        root.setBottom(gpBTM);

        //****************** below is setting up of windows
        Stage window = new Stage();

        clientsFP.prefWidthProperty().bind(window.widthProperty().multiply(0.95));//set whe width of v box
        clientsFP.setPadding(new Insets(1, 5, 2, 4));

        // set priotity to this window.
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle("The Progress");

        // to stop and clean all mess created by this window.
        window.setOnCloseRequest(e -> {
            onClose();
        });

        String css = Servering.class
                .getResource("Servering.css")
                .toExternalForm();

        Scene scene = new Scene(root, 700, 500);
        scene.getStylesheets().add(css);
        window.setScene(scene);
        window.showAndWait();// wait till window is closed to return to caller
    }

    public void onClose() {
        uptd.closeSocket();
        uptd.interrupt();
        uptd = null;
        sl.closeSocket();
        sl.interrupt();
        sl = null;
    }

}
