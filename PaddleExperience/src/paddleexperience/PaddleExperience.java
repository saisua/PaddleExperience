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
import paddleexperience.Stopable;

/**
 *
 * @author joandzmn
 */
public class PaddleExperience extends Application {
    FXMLLoader loader;
    
    private static final HashMap<String, Parent> root = new HashMap<String, Parent>();

    @Override
    public void start(Stage stage) throws Exception {
        // Carrega tots els fxml en un diccionari per poder obrir-los més tard
        for(File file : new File("./src/paddleexperience/").listFiles())
            if (file.isFile() && file.getName().endsWith(".fxml")) 
                root.put(file.getName(), FXMLLoader.load(getClass().getResource(file.getName())));
        
        
        this.loader = new FXMLLoader(getClass().getResource("FXMLPaddleExperience.fxml"));
        
        Parent root = this.loader.load();
        
        Scene scene = new Scene(root);
        
        stage.setTitle("Paddle Experience - International");
        stage.setScene(scene);
        stage.show();
    }

    @Override
    public void stop() throws InterruptedException{
        Stopable controller = this.loader.<Stopable>getController();
        
        controller.stop();
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
    public static void setRoot(Event event, String sceneName){
        ((Node) event.getSource()).getScene().setRoot(root.get(sceneName));
    }
    
}
