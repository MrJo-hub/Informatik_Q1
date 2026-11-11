
/**
 * Beschreiben Sie hier die Klasse Spiel.
 * 
 * @author (Ihr Name) 
 * @version (eine Versionsnummer oder ein Datum)
 */
public class Spiel
{
    // Instanzvariablen - ersetzen Sie das folgende Beispiel mit Ihren Variablen
    private Wuerfel wuerfel;

    /**
     * Konstruktor für Objekte der Klasse Spiel
     */
    public Spiel(int pSeite)
    {
        this.wuerfel = new Wuerfel(pSeite);
    }
    

    public int addiereR(int x){
        if(x == 1){
            return wuerfel.wuerfeln();
        } else {
            return wuerfel.wuerfeln() + addiereR(x - 1);
        }
    }
    public int addiereI(int x){
        int summe = 0;
        for(int i = 0; i < x; i++){
            summe += wuerfel.wuerfeln();
        }
        return summe;
    }


    public int maxFindenR(int x){
        if(x == 1){
            return wuerfel.wuerfeln();
        } else {
            int zahl = wuerfel.wuerfeln();
            int neueZahl = maxFindenR(x - 1);
            return zahl > neueZahl ? zahl : neueZahl;

        }
    }
    public int maxFindenI(int x){
        int maxZahl = 0;
        for(int i = 0; i < x;i++){
            int zahl = wuerfel.wuerfeln();
            maxZahl = maxZahl > zahl ? maxZahl : zahl;
        }
        return maxZahl;
    }


    public int gibErgebnisR(int x){
        if(x == 1){
            return (wuerfel.wuerfeln() * Math.powExact(10, x - 1));
        } else {
            return wuerfel.wuerfeln() * Math.powExact(10, x - 1) + gibErgebnisR(x - 1);
        }
    }
    public int gibErgebnisI(int x){
        int summe = 0;
        for(int i = x; i > 0; i--){
            summe += wuerfel.wuerfeln() * Math.powExact(10, i - 1);
        }
        return summe;
    }



    public int minFindenR(int x){
        if(x == 1){
            return wuerfel.wuerfeln();
        } else {
            int zahl = wuerfel.wuerfeln();
            int neueZahl = minFindenR(x - 1);
            return zahl < neueZahl ? zahl : neueZahl;
        }
    }

    public Wuerfel getWuerfel(){
        return wuerfel;
    }

}