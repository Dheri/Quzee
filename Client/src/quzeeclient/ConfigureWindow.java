/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package quzeeclient;

import utils.*;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.*;
import utils.AlertBox;

/**
 *
 * Created on : 25-Jun-2017, 3:59:17 PM
 *
 * @author Parteek Dheri
 */
public class ConfigureWindow {

    static boolean connected = false;
    String nickName;
    static String IPaddress;
    static int port;
    static Player you;
    boolean validated;
    ArrayList<Question> quesPaper;

    TextField ipAddressTF = null;
    TextField portTF = null;
    TextField yourNickNameTF = null;
    Label connectedLB;

    public ArrayList<Question> display() {
        Stage window = new Stage();

        // set priotity to this window.
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle("Configure Settings");

        BorderPane bp = new BorderPane();

        // for heading
        StackPane spT = new StackPane();
        Label message = new Label("Welcome");

        message.getStyleClass().add("instructions");

        spT.getChildren().add(message);
        bp.setTop(spT);

        // for center area
        GridPane gp = new GridPane();

        ipAddressTF = new TextField("127.0.0.1");
        portTF = new TextField("54685");
        yourNickNameTF = new TextField();

        gp.setVgap(10);
        gp.setHgap(20);
        Label ipAddressLB = new Label("Server's IP Address :");
        Label portLB = new Label("Server's port :");
        Label yourNickNameLB = new Label("Your NickName:");

        gp.add(yourNickNameLB, 0, 0);
        gp.add(yourNickNameTF, 1, 0);
        gp.add(ipAddressLB, 0, 1);
        gp.add(ipAddressTF, 1, 1);
        gp.add(portLB, 0, 2);
        gp.add(portTF, 1, 2);

        bp.setCenter(gp);

        // for right
        connectedLB = new Label();
        connectedLB.setText("Not Connected");
        GridPane gpRT = new GridPane();
        gpRT.setAlignment(Pos.CENTER);
        gpRT.setPadding(new Insets(10, 10, 100, 10));
        gpRT.setVgap(10);
        gpRT.setHgap(12);
        gpRT.add(connectedLB, 0, 0);
        bp.setRight(gpRT);

        // for bottom
        GridPane gpBTM = new GridPane();
        gpBTM.setAlignment(Pos.CENTER);
        gpBTM.setPadding(new Insets(10, 10, 100, 10));
        gpBTM.setVgap(10);
        gpBTM.setHgap(12);

        //***************************************************************************************
        Button connect = new Button("Connect");
        connect.setOnAction(e -> {

            validated = validate();
            if (validated) {
                establishConnection();
            }
            // TODO Connect with Server
            if (quesPaper != null) {
                connectedLB.setText("Connected");

                AlertBox.display("Congratulations", "You can now Start test");
                if (!connected) {
                    connected = true;
                }
            }
             window.close();

        });
        gpBTM.add(connect, 0, 0);

        //**************************************************************************
        Button close = new Button("Close");

        close.setOnAction(e -> {
            window.close();
        });
        gpBTM.add(close, 1, 0);

        bp.setBottom(gpBTM);

        //Setting up the stage
        String css = ConfigureWindow.class
                .getResource("ConfigureWindowStyle.css")
                .toExternalForm();

        Scene scene = new Scene(bp, 450, 450);
        scene.getStylesheets().add(css);
        window.setScene(scene);
        window.showAndWait();// wait till window is closed to return to caller

        // if connection successfull;
        System.out.println(connected);
        return quesPaper;

    }

    public boolean validate() {
        if ((yourNickNameTF.getText() != null && !yourNickNameTF.getText().isEmpty())) {
            this.nickName = yourNickNameTF.getText();
        } else {
            AlertBox.display("Error", "Please Enter your Nick name");
            return false;
        }
        // name valid ^^ ^^ ^^ ^^

        if ((ipAddressTF.getText() != null && !ipAddressTF.getText().isEmpty())) {
            String temp = ipAddressTF.getText();
            String[] tempAr = temp.split("\\.");
            if (tempAr.length == 4) {
                for (int i = 0; i < 4; i++) {
                    try {
                        int quad = Integer.parseInt(tempAr[i]);
                        if (quad < 0 || quad > 255) {
                            throw new NumberFormatException();
                        }
                    } catch (NumberFormatException ex) {
                        AlertBox.display("Error", "Please enter IP address");
                        return false;
                    }
                }
            } else {
                AlertBox.display("Error", "Please enter IP address");
                return false;
            }
        } else {
            AlertBox.display("Error", "Please enter IP address");
            return false;
        }
        this.IPaddress = ipAddressTF.getText();
        System.out.println("IPaddress > " + IPaddress);

        // ip valid ^^^^  ^^^^ ^^^  ^^^^^ ^^^^  ^^^^  ^^^^
        // port valid vvvv  vvvv vvvv  vvvv vvvv  vvvv  vvvv
        if ((portTF.getText() != null && !portTF.getText().isEmpty())) {

            System.out.println(nickName);
            try {
                int tempPort;
                tempPort = Integer.parseInt(portTF.getText());

                if (tempPort > 65535 || tempPort < 0) {
                    throw new NumberFormatException();
                }

                this.port = tempPort;

            } catch (NumberFormatException nfex) {
                AlertBox.display("Error", "Please Enter integers in port feild");
            }
        } else {
            AlertBox.display("Error", "Please Enter Port number, default is 54685");
            return false;
        }

        return true;
    }

    private void establishConnection() {
        try {
            Socket socket = new Socket(IPaddress, port);
            ArrayList<Question> q;

            // info about you that will be sent to the server
            you = new Player(nickName);
           
            you.setIpAddress(InetAddress.getLocalHost() + "");

            //read quespaper from socket
            try (ObjectInputStream ois = new ObjectInputStream(socket.getInputStream())) {
                q = (ArrayList<Question>) ois.readObject();
                quesPaper = q;
                //write player info oon socket
                ObjectOutputStream sktOutput = new ObjectOutputStream(socket.getOutputStream());
                sktOutput.writeObject(you);

            }
        } catch (IOException e) {
            AlertBox.display("Error", "something bad happened while connnecting to server");
            System.out.println("IOException" + e);
        } catch (ClassNotFoundException ex) {
            AlertBox.display("Error", "something bad happened while connnecting to server" + ex);
        }
    }
}
