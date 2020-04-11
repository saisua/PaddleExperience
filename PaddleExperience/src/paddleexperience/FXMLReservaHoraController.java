/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package paddleexperience;

// Java imports
import java.net.URL;
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
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;

// Java imports
import paddleexperience.Structures.Stoppable;
import paddleexperience.Dataclasses.Estat;
import paddleexperience.Dataclasses.Hora;

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
        String hora_actual = Estat.getTime().toString();
        
        this.text_hora.setText(hora_actual);
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
        
        System.out.println(Estat.hores.get(hora_actual).getCourtImages().length);
        
        if(Estat.hores.get(hora_actual).getCourtImages().length > 0){
            ObservableList<Node> courts = this.gridpane_imageview_court.getChildren();

            for(int court_num = 0; court_num < courts.size(); court_num++){
                System.out.println(courts.size());
                
                ((ImageView) courts.get(court_num)).setImage(((ImageView) 
                        Estat.hores.get(hora_actual).getCourtImages()[court_num]).getImage());
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
            
            this.text_reserva.setText("Ninguna pista seleccionada");
            this.button_reserva.setDisable(true);
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
    
    public void on_click_pista(Event event){
        
    }
    
    public void on_click_reservar(Event _e){
        
    }
    
}
