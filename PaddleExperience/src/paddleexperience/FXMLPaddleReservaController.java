/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package paddleexperience;

// Java imports
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.Event;

// JavaFX imports
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;

// Interal imports
import paddleexperience.PaddleExperience;
import paddleexperience.Dataclasses.Hora;
import paddleexperience.PaddleExperience;
import paddleexperience.Structures.Stopable;

/**
 * FXML Controller class
 *
 * @author saisua
 */
public class FXMLPaddleReservaController implements Initializable, Stopable {
    // // Scene
    @FXML
    private Button button_enrere;
    @FXML
    private TableView tableview_horari;
    @FXML
    private TableColumn tablecolumn_hora;
    @FXML
    private TableColumn tablecolumn_test;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        this.tablecolumn_hora.setCellValueFactory(new PropertyValueFactory<>("hora"));
        this.tablecolumn_test.setCellValueFactory(new PropertyValueFactory<>("test"));
        
        this.tablecolumn_hora.setStyle("-fx-alignment: CENTER;");
        
        this.tableview_horari.getItems().add(new Hora("19:00"));
    }    
    
    public void stop() throws InterruptedException{
        System.out.println("Reserva Stoped Successfully");
    }
    
    // Manejadors d'events
    public void on_click_enrere(Event event){
        System.out.println("Click enrere!!");
        PaddleExperience.back(event);
    }
    
}