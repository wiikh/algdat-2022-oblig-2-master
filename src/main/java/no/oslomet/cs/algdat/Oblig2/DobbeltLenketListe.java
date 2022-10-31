package no.oslomet.cs.algdat.Oblig2;


////////////////// class DobbeltLenketListe //////////////////////////////


import java.util.*;


public class DobbeltLenketListe<T> implements Liste<T> {

    /**
     * Node class
     *
     * @param <T>
     */
    private static final class Node<T> {
        private T verdi;                   // nodens verdi
        private Node<T> forrige, neste;    // pekere

        private Node(T verdi, Node<T> forrige, Node<T> neste) {
            this.verdi = verdi;
            this.forrige = forrige;
            this.neste = neste;
        }

        private Node(T verdi) {
            this(verdi, null, null);
        }
    }

    // instansvariabler
    private Node<T> hode;          // peker til den første i listen
    private Node<T> hale;          // peker til den siste i listen
    private int antall;            // antall noder i listen
    private int endringer;         // antall endringer i listen

    public DobbeltLenketListe() {  //Konstruktør som ikke tar inn noen parametere.
        hode=null;
        hale=null;
        antall=0;
        endringer=0;
    }

    public DobbeltLenketListe(T[] a) { //Konstruktør for dobbelt lenket liste.
        if (a==null){  //a kan ikke være null.
            throw new NullPointerException("Tabellen a er null!");
        }

        if (a.length>0){ //Lager et hode for første element som ikke er null.
            int i=0;
            for (; i<a.length; i++){
                if (a[i]!=null){
                    hode = new Node<>(a[i]); //Setter hode til verdien av a[i].
                    antall++; //Antall verdier i listen er minst 1.
                    break;  //Bryter ut av funksjonen når hodet er satt.
                }
            }
            hale=hode;
            if (hode!=null){ //Konstruerer resten av listen.
                i++;
                for(; i<a.length; i++){  //Dytter halen bakover i listen for hver nye indeks som ikke er null.
                    if (a[i]!=null){
                        hale.neste = new Node<>(a[i], hale, null);
                        hale=hale.neste;
                        antall++;
                    }
                }
            }
        }
    }

    public Liste<T> subliste(int fra, int til) {
        fratilKontroll(antall, fra, til);
        Liste<T> liste = new DobbeltLenketListe<>();
        int lengde=til-fra;

        if(lengde<1){
            return liste;
        }

        Node<T> current=finnNode(fra);

        while(lengde>0){
            liste.leggInn(current.verdi);
            current=current.neste;
            lengde--;
        }
        return liste;
    }

    //Hjelpemetode
    private void fratilKontroll(int tLengde, int fra, int til){
        if(fra<0 || til>tLengde){
            throw new IndexOutOfBoundsException();
        } if(fra>til){
            throw new IllegalArgumentException();
        }
    }

    @Override
    public int antall() {
        return antall;
    }

    @Override
    public boolean tom() {
        if (hode==null){
            return true;
        } else{
            return false;
        }
    }

    @Override
    public boolean leggInn(T verdi) {
        Objects.requireNonNull(verdi);
        Node<T> nyNode = new Node<>(verdi);

        if(hode==null && hale==null &&antall == 0){ //For tom liste
            hode = nyNode;
            hale=hode;
            endringer++;
            antall++;
            return true;
        } else{ //For liste som ikke er tom
            nyNode.forrige=hale;
            hale.neste=nyNode;
            hale=nyNode;
            endringer++;
            antall++;
            return true;
        }
    }

    private Node<T>finnNode(int indeks){
        indeksKontroll(indeks, false);
        Node<T> current;

        if(indeks<antall/2){ //Hvis indeks er mindre enn antall/2
            current=hode;
            for(int i=0; i<indeks; i++){
                current=current.neste;
            }
            return current;
        } else{ //Hvis indeks er større enn antall/2
            current=hale;
            for(int i=antall-1; i>indeks; i--){
                current=current.forrige;
            }
            return current;
        }
    }

    @Override
    public void leggInn(int indeks, T verdi) {
        Objects.requireNonNull(verdi, "Verdien må være større enn null.");

        if(indeks>antall){ throw new IndexOutOfBoundsException("Indeksen er større enn mengden noder.");
            } else if (indeks<0){ throw new IndexOutOfBoundsException("Indeksen er negativ.");}

        if(antall==0 && indeks==0){
            hode = hale = new Node<T>(verdi, null, null);
        } else if(indeks==antall){
            hale = new Node<T>(verdi, hale, null);
            hale.forrige.neste=hale;
        } else if(indeks==0){
            hode = new Node<T>(verdi, null, hode);
            hode.neste.forrige=hode;
        }else{
            Node<T> node = hode;
            for(int i=0; i<indeks; i++) node=node.neste;{
                node = new Node<T>(verdi, node.forrige, node);
            }
            node.neste.forrige=node.forrige.neste=node;
        }
        endringer++;
        antall++;
    }

    @Override
    public boolean inneholder(T verdi) {
        return indeksTil(verdi)!=-1;
    }

    @Override
    public T hent(int indeks) {
        Node<T> current = finnNode(indeks);
        return current.verdi;
    }

    @Override
    public int indeksTil(T verdi) {
        if(verdi==null){
            return -1;
        }

        Node<T> p=hode;
        for(int i=0; i<antall; i++, p=p.neste){
            if(p.verdi.equals(verdi)) return i;
        }

        return -1;
    }

    @Override
    public T oppdater(int indeks, T nyVerdi) {
        Objects.requireNonNull(nyVerdi);
        Node<T> current = finnNode(indeks);
        T gammelverdi = current.verdi;
        endringer++;
        current.verdi=nyVerdi;

        return gammelverdi;
    }

    @Override
    public boolean fjern(T verdi) {
        if(verdi==null){
            return false;
        }
        Node<T> current=hode;

        if(verdi.equals(current.verdi)){
            if (current.neste != null) {
                hode=current.neste;
                hode.forrige=null;
            } else{
                hale=null;
                hode=null;
            }
            antall--;
            endringer++;
            return true;
        }

        current = hale;
        if(verdi.equals(current.verdi)){
            hale=current.forrige;
            hale.neste=null;
            antall--;
            endringer++;
            return true;
        }

        current=hode.neste;
        for(; current!=null; current=current.neste){
            if(verdi.equals(current.verdi)){
                current.forrige.neste=current.neste;
                current.neste.forrige=current.forrige;
                antall--;
                endringer++;
                return true;
            }
        }
        return false;
    }

    @Override
    public T fjern(int indeks) {
        indeksKontroll(indeks, false);

        Node<T> current = hode;
        T verdi;

        if(indeks==0){ //Første verdi i indeks fjernes
            verdi=current.verdi;
            if(current.neste!=null){
                hode=current.neste;
                hode.forrige=null;
            } else{
                hode=null;
                hale=null;
            }
        } else if(indeks==antall-1){
            current=hale;
            verdi=hale.verdi;
            hale=current.forrige;
            hale.neste=null;
        } else{
            for(int i=0; i<indeks; i++){
                current=current.neste;
            }
            verdi=current.verdi;
            current.forrige.neste=current.neste;
            current.neste.forrige=current.forrige;
        }
        antall--;
        endringer++;
        return verdi;

    }

    @Override
    public void nullstill() {
        throw new UnsupportedOperationException();
    }

    @Override
    public String toString() {
        Node <T> toStringHode = hode; //Setter hode for String Builderen til Node hode for å hjelpe med å bygge stringen.
        StringBuilder sbToString = new StringBuilder();  //Lager en ny String Builder.
        sbToString.append("[");  //Stringen skal starte med "[".

        if(tom()){
            sbToString.append("]");  //Hvis stringen er tom skal den bare avsluttes med "]".
        } else {
            sbToString.append(toStringHode.verdi);
            toStringHode=toStringHode.neste;
            while (toStringHode!=null){ //Går gjennom hver verdi i inputen og gjør det om til en ny Strneg med ", " mellom hver verdi
                sbToString.append(", ");
                sbToString.append(toStringHode.verdi);
                toStringHode=toStringHode.neste;
            }
            sbToString.append("]");
        }
        String utToString=sbToString.toString();
        return utToString;
    }

    public String omvendtString() {  //Fungerer på samme måte som toString, med fra siste element til det første.
        Node <T> oStringHale = hale;
        StringBuilder sboString = new StringBuilder();
        sboString.append("[");

        if (tom()){
            sboString.append("]");
        } else {
            sboString.append(oStringHale.verdi);
            oStringHale=oStringHale.forrige;
            while (oStringHale!=null){
                sboString.append(", ");
                sboString.append(oStringHale.verdi);
                oStringHale=oStringHale.forrige;
            }
            sboString.append("]");
        }
        String utoString=sboString.toString();
        return utoString;
    }

    @Override
    public Iterator<T> iterator() {
        return new DobbeltLenketListeIterator();
    }

    public Iterator<T> iterator(int indeks) {
        indeksKontroll(indeks, false);
        return new DobbeltLenketListeIterator(indeks);
    }

    private class DobbeltLenketListeIterator implements Iterator<T> {
        private Node<T> denne;
        private boolean fjernOK;
        private int iteratorendringer;

        private DobbeltLenketListeIterator() {
            denne = hode;     // p starter på den første i listen
            fjernOK = false;  // blir sann når next() kalles
            iteratorendringer = endringer;  // teller endringer
        }

        private DobbeltLenketListeIterator(int indeks) {
            denne=finnNode(indeks);
            fjernOK=false;
            iteratorendringer=endringer;
        }

        @Override
        public boolean hasNext() {
            return denne != null;
        }

        @Override
        public T next() {
            if(!hasNext()){
                throw new NoSuchElementException("Ingen verdier i listen.");
            }
            if(endringer!=iteratorendringer){
                throw new ConcurrentModificationException("Listen har blitt endret.");
            }
            T tempverdi=denne.verdi;
            denne=denne.neste;
            fjernOK=true;
            return tempverdi;
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }

    } // class DobbeltLenketListeIterator

    public static <T> void sorter(Liste<T> liste, Comparator<? super T> c) {
        throw new UnsupportedOperationException();
    }

} // class DobbeltLenketListe


