package org.example.controller;

import com.jfoenix.controls.JFXButton;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.Socket;

public class ClientController {

    @FXML
    private JFXButton emojiBtn;

    @FXML
    private AnchorPane pane;

    @FXML
    private ScrollPane scrollpain;

    @FXML
    private TextField txtMsg;

    @FXML
    private VBox vBox;

    private Socket socket;
    private DataInputStream dataInputStream;
    private DataOutputStream dataOutputStream;



    public void initialize(){

           new Thread(new Runnable() {
               @Override
               public void run() {
                   try {
                     socket = new Socket("localhost", 6509);
                     dataInputStream = new DataInputStream(socket.getInputStream());
                     dataOutputStream = new DataOutputStream(socket.getOutputStream());
                     System.out.println("Client connected");

                     while (socket.isConnected()){
                         String receivingMsg = dataInputStream.readUTF();
                         receiveMsg(receivingMsg,ClientController.this.vBox);
                         System.out.println(receivingMsg);
                     }

                   }catch (Exception e){
                       e.printStackTrace();
                   }
               }
           }).start();
    }
    @FXML
    void btnImageAction(ActionEvent event) throws IOException {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Image File");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.gif")
        );
        File selectedFile = fileChooser.showOpenDialog(null);
        String selectFile = selectedFile.getAbsolutePath();
        stringCovertImage(selectFile);
    }

    @FXML
    void btnMsgOnAction(ActionEvent event) throws IOException {
        String msg = txtMsg.getText();
       // System.out.println("Message : "+msg);
        send(msg);
    }
    public void send(String msg) throws IOException {

        vBox.getChildren().add(new Text(msg));

        dataOutputStream.writeUTF(msg);

        try {
            dataOutputStream.flush();
            txtMsg.clear();
        }catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void stringCovertImage(String selectFile){
        javafx.scene.image.Image image = new Image(selectFile);
        ImageView imageView = new ImageView(image);
        imageView.setFitHeight(200);
        imageView.setFitWidth(200);

        HBox hBox = new HBox();
        hBox.setPadding(new Insets(5,6,5,10));
        hBox.getChildren().add(imageView);
        hBox.setAlignment(Pos.CENTER_RIGHT);

        vBox.getChildren().add(hBox);

        try {
            dataOutputStream.writeUTF(selectFile);
            dataOutputStream.flush();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static void receiveMsg(String tMsg , VBox vBox) {
        String msg = tMsg;
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                vBox.getChildren().add(new Text(msg));
               vBox.getChildren().add(new Text("\n"));
            }
        });
    }




}
