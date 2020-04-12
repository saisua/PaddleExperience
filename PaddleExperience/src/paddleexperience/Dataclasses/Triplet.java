/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package paddleexperience.Dataclasses;

/**
 *
 * Jo deixe per aci caure que el Triplet de javafx és
 pitjor que el de C++ i que java no m'agrada
 * 
 * @author saisua
 */
public class Triplet<F, S, T> {
    
    public F first;
    public S second;
    public T third;
    
    public Triplet(F first){
        this.first = first;
    }
    
    public Triplet(F first, S second){
        this.first = first;
        this.second = second;
    }
    
    public Triplet(F first, S second, T third){
        this.first = first;
        this.second = second;
        this.third = third;
    }
    
    // // GETTERS
    public F getFirst(){
        return this.first;
    }
    
    public S getSecond(){
        return this.second;
    }
    
    public T getThird(){
        return this.third;
    }
    
    public void set(F new_first, S new_second, T new_third){
        if(new_first != null){
            this.first = new_first;
        }
        
        if(new_second != null){
            this.second = new_second;
        }
        
        if(new_third != null){
            this.third = new_third;
        }
    }
    
    // // SETTERS
    public Triplet setFirst(F new_first){
        this.first = new_first;
        
        return this;
    }
    
    public Triplet setSecond(S new_second){
        this.second = new_second;
        
        return this;
    }
    
    public Triplet setThird(T new_third){
        this.third = new_third;
        
        return this;
    }
}
