/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package paddleexperience.Structures;

import java.text.SimpleDateFormat;
import java.util.Date;
import javafx.scene.text.Text;

/**
 *
 * @author saisua
 */
public class Clock extends Thread{
    private Date now;
    private SimpleDateFormat format = new SimpleDateFormat("hh:mm");
    
    private boolean open = true;
    
    private Text clock;
    
    public Clock(Text clock){
        this.clock = clock;
    }
    
    @Override
    public void run(){
        this.now = new Date();
        
        this.clock.setText(this.format.format(this.now));
        
        try{
            Thread.sleep(61000 - (System.currentTimeMillis()%60000)); // 1 segon de diferencia per si de cas
        } catch(InterruptedException err){ return; }
        
        while(this.open){
            this.clock.setText(this.format.format(this.now));
            
            try{
                Thread.sleep(999 - (System.currentTimeMillis()%1000)); // Compensar temps d'execució
                
                for(int second = 1; second < 60; second++){
                    if(!this.open) return;
                    
                    Thread.sleep(1000);
                }
            } catch(InterruptedException err){ return; }
        }
    }
    
    public void close() { this.open = false; }
}
