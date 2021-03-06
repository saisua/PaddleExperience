/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package paddleexperience.Dataclasses;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
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

    private int pagada_state;
    private int cancelar_state;

    public UserBooking(Booking data, TableView table) {
        this.booking = data;
        this.table = table;

        this.match_start = LocalDateTime.of(data.getMadeForDay(), data.getFromTime());

        Button button_cancel = new Button("Cancel·lar");
        button_cancel.setStyle("-fx-text-fill: #FAFAFA;");

        button_cancel.setOnMouseClicked((Event event) -> {
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

        String europeanDatePattern = "dd-MM-yyyy";
        DateTimeFormatter europeanDateFormatter = DateTimeFormatter.ofPattern(europeanDatePattern);

        this.dia.getChildren().add(new Text(data.getMadeForDay().format(europeanDateFormatter)));
        this.hora_inici.getChildren().add(new Text(data.getFromTime().toString()));
        this.hora_fi.getChildren().add(new Text(data.getFromTime()
                .plusMinutes(Estat.partides_duracio).toString()));
        this.pista.getChildren().add(new Text(data.getCourt().getName()));

        System.out.println(Estat.getMember().getCreditCard());

        if (data.getPaid()) {
            this.pagada.getChildren().add(new Text("Sí"));

            this.pagada_state = 0;

        } else if (Estat.getMember().getCreditCard().equals("")) {
            this.pagada.getChildren().add(new Text("No"));
        } else {
            Button per_a_pagar = new Button("Pagar");
            per_a_pagar.setStyle("-fx-background-color: #A59979; -fx-fill: #A59979");
            per_a_pagar.setOnMouseClicked((Event event) -> this.on_click_pay(event));

            this.pagada.getChildren().add(per_a_pagar);
            this.pagada.getChildren().get(0).setStyle("-fx-text-fill: #A58279; -fx-fill: #A58279");

            this.pagada_state = 1;
        }

        if (this.match_start.minusDays(1).compareTo(LocalDateTime.now()) > 0) {
            this.cancelar.getChildren().add(button_cancel);
        } else {
            Tooltip.install(this.cancelar, new Tooltip("No pots cancel·lar durant les últimes 24h"));

            this.cancelar_state = 1;
        }

        this.dia.setStyle("-fx-text-alignment: center;");
        this.dia.getChildren().get(0).setStyle("-fx-fill: #fafafa");
        this.hora_inici.setStyle("-fx-text-alignment: center;");
        this.hora_inici.getChildren().get(0).setStyle("-fx-fill: #fafafa");
        this.hora_fi.setStyle("-fx-text-alignment: center;");
        this.hora_fi.getChildren().get(0).setStyle("-fx-fill: #fafafa");
        this.pista.setStyle("-fx-text-alignment: center;");
        this.pista.getChildren().get(0).setStyle("-fx-fill: #fafafa");
        this.pagada.setStyle("-fx-text-alignment: center;");
        this.pagada.getChildren().get(0).setStyle("-fx-fill: #fafafa");
        this.cancelar.setStyle("-fx-background-color: transparent;"
                + "-fx-text-alignment: center;");
    }

    public void refresh() {
        if (this.booking.getPaid()) {
            if (this.pagada_state != 0) {
                this.pagada.getChildren().clear();
                this.pagada.getChildren().add(new Text("Sí"));
            }
        } else if (this.pagada_state != 1 && !Estat.getMember().getCreditCard().equals("")) {
            this.pagada.getChildren().clear();

            Button per_a_pagar = new Button("Pagar");
            per_a_pagar.setOnMouseClicked((Event event) -> this.on_click_pay(event));

            this.pagada.getChildren().add(per_a_pagar);
        }

        if (!(this.match_start.minusDays(1).compareTo(LocalDateTime.now()) > 0)
                && this.cancelar_state != 1) {
            this.cancelar.getChildren().clear();

            Tooltip.install(this.cancelar, new Tooltip("No pots cancel·lar durant les últimes 24h"));
        }
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

            Tooltip.install(this.cancelar, new Tooltip("No pots cancel·lar durant les últimes 24h"));
            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setHeaderText(null);
        alert.setTitle("Confirmació de cancel·lació");
        alert.setContentText("Segur que vols cancel·lar la reserva?");
        ((Button) alert.getDialogPane().lookupButton(ButtonType.OK)).setText("Sí");
        ((Button) alert.getDialogPane().lookupButton(ButtonType.CANCEL)).setText("No");

        alert.initStyle(StageStyle.UTILITY);
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {

            Estat.club.getBookings().remove(this.booking);
            this.table.getItems().remove(this);

            // Açò és literalment per a curar-me en salut
            PaddleExperience.stop("FXMLHistorico.fxml");
            PaddleExperience.refresh("FXMLHome.fxml");
            PaddleExperience.refresh("FXMLPaddleReserva.fxml");
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
        }
    }

    public void on_click_pay(Event event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setHeaderText(null);
        alert.setTitle("Confirmació de pagagment");
        alert.setContentText("Vols pagar la reserva amb la targeta?");
        alert.initStyle(StageStyle.UTILITY);
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {
            this.pagada.getChildren().clear();
            this.pagada.getChildren().add(new Text("Sí"));

            booking.setPaid(true);
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
