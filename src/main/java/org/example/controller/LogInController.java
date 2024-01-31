package org.example.controller;

import com.jfoenix.controls.JFXButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;
import org.example.client.ClientHandler;
import org.example.client.ServerHandler;
import org.example.model.UserModel;
import org.jfree.layout.CenterLayout;

import java.io.IOException;
import java.io.InputStream;
import java.sql.ResultSet;
import java.sql.SQLException;


public class LogInController {


    public TextField txtUserName;
    public JFXButton logBtn;
    public AnchorPane pane;
    public TextField txtPass;
    public Rectangle profileImg;
    UserModel userModel = new UserModel();
    private ServerHandler serverHandler;


    public void initialize() throws IOException {
        Image ima = new Image("assets/icons8-male-user-100.png");
        profileImg.setFill(new ImagePattern(ima));

        new Thread(() -> {
            try {
                serverHandler = ServerHandler.getInstance();
                serverHandler.makeSocket(txtUserName.getText());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();

    }

    public void btnLogInAction(ActionEvent actionEvent) throws IOException, SQLException {
        if (!txtUserName.getText().isEmpty() && txtUserName.getText().matches("[A-Za-z]+")) {
            boolean isValid = userModel.login(txtUserName.getText(), txtPass.getText());

            if (isValid) {
                FXMLLoader loader = new FXMLLoader(this.getClass().getResource("/view/client_form.fxml"));
                Parent rootNode = loader.load();
                ClientController clientController = loader.getController();
                //setName parameter
                clientController.setClientName(txtUserName.getText());
                Scene scene = new Scene(rootNode);
                Stage stage = new Stage();
                stage.setScene(scene);
                stage.setResizable(false);
                stage.getIcons().add(new Image("assets/icons8-male-user-100.png"));
                stage.centerOnScreen();
                stage.setTitle(txtUserName.getText() + " 's Chat");
                stage.show();
            } else {
                new Alert(Alert.AlertType.ERROR, "Invalid User Name or Password").show();
            }
        } else {
            new Alert(Alert.AlertType.ERROR, "Please enter your name").show();
        }
        txtUserName.clear();
        txtPass.clear();

    }

    public void getRegisterPageAction(MouseEvent mouseEvent) throws IOException {
        Parent pane = FXMLLoader.load(getClass().getResource("/view/register_form.fxml"));
        this.pane.getChildren().clear();
        Scene scene = new Scene(pane);
        Stage stage = (Stage) this.pane.getScene().getWindow();
        stage.setTitle("Register");
        stage.setScene(scene);
    }


    public void btnGetProfilePicAction(KeyEvent keyEvent) throws SQLException {
        if (txtUserName.getText().isEmpty()) {
            try {
                Image ima = new Image("assets/icons8-male-user-100.png");
                profileImg.setFill(new ImagePattern(ima));
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (!txtUserName.getText().isEmpty()) {
            ResultSet isValid = userModel.getImageURL(txtUserName.getText());
            while (isValid.next()) {
                String url = isValid.getString("profilePicUrl");
                Image ima = new Image(url);
                profileImg.setFill(new ImagePattern(ima));
            }
        }
    }

    public void btnEnterOnAction(KeyEvent keyEvent) {
        if (keyEvent.getCode().toString().equals("ENTER")) {
            try {
                btnLogInAction(new ActionEvent());
            } catch (IOException | SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
