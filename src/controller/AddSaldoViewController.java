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
import model.Estudiante;
import model.SSHSession;

public class AddSaldoViewController implements Initializable {

    @FXML
    private Button anadirButton;
    @FXML
    private TextField centavoTextField;
    @FXML
    private TextField dolarTextField;
    
    private Estudiante estudiante;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        anadirButton.disableProperty().bind(
            Bindings.isEmpty(dolarTextField.textProperty())
            .or(Bindings.isEmpty(centavoTextField.textProperty()))
        );
    }    

    @FXML
    private void anadirSaldo(ActionEvent event) throws IOException, JSchException, InterruptedException {
        String cantidad = dolarTextField.getText() + "." + centavoTextField.getText();
        this.estudiante.aumentarSaldo(cantidad);
        SSHSession.issueCommand("sudo sqlite3 /db_files/dispenser.db \"update Usuario "
                + "set saldo = '" + this.estudiante.getSaldo() + "' "
                + "where espolID = '" + this.estudiante.getEspolID() + "';\""
        );
        Stage stage = (Stage) anadirButton.getScene().getWindow();
        stage.close();
    }
    
    public void setEstudiante(Estudiante e) {
        this.estudiante = e;
    }
    
}
