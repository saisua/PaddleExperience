/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package paddleexperience;

// Java imports
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.collections.ObservableList;

// JavaFX imports
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.event.Event;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Tooltip;
import javafx.scene.effect.Bloom;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;
import javafx.stage.StageStyle;
import model.Booking;
import model.Court;
import model.Member;

// Java imports
import paddleexperience.Structures.Stoppable;
import paddleexperience.Dataclasses.Estat;
import paddleexperience.Dataclasses.Hora;
import paddleexperience.Structures.Cache;
import paddleexperience.Dataclasses.Triplet;

/**
 * FXML Controller class
 *
 * @author saisua
 */
public class FXMLReservaHoraController implements Initializable, Stoppable {

    // Scene
    @FXML
    private Text text_data;
    @FXML
    private Text text_hora;
    @FXML
    private ImageView imageview_rellotge;
    @FXML
    private ImageView imageview_Lfletxa;
    @FXML
    private Text text_Lhora;
    @FXML
    private GridPane gridpane_imageview_court;
    @FXML
    private ImageView imageview_Rfletxa;
    @FXML
    private Text text_Rhora;
    @FXML
    private VBox vbox_Rhora;
    @FXML
    private VBox vbox_Lhora;
    @FXML
    private VBox vbox_no_login;
    @FXML
    private Text text_reserva;
    @FXML
    private CheckBox checkbox_pagar;
    @FXML
    private Button button_reserva;

    // Auxiliar
    private String hora_actual;
    private boolean te_reserva = false;

    private int selected_index;
    private ImageView selected_image;
    private Image prev_image;

    private boolean reservable = false;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        System.out.println("ReservaHora init");

        this.refresh();
    }

    @Override
    public void stop() throws InterruptedException {
        System.out.println("ReservaHora stopped");
    }

    @Override
    public void refresh() {
        this.hora_actual = Estat.getTime().toString();

        this.text_hora.setText(this.hora_actual);
        this.text_data.setText(Estat.getDate().toString());

        LocalTime to_show = Estat.getBeforeTime();
        if (to_show == null) {
            this.vbox_Lhora.setVisible(false);
        } else {
            this.text_Lhora.setText(to_show.toString());
            this.vbox_Lhora.setVisible(true);
        }

        to_show = Estat.getNextTime();
        if (to_show == null) {
            this.vbox_Rhora.setVisible(false);
        } else {
            this.text_Rhora.setText(to_show.toString());
            this.vbox_Rhora.setVisible(true);
        }

        Hora hora = Estat.hores.get(this.hora_actual);

        //System.out.println(Estat.hores.get(this.hora_actual).getCourtImages().length);
        if (hora.getCourtImages().length > 0) {
            ObservableList<Node> courts = this.gridpane_imageview_court.getChildren();

            for (int court_num = 0; court_num < courts.size(); court_num++) {
                //System.out.println(courts.size());

                ImageView grid_image = ((ImageView) courts.get(court_num));

                grid_image.setImage(((ImageView) hora.getCourtImages()[court_num]).getImage());

                Member ocupat = hora.getMembers()[court_num];
                if (ocupat != null) {
                    Tooltip.install(grid_image, new Tooltip("Pista reservada per " + ocupat.getLogin()));
                }
            }
        }

        if (Estat.getMember() == null) {
            this.vbox_no_login.setVisible(true);
            this.text_reserva.setVisible(false);
            this.button_reserva.setVisible(false);
            
            this.checkbox_pagar.setVisible(false);

            this.reservable = false;
        } else {
            this.vbox_no_login.setVisible(false);
            this.text_reserva.setVisible(true);
            this.button_reserva.setVisible(true);

            this.button_reserva.setDisable(true);

            Estat.setSelectedCourt(null);

            if (LocalDateTime.of(Estat.getDate(), Estat.getTime())
                    .compareTo(LocalDateTime.now()) > 0) {
                this.te_reserva = hora.getTeReserva();
                
                if(te_reserva) {
                    this.text_reserva.setText("Ja tens una hora reservada");
                    
                    this.checkbox_pagar.setVisible(false);
                } else {
                    this.text_reserva.setText("Ninguna pista seleccionada");
                    
                    this.checkbox_pagar.setVisible(!Estat.getMember().getCreditCard().equals(""));
                }

                this.reservable = true;
            } else {
                this.text_reserva.setText("Ja ha passat el temps de reserva");
                
                this.checkbox_pagar.setVisible(false);

                this.reservable = false;
            }

        }

        for (Node court : this.gridpane_imageview_court.getChildren()) {
            ((DropShadow) court.getEffect()).setHeight(0);
            ((DropShadow) court.getEffect()).setWidth(0);
        }

        System.out.println("ReservaHora refreshed");
    }

    public void on_click_nextHora(Event event) {
        event.consume();

        Estat.setTime(Estat.getNextTime());

        this.refresh();
    }

    public void on_click_befHora(Event event) {
        event.consume();

        Estat.setTime(Estat.getBeforeTime());

        this.refresh();
    }

    public void on_click_login(Event event) throws InterruptedException {
        PaddleExperience.setParent(event, "FXMLPaddleLogin.fxml");
    }

    public void on_click_registrar(Event event) throws InterruptedException {
        PaddleExperience.setParent(event, "FXMLRegistro.fxml");
    }

    public void on_hover_enter_pista(Event event) {
        if (!this.reservable || Estat.hores.get(this.hora_actual).getTeReserva() != false
                || ((ImageView) event.getSource()).getImage() != Hora.images.get(0)) {
            return;
        }

        ((DropShadow) ((Node) event.getSource()).getEffect()).setHeight(20);
        ((DropShadow) ((Node) event.getSource()).getEffect()).setWidth(20);
        
        //z((Text) ((VBox) event.getTarget()).getChildren().get(1)).setVisible(true);
    }

    public void on_hover_exit_pista(Event event) {
        ((DropShadow) ((Node) event.getSource()).getEffect()).setHeight(0);
        ((DropShadow) ((Node) event.getSource()).getEffect()).setWidth(0);
        
        //((Text) ((VBox) event.getTarget()).getChildren().get(1)).setVisible(false);
    }
    
    public void on_hover_enter_fletxa(Event event) {
        ((Text) ((VBox) event.getSource()).getChildren().get(1)).setVisible(true);
    }
    
    public void on_hover_exit_fletxa(Event event) {
        ((Text) ((VBox) event.getSource()).getChildren().get(1)).setVisible(false);
    }

    public void on_click_pista(MouseEvent event) {
        if (!this.reservable || this.te_reserva != false) {
            return;
        }

        // Aci ho separe perque en la majoria de casos (El click és valid)
        // puc fer ús de la variable
        ImageView event_source = ((ImageView) event.getSource());

        if (event_source.getImage() != Hora.images.get(0)) {
            return;
        }

        int indexX = 0;
        int indexY = 0;

        // LA PUTA HOSTIA NO EM PUC CREURE QUE AÇO TORNE PUTO
        // NULL SI EL INDEX ES 0 QUE INCONSISTENT ES JAVA HOSTIA
        // per: Ausias
        try {
            indexX = GridPane.getColumnIndex((Node) event.getSource());
        } catch (NullPointerException fuckJava) {;
        }

        try {
            indexY = GridPane.getRowIndex((Node) event.getSource());
        } catch (NullPointerException fuckJava) {;
        }

        this.selected_index = indexX + 2 * indexY;

        Court court = Estat.get_court_by_index(this.selected_index); // 2*y

        Estat.setSelectedCourt(court);
        this.text_reserva.setText(court.getName());

        if (this.selected_image != null) {
            this.selected_image.setImage(this.prev_image);
        }

        this.selected_image = event_source;
        this.prev_image = this.selected_image.getImage();

        this.selected_image.setImage(Hora.images.get(2));

        this.button_reserva.setDisable(false);
    }

    // Esta funció té una carrega computacional prou gran per
    // a ser provocada per un event. Pot ser arrel de problemes
    // de eficiència. El creixement de la funció depén únicament
    // de FXMLPaddleReservaController.refresh()
    public void on_click_reservar(Event _e) {

        // LocalDateTime bookingDate, LocalDate madeForDay, LocalTime fromHour, boolean paid, Court court, Member member
        String europeanDatePattern = "dd-MM-yyyy";
        DateTimeFormatter europeanDateFormatter = DateTimeFormatter.ofPattern(europeanDatePattern);
        String pista = Estat.getSelectedCourt().getName();
        String dia = Estat.getDate().format(europeanDateFormatter);
        String hora = Estat.getTime().toString();

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setHeaderText(null);
        alert.setTitle("Confirmació");
        alert.setContentText("Vols reservar la " + pista + " el dia "
                + dia + " a les " + hora + "?" +
                ((LocalDateTime.of(Estat.getDate(), Estat.getTime()).minusDays(1)
                        .compareTo(LocalDateTime.now()) < 0) 
                ? " No podràs cancelar esta reserva." : ""));
        alert.initStyle(StageStyle.UTILITY);
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {

            Estat.club.getBookings().add(new Booking(LocalDateTime.now(), Estat.getDate(), Estat.getTime(),
                    this.checkbox_pagar.isSelected(), Estat.getSelectedCourt(), Estat.getMember()));

            this.te_reserva = true;

            this.text_reserva.setText("Ja tens una hora reservada");

            Image ocupied_image = Hora.images.get(1);

            this.selected_image.setImage(ocupied_image);

            Hora actual_hora = Estat.hores.get(this.hora_actual);

            actual_hora.getCourtImages()[this.selected_index]
                    .setImage(ocupied_image);
            actual_hora.__setTeReserva(true);

            HashMap<LocalTime, Triplet<Image[], Member[], Boolean>> day_cache = Cache.cache.get(Estat.getDate());
            Triplet<Image[], Member[], Boolean> actual_cache = null;

            if (day_cache == null) {
                Cache.cache.put(Estat.getDate(), new HashMap<LocalTime, Triplet<Image[], Member[], Boolean>>());
            } else {
                actual_cache = day_cache.get(Estat.getTime());
            }

            if (actual_cache == null) {
                actual_cache = new Triplet(Cache.default_court.clone(), new Member[Estat.numero_pistes], true);
                Cache.cache.get(Estat.getDate()).put(Estat.getTime(), actual_cache);
            }

            actual_cache.first[this.selected_index] = ocupied_image;

            this.selected_image = null;
            this.prev_image = null;

            PaddleExperience.refresh("FXMLPaddleReserva.fxml");
            PaddleExperience.refresh("FXMLHome.fxml");
            PaddleExperience.refresh("FXMLHistorico.fxml");

            //Estat.save();
        }
    }

}
