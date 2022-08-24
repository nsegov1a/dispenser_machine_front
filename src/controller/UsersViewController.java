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
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.Estudiante;
import model.SSHSession;

public class UsersViewController implements Initializable {

    @FXML
    private Button backButton;
    @FXML
    private Button refreshButton;
    @FXML
    private Button addButton;
    @FXML
    private Button deleteButton;
    @FXML
    private TableView<Estudiante> usersTable;
    @FXML
    private TableColumn colNombre;
    @FXML
    private TableColumn colApellido;
    @FXML
    private TableColumn colEspolID;
    @FXML
    private TableColumn colTelefono;
    @FXML
    private TableColumn colSaldo;
    @FXML
    private Button addSaldoButton;
    
    private ObservableList<Estudiante> estudiantes;
    private Estudiante selectedEstudiante;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            this.estudiantes = FXCollections.observableArrayList();
            this.colNombre.setCellValueFactory(new PropertyValueFactory("nombre"));
            this.colApellido.setCellValueFactory(new PropertyValueFactory("apellido"));
            this.colEspolID.setCellValueFactory(new PropertyValueFactory("espolID"));
            this.colTelefono.setCellValueFactory(new PropertyValueFactory("telefono"));
            this.colSaldo.setCellValueFactory(new PropertyValueFactory("saldo"));
            usersTable.getSelectionModel().
                    selectedItemProperty().
                    addListener((obs, oldSelection, newSelection) -> {
                deleteButton.setDisable(false);
                addSaldoButton.setDisable(false);
                selectedEstudiante = newSelection;
            });
            this.refresh(new ActionEvent());
        } catch (JSchException | IOException | InterruptedException ex) {
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
        String usuarios = SSHSession.issueCommand("sqlite3 /db_files/dispenser.db 'select * from Usuario;'");
        String[] users = usuarios.split("\n");
        this.estudiantes.clear();
        for (String u : users) {
            String[] user = u.split("\\|");
            Estudiante estudiante = new Estudiante(
                user[0],
                user[1],
                user[2],
                user[3],
                user[4],
                user[5],
                user[6]
            );
            this.estudiantes.add(estudiante);
        }
        this.usersTable.setItems(estudiantes);
        deleteButton.setDisable(true);
        addSaldoButton.setDisable(true);
    }

    @FXML
    private void add(ActionEvent event) throws IOException, JSchException, InterruptedException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/AddUserView.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        Stage stage = new Stage();
        stage.initModality(Modality.WINDOW_MODAL);
        Stage actual = (Stage) this.addButton.getScene().getWindow();
        stage.initOwner(actual);
        stage.setScene(scene);
        stage.showAndWait();
        refresh(event);
    }

    @FXML
    private void eliminar(ActionEvent event) throws IOException, JSchException, InterruptedException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION,
            "Se eliminará al usuario " + selectedEstudiante.getNombre() + " " +
            selectedEstudiante.getApellido() + " con código de matrícula " +
            selectedEstudiante.getEspolID() + " de la máquina.\n" +
            "¿Seguro/a?",
            ButtonType.YES,
            ButtonType.NO);
        alert.showAndWait();
        if (alert.getResult() == ButtonType.YES) {
            SSHSession.issueCommand("sudo sqlite3 /db_files/dispenser.db \"delete from Usuario where " + 
            "id = '" + selectedEstudiante.getId() + "';\"");
            refresh(event);
        }
    }

    @FXML
    private void addSaldo(ActionEvent event) throws IOException, JSchException, InterruptedException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/AddSaldoView.fxml"));
        Parent root = loader.load();
        AddSaldoViewController controlador = loader.getController();
        controlador.setEstudiante(selectedEstudiante);
        Scene scene = new Scene(root);
        Stage stage = new Stage();
        stage.initModality(Modality.WINDOW_MODAL);
        Stage actual = (Stage) this.addSaldoButton.getScene().getWindow();
        stage.initOwner(actual);
        stage.setScene(scene);
        stage.showAndWait();
        refresh(event);
    }
}
