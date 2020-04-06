/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package paddleexperience.Structures;

// Java imports
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.HashMap;

// JavaFX imports
import javafx.scene.image.Image;

// Internal imports
import paddleexperience.Dataclasses.Estat;
import paddleexperience.Dataclasses.Hora;
import paddleexperience.FXMLPaddleReservaController;
import static paddleexperience.FXMLPaddleReservaController.test_booking;
import model.Booking;
import model.Member;

/**
 *
 * @author saisua
 */
public class Cache {
    public static HashMap<LocalDate, HashMap<LocalTime, Image[]>> cache = new HashMap<LocalDate, HashMap<LocalTime, Image[]>>(){};
    
    public static LocalDate max_day = Estat.getBeforeDate();
    public static LocalDate min_day = Estat.getNextDate();
    
    static CacheThread thread_avant = new CacheThread(true);
    static CacheThread thread_arrere = new CacheThread(false);
    
    public static final Image[] default_court = new Image[Estat.numero_pistes];
    
    // Auxiliars
    static Member was_logged_in = null;
    
    public static void start(){
        Arrays.fill(default_court, Hora.images.get(0));
        
        max_day = Estat.getBeforeDate();
        min_day = Estat.getNextDate();
    }
    
    public static void stop() throws InterruptedException{
        if(thread_avant.open){
            thread_avant.open = false;
            thread_avant.join(3000);
        }
        
        if(thread_arrere.open){
            thread_arrere.open = false;
            thread_arrere.join(3000);
        }
    }
    
    public static void clear(){ cache.clear(); }
    
    public static HashMap<LocalTime, Image[]> get(){
        // Hashmap on guarde una representació del dia LocalTime
        // com a un array d'enters que posteriorment es tradueïx
        // a la imatge corresponent
        HashMap<LocalTime, Image[]> courts;
        
        // Es mira si el usuari ha canviat de login
        boolean login_dif = was_logged_in != Estat.getMember();
        was_logged_in = Estat.getMember();
        
        // Si l'usuari ha canviat de login (o entrat o eixit), tant com
        // si l'usuari decideix veure una data que no está en caché, la
        // caché ja existent es considera inútil i es neteja
        if(login_dif || Estat.getDate().compareTo(Cache.max_day) > 0 || Estat.getDate().compareTo(min_day) < 0){
            // Es neteja la caché
            cache.clear();
            
            // Si el fil de caché ja estava obert no es torna
            // a obrir, sino que s'actualitza el dia de búsqueda
            if(thread_avant.isAlive())
                thread_avant.setDate(Estat.getDate().plusDays(1));
            else {
                thread_avant = new CacheThread(true);
                thread_avant.start();
            }

            // Si el fil de caché ja estava obert no es torna
            // a obrir, sino que s'actualitza el dia de búsqueda
            if(thread_arrere.isAlive())
                thread_arrere.setDate(Estat.getDate().minusDays(1));
            else{
                thread_arrere = new CacheThread(false);
                thread_arrere.start();
            }
        
            // Esta variable guarda la caché de un dia representat
            // per un array de enters, que agafen el sentit que la
            // classe Hora li done. Actualment, Hora.imatges
            courts = new HashMap<LocalTime, Image[]>();

            // Fem un recorregut lineal del bookings del dia, i apuntem
            // quines pistes están ocupades.
            // El resultat es guarda dins de courts
            for(Booking reserva : Estat.club.getForDayBookings(Estat.getDate())){
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
            cache.put(Estat.getDate(), courts);
            
            return courts;
        } 
        else {
            // else el dia que volem carregar està en la caché, per tant el carreguem
            
            // agafem el array que representa el dia. Si no està dins del
            // HashMap vol dir que quan es va carregar el caché no n'hi havia
            // ningun booking, per tant agafem un HashMap buit
            System.out.println("Dia ja en cache");
            return cache.getOrDefault(Estat.getDate(), new HashMap<LocalTime, Image[]>());
        }
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
    
    public CacheThread(boolean next){
        this.next = next;
    }
    public CacheThread(boolean next, int max_dies){
        this.next = next;
        this.max_dies = max_dies;
    }
    
    @Override
    public void run(){
        System.out.println("CacheThread.run()");
        
        this.open = true;
        this.dia = 0;
        
        if(this.next){
            this.date = (Estat.getNextDate().compareTo(Cache.max_day) > 0) ? 
                    Estat.getDate().plusDays(1) : Cache.max_day.plusDays(1);
        } else {
            this.date = (Estat.getBeforeDate().compareTo(Cache.min_day) < 0) ? 
                    Estat.getDate().minusDays(1) : Cache.min_day.minusDays(1);
        }
        
        HashMap<LocalTime, Image[]> courts = new HashMap<LocalTime, Image[]>();
        
        while(this.open && dia < max_dies){
            this.valid = true;
           
            if(!Cache.cache.containsKey(this.date)){
                courts.clear();
                
                for(Booking reserva : Estat.club.getForDayBookings(this.date)){
                    Image[] court = courts.get(reserva.getFromTime());
                    if(court == null){
                        court = Cache.default_court.clone();
                        courts.put(reserva.getFromTime(), court);
                    }

                    court[Estat.court_index.get(reserva.getCourt())] 
                            = Hora.images.get(
                                    (reserva.getMember() == Estat.getMember()) ? 2 : 1);

                    if(!this.open) return;
                }

                if(this.valid)
                    Cache.cache.put(this.date, courts);

                System.out.println("Cache carregada " + this.date.toString());
            }
            
            if(this.valid){
                if(this.next){
                    Cache.max_day = this.date;

                    this.date = this.date.plusDays(1);
                } else {
                    Cache.min_day = this.date;

                    this.date = this.date.minusDays(1);
                }

                this.dia++;
            }
        }
        
        Thread.currentThread().interrupt();
        this.open = false;
    }
    
    public void setDate(LocalDate new_date){
        this.valid = false;
        
        this.dia = 0;
        
        this.date = new_date;
    }
}