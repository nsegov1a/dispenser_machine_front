package controller;

import com.jcraft.jsch.JSchException;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.SSHSession;

public class LoginController implements Initializable {

    @FXML
    private Button loginBoton;
    @FXML
    private PasswordField passTextField;
    @FXML
    private TextField userTextField;
    @FXML
    private TextField ipTextField;
    
    public static SSHSession sshSession;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    @FXML
    private void login(ActionEvent event) {
        String username = userTextField.getText();
        String password = passTextField.getText();
        String ip = ipTextField.getText();
        try {
            sshSession = SSHSession.get(ip, username, password);
            this.showMainView();
        } catch (JSchException ex) {
            String tipo = ex.getMessage().split(":")[0];
            System.out.println(tipo);
            switch (tipo) {
                case "UnknownHostKey":
                    addKnownHost(ip);
                    loginBoton.setText("Intenta de nuevo");
                    break;
                case "Auth fail":
                    showIncorrectPassword();
                    break;
                case "timeout":
                    showTimeoutError();
                default:
                    showTimeoutError();
                    break;
            }
            
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
    private void showMainView() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/MainView.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.show();
        Stage actual = (Stage) this.loginBoton.getScene().getWindow();
        actual.close();
    }
    
    private static void showIncorrectPassword() {
        Alert alert = new Alert(AlertType.ERROR, "Autenticación fallida");
        alert.showAndWait();
    }
    
    private static void addKnownHost(String ip) {
        try {
            System.out.println();
            Process p = new ProcessBuilder(
                "/bin/sh",
                "-c",
                "ssh-keyscan -t rsa " + ip +" >> ~/.ssh/known_hosts").start();
            p.waitFor();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
    private static void showTimeoutError() {
        Alert alert = new Alert(AlertType.ERROR, "Tiempo de espera agotado\nRevise la IP de la Raspberry");
        alert.showAndWait();
    }
    
}
