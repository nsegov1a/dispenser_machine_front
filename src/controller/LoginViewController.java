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

public class LoginViewController implements Initializable {

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
    private void login(ActionEvent event) throws InterruptedException, IOException {
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
                    loginBoton.setText("Presiona de nuevo");
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
    
    private void showIncorrectPassword() {
        Alert alert = new Alert(AlertType.ERROR, "Autenticación fallida");
        alert.showAndWait();
    }
    
    private void addKnownHost(String ip) throws InterruptedException, IOException {
        String sist = System.getProperty("os.name").split(" ")[0];
        String tipoShell;
        String switchC;
        String knownHostsDir;
        if (sist.split(" ")[0].equals("Windows")) {
            tipoShell = "CMD";
            switchC = "/c";
            knownHostsDir = "%USERPROFILE%/.ssh/known_hosts";
        } else {
            tipoShell = "/bin/sh";
            switchC = "-c";
            knownHostsDir = "~/.ssh/known_hosts";
        }
        Process p = new ProcessBuilder(
            tipoShell,
            switchC,
            "ssh-keyscan -t rsa " + ip +" >> " + knownHostsDir).start();
        p.waitFor();
    }
    
    private void showTimeoutError() {
        Alert alert = new Alert(AlertType.ERROR, "Tiempo de espera agotado\nRevise la IP de la Raspberry");
        alert.showAndWait();
    }
    
}
