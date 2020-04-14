/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package paddleexperience.Dataclasses;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Optional;
import javafx.event.Event;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableView;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.StageStyle;
import model.Booking;
import model.Member;
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
    private LocalDateTime match_start;

    public UserBooking(Booking data, TableView table) {
        this.booking = data;
        this.table = table;

        this.match_start = LocalDateTime.of(data.getMadeForDay(), data.getFromTime());

        Button cancel = new Button("Cancelar");
        cancel.setStyle("-fx-backgound-color:#37474F");
        cancel.setOnMouseClicked((Event event) -> {
            try {
                this.on_click_cancel(event);
            } catch (InterruptedException err) {
            }
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
        if (this.match_start.minusDays(1).compareTo(LocalDateTime.now()) > 0) {
            this.cancelar.getChildren().add(cancel);
        } else {
            Tooltip.install(this.cancelar, new Tooltip("No pots cancelar durant les últimes 24h"));
        }

        this.dia.setStyle("-fx-text-alignment: center;");
        this.hora_inici.setStyle("-fx-text-alignment: center;");
        this.hora_fi.setStyle("-fx-text-alignment: center;");
        this.pista.setStyle("-fx-text-alignment: center;");
        this.pagada.setStyle("-fx-text-alignment: center;");
        this.cancelar.setStyle("-fx-background-color: transparent;"
                + "-fx-text-alignment: center");
    }

    public int compareTo(UserBooking a_comparar) {
        return this.match_start.compareTo(a_comparar.getDateTime());
    }

    public int compareTo(LocalDateTime a_comparar) {
        return this.match_start.compareTo(a_comparar);
    }

    public void on_click_cancel(Event event) throws InterruptedException {
        if (this.match_start.minusDays(1).compareTo(LocalDateTime.now()) < 0) {
            this.cancelar.getChildren().clear();

            Tooltip.install(this.cancelar, new Tooltip("No pots cancelar durant les últimes 24h"));
            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setHeaderText(null);
        alert.setTitle("Confirmació");
        alert.setContentText("Vols cancelar la reserva?");
        alert.initStyle(StageStyle.UTILITY);
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {

            Estat.club.getBookings().remove(this.booking);
            this.table.getItems().remove(this);

            // Açò és literalment per a curar-me en salut
            PaddleExperience.stop("FXMLHistorico.fxml");

            FXMLHistoricoController.__proximes_count--;

            HashMap<LocalTime, Triplet<Image[], Member[], Boolean>> day_cache = Cache.cache.get(this.booking.getMadeForDay());
            Triplet<Image[], Member[], Boolean> actual_cache = null;

            if (day_cache == null) {
                Cache.cache.put(this.booking.getMadeForDay(), new HashMap<LocalTime, Triplet<Image[], Member[], Boolean>>());
            } else {
                actual_cache = day_cache.get(this.booking.getFromTime());
            }

            if (actual_cache == null) {
                actual_cache = new Triplet(Cache.default_court.clone(), new Member[Estat.numero_pistes], true);
                Cache.cache.get(this.booking.getMadeForDay()).put(this.booking.getFromTime(), actual_cache);
            }

            actual_cache.first[Estat.court_index.get(this.booking.getCourt().getName())] = Hora.images.get(1);

            Estat.save();
        }
    }

    // // GETTERS
    public LocalDateTime getDateTime() {
        return this.match_start;
    }

    public TextFlow getDia() {
        return this.dia;
    }

    public TextFlow getHoraInici() {
        return this.hora_inici;
    }

    public TextFlow getHoraFi() {
        return this.hora_fi;
    }

    public TextFlow getPista() {
        return this.pista;
    }

    public TextFlow getPagada() {
        return this.pagada;
    }

    public TextFlow getCancelar() {
        return this.cancelar;
    }

}
