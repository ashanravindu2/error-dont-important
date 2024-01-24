package org.example.controller;

import com.jfoenix.controls.JFXButton;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import org.example.emoji.EmojiSet;

import java.awt.*;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;


public class ClientController {
    public AnchorPane pane;
    public ScrollPane scrollpain;
    public TextField txtMsg;
    public JFXButton emojiBtn;
    public VBox vBox;
     Socket socket;
    private DataInputStream dataInputStream;
    private DataOutputStream dataOutputStream;

    public void initialize(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    socket = new Socket("localhost",3002);
                    dataInputStream = new DataInputStream(socket.getInputStream());
                    dataOutputStream = new DataOutputStream(socket.getOutputStream());
                    System.out.println("Client connected");



                    while ( socket.isConnected()){
                        String receivingMsg = dataInputStream.readUTF();
                        receiveMessage(receivingMsg, vBox);
                    }
                }catch (IOException e){
                    e.printStackTrace();
                }
            }
        }).start();

        this.vBox.heightProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number oldValue, Number newValue) {
                scrollpain.setVvalue((Double) newValue);
            }
        });
        emoji();

    }

    public void btnMsgOnAction(ActionEvent actionEvent) {
        sendMassege(txtMsg.getText());
    }
    public void sendMassege(String sendtext){
        if (!sendtext.isEmpty()){
            if (!sendtext.matches(".*\\.(png|jpe?g|gif)$")){

                HBox hBox = new HBox();
                hBox.setAlignment(Pos.CENTER_RIGHT);
                hBox.setPadding(new Insets(5, 5, 0, 10));

                Text text = new Text(sendtext);
                text.setStyle("-fx-font-size: 14");
                TextFlow textFlow = new TextFlow(text);

//              #0693e3 #37d67a #40bf75
                textFlow.setStyle("-fx-background-color: #0693e3; -fx-font-weight: bold; -fx-color: white; -fx-background-radius: 20px");
                textFlow.setPadding(new Insets(5, 10, 5, 10));
                text.setFill(Color.color(1, 1, 1));

                hBox.getChildren().add(textFlow);

                HBox hBoxTime = new HBox();
                hBoxTime.setAlignment(Pos.CENTER_RIGHT);
                hBoxTime.setPadding(new Insets(0, 5, 5, 10));
                String stringTime = LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm"));
                Text time = new Text(stringTime);
                time.setStyle("-fx-font-size: 8");

                hBoxTime.getChildren().add(time);

                vBox.getChildren().add(hBox);
                vBox.getChildren().add(hBoxTime);


                try {
                    dataOutputStream.writeUTF(sendtext.trim());
                    dataOutputStream.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                txtMsg.clear();
            }
        }
    }

    private void receiveMessage(String receivingMsg, VBox vBox) {
        if (receivingMsg.matches(".*\\.(png|jpe?g|gif)$")){
            HBox hBoxName = new HBox();
            hBoxName.setAlignment(Pos.CENTER_LEFT);
            Text textName = new Text(receivingMsg.split("[-]")[0]);
            TextFlow textFlowName = new TextFlow(textName);
            hBoxName.getChildren().add(textFlowName);

            Image image = new Image(receivingMsg.split("[-]")[1]);
            ImageView imageView = new ImageView(image);
            imageView.setFitHeight(200);
            imageView.setFitWidth(200);
            HBox hBox = new HBox();
            hBox.setAlignment(Pos.CENTER_LEFT);
            hBox.setPadding(new Insets(5,5,5,10));
            hBox.getChildren().add(imageView);
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    vBox.getChildren().add(hBoxName);
                    vBox.getChildren().add(hBox);
                }
            });
        }else {
            System.out.println(receivingMsg);
            String name = receivingMsg.split("-")[0];
            String msgFromServer = receivingMsg.split("-")[1];

            HBox hBox = new HBox();
            hBox.setAlignment(Pos.CENTER_LEFT);
            hBox.setPadding(new Insets(5,5,5,10));

            HBox hBoxName = new HBox();
            hBoxName.setAlignment(Pos.CENTER_LEFT);
            Text textName = new Text(name);
            TextFlow textFlowName = new TextFlow(textName);
            hBoxName.getChildren().add(textFlowName);

            Text text = new Text(msgFromServer);
            TextFlow textFlow = new TextFlow(text);
            textFlow.setStyle("-fx-background-color: #abb8c3; -fx-font-weight: bold; -fx-background-radius: 20px");
            textFlow.setPadding(new Insets(5,10,5,10));
            text.setFill(Color.color(0,0,0));

            hBox.getChildren().add(textFlow);

            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    vBox.getChildren().add(hBoxName);
                    vBox.getChildren().add(hBox);
                }
            });
        }
    }

    public void btnImageAction(ActionEvent actionEvent) throws IOException {
        FileDialog dialog = new FileDialog((Frame)null, "Select File to Open");
        dialog.setMode(FileDialog.LOAD);
        dialog.setVisible(true);
        String file = dialog.getDirectory()+dialog.getFile();
        dialog.dispose();
        sendImage(file);
        System.out.println(file + " chosen.");
    }

    private void sendImage(String file) {
        Image image = new Image(file);
        ImageView imageView = new ImageView(image);
        imageView.setFitHeight(200);
        imageView.setFitWidth(200);

        HBox hBox = new HBox();
        hBox.setPadding(new Insets(5, 5, 5, 10));
        hBox.getChildren().add(imageView);
        hBox.setAlignment(Pos.CENTER_RIGHT);

        vBox.getChildren().add(hBox);
        try {
            dataOutputStream.writeUTF(file);
            dataOutputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void emoji(){
        EmojiSet emojiSet = new EmojiSet();

        VBox vBox = new VBox(emojiSet);
        vBox.setPrefSize(100,280);
        vBox.setLayoutX(400);
        vBox.setLayoutY(175);
        vBox.setStyle("-fx-font-size: 15");

        pane.getChildren().add(vBox);
        emojiSet.setVisible(false);

        emojiBtn.setOnAction(event -> {
            if (emojiSet.isVisible()){
                emojiSet.setVisible(false);
            }else {
                emojiSet.setVisible(true);
            }
        });
        //set the selected emoji from the picker to the text field
        emojiSet.getEmojiListView().setOnMouseClicked(event -> {
            String selectedEmoji = emojiSet.getEmojiListView().getSelectionModel().getSelectedItem();
            if (selectedEmoji != null) {
                txtMsg.setText(txtMsg.getText()+selectedEmoji);
            }
            emojiSet.setVisible(false);
        });

    }

    public void run() {
   new ServerController().run();

    }
}
