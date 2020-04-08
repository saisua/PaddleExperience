/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package paddleexperience;

// Java imports
import java.io.File;
import java.io.IOException;
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
import paddleexperience.Dataclasses.Estat;
import paddleexperience.Dataclasses.Hora;

// Internal imports
import paddleexperience.Structures.Stoppable;
import paddleexperience.Structures.Cache;

/**
 *
 * @author joandzmn
 */
public class PaddleExperience extends Application {
    
    private static final int minHeight = 350;
    private static final int minWidth = 300;
    
    // Loader
    private static final HashMap<String, Parent> root = new HashMap<String, Parent>();
    private static final HashMap<String, Stoppable> controllers = new HashMap<String, Stoppable>();
    private static String back_root, prev_root;
    
    // Auxiliar
    static Class static_class;

    @Override
    public void start(Stage stage) throws Exception {
        startAll();
        
        // Initial scene
        back_root = "FXMLPaddleExperience.fxml";
        prev_root = back_root;
        
        static_class = getClass();
        
        // Carrega tots els fxml en un diccionari per poder obrir-los més tard
        FXMLLoader loader;
        Parent initial_root = null;
        
        for(File file : new File("./src/paddleexperience/").listFiles())
            if (file.isFile() && file.getName().endsWith(".fxml") 
                    && !root.containsKey(file.getName())){
                
                System.out.println(file.getName());
                
                loader = new FXMLLoader(getClass().getResource(file.getName()));
                root.put(file.getName(), loader.load());
                controllers.put(file.getName(), loader.getController());
                
                if(file.getName().equals(back_root)) {
                    initial_root = root.get(file.getName());
                    
                    controllers.get(file.getName()).refresh();
                }
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
        
        Stoppable controller = controllers.get(back_root);
        
        if(controller == null) return;
        
        controller.stop();
        
        Cache.stop();
        
        System.exit(0);
    }
    
    // Executar static functions abans d'iniciar el programa
    static void startAll(){
        Hora.start();
        Cache.start();
        Estat.start();
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
        
        controllers.get(back_root).stop();
        controllers.get(sceneName).refresh();
        
        prev_root = back_root;
        back_root = sceneName;
    } 
    
    // Canvia el root de l'escena a l'anterior root
    // en la finestra on ha ocorregut l'Event event.
    // Esta funció NO ha de ser utilitzada com a manejador 
    // d'events
    public static void back(Event event) throws InterruptedException{
        ((Node) event.getSource()).getScene().setRoot(root.get(prev_root));
        
        controllers.get(back_root).stop();
        controllers.get(prev_root).refresh();
        
        // Swap prev_root, back_root
            String aux = prev_root;
            prev_root = back_root;
            back_root = aux;
    }
    
    public static void stop(String sceneName) throws InterruptedException{
        controllers.get(sceneName).stop();
    }
    
    public static void refresh(String sceneName){
        controllers.get(sceneName).refresh();
    }
    
    
    // // GETTERS
    
    public static Parent getParent() throws IOException{
        if(controllers.containsKey(back_root))
            controllers.get(back_root).refresh();
        else {
            FXMLLoader loader = new FXMLLoader(static_class.getResource(back_root));
            root.put(back_root, loader.load());
            controllers.put(back_root, loader.getController());
            //controllers.get(back_root).refresh();
        }
        
        return root.get(back_root);
    }
    
    public static Parent getParent(String sceneName) throws IOException{
        if(controllers.containsKey(sceneName))
            controllers.get(sceneName).refresh();
        else {
            FXMLLoader loader = new FXMLLoader(static_class.getResource(sceneName));
            root.put(sceneName, loader.load());
            controllers.put(sceneName, loader.getController());
            //controllers.get(sceneName).refresh();
        }
        
        return root.get(sceneName);
    }
    
    public static Stoppable getController(String sceneName) throws IOException{
        if(controllers.containsKey(sceneName))
            controllers.get(sceneName).refresh();
        else {
            FXMLLoader loader = new FXMLLoader(static_class.getResource(sceneName));
            root.put(sceneName, loader.load());
            controllers.put(sceneName, loader.getController());
            //controllers.get(sceneName).refresh();
        }
        
        return controllers.get(sceneName);
    }
}