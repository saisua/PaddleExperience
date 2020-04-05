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
import javafx.event.Event;
import javafx.scene.Node;
import javafx.scene.control.TableCell;
import javafx.scene.image.Image;

// JavaFX imports
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.scene.text.TextFlow;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Paint;
import javafx.util.Callback;

/**
 *
 * @author saisua
 */
public final class Hora{
    public static final HashMap<Integer, Image> images = new HashMap<Integer, Image>(){{
       put(0, new Image("file:src/paddleexperience/Images/pistaGris.png"));
       put(1, new Image("file:src/paddleexperience/Images/pistaRoig.png"));
       put(2, new Image("file:src/paddleexperience/Images/pistaVerd.png"));
    }};
    
    private LocalTime time;
    private String time_str;
    
    private final TextFlow hora = new TextFlow();
    private final TextFlow courts = new TextFlow();
    private final TextFlow reservat = new TextFlow();
    
    // Estètic
    private static final Background background_selected = new Background(new BackgroundFill(Paint.valueOf(
                "#388E3C"), new CornerRadii(1), null));
    private static final Background background_not_selected = new Background(new BackgroundFill(Paint.valueOf(
                "#37474F"), new CornerRadii(1), null));
    
    // // Auxiliar
    private ImageView courts_images[] = new ImageView[Estat.numero_pistes];
    
    public Hora(LocalTime hora, List<Integer> courts_state){
        this.time = hora;
        this.time_str = hora.toString();
        
        Text text_hora = new Text(this.time_str);
        
        // #388E3C
        
        this.hora.getChildren().add(text_hora);
        
        Text h = new Text(this.time_str);
        h.setVisible(false); 
        
        for(int court_num = 0; court_num < courts_state.size(); court_num++)
            this.courts_images[court_num] = 
                    new ImageView(this.images.get(courts_state.get(court_num)));

        this.courts.getChildren().addAll(this.courts_images);
        
        // Definició de comportaments
        text_hora.setStyle("-fx-fill:  #FAFAFA;"
                + "-fx-font-size: 20;");
        this.hora.setStyle("-fx-text-alignment: center;");
                
        
        this.hora.onMouseEnteredProperty().set((Event ev) -> this.on_hover_enter(ev));
        this.hora.onMouseExitedProperty().set((Event ev) -> this.on_hover_exit(ev));
        this.courts.onMouseEnteredProperty().set((Event ev) -> this.on_hover_enter(ev));
        this.courts.onMouseExitedProperty().set((Event ev) -> this.on_hover_exit(ev));
        this.reservat.onMouseEnteredProperty().set((Event ev) -> this.on_hover_enter(ev));
        this.reservat.onMouseExitedProperty().set((Event ev) -> this.on_hover_exit(ev));
    }
    
    public int compareTo(Hora a_comparar){
        return this.time.compareTo(a_comparar.getTime());
    }
    
    
    public void updateCourt(int index, int state){
        this.courts_images[index].setImage(this.images.get(state));
    }
    
    public void updateCourts(List<Integer> courts_state){
        Iterator states = courts_state.iterator();
        
        for(Node court : this.courts_images)
            ((ImageView) court).setImage(this.images.get(states.next()));
    }
    
    public void updateCourts(Integer[] courts_state){
        int index = 0;
        
        for(ImageView court : this.courts_images)
            court.setImage(this.images.get(courts_state[index++]));
    }
    
    public void updateCourtsImages(List<Image> courts_state){
        Iterator states = courts_state.iterator();
        
        for(ImageView court : this.courts_images)
            court.setImage((Image) states.next());
    }
    
    public void updateCourtsImages(Image[] courts_state){
        int index = 0;
        
        for(ImageView court : this.courts_images)
            court.setImage(courts_state[index++]);
    }
    
    
    public void on_hover_enter(Event event){
        this.hora.setBackground(background_selected);
        this.courts.setBackground(background_selected);
        this.reservat.setBackground(background_selected);
    }
    
    public void on_hover_exit(Event event){
        this.hora.setBackground(background_not_selected);
        this.courts.setBackground(background_not_selected);
        this.reservat.setBackground(background_not_selected);
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
    
    public ImageView[] getCourtImages(){
        return this.courts_images;
    }
}
