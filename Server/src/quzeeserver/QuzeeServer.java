/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package quzeeserver;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import javafx.application.Application;
import javafx.geometry.*;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.*;
import utils.*;

/**
 *
 * Created on : 27-Jun-2017, 3:06:17 AM
 *
 * @author Parteek Dheri
 */
public class QuzeeServer extends Application {

    File file;
    ServerSocket myServerSocket;
    Socket socket = null;
    int port = 54685;
    UploadThread uptd;
    ArrayList<Question> questionPaper;
    Stage window;
    boolean connected;
    Button stopServer;
    Button startServer;
    Button uploadQues;
    TextField portTF = null;

    @Override
    public void start(Stage primaryStage) {

        window = primaryStage;
        BorderPane root = new BorderPane();

        // for bottom
        GridPane gpBTM = new GridPane();
        gpBTM.setAlignment(Pos.CENTER);
        gpBTM.setPadding(new Insets(10, 10, 40, 10));
        gpBTM.setVgap(10);
        gpBTM.setHgap(25);

        // for uploading file.
        uploadQues = new Button("Select Ques Paper");
        uploadQues.setOnAction(e -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Select the Questions file");
            this.file = fileChooser.showOpenDialog(primaryStage);

            if (file != null) {

                ArrayList<Question> temp = getQuestionPaper(file);
                if (temp.isEmpty() || !(temp.get(0) instanceof Question)) {
                    AlertBox.display("Error", "Not a valid Question file");

                } else {
                    this.questionPaper = temp;

                }
            }
        });
        startServer = new Button("Start Server");
        stopServer = new Button("Stop Server");

        portTF = new TextField("54685");

        Label portL = new Label("Port :");
        gpBTM.add(portL, 0, 0);
        gpBTM.add(portTF, 1, 0);
        gpBTM.add(uploadQues, 1, 1);
        gpBTM.add(startServer, 1, 2);
//        gpBTM.add(stopServer, 0, 1);
        gpBTM.setAlignment(Pos.CENTER);

        uploadQues.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        startServer.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        GridPane.setFillWidth(uploadQues, true);
        GridPane.setFillHeight(uploadQues, true);
        GridPane.setFillWidth(startServer, true);
        GridPane.setFillHeight(startServer, true);

        startServer.setOnAction(e -> {
            boolean valid = false;
            if (file == null) {

                AlertBox.display("Error", "Please Select a Question file first");
                return;
            }
            if (!validate()) {
                return;
            }

            new Servering().startUpload(port, questionPaper);

            //**************************
//            if (uptd == null) {
//                uptd = new UploadThread(port, questionPaper);
//                uptd.start();
//                connected = true;
//                AlertBox.display("Congratulations", "Server started successfully");
//            } else {
//                AlertBox.display("Error", "Server is already running \ncan not start a new server \nSorry!");
//            }
//            
        });

//        
//        stopServer.setOnAction(e -> {
//            if (uptd != null) {
//                uptd.closeSocket();
//                uptd.interrupt();
//                uptd = null;
//                connected = false;
//            } else {
//                AlertBox.display("Error", " NO Server is running");
//            }
//        });
        root.setBottom(gpBTM);

        //********************************************************************
        //********************************************************************
        Scene scene = new Scene(root, 450, 400);

        window.setTitle("Quzee Server");
        window.setScene(scene);
        window.show();

        // to safely close the sockets that were open
//        window.setOnCloseRequest(e -> {
//            closeWindow();
//        });
    }

    public ArrayList<Question> getQuestionPaper(File file) {
        ArrayList<Question> questionPaperL = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {

            String sCurrentLine;

            while ((sCurrentLine = br.readLine()) != null) {
                String[] raw = sCurrentLine.split(":");
                if (raw.length < 5 || raw[0].charAt(0) == '#') {
                    continue;
                }

                try {
                    Question q = new Question(raw[0], raw[1], raw[2], raw[3], raw[4], raw[5]);
                    questionPaperL.add(q);
                } catch (ArrayIndexOutOfBoundsException e) {
                    throw new BadFileExeption();
                }

            }
        } catch (BadFileExeption bfe) {
            AlertBox.display("Error", "Not a valid Question file");
        } catch (Exception e) {
            System.out.println(e);
            AlertBox.display("Error", "somthing is bad with given Question file");
        }
        return questionPaperL;
    }
//
//    public void closeWindow() {
//        if (connected) {
//            stopServer.fire();
//        }
//        window.close();
//    }

    public boolean validate() {
        boolean isValid = false;

        if ((portTF.getText() != null && !portTF.getText().isEmpty())) {
            System.out.println(portTF.getText());
            try {
                int tempPort;
                tempPort = Integer.parseInt(portTF.getText());

                if (!(tempPort > 65535 || tempPort < 0)) {
                    this.port = tempPort;
                    isValid = true;
                } else {
                    return false;
                }

            } catch (NumberFormatException nfex) {
                AlertBox.display("Error", "Please Enter a number between 0 and 65535 in the port feild");
            }
        } else {
            AlertBox.display("Error", "Please Enter Port number, default value is 54685");
            return false;
        }
        return isValid;
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

}
