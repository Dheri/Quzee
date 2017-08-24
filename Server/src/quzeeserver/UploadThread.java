/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package quzeeserver;

import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import utils.Question;

/**
 *
 * Created on : 29-Jun-2017, 5:23:08 AM
 *
 * @author Parteek Dheri
 */
public class UploadThread extends Thread implements Runnable {

    String address;
    ServerSocket myServerSocket;
    Socket socket = null;
    int port = 1201;
    boolean running = false;
    ArrayList<Question> questionPaper;

    public UploadThread(int port, ArrayList<Question> questionPaper) {
        this.port = port;
        this.questionPaper = questionPaper;
    }

    public void run() {
        running = true;
        System.out.println("UploadThread run method started");
        try {
            myServerSocket = new ServerSocket(port);
            while (!Thread.currentThread().isInterrupted()) {
//                System.out.println(" currentThread().isInterrupted() >>  " + (Thread.currentThread().isInterrupted()));
                try {
                    socket = myServerSocket.accept();
                    
                    address = socket.getRemoteSocketAddress() +""; //to get address of socket
                    
                    System.out.println(address);

                    if (Thread.currentThread().isInterrupted()) {
                    }
                } catch (IOException ioe) {
                    Thread.currentThread().interrupt();
                    System.out.println("I/O error: " + ioe);
                }
                // new thread for a client
                new ServeQuesThread(socket, questionPaper).start();

            }
        } catch (IOException ioe) {
            System.out.println("I/O error: " + ioe);
            ioe.printStackTrace();
        }
    }

    public void closeSocket() {
        if (running) {

            try {
                myServerSocket.close();
            } catch (IOException ex) {
                Logger.getLogger(UploadThread.class.getName()).log(Level.SEVERE, null, ex);
            }
            running = false;
        }
    }
}
