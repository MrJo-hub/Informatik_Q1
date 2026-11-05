import java.awt.*;  
import java.awt.event.*;
import javax.swing.*;
import java.lang.reflect.*;
import java.util.*; 
@SuppressWarnings("unchecked")

/**
 * Klasse ListGUI
 * 
 * @version 2015-01-29 (v 3.1)
 * @version 2015-11-1
 * 
 * @author Rudolf Silberberg
 * @author Sven Hees ( List-Objekt eingefuegt)
 */

public class ListGUI extends JFrame {   

    // Das Knotenpanel
    private JNodePanel panNodes = new JNodePanel();

    // Andere Attribute
    private List myList;
    private ListGUI andereGUI;
    private java.util.Timer gTimer;
    private java.util.TimerTask gTimerTask;
    private boolean contentVisibility;

    private String listName;

    private String einfuegen="";

    /**
     * Ein Konstruktor ohne Parameter<br>
     * Intern wird ein anderer Konstruktor aufgerufen,
     * der zwei Parameter erwartet. Diesem werden die
     * Parameter 0 und "Liste" uebergeben.
     */
    public ListGUI() {
        this(0, "Liste");
    }

    /**
     * Ein Konstruktor mit einem Parameter<br>
     * Der Parameter gibt den Namen an,
     * der fuer die Liste angezeigt wird.<br>
     * Intern wird ein anderer Konstruktor aufgerufen,
     * der zwei Parameter erwartet. Diesem wird
     * ausserdem der Parameter 0 uebergeben.
     */
    public ListGUI(String name) {
        this(0, name);
    }

    /**
     * Ein Konstruktor mit Parameter<br>
     * Der Parameter gibt an, wo die GUI auf dem
     * Bildschirm erscheinen soll.<br>
     * Wenn der Parameter 0 ist, erscheint die GUI oben.<br>
     * Wenn der Parameter 1 ist, erscheint die GUI in der Mitte.<br>
     * Wenn der Parameter 2 ist, erscheint die GUI unten.
     * 
     * @param nr die Kennzahl fuer die Bildschirmposition
     * @param name der Name der Liste, der angezeigt wird
     */
    public ListGUI(int nr, String name) {

        super("ListGUI");
        listName = name;
        nr %= 3;
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        this.addWindowListener(new MyWindowAdapter());

        panNodes.setOpaque(false);
        panNodes.setBackground(new Color(239,228,176));
        panNodes.setFont(new Font("Arial", Font.BOLD, 16));
        panNodes.setPreferredSize(new Dimension(630,480));
        this.add(panNodes);

        this.pack();
        this.setLocation(10,10+nr*340);
        this.setResizable(false);
        this.setVisible(true);

        // Andere Attribute

        gTimer = new java.util.Timer();
        gTimerTask = new GTimerTask(panNodes);
        gTimer.schedule(gTimerTask,100,100);
        contentVisibility = true;

    } // end of public ListGUI

    //----------------
    // Anfang Methoden
    //----------------

    /**
     * Mit dieser Methode kann eine andere Instanz der
     * Klasse ListGUI angemeldet werden.<br>
     * Damit ist es moeglich, zwei Listen zu verwalten
     * und mithilfe der Methode concat zu verknuepfen.
     * 
     * @param gui die andere GUI
     */
    public void guiAnmelden(ListGUI gui) {
        andereGUI = gui;
    }

    /**
     * Mit dieser Methode kann eine extern erzeugte
     * Liste angemeldet werden.<br>
     * Die Liste wird in dem Attribut myList gespeichert.
     * 
     * @param list die Liste, die angemeldet werden soll.
     */
    public void listAnmelden(List list) { 
        myList = list;
    }

    public void inhalteAnzeigen(boolean pContentVisibility) {
        contentVisibility = pContentVisibility;
    }

    public void nodeEinfuegen(String content){
        einfuegen = content;
    }

    /**
     * Die einfach verkettete Liste dieser GUI wird zurueck gegeben.
     * 
     * @return die Liste
     */
    public List gibMyList() {
        return(myList);
    }

    //--------------
    // Ende Methoden
    //--------------

    //------------------------------------------------------
    // Die inneren Klassen:
    //------------------------------------------------------
    // MyWindowAdapter extends WindowAdapter
    // JNodePanel extends JPanel
    // GTimerTask extends java.util.TimerTask
    //------------------------------------------------------

    /*
     * Vor dem Schliessen des Fensters soll in einem eventuell
     * vorhandenem anderen Fenster dieses Fenster abgemeldet werden.
     * Eine Instanz dieses WindowsAdapters wird als WindowListener
     * in das JFrame eingefuegt.
     */
    private class MyWindowAdapter extends WindowAdapter {
        public void windowClosing( WindowEvent event )
        {
            if (andereGUI != null) {
                andereGUI.guiAnmelden(null);
            } // end of if

            gTimerTask.cancel();
        } 
    }

    /*
     * JNodePanel - hier wird gezeichnet
     */
    private class JNodePanel extends JPanel
    {
        /*
         * Die Methode paintComponent muss ueberschrieben werden.
         * Die eigentlichen Zeichenfunktionen sind in weitere
         * Methoden ausgelagert.
         */
        protected void paintComponent(Graphics g) {
            for (Component comp : getComponents()) {
                remove(comp);
            }
            g.setColor(new Color(239,228,176));
            g.fillRect(0,0,this.getWidth(),this.getHeight());
            if(einfuegen !=""){
                zeigeNewNode(g);
            }
            if (myList != null) {
                if (myList.current == null && myList.first == null) {
                    g.setColor(Color.black);
                    g.setFont(new Font("Arial", Font.BOLD, 24));
                    int x = this.getWidth() / 2 - 75;
                    int y = this.getHeight() / 2;
                    g.drawString("Leere Liste",x,y);
                    zeigeListObject(null,1,g);
                } // end of if
                else {
                    zeigeListe(g);
                } // end of if-else
            } // end of if
            else {
                g.setColor(Color.black);
                g.setFont(new Font("Arial", Font.BOLD, 24));
                int x = this.getWidth() / 2 - 75;
                int y = this.getHeight() / 2;
                g.drawString("Keine Liste",x,y);
            } // end of if-else
        }

        /*
         * Das List-Objekt wird gezeichnet
         */
        private void zeigeListObject(ArrayList<List.ListNode> nodes, double faktor, Graphics g){
            int startx = 20 + (int)(125*faktor);
            int starty = this.getHeight()-80; //-50

            int deltaYKn = -10;
            int deltaYIn = 0;

            int iC = -1; //Index current;

            //besteht aus 3 Rechtecken, Name, haed, tail
            //oben Name
            g.setColor(new Color(131,111,255)); //lila
            g.fillRect(startx,starty-(int)(345*faktor)+(int)(deltaYKn*faktor),(int)(100*faktor),(int)(110*faktor));
            //mitte oben first
            g.setColor(new Color(255,242,0));  // first (gelb)
            g.fillRect(startx,starty-(int)(305*faktor)+(int)(deltaYKn*faktor),(int)(100*faktor),(int)(70*faktor));
            //mitte unten last
            g.setColor(new Color(255,242,0));  // last (gelb)
            g.fillRect(startx,starty-(int)(270*faktor)+(int)(deltaYKn*faktor),(int)(100*faktor),(int)(70*faktor));
            //unten current
            g.setColor(new Color(51,153,0)); //current (gruen)
            g.fillRect(startx,starty-(int)(235*faktor)+(int)(deltaYKn*faktor),(int)(100*faktor),(int)(35*faktor));
            // Jetzt die Umrisse
            // Der ganze Knoten
            g.setColor(Color.black);
            g.drawRect(startx,starty-(int)(345*faktor)+(int)(deltaYKn*faktor),(int)(100*faktor),(int)(145*faktor));
            // mitte oben
            g.drawRect(startx,starty-(int)(305*faktor)+(int)(deltaYKn*faktor),(int)(100*faktor),(int)(105*faktor));
            // mitte unten
            g.drawRect(startx,starty-(int)(270*faktor)+(int)(deltaYKn*faktor),(int)(100*faktor),(int)(70*faktor));
            // unten
            g.drawRect(startx,starty-(int)(235*faktor)+(int)(deltaYKn*faktor),(int)(100*faktor),(int)(35*faktor));

            //Objectname
            {
                g.setColor(Color.black);
                FontMetrics fm;
                int w, h;
                double v = 40.0;
                g.setFont(new Font("Arial", Font.BOLD, (int)(v*faktor)));
                fm = g.getFontMetrics();
                w = fm.stringWidth(listName);
                h = fm.getHeight();
                while (w > (int)(100*faktor) && (v*faktor) > 8.0) { 
                    v *= 0.75;
                    g.setFont(new Font("Arial", Font.BOLD, (int)(v*faktor)));
                    fm = g.getFontMetrics();
                    w = fm.stringWidth(listName);
                    h = fm.getHeight();
                }
                g.drawString(listName,startx+(int)((50*faktor)-w/2.0),starty-(int)(333*faktor)+(int)(h/4.0)+(int)(deltaYIn*faktor));
            }
            //Verweise beschriften
            g.setColor(Color.darkGray);
            g.setFont(new Font("Arial", Font.BOLD, (int)(12*faktor)));
            g.drawString("first",startx+(int)(5*faktor),starty-(int)(290*faktor)+(int)(deltaYKn*faktor));
            g.drawString("last",startx+(int)(5*faktor),starty-(int)(256*faktor)+(int)(deltaYKn*faktor));
            g.drawString("current",startx+(int)(5*faktor),starty-(int)(222*faktor)+(int)(deltaYKn*faktor));

            //null falls Queue leer
            if(nodes==null){
                //leere Queue
                //first, last und current sind null
                g.setColor(Color.red);
                g.setFont(new Font("Arial", Font.BOLD, (int)(20*faktor)));
                g.drawString("null",startx+(int)(55*faktor),starty-(int)(279*faktor)+(int)(deltaYKn*faktor));
                g.drawString("null",startx+(int)(55*faktor),starty-(int)(244*faktor)+(int)(deltaYKn*faktor));
                g.drawString("null",startx+(int)(55*faktor),starty-(int)(210*faktor)+(int)(deltaYKn*faktor));

            }else{//Pfeile
                // Abstand zum Nachbarknoten
                int startNextx = startx - (int)(72*faktor);
                int startNexty = this.getHeight()-80;

                //Koordinaten fuer Pfeilspitze
                int[] xKoors = new int[3];
                int[] yKoors = new int[3];

                //Kreis first
                g.setColor(new Color(255,127,38));  // first (orange)
                g.fillOval(startx+(int)(43*faktor),starty-(int)(294*faktor)+(int)(deltaYKn*faktor),(int)(14*faktor),(int)(14*faktor));
                //Kreis last
                g.setColor(new Color(255,127,38));  // last (orangen)
                g.fillOval(startx+(int)(43*faktor),starty-(int)(259*faktor)+(int)(deltaYKn*faktor),(int)(14*faktor),(int)(14*faktor));
                //Kreis current
                if(myList.current != null){
                    g.setColor(new Color(0,102,0)); //  current (gruen)
                    g.fillOval(startx+(int)(43*faktor),starty-(int)(224*faktor)+(int)(deltaYKn*faktor),(int)(14*faktor),(int)(14*faktor));
                }else{
                    g.setColor(Color.red);
                    g.setFont(new Font("Arial", Font.BOLD, (int)(20*faktor)));
                    g.drawString("null",startx+(int)(55*faktor),starty-(int)(210*faktor)+(int)(deltaYKn*faktor));
                }

                //Pfeil first
                g.setColor(new Color(255,127,38)); // first (orange)
                g.drawLine(startx+(int)(43*faktor),starty-(int)(287*faktor)+(int)(deltaYKn*faktor),startNextx-(int)(19*faktor),startNexty-(int)(287*faktor)+(int)(deltaYKn*faktor));
                g.drawLine(startNextx-(int)(19*faktor),starty-(int)(287*faktor)+(int)(deltaYKn*faktor),startNextx-(int)(19*faktor),starty-(int)(180*faktor)+(int)(deltaYIn*faktor));

                xKoors[0] = startNextx-(int)(19*faktor);
                yKoors[0] = starty-(int)(180*faktor)-(int)(deltaYIn*faktor);
                xKoors[1] = xKoors[0] + (int)(7*faktor);
                yKoors[1] = yKoors[0] - (int)(14*faktor);
                xKoors[2] = xKoors[1] - (int)(14*faktor);
                yKoors[2] = yKoors[1];
                g.fillPolygon(xKoors,yKoors,3);

                //Pfeil last
                g.setColor(new Color(255,127,38)); // last (orange)
                int l = nodes.size()-3;
                g.drawLine(startx+(int)(50*faktor),starty-(int)(252*faktor)+(int)(deltaYKn*faktor),startx+(int)(190*faktor)+(int)(l*125*faktor),starty-(int)(252*faktor)+(int)(deltaYKn*faktor));
                g.drawLine(startx+(int)(190*faktor)+(int)(l*125*faktor),starty-(int)(252*faktor)+(int)(deltaYKn*faktor),startx+(int)(190*faktor)+(int)(l*125*faktor),starty-(int)(180*faktor)+(int)(deltaYKn*faktor));

                xKoors[0] = startx+(int)(190*faktor)+(int)(l*125*faktor);
                yKoors[0] = starty-(int)(180*faktor)-(int)(deltaYIn*faktor);
                xKoors[1] = xKoors[0] + (int)(7*faktor);
                yKoors[1] = yKoors[0] - (int)(14*faktor);
                xKoors[2] = xKoors[1] - (int)(14*faktor);
                yKoors[2] = yKoors[1];
                g.fillPolygon(xKoors,yKoors,3);

                //Pfeil current

                for(int i=0; i<nodes.size(); i++){
                    if(nodes.get(i) == myList.current){
                        iC=i;
                        i=nodes.size();
                    }
                }

                /*if(iC==1){
                g.setColor(new Color(0,102,0)); //  tail (gruen)
                g.drawLine(startx+(int)(50*faktor),starty-(int)(217*faktor)+(int)(deltaYKn*faktor),startx+(int)(50*faktor),starty-(int)(180*faktor)+(int)(deltaYKn*faktor));

                xKoors[0] = startx+(int)(50*faktor);
                yKoors[0] = starty-(int)(180*faktor)-(int)(deltaYIn*faktor);
                xKoors[1] = xKoors[0] + (int)(7*faktor);
                yKoors[1] = yKoors[0] - (int)(14*faktor);
                xKoors[2] = xKoors[1] - (int)(14*faktor);
                yKoors[2] = yKoors[1];
                g.fillPolygon(xKoors,yKoors,3);*/ 
                if(iC>-1){
                    g.setColor(new Color(0,102,0)); //  current (gruen)
                    int f = iC-2;
                    g.drawLine(startx+(int)(50*faktor),starty-(int)(217*faktor)+(int)(deltaYKn*faktor),startx+(int)(175*faktor)+(int)(f*125*faktor),starty-(int)(217*faktor)+(int)(deltaYKn*faktor));
                    g.drawLine(startx+(int)(175*faktor)+(int)(f*125*faktor),starty-(int)(217*faktor)+(int)(deltaYKn*faktor),startx+(int)(175*faktor)+(int)(f*125*faktor),starty-(int)(180*faktor)+(int)(deltaYKn*faktor));

                    xKoors[0] = startx+(int)(175*faktor)+(int)(f*125*faktor);
                    yKoors[0] = starty-(int)(180*faktor)-(int)(deltaYIn*faktor);
                    xKoors[1] = xKoors[0] + (int)(7*faktor);
                    yKoors[1] = yKoors[0] - (int)(14*faktor);
                    xKoors[2] = xKoors[1] - (int)(14*faktor);
                    yKoors[2] = yKoors[1];
                    g.fillPolygon(xKoors,yKoors,3);
                }
            }

            /*
            if(myList.first == myList.current){
            //first
            g.setColor(new Color(255,127,38)); // first (orange)
            g.drawLine(startx+(int)(43*faktor),starty-(int)(252*faktor)+(int)(deltaYKn*faktor),startNextx-(int)(10*faktor),startNexty-(int)(252*faktor)+(int)(deltaYKn*faktor));
            g.drawLine(startNextx-(int)(10*faktor),starty-(int)(252*faktor)+(int)(deltaYKn*faktor),startNextx-(int)(10*faktor),starty-(int)(180*faktor)+(int)(deltaYIn*faktor));

            xKoors[0] = startNextx-(int)(10*faktor);
            yKoors[0] = starty-(int)(180*faktor)-(int)(deltaYIn*faktor);
            xKoors[1] = xKoors[0] + (int)(7*faktor);
            yKoors[1] = yKoors[0] - (int)(14*faktor);
            xKoors[2] = xKoors[1] - (int)(14*faktor);
            yKoors[2] = yKoors[1];
            g.fillPolygon(xKoors,yKoors,3);

            //current
            g.setColor(new Color(0,102,0)); //  current (gruen)
            g.drawLine(startx+(int)(43*faktor),starty-(int)(217*faktor)+(int)(deltaYKn*faktor),startNextx+(int)(10*faktor),startNexty-(int)(217*faktor)+(int)(deltaYKn*faktor));
            g.drawLine(startNextx+(int)(10*faktor),starty-(int)(217*faktor)+(int)(deltaYKn*faktor),startNextx+(int)(10*faktor),starty-(int)(180*faktor)+(int)(deltaYKn*faktor)); 

            xKoors[0] = startNextx+(int)(10*faktor);
            yKoors[0] = starty-(int)(180*faktor)+(int)(deltaYIn*faktor);
            xKoors[1] = xKoors[0] + (int)(7*faktor);
            yKoors[1] = yKoors[0] - (int)(14*faktor);
            xKoors[2] = xKoors[1] - (int)(14*faktor);
            yKoors[2] = yKoors[1];
            g.fillPolygon(xKoors,yKoors,3);
            //}
            }else{
            //first
            g.setColor(new Color(237,28,36));  // first (rot)
            g.drawLine(startx+(int)(43*faktor),starty-(int)(252*faktor)+(int)(deltaYKn*faktor),startNextx,startNexty-(int)(252*faktor)+(int)(deltaYKn*faktor));
            g.drawLine(startNextx,starty-(int)(252*faktor)+(int)(deltaYKn*faktor),startNextx,starty-(int)(180*faktor)+(int)(deltaYIn*faktor));

            xKoors[0] = startNextx;
            yKoors[0] = starty-(int)(180*faktor)-(int)(deltaYIn*faktor);
            xKoors[1] = xKoors[0] + (int)(7*faktor);
            yKoors[1] = yKoors[0] - (int)(14*faktor);
            xKoors[2] = xKoors[1] - (int)(14*faktor);
            yKoors[2] = yKoors[1];
            g.fillPolygon(xKoors,yKoors,3);

            for(int i=0; i<nodes.size(); i++){
            if(nodes.get(i)==myList.current){
            iC=i;
            i=nodes.size();
            }
            }

            if(iC==1){
            g.setColor(new Color(0,102,0)); //  tail (gruen)
            g.drawLine(startx+(int)(50*faktor),starty-(int)(217*faktor)+(int)(deltaYKn*faktor),startx+(int)(50*faktor),starty-(int)(180*faktor)+(int)(deltaYKn*faktor));

            xKoors[0] = startx+(int)(50*faktor);
            yKoors[0] = starty-(int)(180*faktor)-(int)(deltaYIn*faktor);
            xKoors[1] = xKoors[0] + (int)(7*faktor);
            yKoors[1] = yKoors[0] - (int)(14*faktor);
            xKoors[2] = xKoors[1] - (int)(14*faktor);
            yKoors[2] = yKoors[1];
            g.fillPolygon(xKoors,yKoors,3); 
            }else if(iC>1){
            g.setColor(new Color(0,102,0)); //  tail (gruen)
            int f = iC-2;
            g.drawLine(startx+(int)(50*faktor),starty-(int)(217*faktor)+(int)(deltaYKn*faktor),startx+(int)(175*faktor)+(int)(f*125*faktor),starty-(int)(217*faktor)+(int)(deltaYKn*faktor));
            g.drawLine(startx+(int)(175*faktor)+(int)(f*125*faktor),starty-(int)(217*faktor)+(int)(deltaYKn*faktor),startx+(int)(175*faktor)+(int)(f*125*faktor),starty-(int)(180*faktor)+(int)(deltaYKn*faktor));

            xKoors[0] = startx+(int)(175*faktor)+(int)(f*125*faktor);
            yKoors[0] = starty-(int)(180*faktor)-(int)(deltaYIn*faktor);
            xKoors[1] = xKoors[0] + (int)(7*faktor);
            yKoors[1] = yKoors[0] - (int)(14*faktor);
            xKoors[2] = xKoors[1] - (int)(14*faktor);
            yKoors[2] = yKoors[1];
            g.fillPolygon(xKoors,yKoors,3);
            }*/

        }

        /*
         * Ein einzelner Knoten wird gezeichnet.
         * Die Position des Knotens auf dem Panel wird aus
         * seiner Position im ArrayList<node> nodes berechnet. 
         * Die Darstellungsgroesse wird automatisch bestimmt.
         */
        private void zeigeNode(List.ListNode node, ArrayList<List.ListNode> nodes, double faktor, Graphics g) {
            int startx = 20 + (nodes.indexOf(node))*(int)(125*faktor);
            int starty = this.getHeight()-80;
            /*
             * Verschiebungen nach oben oder unten
             * deltaYKn verschiebt die Knoten, deltaYIn die Inhaltsobjekte.
             * Die Verschiebung ist in Pixeln bei Darstellungsfaktor 1 angegeben.
             * Fuer andere Darstellungsfaktoren werden die Verschiebungen entsprechend skaliert.
             */
            int deltaYKn = -10;
            int deltaYIn = 0;

            /*
             * Die Knoten werden gezeichnet.
             * Die Berechnungen der Postitionen sind meist selbsterklaerend.
             */

            // Hintergrund fuer besondere Knoten (tail?)/(Dummy?)
            if (node.getContentObject() == null) { 
                if (node.getNextNode() == null) { // Liste ohne tail - Listenende
                    g.setColor(new Color(255,174,201));
                    g.fillRect(startx-(int)(0*faktor),starty-(int)(90*faktor)+(int)(deltaYKn*faktor),(int)(101*faktor),(int)(90*faktor));
                    g.setColor(Color.black);
                    g.setFont(new Font("Arial", Font.BOLD, (int)(16*faktor)));
                    g.drawString("Dummy?",startx+(int)(20*faktor),starty-(int)(74*faktor)+(int)(deltaYKn*faktor));
                }
                else if (node.getNextNode().getNextNode() == node) { // Liste mit tail - Dummyelement tail
                    g.setColor(new Color(255,174,201));
                    g.fillRect(startx-(int)(0*faktor),starty-(int)(90*faktor)+(int)(deltaYKn*faktor),(int)(101*faktor),(int)(90*faktor));
                    g.setColor(Color.black);
                    g.setFont(new Font("Arial", Font.BOLD, (int)(16*faktor)));
                    g.drawString("Dummy?",startx+(int)(20*faktor),starty-(int)(74*faktor)+(int)(deltaYKn*faktor));    
                }
            } // end of if

            // Der ganze Knoten
            if (myList.current == node) {
                g.setColor(new Color(51,153,0));   // current (gruen)
            } // end of if
            else {
                g.setColor(new Color(255,242,0));  //  andere (gelb)
            } // end of if-else
            g.fillRect(startx,starty-(int)(170*faktor)+(int)(deltaYKn*faktor),(int)(100*faktor),(int)(70*faktor));
            // Der Teil unten rechts
            g.setColor(new Color(153,217,234));
            g.fillRect(startx,starty-(int)(170*faktor)+(int)(deltaYKn*faktor),(int)(100*faktor),(int)(35*faktor));
            // Jetzt die Umrisse
            // Der ganze Knoten
            g.setColor(Color.black);
            g.drawRect(startx,starty-(int)(170*faktor)+(int)(deltaYKn*faktor),(int)(100*faktor),(int)(70*faktor));
            // Der Teil unten rechts
            g.drawRect(startx,starty-(int)(170*faktor)+(int)(deltaYKn*faktor),(int)(100*faktor),(int)(35*faktor));
            // Texte, falls noetig (null)
            if (node.getContentObject() == null) {
                g.setColor(Color.red);
                g.setFont(new Font("Arial", Font.BOLD, (int)(30*faktor)));
                g.drawString("null",startx+(int)(25*faktor),starty-(int)(40*faktor)+(int)(deltaYKn*faktor));
            } // end of if

            if (node.getNextNode() == null) {
                g.setColor(Color.red);
                g.setFont(new Font("Arial", Font.BOLD, (int)(24*faktor)));
                g.drawString("null",startx+(int)(55*faktor),starty-(int)(145*faktor)+(int)(deltaYKn*faktor));
            } // end of if

            // current wird markiert (Text "current" unter dem Knotenbild)
            if (node == myList.current) {
                g.setColor(Color.black);
                g.setFont(new Font("Arial", Font.BOLD, (int)(30*faktor)));
                g.drawString("current",startx+(int)(0*faktor),starty+(int)(65*faktor));
            } // end of if

            // Das Inhaltsobjekt wird gezeichnet.
            Object inhalt = node.getContentObject();
            // Nur Inhaltsobjekte != null werden gezeichnet.
            if (inhalt != null) {
                // Hintergrund
                if (myList.current == node) {
                    g.setColor(new Color(51,153,0));   // current (gruen)
                } // end of if
                else {
                    g.setColor(new Color(255,242,0));  //  andere (Gelb)
                } // end of if-else
                /* 
                 * Nachsehen, ob die Klasse des Inhaltsobjekts oder eine
                 * der Superklassen das Interface IPaintable implementiert.
                 */
                Class c = inhalt.getClass();
                boolean implementsPaintable = false;
                while (c != null && !implementsPaintable) { 
                    for (Class inter : c.getInterfaces()) {
                        if (inter.getName().equals("IPaintable")) {
                            implementsPaintable = true;
                            break;
                        }
                    }
                    c = c.getSuperclass();
                }   
                /*
                 * Falls das Interface IPaintable implementiert wird,
                 * werden entsprechende JContentPanels verwendet
                 */
                if (implementsPaintable) {
                    try { 
                        JContentPanel jCP = new JContentPanel();
                        jCP.setBounds(startx,starty-(int)(70*faktor)+(int)(deltaYIn*faktor),(int)(100*faktor),(int)(100*faktor));
                        jCP.setBackground(g.getColor());
                        jCP.setBorder(new javax.swing.border.LineBorder(Color.black));
                        jCP.setVisibility(contentVisibility);
                        this.add(jCP);
                        Method methode=inhalt.getClass().getMethod("contentPanelAnmelden",Class.forName("JContentPanel"));
                        methode.invoke(inhalt, jCP);
                        methode=inhalt.getClass().getMethod("zeige");
                        methode.invoke(inhalt);
                    }
                    catch (ClassNotFoundException cNFE) {
                        JOptionPane.showMessageDialog(this, cNFE.getMessage(), "ClassNotFoundException", JOptionPane.ERROR_MESSAGE);
                    }
                    catch (NoSuchMethodException nSME) {
                        JOptionPane.showMessageDialog(this, nSME.getMessage(), "NoSuchMethodException", JOptionPane.ERROR_MESSAGE);
                    }
                    catch (IllegalAccessException iAE) {
                        JOptionPane.showMessageDialog(this, iAE.getMessage(), "IllegalAccessException", JOptionPane.ERROR_MESSAGE);
                    }
                    catch (InvocationTargetException iTE) {
                        JOptionPane.showMessageDialog(this, iTE.getMessage(), "InvocationTargetException", JOptionPane.ERROR_MESSAGE);
                    }
                    // Just Paranoia
                    catch (Exception e){
                        JOptionPane.showMessageDialog(this, "Fehler beim Ausfuehren des Befehls! " + e.getMessage(), "Unbekannter Fehler", JOptionPane.ERROR_MESSAGE);
                    }

                } // end of if
                /*
                 * Falls das Interface nicht implementiert wird, 
                 * werden die toString()-Methoden zur Darstellung verwendet.
                 */
                else {
                    g.fillRect(startx,starty-(int)(70*faktor)+(int)(deltaYIn*faktor),(int)(100*faktor),(int)(100*faktor));
                    g.setColor(Color.black);
                    g.drawRect(startx,starty-(int)(70*faktor)+(int)(deltaYIn*faktor),(int)(100*faktor),(int)(100*faktor));
                    if (contentVisibility) {
                        FontMetrics fm;
                        int w, h;
                        double v = 40.0;
                        g.setFont(new Font("Arial", Font.BOLD, (int)(v*faktor)));
                        fm = g.getFontMetrics();
                        w = fm.stringWidth(inhalt.toString());
                        h = fm.getHeight();
                        while (w > (int)(100*faktor) && (v*faktor) > 8.0) { 
                            v *= 0.75;
                            g.setFont(new Font("Arial", Font.BOLD, (int)(v*faktor)));
                            fm = g.getFontMetrics();
                            w = fm.stringWidth(inhalt.toString());
                            h = fm.getHeight();
                        }
                        // Zur Darstellung wird die toString()-Methode verwendet.
                        g.drawString(inhalt.toString(),startx+(int)((50*faktor)-w/2.0),starty-(int)(70*faktor)+(int)(50*faktor)+(int)(h/4.0)+(int)(deltaYIn*faktor));
                    } // end of if
                }
                // Bis hier sind Aenderungen fuer beliebige Objekte erforderlich.

                // Die Pfeile zu den Inhaltsobjekten
                if (myList.current == node) {
                    g.setColor(new Color(237,28,36));  // current (rot)
                } // end of if
                else {
                    g.setColor(new Color(255,127,38)); //  andere (orange)
                } // end of if-else
                g.fillOval(startx+(int)(43*faktor),starty-(int)(123*faktor)+(int)(deltaYKn*faktor),(int)(14*faktor),(int)(14*faktor));
                g.drawLine(startx+(int)(50*faktor),starty-(int)(65*faktor)+(int)(deltaYKn*faktor),startx+(int)(50*faktor),starty-(int)(120*faktor)+(int)(deltaYIn*faktor));
                int[] xKoors = new int[3];
                int[] yKoors = new int[3];
                xKoors[0] = startx+(int)(50*faktor);
                yKoors[0] = starty-(int)(70*faktor)-(int)(deltaYIn*faktor);
                xKoors[1] = xKoors[0] + (int)(7*faktor);
                yKoors[1] = yKoors[0] - (int)(14*faktor);
                xKoors[2] = xKoors[1] - (int)(14*faktor);
                yKoors[2] = yKoors[1];
                g.fillPolygon(xKoors,yKoors,3);
            } // end of if

            // Die Pfeile zu den Nachfolgern
            if (node.getNextNode() != null) {
                g.setColor(new Color(63,72,204)); // blau
                g.fillOval(startx+(int)(70*faktor),starty-(int)(157*faktor)+(int)(deltaYKn*faktor),(int)(10*faktor),(int)(10*faktor));
                List.ListNode nextNode = node.getNextNode();
                if (nodes.indexOf(nextNode) > nodes.indexOf(node)) { // alle "normalen" Knoten
                    int startNextx = 20 + (nodes.indexOf(nextNode))*(int)(125*faktor);
                    int startNexty = this.getHeight()-80;

                    g.drawLine(startx+(int)(75*faktor),starty-(int)(152*faktor)+(int)(deltaYKn*faktor),startNextx,startNexty-(int)(152*faktor)+(int)(deltaYKn*faktor));
                    int[] xKoors = new int[3];
                    int[] yKoors = new int[3];
                    xKoors[0] = startNextx;
                    yKoors[0] = startNexty-(int)(152*faktor)+(int)(deltaYKn*faktor);
                    xKoors[1] = xKoors[0] - (int)(14*faktor);
                    yKoors[1] = yKoors[0] + (int)(7*faktor);
                    xKoors[2] = xKoors[1];
                    yKoors[2] = yKoors[1]-(int)(14*faktor);
                    g.fillPolygon(xKoors,yKoors,3);
                }
                else { // Dummy-Knoten (tail?)
                    int startNextx = 20 + (nodes.indexOf(nextNode))*(int)(125*faktor);
                    int startNexty = this.getHeight()-50;
                    g.drawLine(startx+(int)(75*faktor),starty+(int)(20.0*faktor)+(int)(deltaYKn*faktor),startNextx+(int)(25*faktor),startNexty+(int)(20.0*faktor)+(int)(deltaYKn*faktor));
                    g.drawLine(startx+(int)(75*faktor),starty-(int)((35.0/4.0*2.0)*faktor)+(int)(deltaYKn*faktor),startx+(int)(75*faktor),starty+(int)(20.0*faktor)+(int)(deltaYKn*faktor));
                    g.drawLine(startNextx+(int)(25*faktor),startNexty+(int)(20.0*faktor)+(int)(deltaYKn*faktor),startNextx+(int)(25*faktor),startNexty+(int)(deltaYKn*faktor));
                    //           int[] xKoors = new int[3];
                    //           int[] yKoors = new int[3];
                    //           xKoors[0] = startNextx;
                    //           yKoors[0] = startNexty-(int)(35.0/4.0*2.0*faktor)+(int)(deltaYKn*faktor);
                    //           xKoors[1] = xKoors[0] - (int)(14*faktor);
                    //           yKoors[1] = yKoors[0] + (int)(7*faktor);
                    //           xKoors[2] = xKoors[1];
                    //           yKoors[2] = yKoors[1]-(int)(14*faktor);
                    //           g.fillPolygon(xKoors,yKoors,3);  
                    int[] xKoors = new int[3];
                    int[] yKoors = new int[3];
                    xKoors[0] = startNextx+(int)(25*faktor);
                    yKoors[0] = startNexty+(int)(deltaYKn*faktor);
                    xKoors[1] = xKoors[0] + (int)(7*faktor);
                    yKoors[1] = yKoors[0] + (int)(14*faktor);
                    xKoors[2] = xKoors[1] - (int)(14*faktor);
                    yKoors[2] = yKoors[1];
                    g.fillPolygon(xKoors,yKoors,3);
                }
            } // end of if

        }

        /*
         * Die ganze Liste oder ein moeglichst grosser Teil sollen gezeichnet werden.
         * In dieser Methode wird der darstellbare Bereich berechnet und
         * anschliessend wird die Methode zeigeNode aufgerufen, um 
         * die einzelnen Knoten zu zeichnen.
         */
        private void zeigeListe(Graphics g) { 
            //Eine ArrayList zum Speichern der Knoten
            ArrayList<List.ListNode> nodes = new ArrayList<List.ListNode>();

            /*
             * Alle Knoten der Liste werden in dem
             * Arraylist<List.ListNode> nodes gespeichert.
             */ 
            List.ListNode travers = myList.first;
            nodes.add(travers);
            while (travers.getNextNode() != null && !nodes.contains(travers.getNextNode())) { 
                travers = travers.getNextNode();
                nodes.add(travers);
            }

            /*
             * Es wird der notwendige Faktor fuer die gewuenschte
             * Darstellungsgroesse berechnet. Der Unterschied von einer 
             * Groesse zu der naechsten Groesse ist der Faktor 1/sqrt(2).
             */
            int extent;
            double faktor = 1.0;
            if (nodes.size() <= 5) {
                faktor = 1.0;
                extent = 5;
            } 
            else if (nodes.size() <= 7) {
                faktor /= Math.sqrt(2.0);
                extent = 7;
            }
            else if (nodes.size() <= 10){
                faktor /= 2.0;
                extent = 10;
            } 
            else if (nodes.size() <= 14){
                faktor /= (2.0*Math.sqrt(2.0));
                extent = 14;
            }
            else {
                faktor /= 4.0;
                extent = 20;
            }

            /*
             * Jetzt werden die Knoten unter Beruecksichtigung ihrer
             * Darstellungsgroesse gezeichnet.
             */
            zeigeListObject(nodes, faktor, g);

            for (List.ListNode node : nodes) {
                if (nodes.indexOf(node)>=-1 && nodes.indexOf(node)<=extent) {
                    zeigeNode(node, nodes, faktor, g);
                    zeigeListObject(nodes, faktor, g);                    
                } // end of if
            } // end of for
            zeigeListObject(nodes, faktor, g); 
        }

        public void zeigeNewNode(Graphics g){
            g.setColor(new Color(50,205,50));  //  gelb
            g.fillRect(20,10,100,100);
            g.setColor(Color.black);
            g.drawRect(20,10,100,100);
            if (contentVisibility) {
                FontMetrics fm;
                int w, h;
                double v = 40.0;
                g.setFont(new Font("Arial", Font.BOLD, (int)(v)));
                fm = g.getFontMetrics();
                w = fm.stringWidth(einfuegen);
                h = fm.getHeight();
                while (w > 100 && v > 8.0) { 
                    v *= 0.75;
                    g.setFont(new Font("Arial", Font.BOLD, (int)(v)));
                    fm = g.getFontMetrics();
                    w = fm.stringWidth(einfuegen);
                    h = fm.getHeight();
                }
                // Zur Darstellung wird die toString()-Methode verwendet.
                g.drawString(einfuegen,20+(int)((50)-w/2.0),60);
            } // end of if
        }
    }// end of inner class JNodePanel

    // inner class GTimerTask
    private class GTimerTask extends java.util.TimerTask {
        private JNodePanel nP;

        public GTimerTask(JNodePanel pNP) {
            super();
            nP = pNP;
        }

        public void run() {
            nP.repaint();
        }
    }
    // od of innter class GTimerTask

    // innere Klasse JContentPanel
    private final class JContentPanel extends JPanel     
    {
        private boolean visible = false;
        private Object o;

        private final void zeichne(Object o) {
            this.o = o;
            this.repaint();
        }

        private final void setVisibility(boolean v) {
            visible = v;
        }

        public void paintComponent(Graphics g) {
            g.setColor(this.getBackground());
            g.fillRect(0,0,this.getWidth(),this.getHeight());
        }
    }
} // end of class GUI
