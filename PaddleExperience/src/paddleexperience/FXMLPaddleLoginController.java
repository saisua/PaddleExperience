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
import java.lang.Character;

// Scene
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

// Internal imports
import model.Member;
import DBAcess.ClubDBAccess;
import paddleexperience.Structures.Stopable;
import paddleexperience.PaddleExperience;
import paddleexperience.Structures.PasswordChecker;
import paddleexperience.Dataclasses.Estat;

/**
 * FXML Controller class
 *
 * @author saisua
 */
public class FXMLPaddleLoginController implements Initializable, Stopable {
    // // Scene
    @FXML
    private Text text_usuari;
    @FXML
    private TextField textfield_usuari;
    @FXML
    private Text text_contrasena;
    @FXML
    private PasswordField textfield_contrasena;
    @FXML
    private Button button_continua;
    @FXML
    private Text text_error_usuari;
    @FXML
    private Text text_error_contrasena;
    @FXML
    private Text text_requeriments_contrasena;
    
    // Atributs estètics
    private double button_opacity;
    private final String ERROR_BORDER_COLOR = "#FA0000"; // Canviar al css
    private final String ERROR_USUARI = "El usuari no és vàlid";
    private final String ERROR_CONTRASENA = "La contrasenya no és correcta";
    private final String REQUERIMENTS_CONTRASENA = "La contrasenya "
            + "deu tindre mínim "+PasswordChecker.MINIMUM_LETTERS+" lletres"
            + ((PasswordChecker.MINIMUM_NUMBERS > 0) ? ", "+PasswordChecker.MINIMUM_NUMBERS+" dígits" : "")
            + ((PasswordChecker.MINIMUM_UPPER > 0) ? ", "+PasswordChecker.MINIMUM_UPPER+" majúscules" : "")
            + ((PasswordChecker.MINIMUM_SYMBOL > 0) ? ", "+PasswordChecker.MINIMUM_SYMBOL+" símbols "
                    + " ("+PasswordChecker.VALID_SYMBOLS+")": "")
            + ".";
  
    // Atributs auxiliars
    private boolean password_was_good = true;

    /**
     * Initializes the controller class.
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Tests
        Estat.club.getMembers().add(new Member("John","Doe","601234567","aaaa","AA11@@aa","","",null));
        // End tests
        
        this.button_opacity = button_continua.getOpacity();
        
        this.text_error_usuari.setText(ERROR_USUARI);
        this.text_error_contrasena.setText(ERROR_CONTRASENA);
        this.text_requeriments_contrasena.setText(REQUERIMENTS_CONTRASENA);
    }    
    
    public void stop() throws InterruptedException{
        System.out.println("Login Stopped Successfully");
    }
    
    public void refresh(){
        System.out.println("Login Refreshed");
    }
    
    // // Manejadors d'events
    
    @FXML
    public void on_keyTyped_contrasena(KeyEvent event){
        boolean is_good = PasswordChecker.check_password(
                this.textfield_contrasena.getText() + 
                        ((PasswordChecker.is_valid_string(event.getCharacter())) 
                                ? event.getCharacter() : ""
                        )
            );
        
        // Comprove si la contrasenya era bona abans per
        // si en un futur havera de modificar moltes coses,
        // evitar totes les comprovacions
        if(password_was_good != is_good){
            if(is_good){ // La contraseña es valida
                this.text_contrasena.setFill(Color.BLACK);
                this.textfield_contrasena.setStyle("-fx-border-color: #000000;");
                this.text_error_contrasena.setVisible(false);
                
                if(this.textfield_usuari.getText().length() > 0){
                    this.button_continua.setOpacity(1);
                    this.button_continua.setDisable(false);
                }
                
            } else { // La contraseña no es valida
                this.text_contrasena.setFill(Color.RED);
                this.textfield_contrasena.setStyle("-fx-border-color: "+ERROR_BORDER_COLOR+";");
                this.text_error_contrasena.setVisible(true);
                
                this.button_continua.setOpacity(this.button_opacity);
                this.button_continua.setDisable(true);
            }
        }
        
        this.password_was_good = is_good;
    }
    
    @FXML
    public void on_keyTyped_ususari(KeyEvent event){
        if((this.textfield_usuari.getText() + 
                        ((PasswordChecker.is_valid_string(event.getCharacter())) 
                                ? event.getCharacter() : ""
                        ))
                    .length() > 0){
            this.text_usuari.setFill(Color.BLACK);
            this.textfield_usuari.setStyle("-fx-border-color: #000000;");
            this.text_error_usuari.setVisible(false);
            
            if(this.password_was_good && this.textfield_contrasena.getText().length() > 0){
                this.button_continua.setOpacity(1);
                this.button_continua.setDisable(false);
            }
        }
    }
    
    @FXML
    public void on_click_continua(Event _e){
        String pass = this.textfield_contrasena.getText();
        String login = this.textfield_usuari.getText();
        
        //else
        if(Estat.club.getMemberByCredentials(login, pass) != null){
            System.out.println("Member is valid!");
            // Canvia la escena al home
            return;
        }
        else 
            System.out.println("Member is not valid!");
        
        if(!Estat.club.existsLogin(login)) {
            System.out.println("User "+login+" does not exist");
            this.text_usuari.setFill(Color.RED);
            this.textfield_usuari.setStyle("-fx-border-color: "+ERROR_BORDER_COLOR+";");
            this.text_error_usuari.setVisible(true);
        } else {
            System.out.println("Password does not match user "+login);
            this.text_contrasena.setFill(Color.RED);
            this.textfield_contrasena.setStyle("-fx-border-color: "+ERROR_BORDER_COLOR+";");
            this.text_error_contrasena.setVisible(true);
        }
    }
    
    @FXML
    public void on_click_enrere(Event event) throws InterruptedException{
        System.out.println("Clicked back!");
        PaddleExperience.setRoot(event, "FXMLPaddleExperience.fxml");
    }
    
}
