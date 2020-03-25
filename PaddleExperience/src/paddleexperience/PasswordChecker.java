/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package paddleexperience;

/**
 *
 * @author saisua
 */
public class PasswordChecker {
    // // Login requeriments contrasenya
    private static int MINIMUM_LETTERS = 8;
    private static final int MINIMUM_UPPER = 2;
    private static final int MINIMUM_NUMBERS = 2;
    private static final int MINIMUM_SYMBOL = 1;
    
    // Deixe esta variable en minuscula per a diferenciar-la
    private static final String password_regexp = create_regexp();
    
    public static boolean check_password(String s){
        return s.matches(password_regexp);
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
            for(int numbers = 0; numbers < MINIMUM_NUMBERS;) result += ".*\\d";
            result += ')';
        }

        // Afegir lookahead de símbols (@,$,!,%,*,?,&)
        if(MINIMUM_SYMBOL > 0){
            result += "(?=";
            for(int symbol = 0; symbol < MINIMUM_SYMBOL;) result += ".*[@$!%*?&]";
            result += ')';
        }

        // Afegir possibilitat qualsevol lletra i mínim de lletres
        return result+"[A-Za-z\\d@$!%*?&]{"+MINIMUM_LETTERS+",}$";
    }
}
