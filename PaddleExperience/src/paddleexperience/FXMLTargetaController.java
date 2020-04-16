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
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
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
    private TextField textfield_svc;
    @FXML
    private Button button_aceptar;
    @FXML
    private Button button_cancelar;

    boolean isSvc, isTargeta;
    
    private Stage window;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        System.out.println("Targeta initialized");
    }

    @Override
    public void refresh() {
        if(this.window == null)
            this.window = PaddleExperience.last_window;
        
        this.textfield_targeta.setText("");
        this.textfield_svc.setText("");

        this.button_aceptar.setDisable(true);

        System.out.println("Targeta refreshed");
    }

    @Override
    public void stop() {
        this.window = null;
        
        System.out.println("Targeta stopped");
    }

    @FXML
    public void on_click_acceptar(Event event) {
        Estat.getMember().setCreditCard(this.textfield_targeta.getText());
        Estat.getMember().setSvc(this.textfield_svc.getText());
        
        PaddleExperience.refresh("FXMLHistorico.fxml");
        
        this.on_click_cancelar(event);
    }

    @FXML
    private void on_click_cancelar(Event event) {
        this.window.close();
        
        this.stop();
    }

    @FXML
    private void on_click_svc(KeyEvent event) {
        String lastTyped = event.getCharacter();
        int numOf = this.textfield_svc.getText().length();
        System.out.println(numOf);
        if (!Character.isDigit(lastTyped.charAt(0))
                || numOf > 2) {
            event.consume();
        }
        isSvc = numOf == 3;

        isGood();
    }

    @FXML
    private void on_click_targeta(KeyEvent event) {
        String lastTyped = event.getCharacter();
        int numOf = this.textfield_targeta.getText().length();
        if (!Character.isDigit(lastTyped.charAt(0))
                || numOf > 15) {
            event.consume();
        }

        isTargeta = numOf == 16;

        isGood();
    }

    private void isGood() {
        this.button_aceptar.setDisable(!(isSvc && isTargeta));
    }

}
