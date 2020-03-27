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
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;

// Internal imports
import paddleexperience.Structures.Stoppable;

/**
 *
 * @author joandzmn
 */
public class PaddleExperience extends Application {
    
    private static final int minHeight = 350;
    private static final int minWidth = 300;
    
    // Loader
    private static final HashMap<String, Parent> root = new HashMap<String, Parent>();
    private static final HashMap<String, Object> controllers = new HashMap<String, Object>();
    private static String back_root, prev_root;

    @Override
    public void start(Stage stage) throws Exception {
        // Initial scene
        back_root = "FXMLPaddleExperience.fxml";
        prev_root = back_root;
        
        
        // Carrega tots els fxml en un diccionari per poder obrir-los més tard
        FXMLLoader loader;
        Parent initial_root = null;
        
        for(File file : new File("./src/paddleexperience/").listFiles())
            if (file.isFile() && file.getName().endsWith(".fxml")){
                loader = new FXMLLoader(getClass().getResource(file.getName()));
                root.put(file.getName(), loader.load());
                controllers.put(file.getName(), loader.getController());
                
                if(file.getName().equals(back_root)) initial_root = root.get(file.getName());
            }
        
        Rectangle2D screenBounds = Screen.getPrimary().getBounds();
        
        stage.setWidth(minWidth);//screenBounds.getWidth());
        stage.setHeight(minHeight);//screenBounds.getHeight());
        
        stage.setMinHeight(minHeight);
        stage.setMinWidth(minWidth);
        
        stage.setTitle("Paddle Experience - International");
        stage.setScene(new Scene(initial_root));
        stage.show();
    }

    @Override
    public void stop() throws InterruptedException{
        if(controllers.get(back_root) == null) return;
        
        Stoppable controller = (Stoppable) controllers.get(back_root);
        
        //System.out.println(controller);
        
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
    public static void setParent(Event event, String sceneName) throws InterruptedException{
        ((Node) event.getSource()).getScene().setRoot(root.get(sceneName));
        
        ((Stoppable) controllers.get(back_root)).stop();
        ((Stoppable) controllers.get(sceneName)).refresh();
        
        prev_root = back_root;
        back_root = sceneName;
    }
    
    // Canvia el root de l'escena a l'anterior root
    // en la finestra on ha ocorregut l'Event event.
    // Esta funció NO ha de ser utilitzada com a manejador 
    // d'events
    public static void back(Event event) throws InterruptedException{
        ((Node) event.getSource()).getScene().setRoot(root.get(prev_root));
        
        ((Stoppable) controllers.get(back_root)).stop();
        ((Stoppable) controllers.get(prev_root)).refresh();
        
        // Swap prev_root, back_root
            String aux = prev_root;
            prev_root = back_root;
            back_root = aux;
    }
    
    // 
    public static Parent getParent(){
        ((Stoppable) controllers.get(back_root)).refresh();
        
        return (Parent) root.get(back_root);
    }
    public static Parent getParent(String sceneName){
        ((Stoppable) controllers.get(sceneName)).refresh();
        
        return (Parent) root.get(sceneName);
    }
}