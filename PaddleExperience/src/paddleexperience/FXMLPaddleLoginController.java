/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package paddleexperience;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

// Scene
import javafx.scene.control.Button;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

// Internal imports
import paddleexperience.PasswordChecker;

/**
 * FXML Controller class
 *
 * @author saisua
 */
public class FXMLPaddleLoginController implements Initializable {
    // // Scene
    @FXML
    private Text text_usuari;
    @FXML
    private Text textfield_usuari;
    @FXML
    private Text text_contrasena;
    @FXML
    private Text textfield_contrasena;
    @FXML
    private Button button_continua;
    
    // Atributs estètics
    private double button_opacity = button_continua.getOpacity();
  
    // Atributs auxiliars
    private boolean password_was_good = false;

    /**
     * Initializes the controller class.
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        System.out.println(PasswordChecker.check_password("c00RRectpa$$"));
    }    
    
    public void on_keyreleased_contrasena(Event _e){
        boolean is_good = PasswordChecker.check_password(this.textfield_contrasena.getText());
        
        // Comprove si la contrasenya era bona abans per
        // si en un futur havera de modificar moltes coses,
        // evitar totes les comprovacions
        if(password_was_good != is_good){
            if(is_good){ // La contraseña es valida
                this.text_contrasena.setFill(Color.BLACK);
                this.textfield_contrasena.setStroke(Color.BLACK);
                
                this.button_continua.setOpacity(1);
                if(this.textfield_usuari.getText().length() > 0)
                    this.button_continua.setDisable(false);
                
            } else { // La contraseña no es valida
                this.text_contrasena.setFill(Color.RED);
                this.textfield_contrasena.setStroke(Color.RED);
                
                this.button_continua.setOpacity(this.button_opacity);
                this.button_continua.setDisable(true);
            }
        }
        
        this.password_was_good = is_good;
    }
    
    public void on_click_continua(Event _e){
        // Accedix a la base de dades, comprova veracitat usuari i canvia escena
    }
    
}
