/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package paddleexperience.Dataclasses;

import java.time.LocalDateTime;
import javafx.scene.control.Button;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import model.Booking;
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
    
    
    // Auxiliar
    private LocalDateTime datetime;
    
    public UserBooking(Booking data){
        this.datetime = data.getBookingDate();
        
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
        this.cancelar.getChildren().add(new Button("Cancelar"));
    }
    
    public int compareTo(UserBooking a_comparar){
        return this.datetime.compareTo(a_comparar.getDateTime());
    }
    
    public int compareTo(LocalDateTime a_comparar){
        return this.datetime.compareTo(a_comparar);
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
