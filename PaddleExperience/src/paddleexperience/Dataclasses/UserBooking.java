/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package paddleexperience.Dataclasses;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashMap;
import javafx.event.Event;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.scene.image.Image;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import model.Booking;
import paddleexperience.FXMLHistoricoController;
import paddleexperience.PaddleExperience;
import paddleexperience.Structures.Cache;
/**
 *
 * @author saisua
 */
public class UserBooking {
    
    private TextFlow dia;
    private TextFlow hora_inici;
    private TextFlow hora_fi;
    private TextFlow pista;
    private TextFlow pagada;
    private TextFlow cancelar;
    
    private Booking booking;
    
    private TableView table;
    
    // Auxiliar
    private LocalDateTime datetime;
    
    public UserBooking(Booking data, TableView table){
        this.booking = data;
        this.table = table;
        
        this.datetime = data.getBookingDate();
        
        Button cancel = new Button("Cancelar");
        cancel.setOnMouseClicked((Event event)->{
                try{
                    this.on_click_cancel(event);
                } catch (InterruptedException err) {}
            });
        
        this.dia = new TextFlow();
        this.hora_inici = new TextFlow();
        this.hora_fi = new TextFlow();
        this.pista = new TextFlow();
        this.pagada = new TextFlow();
        this.cancelar = new TextFlow();
        
        this.dia.getChildren().add(new Text(data.getMadeForDay().toString()));
        this.hora_inici.getChildren().add(new Text(data.getFromTime().toString()));
        this.hora_fi.getChildren().add(new Text(data.getFromTime()
                                        .plusMinutes(Estat.partides_duracio).toString()));
        this.pista.getChildren().add(new Text(data.getCourt().getName()));
        this.pagada.getChildren().add(new Text(data.getPaid() ? "Sí" : "No"));
        this.cancelar.getChildren().add(cancel);
    }
    
    public int compareTo(UserBooking a_comparar){
        return this.datetime.compareTo(a_comparar.getDateTime());
    }
    
    public int compareTo(LocalDateTime a_comparar){
        return this.datetime.compareTo(a_comparar);
    }
    
    
    public void on_click_cancel(Event event) throws InterruptedException{
        Estat.club.getBookings().remove(this.booking);
        this.table.getItems().remove(this);
        
        // Açò és literalment per a curar-me en salut
        PaddleExperience.stop("FXMLHistorico.fxml");
        
        FXMLHistoricoController.__proximes_count--;
        
        HashMap<LocalTime, Pair<Image[], Boolean>> day_cache = Cache.cache.get(Estat.getDate());
        Pair<Image[], Boolean> actual_cache = null;
        
        if(day_cache == null)
            Cache.cache.put(Estat.getDate(), new HashMap<LocalTime, Pair<Image[], Boolean>>());
        else 
            actual_cache = day_cache.get(Estat.getTime());
        
        if(actual_cache == null){
            actual_cache = new Pair(Cache.default_court.clone(), true);
            Cache.cache.get(Estat.getDate()).put(Estat.getTime(), actual_cache);
        }
        
        actual_cache.first[Estat.court_index.get(this.booking.getCourt())] = Hora.images.get(1);
    }
    
    // // GETTERS
    public LocalDateTime getDateTime(){
        return this.datetime;
    }
    
    public TextFlow getDia(){
        return this.dia;
    }
    public TextFlow getHoraInici(){
        return this.hora_inici;
    }
    public TextFlow getHoraFi(){
        return this.hora_fi;
    }
    public TextFlow getPista(){
        return this.pista;
    }
    public TextFlow getPagada(){
        return this.pagada;
    }
    public TextFlow getCancelar(){
        return this.cancelar;
    }
    
    
}
