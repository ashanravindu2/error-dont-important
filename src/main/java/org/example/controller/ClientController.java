package org.example.controller;

import com.jfoenix.controls.JFXButton;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.example.model.UserModel;

import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.image.BufferedImage;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;

public class ClientController {


    public Label lblgetName ;
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
        txtMsg.requestFocus();

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
        try{  FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Open Image File");
            fileChooser.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.gif")
            );
            File selectedFile = fileChooser.showOpenDialog(null);
            String selectFile = selectedFile.getAbsolutePath();
            stringCovertImage(selectFile);
        }catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }

    @FXML
    void btnMsgOnAction(ActionEvent event) throws IOException {
        String msg = txtMsg.getText();

        send(msg);
    }
    public void send(String msg) throws IOException {

        String getname = lblgetName.getText();
        if (!msg.isEmpty()) {
            HBox hBox = new HBox();
            hBox.setPadding(new Insets(5,6,5,10));

            Text text = new Text(msg);
            text.setStyle("-fx-background-color: #ffffff;-fx-font-size: 12px;-fx-font-weight: bold;-fx-font-family: TimesNewRoman");

            TextFlow textFlow = new TextFlow(text);
            textFlow.setStyle("-fx-background-color: #008bff; -fx-font-weight: bold; -fx-background-radius: 20px");
            textFlow.setPadding(new Insets(5, 10, 5, 10));

            hBox.getChildren().add(textFlow);
            hBox.setAlignment(Pos.CENTER_RIGHT);
            vBox.getChildren().add(hBox);
            try {
                dataOutputStream.writeUTF(getname+"~"+msg);
                dataOutputStream.flush();
                txtMsg.clear();
            }catch (Exception e) {
                e.printStackTrace();
            }
        }

    }
    public void stringCovertImage(String selectFile){
        String getname = lblgetName.getText();

        javafx.scene.image.Image image = new Image(selectFile);
        ImageView imageView = new ImageView(image);
        imageView.setFitHeight(200);
        imageView.setFitWidth(200);

        HBox hBox = new HBox();
        hBox.setPadding(new Insets(5,5,5,10));
        hBox.getChildren().add(imageView);
        hBox.setAlignment(Pos.CENTER_RIGHT);

        vBox.getChildren().add(hBox);



        try {
            dataOutputStream.writeUTF(getname+"~"+selectFile);
            dataOutputStream.flush();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static void receiveMsg(String recieveNameandMsg , VBox vBox) throws SQLException {


        //Profile Image Load from database
        UserModel userModel = new UserModel();
        ResultSet imageURL = userModel.getImageURL(recieveNameandMsg.split("[~]")[0]);
        HBox profile = new HBox();

        while (imageURL.next()){
            String url = imageURL.getString("profilePicUrl");
                Image image = new Image(url);
                ImageView imageView = new ImageView(image);
                imageView.setFitHeight(35);
                imageView.setFitWidth(35);
                //imageview edit circle
                imageView.setClip(new javafx.scene.shape.Circle(17, 17, 17));
               profile = new HBox();

            profile.setAlignment(Pos.CENTER_LEFT);
            profile.setPadding(new Insets(5, 5, 5, 10));
            profile.getChildren().add(imageView);
        }

        if(!recieveNameandMsg.isEmpty()) {
            if (recieveNameandMsg.matches(".*\\.(png|jpe?g|gif)$")) {

                HBox hBoxName = new HBox();
                hBoxName.setAlignment(Pos.CENTER_LEFT);
                Text textName = new Text(recieveNameandMsg.split("[~]")[0]);
                TextFlow textFlowName = new TextFlow(textName);
                textFlowName.setStyle("-fx-background-color: #dcc9c9; -fx-font-weight: bold; -fx-background-radius: 5px ; -fx-font-size: 14px");
                textFlowName.setPadding(new Insets(5, 10, 5, 10));
                hBoxName.getChildren().add(textFlowName);

                Image image = new Image(recieveNameandMsg.split("[~]")[1]);
                ImageView imageView = new ImageView(image);
                imageView.setFitHeight(200);
                imageView.setFitWidth(200);
                HBox hBox = new HBox();
                hBox.setAlignment(Pos.CENTER_LEFT);
                hBox.setPadding(new Insets(5, 5, 5, 10));
                hBox.getChildren().add(imageView);
                HBox finalProfile = profile;
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        vBox.getChildren().add(hBoxName);
                        vBox.getChildren().add(finalProfile);
                        vBox.getChildren().add(hBox);
                    }
                });

            } else {

                String name =recieveNameandMsg.split("[~]")[0];
                String msgFromServer = recieveNameandMsg.split("[~]")[1];

                HBox hBox = new HBox();
                hBox.setAlignment(Pos.CENTER_LEFT);
                hBox.setPadding(new Insets(5, 5, 5, 10));

                HBox hBoxName = new HBox();
                hBoxName.setAlignment(Pos.CENTER_LEFT);
                Text textName = new Text(name);
                TextFlow textFlowName = new TextFlow(textName);
                textFlowName.setStyle("-fx-background-color: #dcc9c9; -fx-font-weight: bold; -fx-background-radius: 5px ; -fx-font-size: 14px");
                textFlowName.setPadding(new Insets(5, 10, 5, 10));
                hBoxName.getChildren().add(textFlowName);

                Text text = new Text(msgFromServer);
                TextFlow textFlow = new TextFlow(text);
                textFlow.setStyle("-fx-background-color: #abb8c3;-fx-background-radius: 10px;");
                textFlow.setPadding(new Insets(5, 10, 5, 10));
                text.setFill(Color.color(0, 0, 0));

                hBox.getChildren().add(textFlow);

                HBox finalProfile1 = profile;
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        vBox.getChildren().add(hBoxName);
                        vBox.getChildren().add(finalProfile1);
                        vBox.getChildren().add(hBox);
                    }
                });
            }
        }
    }

    public void setClientName(String name){

        lblgetName.setText(name);
    }



}
