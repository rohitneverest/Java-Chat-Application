package org.chat.chatapplication;

//import javafx.fxml.FXML;
//import javafx.scene.control.Label;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Client {


    ServerSocket server;
    Socket socket;


    BufferedReader br;
    PrintWriter pw;

    boolean iAmAReaderAndIwantToInformWriterThatStopTakingInputBcuzTheOtherSideHasTypedExit=false;

    public Client() {
        try {
            System.out.println("Sending request to Server");
            socket = new Socket("127.0.0.1", 7777);
            System.out.println("Connection done");


            br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            pw = new PrintWriter(socket.getOutputStream());


            startReading();
            startWriting();


        } catch (Exception e) {
            e.printStackTrace();
        }

    }

                             //Reading method

    private void startReading() {

        Runnable r1 = () -> {
            System.out.println("Reading Started");
            try {
                while (true) {

                    String msg = null;

                    msg = br.readLine();//here exception will be there when writer itself types "exit" and socket connection is closed
                    if (msg.equals("exit")) {
                        System.out.println("Server terminated the chat");
                        socket.close();
                        iAmAReaderAndIwantToInformWriterThatStopTakingInputBcuzTheOtherSideHasTypedExit=true;
                        break;

                    }

                    System.out.println("Server: " + msg);
                }
            } catch (Exception e) {
//                throw new RuntimeException(e);
                System.out.println("Connection Closed because you typed exit");
            }
        };


        new Thread(r1).start();
    }

                                 //writing method

    private void startWriting() {

        Runnable r2 = () -> {
            System.out.println("Writing Started");
            try {
                while (true) {

                    BufferedReader br1 = new BufferedReader(new InputStreamReader(System.in));

                    String content = null;
                    if (iAmAReaderAndIwantToInformWriterThatStopTakingInputBcuzTheOtherSideHasTypedExit==false) {
                        content = br1.readLine();
                    }else {
                        System.out.println("Connection Closed because the other side has typed exit");
                        break;
                    }

                    pw.println(content);
                    pw.flush();


                    if (content.equals("exit")) {

                        socket.close();
                        break;


                    }




                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        };


        new Thread(r2).start();
    }

    public static void main(String[] args) {
        new Client();
    }


}