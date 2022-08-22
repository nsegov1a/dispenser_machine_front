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
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.SSHSession;

public class ProductsViewController implements Initializable {

    @FXML
    private Button backButton;
    @FXML
    private Button updateButton1;
    @FXML
    private TextField codigoTextField1;
    @FXML
    private TextField descTextField1;
    @FXML
    private TextField precioTextField1;
    @FXML
    private Button updateButton2;
    @FXML
    private TextField codigoTextField2;
    @FXML
    private TextField descTextField2;
    @FXML
    private TextField precioTextField2;
    @FXML
    private Button updateButton3;
    @FXML
    private TextField codigoTextField3;
    @FXML
    private TextField descTextField3;
    @FXML
    private TextField precioTextField3;
    @FXML
    private Button updateButton4;
    @FXML
    private TextField codigoTextField4;
    @FXML
    private TextField descTextField4;
    @FXML
    private TextField precioTextField4;
    @FXML
    private Button updateButton5;
    @FXML
    private TextField codigoTextField5;
    @FXML
    private TextField descTextField5;
    @FXML
    private TextField precioTextField5;
    @FXML
    private Button updateButton6;
    @FXML
    private TextField codigoTextField6;
    @FXML
    private TextField descTextField6;
    @FXML
    private TextField precioTextField6;
    
    private Scene scene;

    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }    

    @FXML
    private void back(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/MainView.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.show();
        Stage actual = (Stage) this.backButton.getScene().getWindow();
        actual.close();
    }

    @FXML
    private void update(ActionEvent event) throws IOException, JSchException, InterruptedException {
        Button b = (Button) event.getSource();
        String numero = b.getId();
        numero = numero.substring(numero.length() - 1);
        TextField codigoTextField = (TextField)this.scene.lookup("#" + "codigoTextField" + numero);
        TextField descTextField = (TextField)this.scene.lookup("#" + "descTextField" + numero);
        TextField precioTextField = (TextField)this.scene.lookup("#" + "precioTextField" + numero);
        SSHSession.issueCommand("sudo sqlite3 /db_files/dispenser.db \"update Producto "
                + "set descripcion = '" + descTextField.getText() + "', "
                + "codigo = '" + codigoTextField.getText() + "', "
                + "precio = '" + precioTextField.getText() + "' "
                + "where id = '" + numero + "';\"");
    }
    
    public void setScene(Scene scene) {
        this.scene = scene;
    }
   
    public void retrieveProductData() throws IOException, JSchException, InterruptedException {
        for (int i = 1; i <= 6; i++) {
            TextField codigoTextField = (TextField)this.scene.lookup("#" + "codigoTextField" + i);
            TextField descTextField = (TextField)this.scene.lookup("#" + "descTextField" + i);
            TextField precioTextField = (TextField)this.scene.lookup("#" + "precioTextField" + i);
            String productoStr = SSHSession.issueCommand("sqlite3 /db_files/dispenser.db \"select * from Producto"
                + " where id = '" + i + "';\"").split("\n")[0];
            String[] producto = productoStr.split("\\|");
            codigoTextField.setText(producto[3]);
            descTextField.setText(producto[1]);
            precioTextField.setText(producto[2]);
        }
    }
    
}
