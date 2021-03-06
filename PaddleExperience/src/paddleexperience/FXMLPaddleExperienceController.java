/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package paddleexperience;

// Java imports
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.Random;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Arrays;
import java.util.Iterator;

// JavaFX imports
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

// Internal imports
import paddleexperience.PaddleExperience;
import paddleexperience.PaddleExperience;
import paddleexperience.PaddleExperience;
import paddleexperience.Structures.Stoppable;
import paddleexperience.Structures.Stoppable;

/**
 *
 * @author saisua
 */
public class FXMLPaddleExperienceController implements Initializable, Stoppable {

    // // SCENE
    @FXML
    private ImageView image_logo;
    @FXML
    private Text text_benvinguda;
    @FXML
    private Button button_login;
    @FXML
    private Text text_no_login;
    @FXML
    private Button button_registrar;
    @FXML
    private Button button_pistes;
    private Tooltip tooltip_text_benvinguda = new Tooltip("Benvingut/da en diversos llenguatges");

    // Llista benvingut en diversos llenguatges
    static final LinkedList WELCOME = new LinkedList<String>(Arrays.asList(
            //"欢迎光临", // Chino mandarín
            "Bienvenido", "Bienvenida", // Español
            "Welcome", // Inglés
            "स्वागत", "सवागत हैं", // Hindú
            "স্বাগতম", // Bengalí
            "Bem-vindo", "Bem-vinda", // Portugués
            "Добро пожаловать", // Ruso
            //"ようこそ", // Japonés
            "ਜੀ ਆਇਆ ਨੂੰ।", // Punjavi
            "सुस्वागतम", // Marathi
            "Hoş geldin", // Turco
            //"환영합니다", // Koreano
            "Bienvenue", // French
            "Willkommen", // Alemán
            "Hoan nghênh", // Vietnamí
            "வாருங்கள்", // Tamil
            "خوش آمديد", // Urdu
            "Benvenuto", "Benvenuta", // Italiano
            "أهلاً و سهلاً", // Egipcio
            "પધારો", // Gujarati
            "خوش آمدید", // Persian
            "आईं ना", // Bhojpuri
            //"歡迎", // Hakka
            "Sannu da zuwa", // Hausa
            "Benvingut", "Benvinguda" // Catalán
    ));

    // Int temps que tardará en fer el fade de welcome (ms)
    static final int FADE = 5000;

    // Objecte que conté el thread que va canviant la opacitat
    private OpacityThread thread;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        this.thread = new OpacityThread(this.text_benvinguda, this.tooltip_text_benvinguda, true);
        this.thread.start();
    }

    public void stop() throws InterruptedException {
        this.thread.open = false;
        this.thread.join(3000);
        System.out.println("Welcome Stoped Successfully");
    }

    public void refresh() {
        if (this.thread == null || !this.thread.isAlive()) {
            this.thread = new OpacityThread(this.text_benvinguda, this.tooltip_text_benvinguda, false);
            this.thread.start();
        }

        System.out.println("Welcome Refreshed");
    }

    // // Manejadors d'events
    @FXML
    public void on_click_login(Event event) throws InterruptedException, IOException {
        PaddleExperience.setParent(event, "FXMLPaddleLogin.fxml");
    }

    @FXML
    public void on_click_registre(Event event) throws InterruptedException, IOException {
        PaddleExperience.setParent(event, "FXMLRegistro.fxml");
    }

    @FXML
    public void on_click_pistes(Event event) throws InterruptedException, IOException {
        PaddleExperience.setParent(event, "FXMLPaddleReserva.fxml");
    }

    //Canvia el color del boto login al passar el mouse per damunt
    @FXML
    private void paintButLogin(MouseEvent event) {
        button_login.setStyle("-fx-background-color:#78909c;-fx-text-fill:#FAFAFA;-fx-opacity:0.5;");
    }

    @FXML
    private void defaultButColor(MouseEvent event) {
        button_login.setStyle("-fx-background-color:transparent;-fx-text-fill:#FAFAFA");
    }

    //Canvia el color dels textos dels botons registrat i veure pistes
    @FXML
    private void defaultTextButton(MouseEvent event) {
        Button anyBtn = (Button) event.getSource();
        anyBtn.setStyle("-fx-background-color:transparent;-fx-text-fill:#FAFAFA");
    }

    @FXML
    private void fillTextButon(MouseEvent event) {
        Button anyBtn = (Button) event.getSource();
        anyBtn.setStyle("-fx-background-color:transparent;-fx-text-fill:#78909c");
    }
}

class OpacityThread extends Thread {

    // Boolean assegura que el fil fill es tancarà quan ho requerisca
    public boolean open = true;

    private static final int FADE = FXMLPaddleExperienceController.FADE;

    private Text text_benvinguda;
    private Tooltip tooltip_text_benvinguda;

    // // Variables auxiliars
    private static final double OPACITY_PERC_ds = Math.pow(10, (Math.floor((Math.log10(FADE)) - 1))) / FADE;

    private static boolean opacity_up = false;
    // Iterador per a accés més veloç
    private Iterator WELCOME_ITERATOR = FXMLPaddleExperienceController.WELCOME.iterator();

    public OpacityThread(Text text_benvinguda, Tooltip tooltip,
            boolean first) {
        this.text_benvinguda = text_benvinguda;
        this.tooltip_text_benvinguda = tooltip;

        if (first) {
            Collections.shuffle(FXMLPaddleExperienceController.WELCOME);

            this.text_benvinguda.setText((String) this.WELCOME_ITERATOR.next());

            Tooltip.install(this.text_benvinguda, this.tooltip_text_benvinguda);
        }
    }

    // Run method changes opacity of text_benvinguda over time
    @Override
    public void run() {
        while (this.open) {
            if (opacity_up) {
                this.text_benvinguda.setOpacity(this.text_benvinguda.getOpacity() + OPACITY_PERC_ds);

                if (this.text_benvinguda.getOpacity() >= 1) {
                    opacity_up = false;
                }

            } else {
                this.text_benvinguda.setOpacity(this.text_benvinguda.getOpacity() - OPACITY_PERC_ds);

                if (this.text_benvinguda.getOpacity() <= 0) {
                    opacity_up = true;
                    this.new_text_benvinguda();
                }
            }

            try {
                Thread.sleep(100); // sleep 1ds
            } catch (InterruptedException err) {
                break;
            }
        }
    }

    private void new_text_benvinguda() {
        if (!this.WELCOME_ITERATOR.hasNext()) {
            this.WELCOME_ITERATOR = FXMLPaddleExperienceController.WELCOME.iterator();
        }

        this.text_benvinguda.setText((String) this.WELCOME_ITERATOR.next());
    }
}
