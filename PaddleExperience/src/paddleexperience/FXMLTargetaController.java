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
import javafx.fxml.FXML;

// JavaFX imports
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import paddleexperience.Dataclasses.Estat;

// Internal imports
import paddleexperience.Structures.Stoppable;

/**
 * FXML Controller class
 *
 * @author saisua
 */
public class FXMLTargetaController implements Initializable, Stoppable {

    @FXML
    private TextField textfield_targeta;
    @FXML
    private TextField textfield_cvc;
    @FXML
    private Button button_aceptar;
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        System.out.println("Targeta initialized");
    }    

    @Override
    public void refresh(){
        this.textfield_targeta.setText("");
        this.textfield_cvc.setText("");
        
        this.button_aceptar.setDisable(true);
        
        System.out.println("Targeta refreshed");
    }
    
    @Override
    public void stop(){
        System.out.println("Targeta stopped");
    }
    
    public void on_click_acceptar(Event event){
        Estat.getMember().setCreditCard(this.textfield_targeta.getText());
        Estat.getMember().setSvc(this.textfield_cvc.getText());
    }
    
}
