/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package paddleexperience.Dataclasses;

// Java imports
import static java.lang.Thread.sleep;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Iterator;

// JavaFX imports
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.scene.text.TextFlow;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Paint;
import javafx.util.Callback;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.TableCell;
import javafx.scene.image.Image;
import model.Member;

// Internal imports
import paddleexperience.Dataclasses.Triplet;

/**
 *
 * @author saisua
 */
public final class Hora{
    public static final HashMap<Integer, Image> images = new HashMap<Integer, Image>(){{
       put(0, new Image("file:src/paddleexperience/Images/PistaGris.png")); // lliure
       put(1, new Image("file:src/paddleexperience/Images/PistaRoig.png")); // ocupada
       put(2, new Image("file:src/paddleexperience/Images/PistaVerd.png")); // seleccionada
    }};
    
    private LocalTime time;
    private String time_str;
    
    private final TextFlow hora = new TextFlow();
    private final HBox courts = new HBox();
    private final TextFlow reservat = new TextFlow();
    
    // Estètic
    private static final Background background_selected_enabled = new Background(new BackgroundFill(Paint.valueOf(
                "#388E3C"), new CornerRadii(1), null));
    private static final Background background_selected_disabled = new Background(new BackgroundFill(Paint.valueOf(
                "#DD2C00"), new CornerRadii(1), null));
    private static final Background background_not_selected = new Background(new BackgroundFill(Paint.valueOf(
                "#37474F"), new CornerRadii(1), null));
    
    
    // // Auxiliar
    private ImageView courts_images[] = new ImageView[Estat.numero_pistes];
    private boolean te_reserva = false;
    private Member members[] = new Member[Estat.numero_pistes];
    
    private boolean enabled;
    
    public Hora(LocalTime hora, List<Integer> courts_state){
        this.enabled = true;
        
        this.time = hora;
        this.time_str = hora.toString();
        
        Text text_hora = new Text(this.time_str);
        text_hora.setTranslateY(50.d);
        
        // #388E3C
        
        this.hora.getChildren().add(text_hora);
        
        for(int court_num = 0; court_num < courts_state.size(); court_num++){
            ImageView image = new ImageView(this.images.get(courts_state.get(court_num)));
            
            image.rotateProperty().set(90);
            image.setFitHeight(50);
            
            image.setPreserveRatio(true);
            
            //image.setTranslateY(25.d);
            
            //System.out.println(image.isResizable());
            // false
            
            this.courts_images[court_num] = image;
        }
        
        //hbox_images = new HBox();
        
        //hbox_images.getChildren().addAll(this.courts_images);
        
        this.courts.getChildren().addAll(this.courts_images);
        
        Text text_reserva = new Text("No");
        text_reserva.setTranslateY(50.d);
        
        this.reservat.getChildren().add(text_reserva);
        
        // Definició de comportaments
        text_hora.setStyle("-fx-fill:  #FAFAFA;"
                + "-fx-font-size: 20;");
        this.hora.setStyle("-fx-text-alignment: center;");
        this.courts.setStyle("-fx-alignment: center;");

        this.courts.setBackground(background_not_selected);
        
        this.hora.onMouseEnteredProperty().set((Event ev) -> this.on_hover_enter(ev));
        this.hora.onMouseExitedProperty().set((Event ev) -> this.on_hover_exit(ev));
        this.courts.onMouseEnteredProperty().set((Event ev) -> this.on_hover_enter(ev));
        this.courts.onMouseExitedProperty().set((Event ev) -> this.on_hover_exit(ev));
        this.reservat.onMouseEnteredProperty().set((Event ev) -> this.on_hover_enter(ev));
        this.reservat.onMouseExitedProperty().set((Event ev) -> this.on_hover_exit(ev));
    }
    
    public void disable(){
        this.enabled = false;
    }
    
    public void enable(){
        this.enabled = true;
    }
    
    public int compareTo(Hora a_comparar){
        return this.time.compareTo(a_comparar.getTime());
    }
    
    public int compareTo(LocalTime a_comparar){
        return this.time.compareTo(a_comparar);
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
    
    public void updateCourtsTriplet(Triplet<Image[], Member[], Boolean> courts_state){
        this.te_reserva = courts_state.third;
        ((Text) this.reservat.getChildren().get(0)).setText((courts_state.third) ? "Si" : "No");
        
        this.members = courts_state.second;
        
        this.updateCourtsImages(courts_state.first);
    }
    
    
    public void on_hover_enter(Event event){
        if(this.enabled){
            this.hora.setBackground(background_selected_enabled);
            this.courts.setBackground(background_selected_enabled);
            this.reservat.setBackground(background_selected_enabled);
        } else {
            this.hora.setBackground(background_selected_disabled);
            this.courts.setBackground(background_selected_disabled);
            this.reservat.setBackground(background_selected_disabled);
        }
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
    
    public HBox getCourts(){
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
    
    public boolean getTeReserva(){
        return this.te_reserva;
    }
    
    public Member[] getMembers(){
        return this.members;
    }
    
    // // SETTERS
    public void __setTeReserva(boolean new_reserva){
        this.te_reserva = new_reserva;
    }
}
