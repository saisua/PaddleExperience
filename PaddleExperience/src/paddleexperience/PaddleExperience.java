/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package paddleexperience;

// Java imports
import java.io.File;
import java.util.HashMap;

// Javafx imports
import javafx.application.Application;
import javafx.event.Event;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

// Internal imports
import paddleexperience.Structures.Stopable;

/**
 *
 * @author joandzmn
 */
public class PaddleExperience extends Application {
    FXMLLoader loader;
    
    private static final HashMap<String, Parent> root = new HashMap<String, Parent>();
    private static String back_root, prev_root;

    @Override
    public void start(Stage stage) throws Exception {
        // Carrega tots els fxml en un diccionari per poder obrir-los més tard
        for(File file : new File("./src/paddleexperience/").listFiles())
            if (file.isFile() && file.getName().endsWith(".fxml")) 
                root.put(file.getName(), FXMLLoader.load(getClass().getResource(file.getName())));
        
        // Initial scene
        back_root = "FXMLPaddleExperience.fxml";
        prev_root = back_root;
        
        this.loader = new FXMLLoader(getClass().getResource(back_root));
        
        stage.setTitle("Paddle Experience - International");
        stage.setScene(new Scene(this.loader.load()));
        stage.show();
    }

    @Override
    public void stop() throws InterruptedException{
        if(this.loader == null) return;
        
        Stopable controller = this.loader.<Stopable>getController();
        
        if(controller == null) return;
        
        controller.stop();
        
        System.exit(0);
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
    // Canvia el root de l'escena en la finestra
    // on ha ocorregut l'Event event. Esta funció
    // NO ha de ser utilitzada com a manejador d'events
    public static void setRoot(Event event, String sceneName){
        ((Node) event.getSource()).getScene().setRoot(root.get(sceneName));
        
        prev_root = back_root;
        back_root = sceneName;
    }
    
    // Canvia el root de l'escena a l'anterior root
    // en la finestra on ha ocorregut l'Event event.
    // Esta funció NO ha de ser utilitzada com a manejador 
    // d'events
    public static void back(Event event){
        ((Node) event.getSource()).getScene().setRoot(root.get(prev_root));
        
        // Swap prev_root, back_root
            String aux = prev_root;
            prev_root = back_root;
            back_root = aux;
    }
}
