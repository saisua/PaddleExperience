/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package paddleexperience;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;

/**
 * FXML Controller class
 *
 * @author joandzmn
 */
public class FXMLSidebarController implements Initializable {

    @FXML
    private BorderPane borderpane;
    @FXML
    private Button btn_home;
    @FXML
    private Button btn_reserva;
    @FXML
    private Button btn_historico;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        btn_home.setStyle("-fx-background-color:#78909C;");
        btn_reserva.setStyle("-fx-background-color:transparent;-fx-text-fill:#FAFAFA;");
        btn_historico.setStyle("-fx-background-color:transparent;-fx-text-fill:#FAFAFA;");
        loadUI("FXMLHome");
    }

    @FXML
    private void goHome(MouseEvent event) {
        btn_home.setStyle("-fx-background-color:#78909C;");
        btn_reserva.setStyle("-fx-background-color:transparent;-fx-text-fill:#FAFAFA;");
        btn_historico.setStyle("-fx-background-color:transparent;-fx-text-fill:#FAFAFA;");
        loadUI("FXMLHome");
    }

    @FXML
    private void goReserva(MouseEvent event) {
        btn_home.setStyle("-fx-background-color:transparent;-fx-text-fill:#FAFAFA;");
        btn_reserva.setStyle("-fx-background-color:#78909C;");
        btn_historico.setStyle("-fx-background-color:transparent;-fx-text-fill:#FAFAFA;");
        loadUI("FXMLPaddleReserva");
    }

    @FXML
    private void goHistorial(MouseEvent event) {
        btn_home.setStyle("-fx-background-color:transparent;-fx-text-fill:#FAFAFA;");
        btn_reserva.setStyle("-fx-background-color:transparent;-fx-text-fill:#FAFAFA;");
        btn_historico.setStyle("-fx-background-color:#78909C;");
        loadUI("FXMLHistorico");
    }

    private void loadUI(String ui) {
        Parent root = null;
        try {
            root = FXMLLoader.load(getClass().getResource(ui + ".fxml"));
        } catch (IOException e) {
            Logger.getLogger(FXMLSidebarController.class.getName()).log(Level.SEVERE, null, e);
        }
        borderpane.setCenter(root);
    }

}
