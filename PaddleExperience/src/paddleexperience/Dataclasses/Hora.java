/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package paddleexperience.Dataclasses;

import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

/**
 *
 * @author saisua
 */
public final class Hora{
    private final Pane hora = new Pane();
    private final Pane test = new Pane();
    
    public Hora(String hora){
        this.hora.getChildren().add(new Text(hora));
        this.test.getChildren().add(new Text("Test"));
        
        //this.hora.setStyle("-fx-alignment: UPPER-RIGHT;");
    }
    
    public Pane getHora(){
        return this.hora;
    }
    
    public Pane getTest(){
        return this.test;
    }
}
