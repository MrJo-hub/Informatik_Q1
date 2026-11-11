import java.util.Random;
/**
 * Beschreiben Sie hier die Klasse Wuerfel.
 * 
 * @author (Ihr Name) 
 * @version (eine Versionsnummer oder ein Datum)
 */
public class Wuerfel
{
    // Instanzvariablen - ersetzen Sie das folgende Beispiel mit Ihren Variablen
    private int seiten;
    private Random rnd;

    /**
     * Konstruktor für Objekte der Klasse Wuerfel
     */
    public Wuerfel(int pSeiten)
    {
        this.seiten = pSeiten;
    }
    
    public int wuerfeln(){
        rnd = new Random();
        int zahl = rnd.nextInt(1,7);
        System.out.println(zahl);

        return zahl;
    }

    public int gibSeiten(){
        return seiten;
    }
}