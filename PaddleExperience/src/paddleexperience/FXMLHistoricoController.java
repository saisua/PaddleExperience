/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package paddleexperience;

import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import model.Booking;
import model.Court;
import model.Member;
import paddleexperience.Dataclasses.Estat;
import paddleexperience.Dataclasses.Hora;
import paddleexperience.Dataclasses.UserBooking;
import paddleexperience.Structures.Stoppable;

/**
 * FXML Controller class
 *
 * @author joandzmn
 */
public class FXMLHistoricoController implements Initializable, Stoppable {

    @FXML
    TableView<UserBooking> tableView_Proximes;
    @FXML
    private TableColumn tablecolumn_dia_proxim;
    @FXML
    private TableColumn tablecolumn_hora_inici_proxim;
    @FXML
    private TableColumn tablecolumn_hora_fi_proxim;
    @FXML
    private TableColumn tablecolumn_pista_proxim;
    @FXML
    private TableColumn tablecolumn_pagada_proxim;
    @FXML
    private TableColumn tablecolumn_cancelar_proxim;
    @FXML
    TableView<UserBooking> tableView_Ultimes;
    @FXML
    private TableColumn tablecolumn_dia_jugades;
    @FXML
    private TableColumn tablecolumn_hora_inici_jugades;
    @FXML
    private TableColumn tablecolumn_hora_fi_jugades;
    @FXML
    private TableColumn tablecolumn_pista_jugades;
    
    // Sols deurien ser modificades per els threads corresponents
    int __proximes_count = 0;
    int __jugades_count = 0;
    
    private ReservaThread thread_per_jugar;
    private ReservaThread thread_ja_jugades;

    // Modificar esta variable mentre els threads estan treballant
    // pot comportar problemes
    // Definit per a testing, borrar despres
    ArrayList<Booking> __user_bookings = new ArrayList<Booking>(){{
        // LocalDateTime bookingDate, LocalDate madeForDay, LocalTime fromHour, boolean paid, Court court, Member member
        // Member(String name, String surname, String telephon, String login, String password, String creditCard, String svc, Image image)
    }};
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        System.out.println("Historico initializing...");
        
        this.tablecolumn_dia_proxim.setCellValueFactory(new PropertyValueFactory<>("dia"));
        this.tablecolumn_hora_inici_proxim.setCellValueFactory(new PropertyValueFactory<>("horaInici"));
        this.tablecolumn_hora_fi_proxim.setCellValueFactory(new PropertyValueFactory<>("horaFi"));
        this.tablecolumn_pista_proxim.setCellValueFactory(new PropertyValueFactory<>("pista"));
        this.tablecolumn_pagada_proxim.setCellValueFactory(new PropertyValueFactory<>("pagada"));
        this.tablecolumn_cancelar_proxim.setCellValueFactory(new PropertyValueFactory<>("cancelar"));
        
        this.tablecolumn_dia_jugades.setCellValueFactory(new PropertyValueFactory<>("dia"));
        this.tablecolumn_hora_inici_jugades.setCellValueFactory(new PropertyValueFactory<>("horaInici"));
        this.tablecolumn_hora_fi_jugades.setCellValueFactory(new PropertyValueFactory<>("horaFi"));
        this.tablecolumn_pista_jugades.setCellValueFactory(new PropertyValueFactory<>("pista"));
        
        this.tableView_Proximes.setSortPolicy(table_view -> {
                Comparator<UserBooking> comparator = (booking1, booking2)
                        -> this.tablecolumn_dia_proxim.getSortType() == TableColumn.SortType.ASCENDING
                                ? booking1.compareTo(booking2) : booking2.compareTo(booking1); //columns are sorted: sort accordingly
                FXCollections.sort(this.tableView_Proximes.getItems(), comparator);
                return true;
        });;
        
        this.tableView_Ultimes.setSortPolicy(table_view -> {
                Comparator<UserBooking> comparator = (booking1, booking2)
                        -> this.tablecolumn_dia_jugades.getSortType() == TableColumn.SortType.ASCENDING
                                ? booking1.compareTo(booking2) : booking2.compareTo(booking1); //columns are sorted: sort accordingly
                FXCollections.sort(this.tableView_Ultimes.getItems(), comparator);
                return true;
        });;
        
        // TESTS
        
        __user_bookings.add(new Booking(LocalDateTime.now(), LocalDate.now().minusDays(1),
                            LocalTime.of(17, 30), true, new Court("Pista4"),
                            new Member("ESP", "AÑA", "", "1", "2", "3", "4", null)));
        __user_bookings.add(new Booking(LocalDateTime.now(), LocalDate.now().plusDays(1),
                            LocalTime.of(10, 30), true, new Court("Pista1"),
                            new Member("ESP", "AÑA", "", "1", "2", "3", "4", null)));
        __user_bookings.add(new Booking(LocalDateTime.now(), LocalDate.now().plusDays(1),
                            LocalTime.of(9, 0), false, new Court("Pista1"),
                            new Member("ESP", "AÑA", "", "1", "2", "3", "4", null)));
        __user_bookings.add(new Booking(LocalDateTime.now(), LocalDate.now().minusDays(1),
                            LocalTime.of(10, 30), true, new Court("Pista4"),
                            new Member("ESP", "AÑA", "", "1", "2", "3", "4", null)));
        
        // END TESTS
        
        System.out.println("Historico initialized");
    }

    // S'executa cada vegada que es tanca l'escena
    @Override
    public void stop() throws InterruptedException {
        if(this.thread_per_jugar != null && this.thread_per_jugar.isAlive()){
            this.thread_per_jugar.open = false;
            
            this.thread_per_jugar.join(3000);
        }
        
        if(this.thread_ja_jugades != null && this.thread_ja_jugades.isAlive()){
            this.thread_ja_jugades.open = false;
            
            this.thread_ja_jugades.join(3000);
        }
        
        System.out.println("Historico stopped");
    }

    // S'executa cada vegada que es carrega l'escena
    @Override
    public void refresh() {
        if(Estat.getMember() == null) return;
        
        try{
            if(this.thread_per_jugar != null && this.thread_per_jugar.isAlive()){
                this.thread_per_jugar.open = false;
                this.thread_per_jugar.join(1000);
            }

            if(this.thread_ja_jugades != null && this.thread_ja_jugades.isAlive()){
                this.thread_ja_jugades.open = false;
                this.thread_ja_jugades.join(1000);
            }
        } catch (InterruptedException err) { return; }
        
        //this.__user_bookings = Estat.club.getUserBookings(Estat.getMemberLogin());
        System.out.println(this.__jugades_count); System.out.println(this.__proximes_count);
        int total_bookings = (this.__jugades_count + this.__proximes_count);
        
        if(this.__user_bookings.size() > total_bookings){
            this.thread_per_jugar = new ReservaThread(this, false, total_bookings);
            this.thread_per_jugar.start();
        }
        
        this.thread_ja_jugades = new ReservaThread(this, true, 0);
        this.thread_ja_jugades.start();
            
        System.out.println("Historico refreshed");
    }

}

class ReservaThread extends Thread{
    boolean open = false;
    
    private FXMLHistoricoController controller;
    
    private boolean ja_jugades;
    
    private int index;
    
    ReservaThread(FXMLHistoricoController controller, boolean ja_jugades, int index_start){
        this.controller = controller;
        
        this.ja_jugades = ja_jugades;
        
        this.index = index_start;
        
        this.open = true;
    }
    
    public void run(){
        if(this.ja_jugades)
            mou_ja_jugades();
        else
            afegix_per_jugar();
    }
    
    private void mou_ja_jugades(){
        for(int index_booking = 0; index_booking < this.controller.tableView_Proximes.
                    getItems().size(); index_booking++, this.index++){
            if(!this.open) return;
            
            LocalDateTime hora_inici = this.controller.tableView_Proximes.getItems()
                        .get(this.index).getDateTime();
            LocalDateTime hora_fi = hora_inici.plusMinutes(Estat.partides_duracio);
            
            // Si una de les proximes partides ja ha passat, mou-la a ultimes
            if(hora_fi.compareTo(LocalDateTime.now()) < 0){
                this.swapItem(this.index, hora_inici);
                System.out.println("Booking mogut!");
            }
            else
                break;
        }
        this.open = false;
    }
    
    private void afegix_per_jugar(){
        for(Iterator<Booking> booking_iter = this.controller.__user_bookings.listIterator(this.index); 
                        booking_iter.hasNext(); ){
            if(!this.open) return;
            
            Booking to_book = booking_iter.next();
            LocalDateTime hora_fi = LocalDateTime.of(
                    to_book.getMadeForDay(),
                    to_book.getFromTime()
                    ).plusMinutes(Estat.partides_duracio);
            
            if(hora_fi.compareTo(LocalDateTime.now()) > 0){
                this.addItemProximes(new UserBooking(to_book), hora_fi);
                
                System.out.println("Proxim Booking afegit!");
            }
            else{
                this.addItemJugades(new UserBooking(to_book), to_book.getBookingDate());
            
                System.out.println("Anterior Booking afegit!");
            }
        }
        
        this.open = false;
    }
    
    @SuppressWarnings("empty-statement")
    private synchronized void addItemProximes(UserBooking to_book, 
                                            LocalDateTime hora_comparar){
        
        int proximes_size = this.controller.tableView_Proximes.getItems().size();
        
        // Busqueda linear ja que el nombre d'elements a afegir sera xicotet
        // i probablement la base de dades vinga ja ordenada
        for(ListIterator<UserBooking> iter = this.controller.tableView_Proximes.getItems()
                        .listIterator(proximes_size);
                        iter.hasPrevious() && iter.previous().compareTo(hora_comparar) < 0;
                        proximes_size--);
        
        this.controller.tableView_Proximes.getItems().add(proximes_size, to_book);
        this.controller.__proximes_count++;
    }
    
    @SuppressWarnings("empty-statement")
    private synchronized void addItemJugades(UserBooking to_book, LocalDateTime hora_comparar){
        int index_to = 0;
        
        // Busqueda linear ja que el nombre d'elements a afegir sera xicotet
        // i probablement la base de dades vinga ja ordenada
        for(Iterator<UserBooking> iter = this.controller.tableView_Ultimes.getItems().iterator();
                        iter.hasNext() && iter.next().compareTo(hora_comparar) < 0;
                        index_to++);
        
        this.controller.tableView_Ultimes.getItems().add(index_to, to_book);
        this.controller.__proximes_count++;
    }
    
    @SuppressWarnings("empty-statement")
    private synchronized void swapItem(int index_from, LocalDateTime hora_comparar){
        int index_to = 0;
        
        // Busqueda linear ja que el nombre d'elements a afegir sera xicotet
        // i probablement la base de dades vinga ja ordenada
        for(Iterator<UserBooking> iter = this.controller.tableView_Ultimes.getItems().iterator();
                        iter.hasNext() && iter.next().compareTo(hora_comparar) < 0;
                        index_to++);
        
        this.controller.tableView_Ultimes.getItems().add(index_to,
                this.controller.tableView_Proximes.getItems().remove(index_from));
        this.controller.__jugades_count++;
        this.controller.__proximes_count--;
    }

}

