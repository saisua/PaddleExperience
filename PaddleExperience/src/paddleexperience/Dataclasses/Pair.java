/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package paddleexperience.Dataclasses;

/**
 *
 * @author saisua
 */
public class Pair<F, S> {
    
    public F first;
    public S second;
    
    public Pair(F first){
        this.first = first;
    }
    
    public Pair(F first, S second){
        this.first = first;
        this.second = second;
    }
    
    // // GETTERS
    public F getFirst(){
        return this.first;
    }
    
    public S getSecond(){
        return this.second;
    }
    
    // // SETTERS
    public void setFirst(F new_first){
        this.first = new_first;
    }
    
    public void setSecond(S new_second){
        this.second = new_second;
    }
}
