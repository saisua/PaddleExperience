/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package paddleexperience;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import model.Booking;
import model.Member;
import paddleexperience.Dataclasses.Estat;
import paddleexperience.Structures.Stoppable;

/**
 * FXML Controller class
 *
 * @author joandzmn
 */
public class FXMLHistoricoController implements Initializable, Stoppable {

    @FXML
    private TableView<ObservableList> tableView_Proximes;
    @FXML
    private TableColumn<Booking, String> diaProx;
    @FXML
    private TableColumn<Booking, String> horaProx;
    @FXML
    private TableColumn<Booking, String> pistaProx;
    @FXML
    private TableColumn<Booking, String> usuariProx;
    @FXML
    private TableColumn<Booking, String> cancela;
    @FXML
    private TableView<ObservableList> tableView_Ultimes;
    @FXML
    private TableColumn<Booking, String> diaUlt;
    @FXML
    private TableColumn<Booking, String> horaUlt;
    @FXML
    private TableColumn<Booking, String> pistaUlt;
    @FXML
    private TableColumn<Booking, String> usuariUlt;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //ObservableList<Booking> bookingList = (ObservableList<Booking>) Estat.getClub().getUserBookings(Estat.getMember().getLogin());
        //Plena les taules

        //tableView_Proximes.setItems(bookingList);
    }

    // S'executa cada vegada que es tanca l'escena
    @Override
    public void stop() throws InterruptedException {
        System.out.println("Historico stopped");
    }

    // S'executa cada vegada que es carrega l'escena
    @Override
    public void refresh() {
        System.out.println("Historico refreshed");
    }

}
