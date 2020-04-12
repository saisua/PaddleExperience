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
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import model.Booking;
import paddleexperience.Dataclasses.Estat;
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

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        if (Estat.getMember() != null) {
            imPerfil.setImage((Image) Estat.getMember().getImage());
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
        ArrayList<Booking> listaReservas = clubDBAccess.getBookings();
        int proximaReserva = proximaReserva(listaReservas);
        int ultimaReserva = ultimaReserva(listaReservas);

        //COMPLETAR
        //Pone el texto de la proxima y la última partida en caso de que haya
        if (proximaReserva >= 0) {
            String dataProx = listaReservas.get(proximaReserva).getMadeForDay().format(DateTimeFormatter.BASIC_ISO_DATE);
            proxPartida.setText(dataProx);
        }
        if (ultimaReserva >= 0) {
            ultPartida.setText(listaReservas.get(ultimaReserva).getMadeForDay().toString());
        }

        if (Estat.getMember() != null) {
            Circle circle = new Circle(73);
            circle.setCenterX(imPerfil.getX() + (imPerfil.getFitWidth() / 2));
            circle.setCenterY(imPerfil.getY() + (imPerfil.getFitHeight() / 2));
            imPerfil.setClip(circle);
            imPerfil.setImage((Image) Estat.getMember().getImage());
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
