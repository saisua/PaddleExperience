/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package paddleexperience;

// Java imports
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.time.temporal.ChronoField;
import java.util.Iterator;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;
import java.util.TreeSet;
import javafx.collections.FXCollections;

// JavaFX imports
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.effect.Effect;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.event.Event;
import javafx.scene.control.TableCell;
import javafx.scene.image.Image;
import javafx.collections.ObservableList;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ScrollBar;
import javafx.scene.control.TableColumn.SortType;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Paint;
import javafx.util.Callback;
import paddleexperience.Dataclasses.Triplet;

// Interal imports
import paddleexperience.PaddleExperience;
import paddleexperience.Dataclasses.Hora;
import paddleexperience.Structures.Stoppable;
import paddleexperience.Structures.Cache;
import model.Booking;
import model.Court;
import model.Member;
import paddleexperience.Dataclasses.Estat;

/**
 * FXML Controller class
 *
 * @author saisua
 */
public class FXMLPaddleReservaController implements Initializable, Stoppable {

    // // Scene
    @FXML
    private Text text_data;
    @FXML
    private BorderPane borderpane_main;
    @FXML
    private BorderPane borderpane_overlay;
    @FXML
    private Button button_enrere;
    @FXML
    private TableView tableview_horari;
    @FXML
    private TableColumn tablecolumn_hora;
    @FXML
    private TableColumn tablecolumn_courts;
    @FXML
    private TableColumn tablecolumn_reservat;
    private ScrollBar scrollbar;

    // Auxiliar
    private boolean menu_hora = false;

    // Testing
    public static HashMap<LocalDate, ArrayList<Booking>> test_booking = new HashMap<LocalDate, ArrayList<Booking>>() {
    };
    @FXML
    private GridPane gridpane_OverBlur;
    @FXML
    private DatePicker date_Picker;

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        System.out.println("Initializing reserva...");

        this.tablecolumn_hora.setCellValueFactory(new PropertyValueFactory<>("hora"));
        this.tablecolumn_courts.setCellValueFactory(new PropertyValueFactory<>("courts"));
        this.tablecolumn_reservat.setCellValueFactory(new PropertyValueFactory<>("reservat"));

        for (Hora hora : Estat.hores.values()) {
            this.tableview_horari.getItems().add(hora);
        }

        this.tableview_horari.setSortPolicy(table_view -> {
            Comparator<Hora> comparator = (hora1, hora2)
                    -> this.tablecolumn_hora.getSortType() == SortType.ASCENDING
                    ? hora1.compareTo(hora2) : hora2.compareTo(hora1); //columns are sorted: sort accordingly
            FXCollections.sort(this.tableview_horari.getItems(), comparator);
            return true;
        });;

        this.borderpane_main.setEffect(new GaussianBlur(0));

        //this.tableview_horari.getStylesheets().add("file:src/paddleexperience/CSS/PaddleReserva.css");
        //this.tableview_horari.applyCss();
        this.scrollbar = ((ScrollBar) this.tableview_horari.lookup(".scroll-bar:vertical"));

        try {
            this.borderpane_overlay.setCenter(PaddleExperience.getParent("FXMLReservaHora.fxml"));
            this.borderpane_overlay.setVisible(false);
        } catch (IOException err) {
        }

        //Posa dia i deshabilita dies anteriors a l'actual
        date_Picker.setValue(LocalDate.now());
        date_Picker.setDayCellFactory((DatePicker picker) -> {
            return new DateCell() {
                @Override
                public void updateItem(LocalDate date, boolean empty) {
                    super.updateItem(date, empty);
                    LocalDate today = LocalDate.now();
                    setDisable(empty || date.compareTo(today) < 0);
                }
            };
        });
        date_Picker.valueProperty().addListener((ov, oldVal, newVal) -> {
            Estat.setDate(newVal);
            this.refresh();
        });

        System.out.println("Reserva initialized");
    }

    @Override
    public void stop() throws InterruptedException {
        System.out.println("Reserva Stoped Successfully");
    }

    // Esta funció és xunga, així que vaig a comentar-la prou
    @Override
    public void refresh() {
        // Si el usuari ha fet login, amaga el botó de tornar enrere,
        // pues ha de gastar el menú
        if (Estat.getMember() != null) {
            this.button_enrere.setVisible(false);
        }

        //this.text_data.setText(Estat.getDate().toString());
        HashMap<LocalTime, Triplet<Image[], Member[], Boolean>> dia = Cache.get();

        int is_past_day = Estat.getDate().compareTo(LocalDate.now());

        // Fem un recorregut per totes les hores, modificant els arxius de
        // Hora hora per els carregats en courts (n^2)
        for (Hora hora : Estat.hores.values()) {
            //System.out.println("Hora "+hora.getTimeStr());

            hora.updateCourtsTriplet(dia.getOrDefault(hora.getTime(),
                    new Triplet(Cache.default_court.clone(), new Member[Estat.numero_pistes], false)));

            // Activem o desactivem les hores corresponents
            // En base a si ja han passat o no
            if (is_past_day > 0) {
                hora.enable();
            } else if (is_past_day < 0) {
                hora.disable();
            } else if (hora.compareTo(LocalTime.now()) < 0) {
                hora.disable();
            }
        }

        if (this.scrollbar == null) {
            this.scrollbar = ((ScrollBar) this.tableview_horari.lookup(".scroll-bar:vertical"));
        } else {
            this.scrollbar.setValue(0);
        }

        // Desactivar overlay si està actiu
        if (this.menu_hora) {
            this.borderpane_main.setDisable(false);

            ((GaussianBlur) this.borderpane_main.getEffect()).setRadius(0);

            this.gridpane_OverBlur.setDisable(true);

            this.borderpane_overlay.setVisible(false);

            this.menu_hora = false;
        }

        System.out.println("Reserva Refreshed");
    }

    // Manejadors d'events
    @FXML
    public void on_click_enrere(Event event) throws InterruptedException {
        System.out.println("Click enrere!!");
        PaddleExperience.back(event);
    }

    @FXML
    public void on_click_horari(Event event) {
        if (TextFlow.class.isInstance(event.getTarget())) {
            Estat.setTime(Estat.time_inici.plusMinutes(Estat.partides_duracio * ((TableCell) ((TextFlow) event.getTarget()).getParent()).getTableRow().getIndex()));

        } else if (ImageView.class.isInstance(event.getTarget())) {
            Estat.setTime(Estat.time_inici.plusMinutes(Estat.partides_duracio * ((TableCell) ((TextFlow) ((HBox) ((ImageView) event.getTarget()).getParent()).getParent()).getParent()).getTableRow().getIndex()));

        } else if (HBox.class.isInstance(event.getTarget())) {
            Estat.setTime(Estat.time_inici.plusMinutes(Estat.partides_duracio * ((TableCell) ((TextFlow) ((HBox) event.getTarget()).getParent()).getParent()).getTableRow().getIndex()));

        } else {
            return;
        }

        this.borderpane_main.setDisable(true);

        ((GaussianBlur) this.borderpane_main.getEffect()).setRadius(10);

        this.gridpane_OverBlur.setDisable(false);

        this.borderpane_overlay.setVisible(true);

        this.menu_hora = true;

        System.out.println(Estat.getTime().toString());

        PaddleExperience.refresh("FXMLReservaHora.fxml");
    }

    @FXML
    public void on_keypress_exit(KeyEvent event) throws InterruptedException {
        if (this.menu_hora && event.getCode().equals(KeyCode.ESCAPE)) {
            this.borderpane_overlay.setVisible(false);

            ((GaussianBlur) this.borderpane_main.getEffect()).setRadius(0);

            this.borderpane_main.setDisable(false);
            this.gridpane_OverBlur.setDisable(true);
        }
    }

    @FXML
    public void on_click_exit(Event event) throws InterruptedException {
        if (this.menu_hora) {
            this.borderpane_overlay.setVisible(false);

            ((GaussianBlur) this.borderpane_main.getEffect()).setRadius(0);

            this.borderpane_main.setDisable(false);
            this.gridpane_OverBlur.setDisable(true);
        }
    }

    @FXML
    public void on_click_consume(Event event) {
        if (BorderPane.class.isInstance(event.getSource())) {
            event.consume();
        }
    }

    @FXML
    public void test__10(Event event) {
        event.consume();

        Estat.setDate(Estat.getDate().minusDays(10));

        System.out.println("Dia: " + Estat.getDate().toString());

        this.refresh();
    }

    @FXML
    public void test__1(Event event) {
        event.consume();

        Estat.setDate(date_Picker.getValue());

        System.out.println("Dia: " + Estat.getDate().toString());

        this.refresh();
    }

    @FXML
    public void test_1(Event event) {
        event.consume();

        Estat.setDate(Estat.getNextDate());

        System.out.println("Dia: " + Estat.getDate().toString());

        this.refresh();
    }

    @FXML
    public void test_10(Event event) {
        event.consume();

        Estat.setDate(Estat.getDate().plusDays(10));

        System.out.println("Dia: " + Estat.getDate().toString());

        this.refresh();
    }
}
