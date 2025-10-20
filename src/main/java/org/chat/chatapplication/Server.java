package org.chat.chatapplication;

//import javafx.application.Application;
//import javafx.fxml.FXMLLoader;
//import javafx.scene.Scene;
//import javafx.stage.Stage;
//
//import java.io.IOException;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    ServerSocket server;
    Socket socket;


    BufferedReader br;
    PrintWriter pw;

    boolean stopTakingInputBcuzTheOtherSideHasTypedExit=false;

    public Server() {
        try {
            server = new ServerSocket(7777);
            System.out.println("Server is Ready to Accept Conection");
            System.out.println("Waiting..");
            socket = server.accept();
            System.out.println("Accepted");

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
                    msg = br.readLine();
                    if (msg.equals("exit")) {
                        System.out.println("Client Terminated the Chat");
                        socket.close();
                        stopTakingInputBcuzTheOtherSideHasTypedExit=true;
                        break;

                    }

                    System.out.println("Client: " + msg);
                }
            } catch (Exception e) {
//                throw new RuntimeException(e);
                System.out.println("Connection Closed Because you typed exit");
            }
        };


        new Thread(r1).start();
    }

                                   //writing mehtod

    private void startWriting() {

        Runnable r2 = () -> {
            System.out.println("Writing Started");
            try {
                while (true) {


                    BufferedReader br1 = new BufferedReader(new InputStreamReader(System.in));


                    String content = null;
                    if (stopTakingInputBcuzTheOtherSideHasTypedExit==false) {
                        content = br1.readLine();
                    }else{
                        System.out.println("Connection Closed Because the other side has typed exit");
                        break;
                    }

                    pw.println(content);
                    pw.flush();


                    if(content.equals("exit")){

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
        new Server();
    }


}