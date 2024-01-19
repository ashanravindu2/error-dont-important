package org.example.controller;

import com.jfoenix.controls.JFXButton;
import javafx.event.ActionEvent;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import org.example.emoji.EmojiSet;

public class ClientController {
    public AnchorPane pane;
    public ScrollPane scrollpain;
    public TextField txtMsg;
    public JFXButton emojiBtn;


    public void initialize(){
        emoji();
    }
    public void btnMsgOnAction(ActionEvent actionEvent) {
    }

    public void btnImageAction(ActionEvent actionEvent) {
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
}
