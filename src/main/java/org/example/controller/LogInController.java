package org.example.controller;

import com.jfoenix.controls.JFXButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.example.client.ServerHandler;
import org.example.model.UserModel;

import java.io.IOException;
import java.sql.SQLException;


public class LogInController {


    public TextField txtUserName;
    public JFXButton logBtn;
    public AnchorPane pane;
    public TextField txtPass;
    UserModel userModel = new UserModel();
    private ServerHandler serverHandler;


    public void initialize() throws IOException {

        new Thread(()->{
            try {
                serverHandler = ServerHandler.getInstance();
                serverHandler.makeSocket();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();

    }

    public void btnLogInAction(ActionEvent actionEvent) throws IOException, SQLException {
        ClientController clientController = new ClientController();

        if (!txtUserName.getText().isEmpty()&&txtUserName.getText().matches("[A-Za-z]+")){
            boolean isValid = userModel.login(txtUserName.getText(),txtPass.getText());
            if (isValid){
                Stage stage = new Stage();
                stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("/view/client_form.fxml"))));
                stage.setTitle(txtUserName.getText());
                stage.setResizable(false);
                stage.centerOnScreen();
                stage.getIcons().add(new javafx.scene.image.Image("assets/icons8-male-user-100.png"));
                stage.setOnCloseRequest(windowEvent -> {

                });
                stage.show();
                txtUserName.clear();
            }else{
                new Alert(Alert.AlertType.ERROR, "Invalid User Name or Password").show();
            }
        }else{
           new Alert(Alert.AlertType.ERROR, "Please enter your name").show();
        }

    }

    public void getRegisterPageAction(MouseEvent mouseEvent) throws IOException {
        Parent pane = FXMLLoader.load(getClass().getResource("/view/register_form.fxml"));
        this.pane.getChildren().clear();
        Scene scene = new Scene(pane);
        Stage stage = (Stage) this.pane.getScene().getWindow();
        stage.setTitle("Register");
        stage.setScene(scene);
    }
}
