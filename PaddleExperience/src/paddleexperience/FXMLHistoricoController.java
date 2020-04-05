/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package paddleexperience;

import java.net.URL;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.ResourceBundle;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import model.Booking;
import model.Member;
import paddleexperience.Dataclasses.Estat;
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
    ArrayList<Booking> __user_bookings;
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
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
        
        System.out.println(Estat.club.existsLogin(Estat.getMemberLogin()));
        
        this.__user_bookings = Estat.club.getUserBookings(Estat.getMemberLogin());
        System.out.println("0");
        int total_bookings = (this.__jugades_count + this.__proximes_count);
        System.out.println("1");
        
        if(this.__user_bookings.size() > total_bookings){
            this.thread_per_jugar = new ReservaThread(this, false, total_bookings);
            this.thread_per_jugar.start();
        }
        
        System.out.println("2");
        
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
            if(hora_fi.compareTo(LocalDateTime.now()) < 0)
                this.swapItem(this.index, hora_inici);
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
            LocalDateTime hora_fi = to_book.getBookingDate().plusMinutes(Estat.partides_duracio);
            
            if(hora_fi.compareTo(LocalDateTime.now()) > 0)
                this.addItemProximes(new UserBooking(to_book), hora_fi);
            else
                this.addItemJugades(new UserBooking(to_book), to_book.getBookingDate());
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
                        iter.hasPrevious() && iter.previous().compareTo(hora_comparar) > 0;
                        proximes_size--);
        
        this.controller.tableView_Proximes.getItems().add(proximes_size, to_book);
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

