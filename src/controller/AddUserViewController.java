package controller;

import com.jcraft.jsch.JSchException;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.binding.Bindings;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.SSHSession;

public class AddUserViewController implements Initializable {

    @FXML
    private Button registrarButton;
    @FXML
    private TextField carnetTextField;
    @FXML
    private TextField nombreTextField;
    @FXML
    private TextField apellidoTextField;
    @FXML
    private TextField telefonoTextField;
    @FXML
    private Button scanButton;
    @FXML
    private TextField tarjetaTextField;
    
    private String tarjetaID;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        registrarButton.disableProperty().bind(
            Bindings.isEmpty(carnetTextField.textProperty())
            .or(Bindings.isEmpty(nombreTextField.textProperty()))
            .or(Bindings.isEmpty(apellidoTextField.textProperty()))
            .or(Bindings.isEmpty(telefonoTextField.textProperty()))
            .or(Bindings.isEmpty(tarjetaTextField.textProperty()))
        );
    }    

    @FXML
    private void registrarUsuario(ActionEvent event) throws IOException, JSchException, InterruptedException {
        String output = SSHSession.issueCommand("sudo sqlite3 /db_files/dispenser.db \"insert into Usuario"
                + "(espolID, nombre, apellido, tarjetaID, telefono, saldo) values"
                + "('" + carnetTextField.getText() + "', '"
                + nombreTextField.getText() + "', '"
                + apellidoTextField.getText() + "', '"
                + tarjetaID + "', '"
                + telefonoTextField.getText() + "', "
                + "'0'" +  ");\"");
        Stage stage = (Stage) registrarButton.getScene().getWindow();
        System.out.println(output);
        stage.close();
    }

    @FXML
    private void scanTarjeta(ActionEvent event) throws IOException, JSchException, InterruptedException {
        scanButton.setDisable(true);
        SSHSession.issueCommand("sudo pkill python3");
        tarjetaID = SSHSession.issueCommand("/home/nisenare/Proyecto/main_registro.py &").split("\n")[0];
        SSHSession.issueCommand("sudo nohup /home/nisenare/Proyecto/main.py &>/dev/null &");
        censorID();
        scanButton.setStyle("-fx-background-color:#2ECC71;");
    }
    
    private void censorID() {
        String[] separado = tarjetaID.split("-");
        String aMostrar = "";
        for (int i = 0; i < separado.length; i++) {
            if (i == 0) {
                aMostrar += separado[i] + "-";
                continue;
            }
            
            if (i == separado.length - 1) {
                aMostrar += separado[i];
                continue;
            }
            
            aMostrar += "XX-";
        }
        tarjetaTextField.setText(aMostrar);
    }
    
}
