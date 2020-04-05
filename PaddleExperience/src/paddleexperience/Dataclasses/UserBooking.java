/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package paddleexperience.Dataclasses;

import java.time.LocalDateTime;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import model.Booking;
/**
 *
 * @author saisua
 */
public class UserBooking {
    
    private AnchorPane dia;
    private AnchorPane hora_inici;
    private AnchorPane hora_fi;
    private AnchorPane pista;
    private AnchorPane pagada;
    private AnchorPane cancelar;
    
    
    // Auxiliar
    private LocalDateTime datetime;
    
    public UserBooking(Booking data){
        this.datetime = data.getBookingDate();
        
        this.dia = new AnchorPane();
        this.hora_inici = new AnchorPane();
        this.hora_fi = new AnchorPane();
        this.pista = new AnchorPane();
        this.pagada = new AnchorPane();
        this.cancelar = new AnchorPane();
        
        this.dia.getChildren().add(new Text(data.getMadeForDay().toString()));
        this.hora_inici.getChildren().add(new Text(data.getFromTime().toString()));
        this.hora_fi.getChildren().add(new Text(data.getFromTime()
                                        .plusMinutes(Estat.partides_duracio).toString()));
        this.pista.getChildren().add(new Text(data.getCourt().getName()));
        this.pagada.getChildren().add(new Text(data.getPaid() ? "Sí" : "No"));
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
    
    public AnchorPane getDia(){
        return this.dia;
    }
    public AnchorPane getHoraInici(){
        return this.hora_inici;
    }
    public AnchorPane getHoraFi(){
        return this.hora_fi;
    }
    public AnchorPane getPista(){
        return this.pista;
    }
    public AnchorPane getPagada(){
        return this.pagada;
    }
    public AnchorPane getCancelar(){
        return this.cancelar;
    }
    
    
}
