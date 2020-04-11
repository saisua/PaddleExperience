/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package paddleexperience;

// Java imports
import java.net.URL;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
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
import javafx.scene.control.Button;
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
import model.Booking;
import model.Court;

// Java imports
import paddleexperience.Structures.Stoppable;
import paddleexperience.Dataclasses.Estat;
import paddleexperience.Dataclasses.Hora;
import paddleexperience.Dataclasses.Pair;
import paddleexperience.Structures.Cache;

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
    private Button button_reserva;
    
    // Auxiliar
    private String hora_actual;
    private boolean te_reserva = false;
    
    private int selected_index;
    private ImageView selected_image;
    private Image prev_image;
    
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
        if(to_show == null) this.vbox_Lhora.setVisible(false);
        else {
            this.text_Lhora.setText(to_show.toString());
            this.vbox_Lhora.setVisible(true);
        }
        
        to_show = Estat.getNextTime();
        if(to_show == null) this.vbox_Rhora.setVisible(false);
        else {
            this.text_Rhora.setText(to_show.toString());
            this.vbox_Rhora.setVisible(true);
        }
        
        System.out.println(Estat.hores.get(this.hora_actual).getCourtImages().length);
        
        if(Estat.hores.get(this.hora_actual).getCourtImages().length > 0){
            ObservableList<Node> courts = this.gridpane_imageview_court.getChildren();

            for(int court_num = 0; court_num < courts.size(); court_num++){
                System.out.println(courts.size());
                
                ((ImageView) courts.get(court_num)).setImage(((ImageView) 
                        Estat.hores.get(this.hora_actual).getCourtImages()[court_num]).getImage());
            }
        }
        
        if(Estat.getMember() == null){
            this.vbox_no_login.setVisible(true);
            this.text_reserva.setVisible(false);
            this.button_reserva.setVisible(false);
        } else {
            this.vbox_no_login.setVisible(false);
            this.text_reserva.setVisible(true);
            this.button_reserva.setVisible(true);
            
            this.te_reserva = Estat.hores.get(this.hora_actual).getTeReserva();
            
            this.text_reserva.setText((te_reserva) ? "Ja tens una hora reservada":"Ninguna pista seleccionada");
            this.button_reserva.setDisable(true);
            
            Estat.setSelectedCourt(null);
        }
        
        for(Node court : this.gridpane_imageview_court.getChildren()){
            ((DropShadow) court.getEffect()).setHeight(0);
            ((DropShadow) court.getEffect()).setWidth(0);
        }
        
        System.out.println("ReservaHora refreshed");
    }   
    
    public void on_click_nextHora(Event event){    
        event.consume();
        
        Estat.setTime(Estat.getNextTime());
        
        this.refresh();
    }
    
    public void on_click_befHora(Event event){ 
        event.consume();
        
        Estat.setTime(Estat.getBeforeTime());
        
        this.refresh();
    }
    
    public void on_Lhora_horver_enter(Event _e){
        this.vbox_Lhora.setBackground(new Background(new BackgroundFill(Paint.valueOf(
                "#678080"), new CornerRadii(1), null)));
        
        ((Bloom) this.imageview_Lfletxa.getEffect()).setThreshold(0);
    }
    
    public void on_Rhora_horver_enter(Event _e){
        this.vbox_Rhora.setBackground(new Background(new BackgroundFill(Paint.valueOf(
                "#678080"), new CornerRadii(1), null)));
        
        ((Bloom) this.imageview_Rfletxa.getEffect()).setThreshold(0);
    }
    
    public void on_Lhora_horver_exit(Event _e){
        this.vbox_Lhora.setBackground(new Background(new BackgroundFill(Paint.valueOf(
                "#789090"), new CornerRadii(1), null)));
        
        ((Bloom) this.imageview_Lfletxa.getEffect()).setThreshold(1);
    }
    
    public void on_Rhora_horver_exit(Event _e){
        this.vbox_Rhora.setBackground(new Background(new BackgroundFill(Paint.valueOf(
                "#789090"), new CornerRadii(1), null)));
        
        ((Bloom) this.imageview_Rfletxa.getEffect()).setThreshold(1);
    }
    
    public void on_click_login(Event event) throws InterruptedException {
        PaddleExperience.setParent(event, "FXMLPaddleLogin.fxml");
    }
    
    public void on_click_registrar(Event event) throws InterruptedException {
        PaddleExperience.setParent(event, "FXMLRegistro.fxml");
    }
    
    public void on_hover_enter_pista(Event event){
        if(Estat.getMember() == null || Estat.hores.get(this.hora_actual).getTeReserva() != false ||
                ((ImageView) event.getSource()).getImage() != Hora.images.get(0))
            return;
        
        ((DropShadow) ((Node) event.getSource()).getEffect()).setHeight(20);
        ((DropShadow) ((Node) event.getSource()).getEffect()).setWidth(20);
    }
    
    public void on_hover_exit_pista(Event event){
        ((DropShadow) ((Node) event.getSource()).getEffect()).setHeight(0);
        ((DropShadow) ((Node) event.getSource()).getEffect()).setWidth(0);
    }
    
    public void on_click_pista(MouseEvent event){
        if(Estat.getMember() == null || this.te_reserva != false)
            return;
        
        // Aci ho separe perque en la majoria de casos (El click és valid)
        // puc fer ús de la variable
        ImageView event_source =  ((ImageView) event.getSource());
        
        if(event_source.getImage() != Hora.images.get(0))
            return;
        
        int indexX = 0;
        int indexY = 0;

        // LA PUTA HOSTIA NO EM PUC CREURE QUE AÇO TORNE PUTO
        // NULL SI EL INDEX ES 0 QUE INCONSISTENT ES JAVA HOSTIA
        // per: Ausias
        try{
            indexX = GridPane.getColumnIndex((Node) event.getSource());
        } catch (NullPointerException fuckJava) { ; }
        
        try{
            indexY = GridPane.getRowIndex((Node) event.getSource());
        } catch (NullPointerException fuckJava) { ; }
        
        this.selected_index = indexX + 2*indexY;
        
        Court court = Estat.court_by_index.get(this.selected_index); // 2*y
        
        Estat.setSelectedCourt(court);
        this.text_reserva.setText(court.getName());
        
        if(this.selected_image != null)
            this.selected_image.setImage(this.prev_image);
        
        this.selected_image = event_source;
        this.prev_image = this.selected_image.getImage();
        
        this.selected_image.setImage(Hora.images.get(2));
        
        this.button_reserva.setDisable(false);
    }
    
    public void on_click_reservar(Event _e){
        // LocalDateTime bookingDate, LocalDate madeForDay, LocalTime fromHour, boolean paid, Court court, Member member
        Estat.club.getBookings().add(new Booking(LocalDateTime.now(), Estat.getDate(), Estat.getTime(),
                false, Estat.getSelectedCourt(), Estat.getMember()));
        
        this.te_reserva = true;
        
        this.text_reserva.setText("Ja tens una hora reservada");
        
        Image ocupied_image = Hora.images.get(1);
        
        this.selected_image.setImage(ocupied_image);
        
        Hora actual_hora = Estat.hores.get(this.hora_actual);
        
        actual_hora.getCourtImages()[this.selected_index]
                .setImage(ocupied_image);
        actual_hora.__setTeReserva(true);
        
        Pair<Image[], Boolean> actual_cache = Cache.cache.get(Estat.getDate()).get(Estat.getTime());
        
        actual_cache.first[this.selected_index] = ocupied_image;
        actual_cache.setSecond(true);
        
        this.selected_image = null;
        this.prev_image = null;
    }
    
}
