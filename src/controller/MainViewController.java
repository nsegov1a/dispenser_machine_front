package controller;

import com.jcraft.jsch.JSchException;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import model.SSHSession;

public class MainViewController implements Initializable {

    @FXML
    private Button usuariosButton;
    @FXML
    private Button transaccionesButton;
    @FXML
    private Button productosButton;
    @FXML
    private Button logoutButton;
    @FXML
    private Button shutdownButton;
    @FXML
    private Button resetButton;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    @FXML
    private void seeUsers(ActionEvent event) throws IOException, JSchException, InterruptedException {
        if (!SSHSession.isConnected()) {
            this.logout(event);
        }
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/UsersView.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.show();
        Stage actual = (Stage) this.transaccionesButton.getScene().getWindow();
        actual.close();
    }

    @FXML
    private void seeTransactions(ActionEvent event) throws IOException {
        if (!SSHSession.isConnected()) {
            this.logout(event);
        }
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/TransactionsView.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.show();
        Stage actual = (Stage) this.usuariosButton.getScene().getWindow();
        actual.close();
    }

    @FXML
    private void seeProducts(ActionEvent event) throws IOException {
        if (!SSHSession.isConnected()) {
            this.logout(event);
        }
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/ProductsView.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        Stage stage = new Stage();
        ProductsViewController control = (ProductsViewController) loader.getController();
        stage.addEventHandler(WindowEvent.WINDOW_SHOWN, (WindowEvent window) -> {
            Platform.runLater(() -> {
                control.setScene(scene);
                try {
                    control.retrieveProductData();
                } catch (IOException | JSchException | InterruptedException ex) {
                    Logger.getLogger(MainViewController.class.getName()).log(Level.SEVERE, null, ex);
                }
            });
        });
        stage.setScene(scene);
        stage.show();
        Stage actual = (Stage) this.productosButton.getScene().getWindow();
        actual.close();
    }

    @FXML
    private void logout(ActionEvent event) throws IOException {
        SSHSession.close();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/LoginView.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.show();
        Stage actual = (Stage) this.logoutButton.getScene().getWindow();
        actual.close();
    }

    @FXML
    private void shutdown(ActionEvent event) throws IOException, JSchException, InterruptedException {
        showLogoutWarning(false, event);
    }

    @FXML
    private void reset(ActionEvent event) throws IOException, JSchException, InterruptedException {
        showLogoutWarning(true, event);
    }
    
    private void showLogoutWarning(boolean reset, ActionEvent event) throws IOException, JSchException, InterruptedException {
        String placeholder = "";
        if (reset) {
            placeholder = "resetear";
        } else {
            placeholder = "apagar";
        }
        Alert alert = new Alert(AlertType.CONFIRMATION,
            "Se desconectará la sesión al " + placeholder + " la máquina.\n" +
            "¿Seguro/a?",
            ButtonType.YES,
            ButtonType.NO);
        alert.showAndWait();
        if (alert.getResult() == ButtonType.YES) {
            if (reset) {
                SSHSession.issueCommand("sudo nohup reboot now &>/dev/null &");
                logout(event);
            } else {
                SSHSession.issueCommand("sudo nohup shutdown now &>/dev/null &");
                logout(event);
            }
        }
    }
}
