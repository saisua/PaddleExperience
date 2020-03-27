/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package paddleexperience.Structures;

// internal imports
import DBAcess.ClubDBAccess;

/**
 *
 * @author saisua
 */
public class PasswordChecker {
    // // Login requeriments contrasenya
    public static int MINIMUM_LETTERS = 6;
    public static final int MINIMUM_UPPER = 1;
    public static final int MINIMUM_NUMBERS = 1;
    public static final int MINIMUM_SYMBOL = 0;
    
    public static final String VALID_SYMBOLS = "@$!%()*?&";
    
    // Deixe esta variable en minuscula per a diferenciar-la
    private static final String password_regexp = create_regexp();
    
    // Comprovar validesa contrasenyes  
    public static boolean check_password(String password){
        return password.matches(password_regexp);
    }
    
    public static boolean is_valid_string(String string){
        return string.matches(".*[a-z\\d"+VALID_SYMBOLS+"]");
    }
    public static boolean is_valid_string(char character){
        return String.valueOf(character).matches("[a-z\\d"+VALID_SYMBOLS+"]");
    }
    
    
    // // AUXILIAR FUNCTIONS
    
    private static String create_regexp(){
        // El codi no és bonico del tot però és eficient
        String result = "^(?=.*[a-z])";

        // Comprova MINIMUM_LETTERS
        if(MINIMUM_LETTERS < (MINIMUM_UPPER+MINIMUM_NUMBERS+MINIMUM_SYMBOL))
            MINIMUM_LETTERS = (MINIMUM_UPPER+MINIMUM_NUMBERS+MINIMUM_SYMBOL);

        // Afegir lookahead de majúscules
        if(MINIMUM_UPPER > 0){
            result += "(?=";
            for(int upper = 0; upper < MINIMUM_UPPER; upper++) result += ".*[A-Z]";
            result += ')';
        }

        // Afegir lookahead de numeros
        if(MINIMUM_NUMBERS > 0){
            result += "(?=";
            for(int numbers = 0; numbers < MINIMUM_NUMBERS; numbers++) result += ".*\\d";
            result += ')';
        }

        // Afegir lookahead de símbols (@,$,!,%,*,?,&)
        if(MINIMUM_SYMBOL > 0){
            result += "(?=";
            for(int symbol = 0; symbol < MINIMUM_SYMBOL; symbol++) 
                result += ".*["+VALID_SYMBOLS+"]";
            result += ')';
        }

        // Afegir possibilitat qualsevol lletra i mínim de lletres
        return result+"[A-Za-z\\d@$!%*?&]{"+MINIMUM_LETTERS+",}$";
    }
}
