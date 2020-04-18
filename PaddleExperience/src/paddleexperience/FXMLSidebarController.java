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
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Separator;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import paddleexperience.PaddleExperience;
import paddleexperience.Structures.Stoppable;
import paddleexperience.Dataclasses.Estat;

/**
 * FXML Controller class
 *
 * @author joandzmn
 */
public class FXMLSidebarController implements Initializable, Stoppable {

    @FXML
    private HBox hbox_sidebar;
    @FXML
    private BorderPane borderpane;
    @FXML
    private Button btn_home;
    @FXML
    private Button btn_reserva;
    @FXML
    private Button btn_historico;
    @FXML
    private Separator separator_resize;
    @FXML
    private Text text_Historico;

    private Stoppable sub_menu_controller;

    private final double MIN_SIZE = 145.d;
    private final double MAX_SIZE = 280.d;

    public void FXMLSidebarController() {
        Estat.setSidebar(this);
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        btn_home.setStyle("-fx-background-color:#78909C;-fx-background-radius:0; -fx-text-fill: #FAFAFA");
        btn_reserva.setStyle("-fx-background-color:transparent;-fx-text-fill:#FAFAFA;");
        btn_historico.setStyle("-fx-background-color:transparent;-fx-text-fill:#FAFAFA;");

        try {
            loadUI("FXMLHome");
        } catch (IOException err) {
        }
    }

    // S'executa cada vegada que es tanca l'escena
    @Override
    public void stop() throws InterruptedException {
        if (this.sub_menu_controller != null) {
            this.sub_menu_controller.stop();
        }

        System.out.println("Sidebar stopped");
    }

    // S'executa cada vegada que es carrega l'escena
    @Override
    public void refresh() {
        System.out.println("Sidebar refreshed");
    }

    @FXML
    private void goHome(MouseEvent event) throws IOException {
        btn_home.setStyle("-fx-background-color:#78909C;-fx-background-radius:0; -fx-text-fill: #FAFAFA");
        btn_reserva.setStyle("-fx-background-color:transparent;-fx-text-fill:#FAFAFA;");
        btn_historico.setStyle("-fx-background-color:transparent;-fx-text-fill:#FAFAFA;");
        loadUI("FXMLHome");
    }

    @FXML
    private void goReserva(MouseEvent event) throws IOException {
        btn_home.setStyle("-fx-background-color:transparent;-fx-text-fill:#FAFAFA;");
        btn_reserva.setStyle("-fx-background-color:#78909C;-fx-background-radius:0; -fx-text-fill: #FAFAFA");
        btn_historico.setStyle("-fx-background-color:transparent;-fx-text-fill:#FAFAFA;");
        loadUI("FXMLPaddleReserva");
    }

    @FXML
    private void goHistorial(MouseEvent event) throws IOException {
        btn_home.setStyle("-fx-background-color:transparent;-fx-text-fill:#FAFAFA;");
        btn_reserva.setStyle("-fx-background-color:transparent;-fx-text-fill:#FAFAFA;");
        btn_historico.setStyle("-fx-background-color:#78909C;-fx-background-radius:0; -fx-text-fill: #FAFAFA");
        loadUI("FXMLHistorico");
    }

    public void loadUI(String ui) throws IOException {
        //Parent root = null;
        //try {
        Parent root = PaddleExperience.getParent(ui + ".fxml");
        this.sub_menu_controller = PaddleExperience.getController(ui + ".fxml");
        //} catch (IOException e) {
        //    Logger.getLogger(FXMLSidebarController.class.getName()).log(Level.SEVERE, null, e);
        //}
        borderpane.setCenter(root);
    }

    @FXML
    public void on_drag_resize(MouseEvent event) {
        double x = event.getSceneX();

        if (x > this.MIN_SIZE && x < this.MAX_SIZE) {
            this.hbox_sidebar.setPrefWidth(x);
        }

    }

}
