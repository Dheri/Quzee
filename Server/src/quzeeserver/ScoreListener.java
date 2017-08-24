/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package quzeeserver;

import java.io.*;
import java.net.*;
import java.util.Arrays;
import utils.Player;

/**
 *
 * Created on : 20-Jul-2017, 11:31:59 PM
 *
 * @author Parteek Dheri
 */
public class ScoreListener extends Thread {

    String address;
    ServerSocket myServerSocket;
    Socket socket = null;
    int port;
    boolean running = false;

    public ScoreListener(int port) {
        this.port = port;

    }

    public void run() {
        running = true;
        System.out.println("ScoreListener run method started");
        try {
            myServerSocket = new ServerSocket(port);
            while (!Thread.currentThread().isInterrupted()) {
//                System.out.println(" currentThread().isInterrupted() >>  " + (Thread.currentThread().isInterrupted()));
                try {
                    socket = myServerSocket.accept();
                } catch (IOException ioe) {
                    Thread.currentThread().interrupt();
                    System.out.println("I/O error: " + ioe);
                }
                // new 

                try {

                    ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
                    Player p = (Player) ois.readObject();
                    Servering.addClient(p);
                    System.out.println(p + "from Score Listener");

                } catch (IOException e) {
                    System.out.println("Some IOException" + e);
                } catch (Exception e) {
                    System.out.println("Some error" + e);
                }

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
                System.out.println(Arrays.toString(ex.getStackTrace()));
            }
            running = false;
        }
    }
}
