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
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;
import java.util.TreeSet;
import javafx.collections.ObservableList;

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

// Interal imports
import paddleexperience.PaddleExperience;
import paddleexperience.Dataclasses.Hora;
import paddleexperience.Structures.Stoppable;
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
    private BorderPane borderpane_main;
    @FXML
    private GridPane gridpane_main;
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
    
    // Cache
    static HashMap<LocalDate, HashMap<LocalTime, Image[]>> cache = new HashMap<LocalDate, HashMap<LocalTime, Image[]>>(){};
    
    private CacheThread thread_avant = new CacheThread(true);
    private CacheThread thread_arrere = new CacheThread(false);
    
    static LocalDate max_day = Estat.getBeforeDate();
    static LocalDate min_day = Estat.getNextDate();
    
    // Auxiliar
    private Member was_logged_in = null;
    private boolean menu_hora = false;
    
    private HashMap<String, Hora> hores = new HashMap<String, Hora>();
    
    // Testing
    public static ArrayList<Booking> test_booking = new ArrayList<Booking>(){};
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Tests
        this.tablecolumn_hora.setCellValueFactory(new PropertyValueFactory<>("hora"));
        this.tablecolumn_courts.setCellValueFactory(new PropertyValueFactory<>("courts"));
        this.tablecolumn_reservat.setCellValueFactory(new PropertyValueFactory<>("reservat"));
        
        this.tablecolumn_hora.setStyle("-fx-alignment: CENTER;"); 
        
        for(int partida = 0; partida < Estat.partides_dia; partida++){
            // Hora falta des-comentar
            Hora hora = new Hora(Estat.time_inici.plusMinutes(Estat.partides_duracio*partida),
                    Arrays.asList(0,0,0,0));
            
            this.tableview_horari.getItems().add(hora);
            this.hores.put(hora.getTimeStr(), hora);
        }
        
        //Booking(LocalDateTime bookingDate, LocalDate madeForDay, 
        //    LocalTime fromHour, boolean paid, Court court, Member member)
        Booking new_booking = new Booking(LocalDateTime.now(), Estat.getDate(),
                            Estat.getInitialTime(),
                            true, Estat.club.getCourts().get(1), Estat.getMember());
        
        for(int partida = 0; partida < Estat.partides_dia; partida++)
            test_booking.add(new Booking(LocalDateTime.now(), Estat.getDate(),
                            Estat.getInitialTime().plusMinutes(
                                    Estat.partides_duracio*partida),
                            true, Estat.club.getCourts().get(0), Estat.getMember()));
        
        // No funciona perque el xml no està en el directori
        // que toca (vore pdf)
        Estat.club.getBookings().add(new_booking);
        
        test_booking.add(new_booking);
        
        //System.out.println("Test Booking at " + new_booking.getFromTime().toString());
        // End tests
        
        this.max_day = Estat.getBeforeDate();
        this.min_day = Estat.getNextDate();
        
        this.gridpane_main.setEffect(new GaussianBlur(0));
        
        try{
            this.borderpane_main.setCenter(PaddleExperience.getParent("FXMLReservaHora.fxml"));
        } catch(IOException err) {};
        
        System.out.println("Reserva initialized");
    }    
    
    @Override
    public void stop() throws InterruptedException{
        if(this.thread_avant.open){
            this.thread_avant.open = false;
            this.thread_avant.join(3000);
        }
        
        if(this.thread_arrere.open){
            this.thread_arrere.open = false;
            this.thread_arrere.join(3000);
        }
        
        System.out.println("Reserva Stoped Successfully");
    }
    
    // Esta funció és xunga, així que vaig a comentar-la prou
    @Override
    public void refresh(){
        // Si el usuari ha fet login, amaga el botó de tornar enrere,
        // pues ha de gastar el menú
        if(Estat.getMember() != null) this.button_enrere.setVisible(false);
        
        // Hashmap on guarde una representació del dia LocalTime
        // com a un array d'enters que posteriorment es tradueïx
        // a la imatge corresponent
        HashMap<LocalTime, Image[]> courts;
        
        // En cas que no n'hi ha ningún booking, es farà ús de
        // una còpia de default_court que és un array plé de 0
        Image[] default_court = new Image[Estat.numero_pistes];
        Arrays.fill(default_court, Hora.images.get(0));
        
        // Es mira si el usuari ha canviat de login
        boolean login_dif = this.was_logged_in != Estat.getMember();
        this.was_logged_in = Estat.getMember();
        
        // Si l'usuari ha canviat de login (o entrat o eixit), tant com
        // si l'usuari decideix veure una data que no está en caché, la
        // caché ja existent es considera inútil i es neteja
        if(login_dif || Estat.getDate().compareTo(max_day) > 0 || Estat.getDate().compareTo(min_day) < 0){
            // Es neteja la caché
            cache.clear();
            
            // Si el fil de caché ja estava obert no es torna
            // a obrir, sino que s'actualitza el dia de búsqueda
            if(this.thread_avant.open)
                this.thread_avant.setDate(Estat.getNextDate());
            else 
                this.thread_avant.start();

            // Si el fil de caché ja estava obert no es torna
            // a obrir, sino que s'actualitza el dia de búsqueda
            if(this.thread_arrere.open)
                this.thread_arrere.setDate(Estat.getBeforeDate());
            else
                this.thread_arrere.start();
        
            // Esta variable guarda la caché de un dia representat
            // per un array de enters, que agafen el sentit que la
            // classe Hora li done. Actualment, Hora.imatges
            courts = new HashMap<LocalTime, Image[]>();

            // Fem un recorregut lineal del bookings del dia, i apuntem
            // quines pistes están ocupades.
            // El resultat es guarda dins de courts
            for(Booking reserva : test_booking){//Estat.club.getForDayBookings(Estat.getDate())){
                // Agafem el array si ja n'hem guardat
                Image[] court = courts.get(reserva.getFromTime());
                
                // si no n'hem guardat encara, clonem default_court
                if(court == null){
                    court = default_court.clone();
                    courts.put(reserva.getFromTime(), court);
                }

                // Actualitzem l'estat del array
                court[Estat.court_index.get(reserva.getCourt())] 
                        = Hora.images.get((reserva.getMember() == Estat.getMember()) ? 2 : 1);
            }
        } 
        else 
            // else el dia que volem carregar està en la caché, per tant el carreguem
            
            // agafem el array que representa el dia. Si no està dins del
            // HashMap vol dir que quan es va carregar el caché no n'hi havia
            // ningun booking, per tant agafem un HashMap buit
            courts = cache.getOrDefault(Estat.getDate(), new HashMap<LocalTime, Image[]>());
        
        // Fem un recorregut per totes les hores, modificant els arxius de
        // Hora hora per els carregats en courts
        for(Hora hora : this.hores.values()){
            System.out.println("Hora "+hora.getTimeStr()+" "+Arrays.toString(
                    courts.getOrDefault(hora.getTime(), default_court)));
            
            hora.updateCourtsImages(courts.getOrDefault(hora.getTime(), default_court));
        }
        // Tests
        //System.out.println(Estat.club.getForDayBookings(Estat.getDate()));
        // End tests
        
        cache.put(Estat.getDate(), courts);
        
        System.out.println("Reserva Refreshed");
    }
    
    // Manejadors d'events
    public void on_click_enrere(Event event) throws InterruptedException{
        System.out.println("Click enrere!!");
        PaddleExperience.back(event);
    }
    
    public void on_click_horari(Event event){
        if(TextFlow.class.isInstance(event.getTarget())){
            Estat.setTime(Estat.time_inici.plusMinutes(Estat.partides_duracio*((TableCell) ((TextFlow)
                            event.getTarget()).getParent()).getTableRow().getIndex()));

            this.gridpane_main.setDisable(true);
            
            ((GaussianBlur) this.gridpane_main.getEffect()).setRadius(10);
            
            this.borderpane_main.setVisible(true);
            
            this.menu_hora = true;
            
            PaddleExperience.refresh("FXMLReservaHora.fxml");
        }
    }
    
    public void on_keypress_exit(KeyEvent event) throws InterruptedException {
        System.out.println("Exit");
        
        if(this.menu_hora && event.getCode().equals(KeyCode.ESCAPE)){
            this.borderpane_main.setVisible(false);
            
            ((GaussianBlur) this.gridpane_main.getEffect()).setRadius(0);
            
            this.gridpane_main.setDisable(false);
        }
    }
    
    public void on_click_exit(Event event) throws InterruptedException {
        System.out.println("Exit");
        
        if(this.menu_hora){
            this.borderpane_main.setVisible(false);
            
            ((GaussianBlur) this.gridpane_main.getEffect()).setRadius(0);
            
            this.gridpane_main.setDisable(false);
        }
    }
    
    public void on_click_consume(Event event){ 
        if(BorderPane.class.isInstance(event.getSource()))
            event.consume();
    }
    
}

class CacheThread extends Thread{
    
    public boolean open;
    
    public int max_dies = 7;
    private boolean next;
    
    private int dia;
    private LocalDate date;
    
    // // Auxiliar
    private boolean valid = true;
    private Image[] default_court = new Image[Estat.numero_pistes];
    
    public CacheThread(boolean next){
        this.next = next;
        
        Arrays.fill(default_court, Hora.images.get(0));
    }
    public CacheThread(boolean next, int max_dies){
        this.next = next;
        this.max_dies = max_dies;
        
        Arrays.fill(default_court, Hora.images.get(0));
    }
    
    @Override
    public void run(){
        System.out.println("CacheThread.run()");
        
        this.open = true;
        this.dia = 0;
        
        if(this.next){
            this.date = (Estat.getNextDate().compareTo(FXMLPaddleReservaController.max_day) > 0) ? 
                    Estat.getNextDate() : FXMLPaddleReservaController.max_day.plusDays(1);
        } else {
            this.date = (Estat.getBeforeDate().compareTo(FXMLPaddleReservaController.min_day) < 0) ? 
                    Estat.getBeforeDate() : FXMLPaddleReservaController.min_day.minusDays(1);
        }
        
        HashMap<LocalTime, Image[]> courts = new HashMap<LocalTime, Image[]>();
        
        while(this.open && dia < max_dies){
            this.valid = true;
           
            if(!FXMLPaddleReservaController.cache.containsKey(this.date)){
                courts.clear();

                for(Booking reserva : FXMLPaddleReservaController.test_booking){//Estat.club.getForDayBookings(this.date)){
                    Image[] court = courts.get(reserva.getFromTime());
                    if(court == null){
                        court = default_court.clone();
                        courts.put(reserva.getFromTime(), court);
                    }

                    court[Estat.court_index.get(reserva.getCourt())] 
                            = Hora.images.get(
                                    (reserva.getMember() == Estat.getMember()) ? 2 : 1);

                    if(!this.open) return;
                }

                if(this.valid)
                    FXMLPaddleReservaController.cache.put(this.date, courts);

                System.out.println("Cache carregada " + this.date.toString());
            }
            
            if(this.valid){
                if(this.next){
                    FXMLPaddleReservaController.max_day = this.date;

                    this.date = this.date.plusDays(1);
                } else {
                    FXMLPaddleReservaController.min_day = this.date;

                    this.date = this.date.minusDays(1);
                }

                this.dia++;
            }
        }
        
        this.open = false;
    }
    
    public void setDate(LocalDate new_date){
        this.valid = false;
        
        this.dia = 0;
        
        this.date = new_date;
    }
}