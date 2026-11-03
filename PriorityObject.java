/**
 * Die abstrakte Klasse PriorityObject dient als Elternklasse fuer Klassen
 * deren Objekte in einer PriorityQueue verwaltet werden sollen.
 * Die Klasse implementiert das generische Interface ComparableContent.
 * 
 * @author Sven Hees
 * @version 2017-04-01
 */

public abstract class PriorityObject implements ComparableContent<PriorityObject>{
    //Attribute 
    private int priority; 

    //Konstruktor
    /**
     * Erzeugt ein PriorityObjekt mit der uebergebenen Prioritaet
     * 
     * @param pPriority Prioritaet
     */
    public PriorityObject(int pPriority) {
        priority = pPriority;
    }

    //Methoden
    /**
     * Gibt die Prioritaet zurueck
     * 
     * @return Prioritaet
     */
    public int gibPriority() {
        return priority;
    }

    //Interface-Methoden
    /**
     * Wenn festgestellt wird, dass die Prioritaet des aufrufenden Objekts
     * gleich der Prioritaet des uebergebenen Objekts pPriorityObject ist
     * wird true zurueckgegeben, sonst false.
     * 
     * @param pPriorityObject Objekt mit dem verglichen werden soll
     * 
     * @return true, wenn das aufrufende Objekt bzgl. der Prioritaet 
     *         gleich gross ist wie das Objekt pPriorityObjekt, sonst false
     */
    public boolean isEqual(PriorityObject pPriorityObject) {
        return (priority == pPriorityObject.gibPriority());
    }

    /**
     * Wenn festgestellt wird, dass die Prioritaet des aufrufenden Objekts
     * groesser als die Prioritaet des uebergebenen Objekts pPriorityObject ist
     * wird true zurueckgegeben, sonst false.
     * 
     * @param pPriorityObject Objekt mit dem verglichen werden soll
     * 
     * @return true, wenn das aufrufende Objekt bzgl. der Prioritaet 
     *         groesser ist als das Objekt pPriorityObjekt, sonst false
     */
    public boolean isGreater(PriorityObject pPriorityObject) {
        return (priority > pPriorityObject.gibPriority());
    }

    /**
     * Wenn festgestellt wird, dass die Prioritaet des aufrufenden Objekts
     * kleiner als die Prioritaet des uebergebenen Objekts pPriorityObject ist
     * wird true zurueckgegeben, sonst false.
     * 
     * @param pPriorityObject Objekt mit dem verglichen werden soll
     * 
     * @return true, wenn das aufrufende Objekt bzgl. der Prioritaet 
     *         kleiner ist als das Objekt pPriorityObjekt, sonst false
     */
    public boolean isLess(PriorityObject pPriorityObject) {
        return (this.priority < pPriorityObject.gibPriority());
    }
}
