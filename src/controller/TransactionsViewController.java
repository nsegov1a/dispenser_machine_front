package controller;

import com.jcraft.jsch.JSchException;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.SSHSession;
import model.Transaccion;

public class TransactionsViewController implements Initializable {

    @FXML
    private Button backButton;
    @FXML
    private Button refreshButton;
    @FXML
    private TableView<Transaccion> transactionsTable;
    @FXML
    private TableColumn colId;
    @FXML
    private TableColumn colEspolId;
    @FXML
    private TableColumn colDescripcion;
    @FXML
    private TableColumn colPrecio;
    @FXML
    private TableColumn colFechaHora;
    
    private ObservableList<Transaccion> transacciones;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            this.transacciones = FXCollections.observableArrayList();
            this.colId.setCellValueFactory(new PropertyValueFactory("id"));
            this.colEspolId.setCellValueFactory(new PropertyValueFactory("espolId"));
            this.colDescripcion.setCellValueFactory(new PropertyValueFactory("descripcion"));
            this.colPrecio.setCellValueFactory(new PropertyValueFactory("precio"));
            this.colFechaHora.setCellValueFactory(new PropertyValueFactory("fechaHora"));
            this.refresh(new ActionEvent());
        } catch (Exception ex) {
            Logger.getLogger(UsersViewController.class.getName()).log(Level.SEVERE, null, ex);
        }
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
    private void refresh(ActionEvent event) throws IOException, JSchException, InterruptedException {
        String transactions = SSHSession.issueCommand("sqlite3 /db_files/dispenser.db 'select * from Transaccion;'");
        String[] transacs = transactions.split("\n");
        this.transacciones.clear();
        for (String t : transacs) {
            String[] transaccion = t.split("\\|");
            Transaccion trans = new Transaccion(
                transaccion[0],
                transaccion[1],
                transaccion[2],
                transaccion[3],
                transaccion[4]
            );
            this.transacciones.add(trans);
        }
        this.transactionsTable.setItems(this.transacciones);
    }
}
