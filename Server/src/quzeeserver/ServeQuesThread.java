package quzeeserver;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import utils.*;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * Created on : 29-Jun-2017, 3:25:49 AM
 *
 * @author Parteek Dheri
 */
public class ServeQuesThread extends Thread implements Runnable {

    Socket skt = null;
    ArrayList<Question> questionPaper;

    public ServeQuesThread(Socket skt, ArrayList<Question> questionPaper) {
        this.skt = skt;
        this.questionPaper = questionPaper;
    }

    @Override
    public void run() {

        try {
            ObjectOutputStream sktOutput = new ObjectOutputStream(skt.getOutputStream());
            sktOutput.writeObject(questionPaper);
            System.out.println(" questionPaper Written");
            
            
            ObjectInputStream ois = new ObjectInputStream(skt.getInputStream());
                Player p = (Player) ois.readObject();
                Servering.addNewClient(p);
//                Servering.clients.add(p);
//            System.out.println(p + "from client");
            
            
            
        } catch (IOException e) {
            System.out.println("Some IOException" +e);
        }catch (Exception e){
            System.out.println("Some error" +e);
        }
        
        System.out.println(" Secondary thread finished");
    }

}
