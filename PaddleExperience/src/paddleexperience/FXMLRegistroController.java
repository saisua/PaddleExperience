/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package paddleexperience;

import DBAcess.ClubDBAccess;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import java.io.File;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelReader;
import javafx.scene.image.WritableImage;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.StageStyle;
import javax.sound.sampled.Clip;
import model.Member;
import paddleexperience.Dataclasses.Estat;
import paddleexperience.Structures.Stoppable;

/**
 * FXML Controller class
 *
 * @author joandzmn
 */
public class FXMLRegistroController implements Initializable, Stoppable {

    @FXML
    private ImageView userImage;
    @FXML
    private JFXTextField textfield_nom;
    @FXML
    private JFXTextField textfield_cognom;
    @FXML
    private JFXTextField textfield_usuari;
    @FXML
    private JFXTextField textfield_telefon;
    @FXML
    private JFXPasswordField textfield_contrasena;
    @FXML
    private JFXPasswordField textfield_ConfContrasena;
    @FXML
    private JFXTextField textfield_targeta;
    @FXML
    private JFXTextField textfield_svc;
    @FXML
    private Button button_continua;
    @FXML
    private Text notEqual_text;
    @FXML
    private Text text_usuari_existent;
    @FXML
    private Text text_errorContrsena;
    @FXML
    private Text text_nomObligatori;
    @FXML
    private Text text_cognomObligatori;
    @FXML
    private Text text_errorTelefon;
    @FXML
    private Text text_usuariObligatori;
    @FXML
    private Button button_Error;

    private ClubDBAccess clubDBAccess;

    //Condicions des camps
    private boolean isNom, isCognom, isUsuari, isTelefon, isContrasena, isConfirmacio,
            isTargeta, isSvc;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        System.out.println("Registro init");

        //Dona la forma rodona a la imatge d'usuari
        Circle circle = new Circle(73);
        circle.setCenterX(userImage.getX() + (userImage.getFitWidth() / 2));
        circle.setCenterY(userImage.getY() + (userImage.getFitHeight() / 2));
        userImage.setClip(circle);

        isNom = isCognom = isUsuari = isTelefon = isContrasena = isConfirmacio = false;
        isTargeta = isSvc = true;

        this.textfield_svc.setDisable(true);

    }

    // S'executa cada vegada que es tanca l'escena
    @Override
    public void stop() throws InterruptedException {
        System.out.println("Registro stopped");
    }

    // S'executa cada vegada que es carrega l'escena
    @Override
    public void refresh() {
        System.out.println("Registro refreshed");
    }

    @FXML
    private void on_click_enrere(MouseEvent event) throws InterruptedException {
        PaddleExperience.setParent(event, "FXMLPaddleExperience.fxml");
    }

    @FXML
    private void on_click_continuar(MouseEvent event) throws InterruptedException {
        String pass = this.textfield_contrasena.getText();
        String login = this.textfield_usuari.getText();
        String nom = this.textfield_nom.getText();
        String cognom = this.textfield_contrasena.getText();
        String telefon = this.textfield_telefon.getText();
        String targeta = this.textfield_targeta.getText();
        String svc = this.textfield_svc.getText();

        Image image_perfil = this.userImage.getImage();

        Member new_member = new Member(nom, cognom, telefon, login, pass, targeta, svc, image_perfil);
        clubDBAccess = ClubDBAccess.getSingletonClubDBAccess();
        clubDBAccess.getMembers().add(new_member);
        Estat.setMember(new_member);

        //Pantalla de confirmacio i passar a la següent pantalla
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setHeaderText(null);
        alert.setTitle("Gracies per unir-te");
        alert.setContentText("Hola " + nom + " gràcies per unir-te. "
                + "Ara es guardaran les teues dades");
        alert.initStyle(StageStyle.UTILITY);
        alert.getButtonTypes().clear();
        ButtonType buttonContinuar = new ButtonType("Continuar");
        alert.getButtonTypes().add(buttonContinuar);
        //alert.getDialogPane().lookupButton(buttonContinuar).disableProperty().setValue(true);
        alert.showAndWait();

        //POSAR SAVEDB AL TANCAR APLICACIO
        //clubDBAccess.saveDB();
        PaddleExperience.refresh("FXMLHome.fxml");
        PaddleExperience.setParent(event, "FXMLSidebar.fxml");

        //alert.getDialogPane().lookupButton(buttonContinuar).disableProperty().setValue(false);
    }

    @FXML
    private void on_keyTyped_comprova(KeyEvent event) {

        boolean is_good;
        String contrasena = this.textfield_contrasena.getText();
        String comprova = this.textfield_ConfContrasena.getText();

        is_good = contrasena.equals(comprova);
        notEqual_text.setVisible(!is_good);
        notEqual_text.setSmooth(true);

        isConfirmacio = is_good;
        //System.out.println("Comprovació" + is_good);
        isGood();
    }

    @FXML
    private void on_click_telefon(KeyEvent event) {
        String lastTyped = event.getCharacter();

        //Comprova que només s'escriuen números
        if (!Character.isDigit(lastTyped.charAt(0))) {
            event.consume();
        }

        isTelefon = this.textfield_telefon.getText().length() > 7;
        if (isTelefon) {
            this.text_errorTelefon.setStyle("-fx-fill: #FAFAFA");
        } else {
            this.text_errorTelefon.setStyle("-fx-fill: #FF5722");
        }

        isGood();
    }

    @FXML
    private void on_click_targeta(KeyEvent event) {
        String lastTyped = event.getCharacter();
        int numOf = this.textfield_targeta.getText().length();
        if (!Character.isDigit(lastTyped.charAt(0))
                || numOf > 15) {
            event.consume();
        }
        if (numOf != 16) {
            this.textfield_svc.setDisable(true);
        } else {
            this.textfield_svc.setDisable(false);
        }
        isTargeta = (numOf == 0 || numOf == 16);
        isGood();
    }

    @FXML
    private void on_click_svc(KeyEvent event) {
        String lastTyped = event.getCharacter();
        int numOf = this.textfield_svc.getText().length();
        if (!Character.isDigit(lastTyped.charAt(0))
                || numOf > 2) {
            event.consume();
        }
        isSvc = numOf == 0 || numOf == 3;
        isGood();
    }

    @FXML
    private void on_keyTyped_usuari(KeyEvent event) {
        String lastTyped = event.getCharacter();
        if (Character.isSpace(lastTyped.charAt(0))) {
            event.consume();
        }
        if (this.textfield_usuari.getText().length() > 0) {
            if (!Estat.club.existsLogin(textfield_usuari.getText())) {
                isUsuari = true;
                this.text_usuari_existent.setVisible(false);
            } else {
                isUsuari = false;
                System.out.println("Usuari ja existent");
                this.text_usuari_existent.setVisible(true);
            }
            this.text_usuariObligatori.setVisible(false);
        } else {
            isUsuari = false;
            this.text_usuariObligatori.setVisible(true);
        }
        isGood();
    }

    @FXML
    private void comprovaCognom(KeyEvent event) {
        JFXTextField source = (JFXTextField) event.getSource();
        isCognom = source.getText().length() > 0;
        System.out.println("isCognom es " + isCognom);
        text_cognomObligatori.setVisible(!isCognom);
        isGood();
    }

    @FXML
    private void comprovaNom(KeyEvent event) {
        JFXTextField source = (JFXTextField) event.getSource();
        isNom = source.getText().length() > 0;
        System.out.println("isCognom es " + isNom);
        text_nomObligatori.setVisible(!isNom);
        isGood();
    }

    @FXML
    private void on_keyTyped_contrasena(KeyEvent event) {
        isContrasena = this.textfield_contrasena.getText().length() > 5;
        if (isContrasena) {
            this.text_errorContrsena.setStyle("-fx-fill:#FAFAFA;-fx-opacity:0.85");
        } else {
            this.text_errorContrsena.setStyle("-fx-fill:#FF5722;-fx-opacity:1");
        }
        isGood();
    }

    private void isGood() {
        boolean is_good = isCognom && isConfirmacio && isContrasena && isNom
                && isSvc && isTargeta && isTelefon && isUsuari;

        if (is_good) { //Es compleix tot
            if (this.textfield_usuari.getText().length() > 0) {
                this.button_continua.setOpacity(1);
                this.button_continua.setDisable(false);
                this.button_Error.setVisible(false);
            }

        } else { //Alguna condicio no es compleix
            this.button_continua.setOpacity(0.2);
            this.button_continua.setDisable(true);
            this.button_Error.setVisible(true);
        }
    }

    @FXML
    private void seleccioImatge(MouseEvent event) {

        //Obre una finestra per escollir les imatges
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Cercar imatge");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Tots els tipus", "*.jpg", "*.png"),
                new FileChooser.ExtensionFilter("JPG", "*.jpg"),
                new FileChooser.ExtensionFilter("PNG", "*png")
        );

        File selectedFile = fileChooser.showOpenDialog(null);

        System.out.print(selectedFile.getPath());
        Image aux = new Image("file:" + selectedFile.getPath());
        double limiter = 0;
        int startX = 0;
        int startY = 0;
        if (aux.getWidth() < aux.getHeight()) {
            limiter = aux.getWidth();
            startY = ((int) aux.getHeight() - (int) aux.getWidth()) / 2;
        } else {
            limiter = aux.getHeight();
            startX = ((int) aux.getWidth() - (int) aux.getHeight()) / 2;
        }

        PixelReader reader = aux.getPixelReader();
        WritableImage newImage = new WritableImage(reader, startX, startY, (int) limiter, (int) limiter);
        userImage.setImage(newImage);

    }

    @FXML
    private void mostraErrors(MouseEvent event) {
        this.text_cognomObligatori.setVisible(!isCognom);
        this.text_nomObligatori.setVisible(!isNom);
        if (!isTelefon) {
            this.text_errorTelefon.setStyle("-fx-fill: #FF5722");
        }
        if (!this.text_usuari_existent.isVisible()) {
            this.text_usuariObligatori.setVisible(!isUsuari);
        }
        if (this.textfield_contrasena.getText().length() < 6) {
            this.text_errorContrsena.setStyle("-fx-fill: #FF5722; -fx-opacity: 0.75");
        }
        this.notEqual_text.setVisible(!this.textfield_contrasena.getText().equals(
                this.textfield_ConfContrasena.getText()));
    }

}
