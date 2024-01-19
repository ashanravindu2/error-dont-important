package org.example.controller;

import com.jfoenix.controls.JFXButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;


public class LogInController {


    public TextField txtUserName;
    public JFXButton logBtn;

    public void initialize(){

    }

    public void btnLogInAction(ActionEvent actionEvent) throws IOException {
        if (!txtUserName.getText().isEmpty()&&txtUserName.getText().matches("[A-Za-z0-9]+")){
            Stage stage = new Stage();
            stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("/view/client_form.fxml"))));
            stage.setTitle(txtUserName.getText());
            stage.setResizable(false);
            stage.centerOnScreen();
            stage.getIcons().add(new javafx.scene.image.Image("assets/icons8-male-user-100.png"));
            stage.show();
            txtUserName.clear();
        }else{
           new Alert(Alert.AlertType.ERROR, "Please enter your name").show();
        }

    }
}
