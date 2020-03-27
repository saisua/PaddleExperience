/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package paddleexperience;

// Java imports
import java.net.URL;
import java.util.ResourceBundle;

// JavaFX imports
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;

// Java imports
import paddleexperience.Structures.Stoppable;

/**
 * FXML Controller class
 *
 * @author saisua
 */
public class FXMLReservaHoraController implements Initializable, Stoppable {

    // Scene
    @FXML
    private Text text_data;
    @FXML
    private Text text_hora;
    @FXML
    private ImageView imageview_rellotge;
    @FXML
    private ImageView imageview_Lfletxa;
    @FXML
    private Text text_Lhora;
    @FXML
    private GridPane gridpane_imageview_court;
    @FXML
    private ImageView imageview_Rfletxa;
    @FXML
    private Text text_Rhora;
    
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        System.out.println("ReservaHora init");
    }    
    
    @Override
    public void stop() throws InterruptedException {
        System.out.println("ReservaHora stopped");
    }    
    
    @Override
    public void refresh() {
        System.out.println("ReservaHora refreshed");
    }   
}
