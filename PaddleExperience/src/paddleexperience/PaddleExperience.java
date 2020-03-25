/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package paddleexperience;

// Javafx imports
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

// Internal imports
import paddleexperience.Stopable;

/**
 *
 * @author joandzmn
 */
import javafx.fxml.Initializable;
public class PaddleExperience extends Application {
    FXMLLoader loader;
    
    @Override
    public void start(Stage stage) throws Exception {
        this.loader = new FXMLLoader(getClass().getResource("FXMLPaddleExperience.fxml"));
        
        Parent root = this.loader.load();
        
        Scene scene = new Scene(root);
        
        stage.setScene(scene);
        stage.show();
    }

    @Override
    public void stop(){
        Stopable controller = this.loader.<Stopable>getController();
        
        controller.stop();
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
