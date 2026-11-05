
/**
 * Beschreiben Sie hier die Klasse Patient.
 * 
 * @author (Ihr Name) 
 * @version (eine Versionsnummer oder ein Datum)
 */
public class Patient extends PriorityObject
{
    // Instanzvariablen - ersetzen Sie das folgende Beispiel mit Ihren Variablen
    private String name;

    /**
     * Konstruktor für Objekte der Klasse Patient
     */
    public Patient(String pName, int pPriority)
    {
        super(pPriority);
        this.name = pName;

    }

    public String gibName(){
        return name;
    }
}