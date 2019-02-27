/**
 * @class Excepcions
 * @brief És una classe contenidora d'excepcions vàries del projecte.
 * @author Tots
 * @version 2017.4.5
 */
public class Excepcions extends Exception {
    static public class jaExistiaClient extends Exception{
        jaExistiaClient(String a){
            super(a);
        }
    }
    static public class jaExistiaLloc extends Exception{
        jaExistiaLloc(String a){
            super(a);
        }
    }
    public static class jaExistiaPuntInteres extends Exception {
        jaExistiaPuntInteres(String nom) {
            super(nom);
        }
    }
    public static class noExisteixClient extends Exception {
        public noExisteixClient(String nom) {
            super(nom);
        }
    }
    public static class noExisteixPuntInteres extends Exception {
        public noExisteixPuntInteres(String nom) {
            super(nom);
        }
    }
    public static class noExisteixDestinacio extends Exception {
        public noExisteixDestinacio(String nom) {
            super(nom);
        }
    }
}
