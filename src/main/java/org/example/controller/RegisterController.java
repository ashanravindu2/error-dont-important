package org.example.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.example.model.UserModel;

import java.io.IOException;
import java.sql.SQLException;

public class RegisterController {
    public AnchorPane pane;
    public TextField txtUserName;
    public TextField txtPass;

    UserModel userModel = new UserModel();

    public void btnCreateOnAction(ActionEvent actionEvent) throws SQLException {

    if (!txtUserName.getText().isEmpty()&&txtUserName.getText().matches("[A-Za-z]+")){
        if (!txtPass.getText().isEmpty()&&txtPass.getText().matches("[A-Za-z0-9]+")) {
            boolean isValid = userModel.exists(txtUserName.getText());
            if (isValid){
                new Alert(Alert.AlertType.ERROR, "User already exists").show();
            }else {
                boolean isRegister = userModel.register(txtUserName.getText(), txtPass.getText());
                if (isRegister){
                    new Alert(Alert.AlertType.CONFIRMATION, "User created successfully").show();
                }
            }
        }
        }else {
            new Alert(Alert.AlertType.ERROR, "Please enter your name").show();
        }

    }

    public void getLogInPageAction(MouseEvent mouseEvent) throws IOException {
        Parent pane = FXMLLoader.load(getClass().getResource("/view/logIn_form.fxml"));
        this.pane.getChildren().clear();
        Scene scene = new Scene(pane);
        Stage stage = (Stage) this.pane.getScene().getWindow();
        stage.setTitle("Join");
        stage.setScene(scene);
    }
}
