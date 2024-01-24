package org.example.controller;


import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import org.example.server.Server;

import java.io.IOException;

public class ServerController {

    private Server server;

    public  void run() {
        new Thread(() -> {
            try {
                server = Server.getInstance();
                server.makeSocket();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();

    }

}
