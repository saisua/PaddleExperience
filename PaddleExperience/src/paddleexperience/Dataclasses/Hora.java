/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package paddleexperience.Dataclasses;

// Java imports
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Iterator;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.image.Image;

// JavaFX imports
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.scene.text.TextFlow;
import javafx.scene.image.ImageView;

/**
 *
 * @author saisua
 */
public final class Hora{
    private final HashMap<Integer, String> images = new HashMap<Integer, String>(){{
       // put(0, "/image_pista_lliure")
       // put(1, "/image_pista_ocupada")
       // put(2, "/image_pista_reservada")
    }};
    
    private LocalTime time;
    private String time_str;
    
    private final TextFlow hora = new TextFlow();
    private final TextFlow courts = new TextFlow();
    private final TextFlow reservat = new TextFlow();
    
    // // Auxiliar
    private ObservableList<ImageView> courts_images;
    
    public Hora(LocalTime hora, List<Integer> courts_state){
        this.time = hora;
        this.time_str = hora.toString();
        
        this.hora.getChildren().add(new Text(this.time_str));
        
        Text h = new Text(this.time_str);
        h.setVisible(false);
        
        // Falta plenar this.images
        //this.courts.getChildren().setAll(this.courts_images);
        
        //for(int court : courts_state)
        //    this.courts_images.add(new ImageView(this.images.get(court)));
    }
    
    
    public void updateCourt(int index, int state){
        ((ImageView) this.courts_images.get(index))
                .setImage(new Image(this.images.get(state)));
    }
    
    public void updateCourts(List<Integer> courts_state){
        Iterator states = courts_state.iterator();
        
        for(ImageView court : this.courts_images)
            court.setImage(new Image(this.images.get(states.next())));
    }
    
    public void updateCourts(Integer[] courts_state){
        int index = 0;
        
        for(ImageView court : this.courts_images)
            court.setImage(new Image(this.images.get(courts_state[index++])));
    }
    
    
    // // GETTERS
    
    public TextFlow getHora(){
        return this.hora;
    }
    
    public TextFlow getCourts(){
        return this.courts;
    }
    
    public TextFlow getReservat(){
        return this.reservat;
    }
    
    public LocalTime getTime(){
        return time;
    }
    
    public String getTimeStr(){
        return time_str;
    }
}
