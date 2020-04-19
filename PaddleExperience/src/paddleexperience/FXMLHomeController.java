/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package paddleexperience;

import DBAcess.ClubDBAccess;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.util.Pair;
import model.Booking;
import paddleexperience.Dataclasses.Estat;
import paddleexperience.Dataclasses.Triplet;
import paddleexperience.Structures.Stoppable;

/**
 * FXML Controller class
 *
 * @author joandzmn
 */
public class FXMLHomeController implements Initializable, Stoppable {

    @FXML
    private ImageView imPerfil;
    @FXML
    private Text proxPartida;
    @FXML
    private Text ultPartida;

    /**
     * Initializes the controller class.
     */
    LocalDate todayDate = LocalDate.now();
    LocalTime nowTime = LocalTime.now();
    @FXML
    private Text text_Benvinguda;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        if (Estat.getMember() != null) {
            Circle circle = new Circle(73);
            circle.setCenterX(imPerfil.getX() + (imPerfil.getFitWidth() / 2));
            circle.setCenterY(imPerfil.getY() + (imPerfil.getFitHeight() / 2));
            imPerfil.setClip(circle);
        }
    }

    // S'executa cada vegada que es tanca l'escena
    @Override
    public void stop() throws InterruptedException {
        System.out.println("Home closed");
    }

    // S'executa cada vegada que es carrega l'escena
    @Override
    public void refresh() {
        ClubDBAccess clubDBAccess;
        clubDBAccess = Estat.club;
        if (Estat.getMember() != null) {
            todayDate = LocalDate.now();
            nowTime = LocalTime.now();
            
            Booking[] ant_prox = reserves_ant_prox(Estat.club.getUserBookings(Estat.getMemberLogin()));
            
            Booking reserva_anterior = ant_prox[0];
            Booking reserva_seguent = ant_prox[1];

            this.text_Benvinguda.setText("Hola " + Estat.getMember().getName() + "!");
            //Pone el texto de la proxima y la última partida en caso de que haya
            String europeanDatePattern = "dd-MM-yyyy";
            DateTimeFormatter europeanDateFormatter = DateTimeFormatter.ofPattern(europeanDatePattern);

            if (reserva_seguent != null) {
                String dataProx = reserva_seguent.getMadeForDay().format(europeanDateFormatter);
                String pistaProx = reserva_seguent.getCourt().getName();
                String horaProx = reserva_seguent.getFromTime().toString();
                proxPartida.setText("El dia " + dataProx + " a les " + horaProx + " en la " + pistaProx);
            } else {
                proxPartida.setText("No tens partides pròximament");
            }
            if (reserva_anterior != null) {
                String dataUlt = reserva_anterior.getMadeForDay().format(europeanDateFormatter);
                String pistaUlt = reserva_anterior.getCourt().getName();
                String horaUlt = reserva_anterior.getFromTime().toString();
                ultPartida.setText("El dia " + dataUlt + " a les " + horaUlt + " en la " + pistaUlt);
            } else {
                ultPartida.setText("No has jugat encara");
            }

            //if (Estat.getMember() != null) {
            imPerfil.setImage((Image) Estat.getMember().getImage());
            //}
        }
    }
    
    private Booking[] reserves_ant_prox(ArrayList<Booking> listaReservas){
        Booking next = null;
        Booking last = null;
        
        for(Iterator<Booking> iter = listaReservas.iterator(); iter.hasNext();){
            Booking reserva = iter.next();
            
            int day_compare = todayDate.compareTo(reserva.getMadeForDay());
            // Si ja ha passat el dia
            if(day_compare > 0) {
                if(last == null || last.compareTo(reserva) < 0)
                    last = reserva;
            // Si encara no ha passat el dia
            } else if(day_compare < 0){
                if(next == null || next.compareTo(reserva) > 0)
                    next = reserva;
            // Si és hui
            } else {                
                int time_compare = nowTime.compareTo(reserva.getFromTime());
                
                // Si ja ha passat l'hora
                if(time_compare >= 0){
                    if(last == null || last.compareTo(reserva) < 0)
                        last = reserva;
                
                // Si encara no ha passat l'hora
                } else 
                    if(next == null || next.compareTo(reserva) > 0)
                        next = reserva;
            } 
        }
        
        return new Booking[]{last, next};
    }

}
