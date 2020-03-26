/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package paddleexperience.Dataclasses;

import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.scene.text.TextFlow;

/**
 *
 * @author saisua
 */
public final class Hora{
    private final TextFlow hora = new TextFlow();
    private final TextFlow test = new TextFlow();
    
    public Hora(String hora){
        Text h = new Text(hora);
        
        //System.out.println(this.hora.getLayoutY());
        //System.out.println(this.hora.layoutBoundsProperty().getValue());
        //h.layoutYProperty().set(this.hora.layoutBoundsProperty().getValue().getHeight()/2);
        
        
        this.hora.getChildren().add(h);
        this.test.getChildren().add(new Text("Test"));
    }
    
    public TextFlow getHora(){
        return this.hora;
    }
    
    public TextFlow getTest(){
        return this.test;
    }
}
