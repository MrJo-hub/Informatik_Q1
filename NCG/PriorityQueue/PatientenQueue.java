
/**
 * Beschreiben Sie hier die Klasse PatientenQueue.
 * 
 * @author (Ihr Name) 
 * @version (eine Versionsnummer oder ein Datum)
 */
public class PatientenQueue
{
    // Instanzvariablen - ersetzen Sie das folgende Beispiel mit Ihren Variablen
    private PriorityQueue<Patient> patienten; 

    /**
     * Konstruktor für Objekte der Klasse PatientenQueue
     */
    public PatientenQueue()
    {
        patienten = new PriorityQueue<Patient>();

        Patient p1 = new Patient("Tobi", 2);
        Patient p2 = new Patient("Adam", 3);
        Patient p3 = new Patient("Elias", 4);

        patientEinfuegen(p3);
        patientEinfuegen(p2);
        patientEinfuegen(p1);
    }

    public Patient vorne() {
        return patienten.front();
    }

    public void patientEntfernen() {
        patienten.dequeue();
    }

    public boolean istLeer() {
        return patienten.isEmpty();
    }

    public void patientEinfuegen(Patient pPatient){
        patienten.inqueue(pPatient);
    }
}