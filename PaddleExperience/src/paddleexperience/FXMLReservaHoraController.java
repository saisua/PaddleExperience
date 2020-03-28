/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package paddleexperience;

// Java imports
import java.net.URL;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ResourceBundle;

// JavaFX imports
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.event.Event;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;

// Java imports
import paddleexperience.Structures.Stoppable;
import paddleexperience.Dataclasses.Estat;

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
    @FXML
    private VBox vbox_Rhora;
    @FXML
    private VBox vbox_Lhora;
    
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        System.out.println("ReservaHora init");
        
        this.refresh();
    }    
    
    @Override
    public void stop() throws InterruptedException {
        System.out.println("ReservaHora stopped");
    }    
    
    @Override
    public void refresh() {
        this.text_hora.setText(Estat.getTime().toString());
        this.text_data.setText(Estat.getDate().toString());
        
        LocalTime to_show = Estat.getBeforeTime();
        if(to_show == null) this.vbox_Lhora.setVisible(false);
        else {
            this.text_Lhora.setText(to_show.toString());
            this.vbox_Lhora.setVisible(true);
        }
        
        to_show = Estat.getNextTime();
        if(to_show == null) this.vbox_Rhora.setVisible(false);
        else {
            this.text_Rhora.setText(to_show.toString());
            this.vbox_Rhora.setVisible(true);
        }

        System.out.println("ReservaHora refreshed");
    }   
    
    public void on_click_nextHora(Event event){    
        event.consume();
        
        Estat.setTime(Estat.getNextTime());
        
        this.refresh();
    }
    
    public void on_click_befHora(Event event){ 
        event.consume();
        
        Estat.setTime(Estat.getBeforeTime());
        
        this.refresh();
    }
    
    
}
