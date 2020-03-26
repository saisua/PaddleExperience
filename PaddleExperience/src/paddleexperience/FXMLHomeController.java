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
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import model.Booking;

/**
 * FXML Controller class
 *
 * @author joandzmn
 */
public class FXMLHomeController implements Initializable {

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

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        ClubDBAccess clubDBAccess;
        clubDBAccess = ClubDBAccess.getSingletonClubDBAccess();
        ArrayList<Booking> listaReservas = clubDBAccess.getBookings();
        int proximaReserva = proximaReserva(listaReservas);
        int ultimaReserva = ultimaReserva(listaReservas);

        //COMPLETAR
        //Pone el texto de la proxima y la última partida en caso de que haya
        if (proximaReserva >= 0) {
            proxPartida.setText(listaReservas.get(proximaReserva).getMadeForDay().toString());
        }
        if (ultimaReserva >= 0) {
            ultPartida.setText(listaReservas.get(ultimaReserva).getMadeForDay().toString());
        }
    }

    //Calcula cunado será la próxima reserva
    private int proximaReserva(ArrayList<Booking> listaReservas) {
        if (listaReservas.isEmpty()) {
            return -1;
        }
        for (int i = 0; i < listaReservas.size(); i++) {
            if (todayDate.compareTo(listaReservas.get(i).getMadeForDay()) < 0
                    && nowTime.compareTo(listaReservas.get(i).getFromTime()) < 0) {
                return i;
            }
        }
        return -1;
    }

    //Calcula cuándo fue la última partida
    private int ultimaReserva(ArrayList<Booking> listaReservas) {
        if (listaReservas.isEmpty()) {
            return -1;
        }
        for (int i = 0; i < listaReservas.size(); i++) {
            if (todayDate.compareTo(listaReservas.get(i).getMadeForDay()) < 0
                    && nowTime.compareTo(listaReservas.get(i).getFromTime()) < 0) {
                return i - 1;
            }
        }
        return listaReservas.size() - 1;
    }

}
