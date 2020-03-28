/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package paddleexperience;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;
import paddleexperience.Structures.Stoppable;

/**
 * FXML Controller class
 *
 * @author joandzmn
 */
public class FXMLRegistroController implements Initializable, Stoppable {

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        System.out.println("Registro init");
    }

    // S'executa cada vegada que es tanca l'escena
    @Override
    public void stop() throws InterruptedException{
        System.out.println("Registro stopped");
    }
    
    // S'executa cada vegada que es carrega l'escena
    @Override
    public void refresh(){
        System.out.println("Registro refreshed");
    }
    
}
