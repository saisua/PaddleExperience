/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package paddleexperience.Dataclasses;

// Java imports
import java.util.ArrayList;
import java.time.LocalDate;
import java.time.LocalDateTime;

// Internal imports
import model.Member;
import DBAcess.ClubDBAccess;

/**
 *
 * @author saisua
 */
public final class Estat {
    // Instancia de ClubDBAccess per poder accedir a la mateixa instància
    // amb mètodes estàtics desde tot el projecte
    public static final ClubDBAccess club = ClubDBAccess.getSingletonClubDBAccess();
    
    private static Member member;
    private static LocalDateTime data;
    private static LocalDate hora;
    
    
    
    // // STATE METHODS
    
    public static void save(){
        if(club.saveDB())
            System.out.println("Base de dades actualitzada!");
        else{
            System.out.println("La base de dades no ha sigut actualitzada. Tornant a intentar...");
            if(club.saveDB())
                System.out.println("Base de dades actualitzada!");
            else
                System.out.println("[ERROR] La base de dades no pot ser actualitzada");
        }
    }
    
    
    // // GETTERS
    
    public static ClubDBAccess getClub(){
        return club;
    }
    
    public static Member getMember(){
        return member;
    }
    
    public static LocalDateTime getDate(){
        return data;
    }
    
    public static LocalDate getTime(){
        return hora;
    }
    
    
    // // SETTERS
    
    public static void setMember(Member member_set){
        member = member_set;
    }
    
    public static void setDate(LocalDateTime new_date){
        data = new_date;
    }
    
    public static void setTime(LocalDate new_time){
        hora = new_time;
    }
}
